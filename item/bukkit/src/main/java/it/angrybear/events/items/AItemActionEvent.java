package it.angrybear.events.items;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * An abstract event that represents a call to a {@link it.angrybear.actions.BukkitItemAction#execute(Player, ItemStack)} method.
 */
@Getter
abstract class AItemActionEvent extends Event implements Cancellable {
    @Getter
    private static final HandlerList handlerList = new HandlerList();
    protected final Player player;
    protected final ItemStack itemStack;
    @Setter
    protected boolean cancelled;

    /**
     * Instantiates a new item action event.
     *
     * @param player    the player
     * @param itemStack the item stack
     */
    public AItemActionEvent(final @NotNull Player player, final @NotNull ItemStack itemStack) {
        super(!Bukkit.isPrimaryThread());
        this.player = player;
        this.itemStack = itemStack;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
