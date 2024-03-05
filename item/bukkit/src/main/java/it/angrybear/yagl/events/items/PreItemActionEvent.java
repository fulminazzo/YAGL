package it.angrybear.yagl.events.items;

import it.angrybear.yagl.actions.BukkitItemAction;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called before executing a {@link BukkitItemAction}.
 */
@Getter
@Setter
public abstract class PreItemActionEvent extends AbstractItemActionEvent implements Cancellable {
    protected boolean cancelled;

    /**
     * Instantiates a new Pre item action event.
     *
     * @param player    the player
     * @param itemStack the item stack
     */
    public PreItemActionEvent(final @NotNull Player player, final @NotNull ItemStack itemStack) {
        super(player, itemStack);
    }
}
