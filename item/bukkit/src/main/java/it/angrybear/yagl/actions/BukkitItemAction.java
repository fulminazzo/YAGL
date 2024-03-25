package it.angrybear.yagl.actions;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * A general interface for executing actions requiring an {@link ItemStack}.
 */
@FunctionalInterface
public interface BukkitItemAction {

    /**
     * Execute.
     *
     * @param player    the player
     * @param itemStack the item stack
     */
    void execute(final @NotNull Player player, final @NotNull ItemStack itemStack);

    /**
     * Run click item action.
     *
     * @param action    the action
     * @param player    the player
     * @param itemStack the item stack
     * @param clickType the click type
     */
    static void runClickItemAction(final @NotNull ClickItemAction action,
                                   final @NotNull Player player,
                                   final @NotNull ItemStack itemStack,
                                   final @NotNull ClickType clickType) {
        action.execute(player, itemStack, clickType);
    }

    /**
     * Run action item action.
     *
     * @param action         the action
     * @param player         the player
     * @param itemStack      the item stack
     * @param interactAction the interact action
     */
    static void runInteractItemAction(final @NotNull InteractItemAction action,
                                      final @NotNull Player player,
                                      final @NotNull ItemStack itemStack,
                                      final @NotNull Action interactAction) {
        action.execute(player, itemStack, interactAction);
    }
}
