package it.angrybear.items;

import it.angrybear.persistent.DeathAction;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of {@link BukkitItemImpl} created to be a constant item in a player's inventory.
 */
@Getter
public class PersistentItem extends BukkitItemImpl {
    private static final List<PersistentItem> PERSISTENT_ITEMS = new ArrayList<>();
    private DeathAction deathAction;

    /**
     * Instantiates a new Persistent item.
     */
    public PersistentItem() {
        super();
    }

    /**
     * Instantiates a new Persistent item.
     *
     * @param material the material
     */
    public PersistentItem(String material) {
        super(material);
    }

    /**
     * Instantiates a new Persistent item.
     *
     * @param material the material
     * @param amount   the amount
     */
    public PersistentItem(String material, int amount) {
        super(material, amount);
    }

    /**
     * Sets death action.
     *
     * @param deathAction the death action
     * @return the death action
     */
    public PersistentItem setDeathAction(final @NotNull DeathAction deathAction) {
        this.deathAction = deathAction;
        return this;
    }

    /**
     * Tries to get the corresponding {@link PersistentItem} from the given {@link ItemStack}.
     *
     * @param itemStack the item stack
     * @return the persistent item
     */
    public static @Nullable PersistentItem getPersistentItem(final @Nullable ItemStack itemStack) {
        if (itemStack == null) return null;
        for (final PersistentItem item : PERSISTENT_ITEMS)
            if (item.isSimilar(itemStack)) return item;
        return null;
    }

    /**
     * Clear persistent items.
     */
    public void clearPersistentItems() {
        PERSISTENT_ITEMS.clear();
    }
}
