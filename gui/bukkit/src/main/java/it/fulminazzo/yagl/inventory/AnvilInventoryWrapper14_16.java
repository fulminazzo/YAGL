package it.fulminazzo.yagl.inventory;

import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link AnvilInventoryWrapper} with support from Minecraft 1.14 to 1.16.
 */
final class AnvilInventoryWrapper14_16 extends AnvilInventoryWrapper12_13 {

    /**
     * Instantiates a new Anvil inventory wrapper.
     *
     * @param actualInventory the actual inventory
     */
    public AnvilInventoryWrapper14_16(final @NotNull Inventory actualInventory) {
        super(actualInventory);
    }

}
