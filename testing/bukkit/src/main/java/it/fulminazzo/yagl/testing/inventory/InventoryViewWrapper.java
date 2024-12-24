package it.fulminazzo.yagl.testing.inventory;

import it.fulminazzo.jbukkit.inventory.MockInventoryView;
import it.fulminazzo.yagl.testing.Wrapper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a wrapper for an InventoryView.
 * This is required for Minecraft version 1.20.6+ tests since
 * InventoryView is not an abstract class anymore.
 */
public class InventoryViewWrapper extends Wrapper<MockInventoryView> {

    /**
     * Instantiates a new Inventory view wrapper.
     *
     * @param inventory the top inventory
     * @param player    the player
     * @param title     the title
     */
    public InventoryViewWrapper(@NotNull Inventory inventory, @NotNull Player player, @NotNull String title) {
        super(MockInventoryView.class, inventory, player, title);
    }

    /**
     * Gets top inventory.
     *
     * @return the top inventory
     */
    public Inventory getTopInventory() {
        return this.internalObject.invokeMethod("getTopInventory");
    }

    /**
     * Gets bottom inventory.
     *
     * @return the bottom inventory
     */
    public Inventory getBottomInventory() {
        return this.internalObject.invokeMethod("getBottomInventory");
    }

    /**
     * Sets item.
     *
     * @param slot the slot
     * @param item the item
     */
    public void setItem(int slot, @NotNull ItemStack item) {
        this.internalObject.invokeMethod("setItem", slot, item);
    }

}
