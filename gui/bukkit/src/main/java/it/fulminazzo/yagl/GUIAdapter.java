package it.fulminazzo.yagl;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yagl.contents.GUIContent;
import it.fulminazzo.yagl.guis.FullSizeGUI;
import it.fulminazzo.yagl.guis.GUI;
import it.fulminazzo.yagl.guis.GUIType;
import it.fulminazzo.yagl.guis.TypeGUI;
import it.fulminazzo.yagl.items.BukkitItem;
import it.fulminazzo.yagl.utils.MessageUtils;
import it.fulminazzo.yagl.viewers.PlayerOfflineException;
import it.fulminazzo.yagl.viewers.Viewer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
    public static void openGUI(final @NotNull GUI gui, @NotNull Viewer viewer) {
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
    public static void openGUI(final @NotNull GUI gui, @NotNull Viewer viewer, final @NotNull Consumer<ItemMeta> metaFunction) {
        openGUI(gui, viewer, ItemMeta.class, metaFunction);
    }

    /**
     * Opens the given {@link GUI} for the specified {@link Viewer}.
     * Uses the given {@link ItemMeta} class and function to {@link BukkitItem#create(Class, Consumer)} the contents.
     *
     * @param <M>           the type parameter
     * @param gui           the gui
     * @param viewer        the viewer
     * @param itemMetaClass the ItemMeta class
     * @param metaFunction  the meta function
     */
    public static <M extends ItemMeta> void openGUI(final @NotNull GUI gui, final @NotNull Viewer viewer,
                                                    final @Nullable Class<M> itemMetaClass, final @Nullable Consumer<M> metaFunction) {
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
                g.changeGUIAction().ifPresent(a -> a.execute(vi, g, gui));
            });
            // Set global variables
            for (final @NotNull BukkitVariable variable : BukkitVariable.DEFAULT_VARIABLES)
                gui.setVariable(variable.getName(), variable.getValue(player));
            // Open inventory
            gui.apply(gui);
            final Inventory inventory;
            // Check if GUI is FullSize
            if (gui instanceof FullSizeGUI) {
                FullSizeGUI fullSizeGUI = (FullSizeGUI) gui;

                GUI upperGUI = fullSizeGUI.getUpperGUI();
                inventory = guiToInventory(upperGUI);
                populateInventoryWithGUIContents(upperGUI, v, itemMetaClass, metaFunction, inventory, upperGUI.size());
                player.openInventory(inventory);

                PlayersInventoryCache inventoryCache = GUIManager.getInstance().getInventoryCache();
                inventoryCache.storePlayerContents(player);
                inventoryCache.clearPlayerStorage(player);
                PlayerInventory playerInventory = player.getInventory();

                // Since Minecraft handles player inventory in a "particular" way,
                // it is necessary to manually set each item.
                List<ItemStack> itemStacks = new ArrayList<>();

                GUI lowerGUI = fullSizeGUI.getLowerGUI();
                for (int i = 0; i < lowerGUI.size() - 27; i++) {
                    GUIContent content = gui.getContent(v, i + upperGUI.size() + 27);
                    if (content == null) itemStacks.add(null);
                    else itemStacks.add(convertToItemStack(gui, itemMetaClass, metaFunction, content));
                }

                for (int i = 0; i < 27; i++) {
                    GUIContent content = gui.getContent(v, i + upperGUI.size());
                    if (content == null) itemStacks.add(null);
                    else itemStacks.add(convertToItemStack(gui, itemMetaClass, metaFunction, content));
                }

                playerInventory.setStorageContents(itemStacks.toArray(new ItemStack[0]));
            } else {
                inventory = guiToInventory(gui);
                populateInventoryWithGUIContents(gui, v, itemMetaClass, metaFunction, inventory, gui.size());
                player.openInventory(inventory);
            }
            // Set new GUI
            reflViewer.setFieldObject("openGUI", gui);
            // Execute action if present
            gui.openGUIAction().ifPresent(a -> a.execute(reflViewer.getObject(), gui));
        };
        // Check if context is Async and synchronize
        if (Bukkit.isPrimaryThread()) runnable.accept(viewer);
        else
            Bukkit.getScheduler().runTask(JavaPlugin.getProvidingPlugin(GUIAdapter.class), () -> runnable.accept(viewer));
    }

    private static <M extends ItemMeta> void populateInventoryWithGUIContents(
            final @NotNull GUI gui, final @NotNull Viewer v,
            final @Nullable Class<M> itemMetaClass, final @Nullable Consumer<M> metaFunction,
            final @NotNull Inventory inventory,
            final int size
    ) {
        for (int i = 0; i < size; i++) {
            GUIContent content = gui.getContent(v, i);
            if (content != null) {
                final ItemStack o = convertToItemStack(gui, itemMetaClass, metaFunction, content);
                inventory.setItem(i, o);
            }
        }
    }

    private static <M extends ItemMeta> @NotNull ItemStack convertToItemStack(final @NotNull GUI gui,
                                                                              final @Nullable Class<M> itemMetaClass,
                                                                              final @Nullable Consumer<M> metaFunction,
                                                                              final @NotNull GUIContent content) {
        content.copyFrom(gui, false);
        BukkitItem render = content
                .apply(content)
                .render()
                .copy(BukkitItem.class);
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
                    Bukkit.getScheduler().runTask(JavaPlugin.getProvidingPlugin(GUIAdapter.class), () -> a.execute(v, g))
            );
        });
    }

    /**
     * Converts the given {@link GUI} to a {@link Inventory}.
     *
     * @param gui the gui
     * @return the inventory
     */
    public static Inventory guiToInventory(final @NotNull GUI gui) {
        String title = MessageUtils.color(gui.getTitle());
        final Inventory inventory;
        if (title == null) {
            if (gui instanceof TypeGUI) {
                InventoryType type = guiToInventoryType(((TypeGUI) gui).getInventoryType());
                inventory = Bukkit.createInventory(null, type);
            } else inventory = Bukkit.createInventory(null, gui.size());
        } else {
            if (gui instanceof TypeGUI) {
                InventoryType type = guiToInventoryType(((TypeGUI) gui).getInventoryType());
                inventory = Bukkit.createInventory(null, type, title);
            } else inventory = Bukkit.createInventory(null, gui.size(), title);
        }
        return inventory;
    }

    /**
     * Converts the given {@link GUIType} to a {@link InventoryType}.
     *
     * @param guiType the gui type
     * @return the inventory type
     */
    public static InventoryType guiToInventoryType(final @NotNull GUIType guiType) {
        return InventoryType.valueOf(guiType.name());
    }
}
