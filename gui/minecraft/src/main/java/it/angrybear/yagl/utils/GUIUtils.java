package it.angrybear.yagl.utils;

import it.angrybear.yagl.guis.GUI;
import it.angrybear.yagl.guis.GUIType;
import it.angrybear.yagl.guis.TypeGUI;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * The type Gui utils.
 */
public class GUIUtils {

    /**
     * Converts the given {@link GUI} to a {@link Inventory}.
     *
     * @param gui the gui
     * @return the inventory
     */
    public static Inventory guiToInventory(final @NotNull GUI gui) {
        String title = MessageUtils.color(gui.getTitle());
        final Inventory inventory;
        if (title == null) inventory = Bukkit.createInventory(null, gui.getSize());
        else inventory = Bukkit.createInventory(null, gui.getSize(), title);
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
