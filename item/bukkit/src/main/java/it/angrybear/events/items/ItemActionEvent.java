package it.angrybear.events.items;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called after executing a {@link it.angrybear.actions.BukkitItemAction}.
 */
public abstract class ItemActionEvent extends AItemActionEvent {

    /**
     * Instantiates a new Pre item action event.
     *
     * @param player    the player
     * @param itemStack the item stack
     */
    public ItemActionEvent(final @NotNull Player player, final @NotNull ItemStack itemStack) {
        super(player, itemStack);
    }
}
