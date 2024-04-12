package it.angrybear.yagl;

import it.angrybear.yagl.contents.GUIContent;
import it.angrybear.yagl.guis.GUI;
import it.angrybear.yagl.guis.GUIType;
import it.angrybear.yagl.guis.TypeGUI;
import it.angrybear.yagl.items.BukkitItem;
import it.angrybear.yagl.utils.MessageUtils;
import it.angrybear.yagl.viewers.PlayerOfflineException;
import it.angrybear.yagl.viewers.Viewer;
import it.fulminazzo.fulmicollection.objects.Refl;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

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
        final UUID uuid = viewer.getUniqueId();
        final Player player = Bukkit.getPlayer(uuid);
        if (player == null) throw new PlayerOfflineException(viewer.getName());
        final Refl<Viewer> reflViewer = new Refl<>(viewer);
        // Add to GUIManager if not present
        viewer = GUIManager.getViewer(player);
        // Save previous GUI, if present
        GUIManager.getOpenGUIViewer(uuid).ifPresent((v, g) -> {
            reflViewer.setFieldObject("previousGUI", g).setFieldObject("openGUI", null);
            g.changeGUIAction().ifPresent(a -> a.execute(v, g, gui));
            player.closeInventory();
        });
        // Set new GUI
        reflViewer.setFieldObject("openGUI", gui);
        // Open inventory
        Inventory inventory = guiToInventory(gui);
        for (int i = 0; i < gui.size(); i++) {
            GUIContent content = gui.getContent(viewer, i);
            if (content != null) {
                ItemStack o = content.render().copy(BukkitItem.class).create();
                inventory.setItem(i, o);
            }
        }
        player.openInventory(inventory);
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
