package it.fulminazzo.yagl.inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of {@link InventoryWrapper} to open a
 * {@link org.bukkit.event.inventory.InventoryType#ANVIL} in legacy Minecraft versions (1.8-1.16.5).
 * <br>
 * This wrapping is necessary, as trying to set a content to an anvil inventory in
 * previous versions would result in an empty inventory.
 */
final class AnvilInventoryWrapper implements InventoryWrapper {

    @Override
    public void open(@NotNull Player player) {

    }

    @Override
    public @NotNull Inventory getActualInventory() {
        return null;
    }

}
