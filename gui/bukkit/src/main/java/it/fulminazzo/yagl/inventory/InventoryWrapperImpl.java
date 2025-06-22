package it.fulminazzo.yagl.inventory;

import it.fulminazzo.yagl.GUIAdapter;
import it.fulminazzo.yagl.scheduler.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * A basic implementation of a {@link InventoryWrapper}.
 */
abstract class InventoryWrapperImpl implements InventoryWrapper {

    /**
     * Checks if it is running on the main thread.
     * If it is not, return to it,
     * otherwise simply open using {@link #internalOpen(Player)}.
     *
     * @param player the player
     */
    @Override
    public void open(final @NotNull Player player) {
        if (Bukkit.isPrimaryThread()) internalOpen(player);
        else Scheduler.getScheduler().run(GUIAdapter.getProvidingPlugin(), () -> internalOpen(player));
    }

    /**
     * Executes the actual opening function.
     *
     * @param player the player
     */
    abstract void internalOpen(@NotNull Player player);

}
