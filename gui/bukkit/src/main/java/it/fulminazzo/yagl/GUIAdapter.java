package it.fulminazzo.yagl;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yagl.contents.GUIContent;
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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
            gui.apply(gui);
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
            Inventory inventory = guiToInventory(gui);
            for (int i = 0; i < gui.size(); i++) {
                GUIContent content = gui.getContent(v, i);
                if (content != null) {
                    content.copyFrom(gui, false);
                    BukkitItem render = content
                            .apply(content)
                            .render()
                            .copy(BukkitItem.class);
                    final ItemStack o;
                    if (itemMetaClass == null || metaFunction == null) o = render.create();
                    else o = render.create(itemMetaClass, metaFunction);
                    inventory.setItem(i, o);
                }
            }
            player.openInventory(inventory);
            // Set new GUI
            reflViewer.setFieldObject("openGUI", gui);
            // Execute action if present
            gui.openGUIAction().ifPresent(a -> a.execute(reflViewer.getObject(), gui));
        };
        // Check if context is Async and synchronize
        if (Bukkit.isPrimaryThread()) runnable.accept(viewer);
        else Bukkit.getScheduler().runTask(JavaPlugin.getProvidingPlugin(GUIAdapter.class), () -> runnable.accept(viewer));
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
