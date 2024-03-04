package it.angrybear.items;

import it.angrybear.actions.BukkitItemAction;
import it.angrybear.actions.ClickItemAction;
import it.angrybear.events.items.interact.InteractItemEvent;
import it.angrybear.listeners.PersistentListener;
import it.angrybear.persistent.DeathAction;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * An implementation of {@link BukkitItemImpl} created to be a constant item in a player's inventory.
 */
@Getter
public class PersistentItem extends BukkitItemImpl {
    private static final String WARNING_MESSAGE = "Creating a PersistentItem without registering a PersistentListener will cause the former to fail. Please register one listener.";
    private static final List<PersistentItem> PERSISTENT_ITEMS = new ArrayList<>();
    private DeathAction deathAction;
    @Getter(AccessLevel.NONE)
    private ClickItemAction clickAction;
    @Getter(AccessLevel.NONE)
    private InteractItemAction interactAction;

    /**
     * Instantiates a new Persistent item.
     */
    public PersistentItem() {
        this(null);
    }

    /**
     * Instantiates a new Persistent item.
     *
     * @param material the material
     */
    public PersistentItem(final @Nullable String material) {
        this(material, 1);
    }

    /**
     * Instantiates a new Persistent item.
     *
     * @param material the material
     * @param amount   the amount
     */
    public PersistentItem(final @Nullable String material, final int amount) {
        super(material, amount);
        this.deathAction = DeathAction.MAINTAIN;
        PERSISTENT_ITEMS.add(this);
    }

    @Override
    public @NotNull <M extends ItemMeta> ItemStack create(Class<M> itemMetaClass, Consumer<M> metaFunction) {
        if (!PersistentListener.isInitialized())
            Logger.getGlobal().warning(WARNING_MESSAGE);
        return super.create(itemMetaClass, metaFunction);
    }

    /**
     * Sets death action.
     *
     * @param deathAction the death action
     * @return this persistent item
     */
    public PersistentItem setDeathAction(final @NotNull DeathAction deathAction) {
        this.deathAction = deathAction;
        return this;
    }

    /**
     * Set the action executed on interacting.
     * A player interacts with an item when they right-click with it in game.
     *
     * @param action the action
     * @return this persistent item
     */
    public PersistentItem onInteract(final @Nullable InteractItemAction action) {
        this.interactAction = action;
        return this;
    }

    /**
     * Executes {@link #clickAction}.
     *
     * @param player    the player
     * @param itemStack the item stack
     * @param clickType the click type
     */
    public void click(final @NotNull Player player, final @NotNull ItemStack itemStack, final @NotNull ClickType clickType) {
        if (this.clickAction != null) BukkitItemAction.runClickItemAction(this.clickAction, player, itemStack, clickType);
    }

    /**
     * Set the action executed on clicking.
     * A player clicks with an item when they click on it in their inventory.
     *
     * @param action the action
     * @return this persistent item
     */
    public PersistentItem onClick(final @Nullable ClickItemAction action) {
        this.clickAction = action;
        return this;
    }

    /**
     * Tries to get the corresponding {@link PersistentItem} from the given {@link ItemStack}.
     *
     * @param itemStack the item stack
     * @return this persistent item
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
