package it.angrybear.yagl.events.items;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * An abstract event that represents an action executed on an item by a player.
 * This can either be 'interact' or 'click'.
 */
@Getter
abstract class AbstractItemActionEvent extends Event {
    @Getter
    private static final HandlerList handlerList = new HandlerList();
    protected final Player player;
    protected final ItemStack itemStack;

    /**
     * Instantiates a new item action event.
     *
     * @param player    the player
     * @param itemStack the item stack
     */
    public AbstractItemActionEvent(final @NotNull Player player, final @NotNull ItemStack itemStack) {
        super(!Bukkit.isPrimaryThread());
        this.player = player;
        this.itemStack = itemStack;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
