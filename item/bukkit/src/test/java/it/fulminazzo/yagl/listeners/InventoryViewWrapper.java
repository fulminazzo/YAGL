package it.fulminazzo.yagl.listeners;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.jbukkit.inventory.MockInventoryView;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a wrapper for an InventoryView.
 * This is required for Minecraft version 1.20.6+ tests since
 * InventoryView is not an abstract class anymore.
 */
class InventoryViewWrapper {
    private final Refl<?> internalObject;

    /**
     * Instantiates a new Inventory view wrapper.
     *
     * @param inventory the top inventory
     * @param player    the player
     * @param title     the title
     */
    public InventoryViewWrapper(@NotNull Inventory inventory, @NotNull Player player, @NotNull String title) {
        this.internalObject = new Refl<>(MockInventoryView.class, inventory, player, title);
    }

    /**
     * Gets the wrapped view.
     *
     * @param <V> the type of the view
     * @return the view
     */
    @SuppressWarnings("unchecked")
    public <V> V getWrapped() {
        return (V) this.internalObject.getObject();
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
