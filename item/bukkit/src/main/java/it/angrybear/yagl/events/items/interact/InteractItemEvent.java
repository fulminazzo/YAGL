package it.angrybear.yagl.events.items.interact;

import it.angrybear.yagl.events.items.ItemActionEvent;
import it.angrybear.yagl.actions.InteractItemAction;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * An event called <b>after</b> executing a {@link InteractItemAction}.
 */
@Getter
public final class InteractItemEvent extends ItemActionEvent implements InteractEvent {
    private final Action interactAction;

    /**
     * Instantiates a new Interact item event.
     *
     * @param player         the player
     * @param itemStack      the item stack
     * @param interactAction the interact action
     */
    public InteractItemEvent(@NotNull Player player, @NotNull ItemStack itemStack, @NotNull Action interactAction) {
        super(player, itemStack);
        this.interactAction = interactAction;
    }
}
