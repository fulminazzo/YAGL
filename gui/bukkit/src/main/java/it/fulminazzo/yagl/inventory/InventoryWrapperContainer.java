package it.fulminazzo.yagl.inventory;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * A basic {@link InventoryWrapper} that simply wraps the provided inventory.
 */
@Getter
final class InventoryWrapperContainer extends InventoryWrapperImpl {
    private final @NotNull Inventory actualInventory;

    /**
     * Instantiates a new Inventory wrapper.
     *
     * @param actualInventory the actual inventory
     */
    public InventoryWrapperContainer(final @NotNull Inventory actualInventory) {
        this.actualInventory = actualInventory;
    }

    @Override
    public void internalOpen(final @NotNull Player player) {
        player.openInventory(this.actualInventory);
    }

}
