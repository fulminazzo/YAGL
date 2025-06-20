package it.fulminazzo.yagl.inventory;

import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link AnvilInventoryWrapper} with support for Minecraft 1.13.
 */
class AnvilInventoryWrapper13 extends AnvilInventoryWrapper12 {

    /**
     * Instantiates a new Anvil inventory wrapper.
     *
     * @param actualInventory the actual inventory
     */
    public AnvilInventoryWrapper13(final @NotNull Inventory actualInventory) {
        super(actualInventory);
    }

}
