package it.angrybear.actions;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * The interface Click item action.
 */
@FunctionalInterface
public interface ClickItemAction {

    /**
     * Execute.
     *
     * @param player    the player
     * @param itemStack the item stack
     * @param clickType the click type
     * @deprecated this method should be used only for defining the interface. This will NOT call the relative events.
     */
    @Deprecated
    void execute(final @NotNull Player player, final @NotNull ItemStack itemStack, final @NotNull ClickType clickType);
}
