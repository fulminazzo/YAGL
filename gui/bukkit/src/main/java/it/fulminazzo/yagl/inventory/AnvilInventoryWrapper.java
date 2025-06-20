package it.fulminazzo.yagl.inventory;

import it.fulminazzo.yagl.utils.NMSUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
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
    /**
     * The Actual inventory.
     */
    @Getter
    protected final @NotNull Inventory actualInventory;

    /**
     * Instantiates a new Anvil inventory wrapper.
     *
     * @param actualInventory the actual inventory
     */
    public AnvilInventoryWrapper(final @NotNull Inventory actualInventory) {
        this.actualInventory = actualInventory;
    }

    /**
     * Creates a new AnvilInventoryWrapper.
     *
     * @param owner the owner
     * @return the inventory wrapper
     */
    public static @NotNull InventoryWrapper newWrapper(final @NotNull Player owner) {
        return newWrapper(Bukkit.createInventory(owner, InventoryType.ANVIL));
    }

    /**
     * Creates a new AnvilInventoryWrapper.
     *
     * @param owner the owner
     * @param title the title
     * @return the inventory wrapper
     */
    public static @NotNull InventoryWrapper newWrapper(final @NotNull Player owner,
                                                       final @NotNull String title) {
        return newWrapper(Bukkit.createInventory(owner, InventoryType.ANVIL, title));
    }

    /**
     * Creates a new AnvilInventoryWrapper.
     *
     * @param inventory the inventory
     * @return the inventory wrapper
     */
    static @NotNull InventoryWrapper newWrapper(final @NotNull Inventory inventory) {
        double version = NMSUtils.getServerVersion();
        if (version >= 12 && version < 14)
            return new AnvilInventoryWrapper12_13(inventory);
        else return new InventoryWrapperContainer(inventory);
    }

}
