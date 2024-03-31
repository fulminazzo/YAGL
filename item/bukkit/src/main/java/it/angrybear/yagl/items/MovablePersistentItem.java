package it.angrybear.yagl.items;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An implementation of {@link PersistentItem} that allows to be moved inside the player's inventory.
 */
public class MovablePersistentItem extends PersistentItem {

    /**
     * Instantiates a new Movable persistent item.
     */
    public MovablePersistentItem() {
        super();
    }

    /**
     * Instantiates a new Movable persistent item.
     *
     * @param material the material
     */
    public MovablePersistentItem(@NotNull Material material) {
        super(material);
    }

    /**
     * Instantiates a new Movable persistent item.
     *
     * @param material the material
     */
    public MovablePersistentItem(@Nullable String material) {
        super(material);
    }

    /**
     * Instantiates a new Movable persistent item.
     *
     * @param material the material
     * @param amount   the amount
     */
    public MovablePersistentItem(@NotNull Material material, int amount) {
        super(material, amount);
    }

    /**
     * Instantiates a new Movable persistent item.
     *
     * @param material the material
     * @param amount   the amount
     */
    public MovablePersistentItem(@Nullable String material, int amount) {
        super(material, amount);
    }
}
