package it.angrybear.events.items.interact;

import it.angrybear.events.items.PreItemActionEvent;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * An event called <b>before</b> executing a {@link it.angrybear.actions.InteractItemAction}.
 */
@Getter
public final class PreInteractItemEvent extends PreItemActionEvent implements InteractEvent {
    private final Action interactAction;

    /**
     * Instantiates a new Pre interact item event.
     *
     * @param player         the player
     * @param itemStack      the item stack
     * @param interactAction the interact action
     */
    public PreInteractItemEvent(@NotNull Player player, @NotNull ItemStack itemStack, @NotNull Action interactAction) {
        super(player, itemStack);
        this.interactAction = interactAction;
    }
}
