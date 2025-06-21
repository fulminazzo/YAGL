package it.fulminazzo.yagl.inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link AnvilInventoryWrapper} with support for Minecraft 1.8.
 */
class AnvilInventoryWrapper8 extends AnvilInventoryWrapper {

    /**
     * Instantiates a new Anvil inventory wrapper.
     *
     * @param actualInventory the actual inventory
     */
    public AnvilInventoryWrapper8(final @NotNull Inventory actualInventory) {
        super(actualInventory);
    }

    @Override
    public void open(@NotNull Player player) {
    }

}
