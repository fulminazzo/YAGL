package it.fulminazzo.yagl.inventory;

import lombok.Getter;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of {@link InventoryWrapper} to open a
 * {@link org.bukkit.event.inventory.InventoryType#ANVIL} in legacy Minecraft versions (1.8-1.16.5).
 * <br>
 * This wrapping is necessary, as trying to set a content to an anvil inventory in
 * previous versions would result in an empty inventory.
 */
abstract class AnvilInventoryWrapper implements InventoryWrapper {
    @Getter
    protected final @NotNull Inventory actualInventory;

    public AnvilInventoryWrapper(final @NotNull Inventory actualInventory) {
        this.actualInventory = actualInventory;
    }

}
