package it.angrybear.yagl.utils;

import it.angrybear.yagl.guis.GUI;
import org.bukkit.Bukkit;
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
}
