package it.fulminazzo.yagl.actions;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * The interface Interact item action.
 */
@FunctionalInterface
public interface InteractItemAction {

    /**
     * Execute.
     *
     * @param player         the player
     * @param itemStack      the item stack
     * @param interactAction the interact action
     */
    void execute(final @NotNull Player player, final @NotNull ItemStack itemStack, final @NotNull Action interactAction);
}
