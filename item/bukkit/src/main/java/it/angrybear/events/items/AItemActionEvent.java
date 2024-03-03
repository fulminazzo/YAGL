package it.angrybear.events.items;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * An abstract event that represents a call to a {@link it.angrybear.actions.BukkitItemAction#execute(Player, ItemStack)} method.
 */
@Getter
abstract class AItemActionEvent extends Event {
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
    public AItemActionEvent(Player player, ItemStack itemStack) {
        super(!Bukkit.isPrimaryThread());
        this.player = player;
        this.itemStack = itemStack;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
