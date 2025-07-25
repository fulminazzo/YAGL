package it.fulminazzo.yagl.inventory;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * A wrapper for {@link Inventory} objects.
 */
public interface InventoryWrapper {

    /**
     * Opens the inventory for the given player.
     *
     * @param player the player
     */
    void open(final @NotNull Player player);

    /**
     * Gets the actual bukkit inventory.
     *
     * @return the actual inventory
     */
    @NotNull Inventory getActualInventory();

    /**
     * Creates a new inventory wrapper.
     *
     * @param player the owner of the inventory
     * @param size   the size
     * @return the inventory wrapper
     */
    static @NotNull InventoryWrapper createInventory(final @NotNull Player player,
                                                     final int size) {
        return new InventoryWrapperContainer(Bukkit.createInventory(player, size));
    }

    /**
     * Creates a new inventory wrapper.
     *
     * @param player the owner of the inventory
     * @param type   the type of the inventory
     * @return the inventory wrapper
     */
    static @NotNull InventoryWrapper createInventory(final @NotNull Player player,
                                                     final @NotNull InventoryType type) {
        if (type == InventoryType.ANVIL) return AnvilInventoryWrapper.newWrapper(player);
        else return new InventoryWrapperContainer(Bukkit.createInventory(player, type));
    }

    /**
     * Creates a new inventory wrapper.
     *
     * @param player the owner of the inventory
     * @param size   the size
     * @param title  the title
     * @return the inventory wrapper
     */
    static @NotNull InventoryWrapper createInventory(final @NotNull Player player,
                                                     final int size,
                                                     final @NotNull String title) {
        return new InventoryWrapperContainer(Bukkit.createInventory(player, size, title));
    }

    /**
     * Creates a new inventory wrapper.
     *
     * @param player the owner of the inventory
     * @param type   the type of the inventory
     * @param title  the title
     * @return the inventory wrapper
     */
    static @NotNull InventoryWrapper createInventory(final @NotNull Player player,
                                                     final @NotNull InventoryType type,
                                                     final @NotNull String title) {
        if (type == InventoryType.ANVIL) return AnvilInventoryWrapper.newWrapper(player, title);
        else return new InventoryWrapperContainer(Bukkit.createInventory(player, type, title));
    }

}
