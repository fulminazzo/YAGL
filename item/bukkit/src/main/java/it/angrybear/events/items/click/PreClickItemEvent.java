package it.angrybear.events.items.click;

import it.angrybear.events.items.PreItemActionEvent;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * An event called <b>before</b> executing a {@link it.angrybear.actions.ClickItemAction}.
 */
@Getter
public final class PreClickItemEvent extends PreItemActionEvent implements ClickEvent {
    private final ClickType clickType;

    /**
     * Instantiates a new Pre click item event.
     *
     * @param player    the player
     * @param itemStack the item stack
     * @param clickType the click type
     */
    public PreClickItemEvent(@NotNull Player player, @NotNull ItemStack itemStack, @NotNull ClickType clickType) {
        super(player, itemStack);
        this.clickType = clickType;
    }
}
