package it.fulminazzo.yagl;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yagl.contents.GUIContent;
import it.fulminazzo.yagl.guis.FullSizeGUI;
import it.fulminazzo.yagl.guis.GUI;
import it.fulminazzo.yagl.guis.GUIType;
import it.fulminazzo.yagl.guis.TypeGUI;
import it.fulminazzo.yagl.inventory.InventoryWrapper;
import it.fulminazzo.yagl.items.BukkitItem;
import it.fulminazzo.yagl.metadatable.PAPIParser;
import it.fulminazzo.yagl.scheduler.Scheduler;
import it.fulminazzo.yagl.utils.MessageUtils;
import it.fulminazzo.yagl.utils.NMSUtils;
import it.fulminazzo.yagl.viewers.PlayerOfflineException;
import it.fulminazzo.yagl.viewers.Viewer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * A collection of utilities for handling with {@link GUI}s and Bukkit.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GUIAdapter {

    /**
     * Opens the given {@link GUI} for the specified {@link Viewer}.
     *
     * @param gui    the gui
     * @param viewer the viewer
     */
    public static void openGUI(final @NotNull GUI gui,
                               final @NotNull Viewer viewer) {
        openGUI(gui, viewer, null, null);
    }


    /**
     * Opens the given {@link GUI} for the specified {@link Viewer}.
     * Uses the given {@link ItemMeta} function to {@link BukkitItem#create(Class, Consumer)} the contents.
     *
     * @param gui          the gui
     * @param viewer       the viewer
     * @param metaFunction the meta function
     */
    public static void openGUI(final @NotNull GUI gui,
                               final @NotNull Viewer viewer,
                               final @NotNull Consumer<ItemMeta> metaFunction) {
        openGUI(gui, viewer, ItemMeta.class, metaFunction);
    }

    /**
     * Opens the given {@link GUI} for the specified {@link Viewer}.
     * Uses the given {@link ItemMeta} class and function to {@link BukkitItem#create(Class, Consumer)} the contents.
     *
     * @param <M>           the type of the item meta
     * @param gui           the gui
     * @param viewer        the viewer
     * @param itemMetaClass the ItemMeta class
     * @param metaFunction  the meta function
     */
    public static <M extends ItemMeta> void openGUI(final @NotNull GUI gui,
                                                    final @NotNull Viewer viewer,
                                                    final @Nullable Class<M> itemMetaClass,
                                                    final @Nullable Consumer<M> metaFunction) {
        openGUIHelper(gui, viewer, (p, v) -> {
            // Open inventory
            final InventoryWrapper inventory;
            // Check if GUI is FullSize
            if (gui instanceof FullSizeGUI) {
                FullSizeGUI fullSizeGUI = (FullSizeGUI) gui;
                GUI upperGUI = fullSizeGUI.getUpperGUI();
                GUI lowerGUI = fullSizeGUI.getLowerGUI();
                upperGUI.apply(upperGUI);
                lowerGUI.apply(lowerGUI);

                inventory = guiToInventory(p, upperGUI);
                fillInventoryWithGUIContents(upperGUI, v, itemMetaClass, metaFunction, inventory.getActualInventory(), upperGUI.size());
                inventory.open(p);

                PlayersInventoryCache inventoryCache = GUIManager.getInstance().getInventoryCache();
                if (v.getNextGUI() == null) {
                    inventoryCache.storePlayerContents(p);
                    inventoryCache.clearPlayerStorage(p, lowerGUI.size());
                }
                int upperGUISize = upperGUI.size();
                int lowerGUISize = lowerGUI.size();
                setGUIContentsToPlayerInventory(gui, itemMetaClass, metaFunction, p, lowerGUISize, upperGUISize);
            } else {
                inventory = guiToInventory(p, gui);
                fillInventoryWithGUIContents(gui, v, itemMetaClass, metaFunction, inventory.getActualInventory(), gui.size());
                inventory.open(p);
            }
        });
    }

    /**
     * Updates the player's open {@link FullSizeGUI} by setting the updated contents in
     * the player's inventory.
     *
     * @param gui    the gui
     * @param viewer the viewer
     */
    public static void updatePlayerGUI(final @NotNull GUI gui,
                                       final @NotNull Viewer viewer) {
        updatePlayerGUI(gui, viewer, null, null);
    }

    /**
     * Updates the player's open {@link FullSizeGUI} by setting the updated contents in
     * the player's inventory.
     * Uses the given {@link ItemMeta} class and function to {@link BukkitItem#create(Class, Consumer)} the contents.
     *
     * @param <M>           the type of the item meta
     * @param gui           the gui
     * @param viewer        the viewer
     * @param itemMetaClass the ItemMeta class
     * @param metaFunction  the meta function
     */
    public static <M extends ItemMeta> void updatePlayerGUI(final @NotNull GUI gui,
                                                            final @NotNull Viewer viewer,
                                                            final @Nullable Class<M> itemMetaClass,
                                                            final @Nullable Consumer<M> metaFunction) {
        openGUIHelper(gui, viewer, (p, v) -> {
            if (gui instanceof FullSizeGUI) {
                FullSizeGUI fullSizeGUI = (FullSizeGUI) gui;
                GUI upperGUI = fullSizeGUI.getUpperGUI();
                GUI lowerGUI = fullSizeGUI.getLowerGUI();
                upperGUI.apply(upperGUI);
                lowerGUI.apply(lowerGUI);

                InventoryView inventoryView = p.getOpenInventory();
                String title = MessageUtils.color(gui.getTitle());

                if (!inventoryView.getTitle().equals(title)) {
                    fillInventoryWithGUIContents(upperGUI, v,
                            itemMetaClass, metaFunction,
                            inventoryView.getTopInventory(), upperGUI.size());

                    if (title != null) NMSUtils.updateInventoryTitle(p, title);
                }

                int upperGUISize = upperGUI.size();
                int lowerGUISize = lowerGUI.size();
                setGUIContentsToPlayerInventory(gui, itemMetaClass, metaFunction, p, lowerGUISize, upperGUISize);
            } else throw new IllegalArgumentException("updatePlayerGUI can only be used with FullSizeGUI");
        });
    }

    /**
     * Support function for {@link #openGUI(GUI, Viewer, Class, Consumer)}.
     *
     * @param gui    the gui
     * @param viewer the viewer
     * @param action the action to execute
     */
    static void openGUIHelper(final @NotNull GUI gui,
                              final @NotNull Viewer viewer,
                              final @NotNull BiConsumer<Player, Viewer> action) {
        Consumer<Viewer> runnable = v -> {
            final UUID uuid = v.getUniqueId();
            final Player player = Bukkit.getPlayer(uuid);
            if (player == null) throw new PlayerOfflineException(v.getName());
            final Refl<Viewer> reflViewer = new Refl<>(v);
            // Add to GUIManager if not present
            v = GUIManager.getViewer(player);
            // Save previous GUI, if present
            GUIManager.getOpenGUIViewer(uuid).ifPresent((vi, g) -> {
                reflViewer.setFieldObject("previousGUI", g).setFieldObject("openGUI", null);
                reflViewer.setFieldObject("nextGUI", gui);
                g.changeGUIAction().ifPresent(a -> a.execute(vi, g, gui));
            });
            // Set global variables
            for (final @NotNull BukkitVariable variable : BukkitVariable.DEFAULT_VARIABLES)
                gui.setVariable(variable.getName(), variable.getValue(player));
            gui.apply(gui);
            if (isPlaceholderAPIEnabled()) PAPIParser.parse(player, gui);
            action.accept(player, v);
            // Set new GUI
            reflViewer.setFieldObject("openGUI", gui);
            reflViewer.setFieldObject("nextGUI", null);
            // Execute action if present
            gui.openGUIAction().ifPresent(a -> a.execute(reflViewer.getObject(), gui));
        };
        // Check if context is Async and synchronize
        if (Bukkit.isPrimaryThread()) runnable.accept(viewer);
        else
            Scheduler.getScheduler().run(JavaPlugin.getProvidingPlugin(GUIAdapter.class), () -> runnable.accept(viewer));
    }

    /**
     * Sets the given {@link GUI} contents to the {@link Player}'s inventory.
     *
     * @param <M>            the type of the item meta
     * @param gui            the gui
     * @param itemMetaClass  the ItemMeta class
     * @param metaFunction   the meta function
     * @param player         the player
     * @param contentsSize   the amount of contents to set
     * @param contentsOffset the offset upon which to start getting the contents
     */
    static <M extends ItemMeta> void setGUIContentsToPlayerInventory(final @NotNull GUI gui,
                                                                     final @Nullable Class<M> itemMetaClass,
                                                                     final @Nullable Consumer<M> metaFunction,
                                                                     final @NotNull Player player,
                                                                     final int contentsSize, final int contentsOffset) {
        Viewer viewer = GUIManager.getViewer(player);
        PlayerInventory playerInventory = player.getInventory();

        // Since Minecraft handles player inventory in a "particular" way,
        // it is necessary to manually set each item.
        List<ItemStack> itemStacks = new ArrayList<>(Arrays.asList(playerInventory.getContents()));

        // Hotbar contents
        for (int i = 27; i < contentsSize; i++) {
            GUIContent content = gui.getContent(viewer, i + contentsOffset);
            int slot = i - 27;
            if (content == null) itemStacks.set(slot, null);
            else itemStacks.set(slot, convertContentToItemStack(gui, itemMetaClass, metaFunction, content));
        }

        // Storage contents
        for (int i = 0; i < Math.min(contentsSize, 27); i++) {
            GUIContent content = gui.getContent(viewer, i + contentsOffset);
            int slot = i + 9;
            if (content == null) itemStacks.set(slot, null);
            else itemStacks.set(slot, convertContentToItemStack(gui, itemMetaClass, metaFunction, content));
        }

        playerInventory.setContents(itemStacks.toArray(new ItemStack[0]));
    }

    /**
     * Fills the given inventory with the gui contents.
     *
     * @param <M>           the type of the item meta
     * @param gui           the gui
     * @param viewer        the viewer
     * @param itemMetaClass the ItemMeta class
     * @param metaFunction  the meta function
     * @param inventory     the inventory
     * @param size          the size of the inventory
     */
    static <M extends ItemMeta> void fillInventoryWithGUIContents(
            final @NotNull GUI gui, final @NotNull Viewer viewer,
            final @Nullable Class<M> itemMetaClass, final @Nullable Consumer<M> metaFunction,
            final @NotNull Inventory inventory,
            final int size
    ) {
        for (int i = 0; i < size; i++) {
            GUIContent content = gui.getContent(viewer, i);
            if (content != null) {
                final ItemStack o = convertContentToItemStack(gui, itemMetaClass, metaFunction, content);
                inventory.setItem(i, o);
            }
        }
    }

    /**
     * Converts the given content to a {@link ItemStack}, using the given data.
     *
     * @param <M>           the type of the item meta
     * @param gui           the gui
     * @param itemMetaClass the ItemMeta class
     * @param metaFunction  the meta function
     * @param content       the content
     * @return the item stack
     */
    static <M extends ItemMeta> @NotNull ItemStack convertContentToItemStack(final @NotNull GUI gui,
                                                                             final @Nullable Class<M> itemMetaClass,
                                                                             final @Nullable Consumer<M> metaFunction,
                                                                             final @NotNull GUIContent content) {
        content.copyFrom(gui, false);
        BukkitItem render = content.apply(content).render().copy(BukkitItem.class);
        final ItemStack itemStack;
        if (itemMetaClass == null || metaFunction == null) itemStack = render.create();
        else itemStack = render.create(itemMetaClass, metaFunction);
        return itemStack;
    }

    /**
     * Closes the currently open {@link GUI} for the specified {@link Viewer}, if present.
     *
     * @param viewer the viewer
     */
    public static void closeGUI(final @NotNull Viewer viewer) {
        final UUID uuid = viewer.getUniqueId();
        final Player player = Bukkit.getPlayer(uuid);
        if (player == null) throw new PlayerOfflineException(viewer.getName());
        final Refl<Viewer> reflViewer = new Refl<>(viewer);
        // Save previous GUI, if present
        GUIManager.getOpenGUIViewer(uuid).ifPresent((v, g) -> {
            reflViewer.setFieldObject("previousGUI", g).setFieldObject("openGUI", null);
            player.closeInventory();
            g.closeGUIAction().ifPresent(a ->
                    Scheduler.getScheduler().run(JavaPlugin.getProvidingPlugin(GUIAdapter.class), () -> a.execute(v, g))
            );
        });
    }

    /**
     * Converts the given {@link GUI} to a {@link Inventory}.
     *
     * @param gui   the gui
     * @param owner the owner of the inventory
     * @return the inventory
     */
    public static @NotNull InventoryWrapper guiToInventory(final @NotNull Player owner,
                                           final @NotNull GUI gui) {
        String title = MessageUtils.color(gui.getTitle());
        final InventoryWrapper inventory;
        if (title == null) {
            if (gui instanceof TypeGUI) {
                InventoryType type = guiToInventoryType(((TypeGUI) gui).getInventoryType());
                inventory = InventoryWrapper.createInventory(owner, type);
            } else inventory = InventoryWrapper.createInventory(owner, gui.size());
        } else {
            if (gui instanceof TypeGUI) {
                InventoryType type = guiToInventoryType(((TypeGUI) gui).getInventoryType());
                inventory = InventoryWrapper.createInventory(owner, type, title);
            } else inventory = InventoryWrapper.createInventory(owner, gui.size(), title);
        }
        return inventory;
    }

    /**
     * Converts the given {@link GUIType} to a {@link InventoryType}.
     *
     * @param guiType the gui type
     * @return the inventory type
     */
    public static @NotNull InventoryType guiToInventoryType(final @NotNull GUIType guiType) {
        return InventoryType.valueOf(guiType.name());
    }

    /**
     * Checks if PlaceholderAPI is enabled.
     *
     * @return true if it is
     */
    public static boolean isPlaceholderAPIEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }


    /**
     * Gets the plugin associated with the YAGL library.
     *
     * @return the plugin
     */
    public static @NotNull JavaPlugin getProvidingPlugin() {
        return JavaPlugin.getProvidingPlugin(GUIAdapter.class);
    }

}
