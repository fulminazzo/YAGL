package it.angrybear.yagl.events.items.click;

import it.angrybear.yagl.events.items.ItemActionEvent;
import it.angrybear.yagl.actions.ClickItemAction;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * An event called <b>after</b> executing a {@link ClickItemAction}.
 */
@Getter
public final class ClickItemEvent extends ItemActionEvent implements ClickEvent {
    private final ClickType clickType;

    /**
     * Instantiates a new Click item event.
     *
     * @param player    the player
     * @param itemStack the item stack
     * @param clickType the click type
     */
    public ClickItemEvent(@NotNull Player player, @NotNull ItemStack itemStack, @NotNull ClickType clickType) {
        super(player, itemStack);
        this.clickType = clickType;
    }
}
