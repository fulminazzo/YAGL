package it.angrybear.actions;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * A general interface for executing actions requiring an {@link ItemStack}.
 */
@FunctionalInterface
public interface BukkitAction {

    /**
     * Execute.
     *
     * @param player    the player
     * @param itemStack the item stack
     */
    void execute(final @NotNull Player player, final @NotNull ItemStack itemStack);
}
