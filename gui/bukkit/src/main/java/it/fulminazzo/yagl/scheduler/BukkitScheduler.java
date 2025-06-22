package it.fulminazzo.yagl.scheduler;

import it.fulminazzo.fulmicollection.objects.Refl;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link Scheduler} for Bukkit.
 */
final class BukkitScheduler implements Scheduler {
    private final @NotNull org.bukkit.scheduler.BukkitScheduler internal;

    BukkitScheduler() {
        this.internal = Bukkit.getServer().getScheduler();
    }

    @Override
    public @NotNull Task run(@NotNull Plugin owningPlugin, @NotNull Runnable task) {
        return new BukkitSchedulerTask(
                this.internal.runTask(owningPlugin, task)
        );
    }

    @Override
    public @NotNull Task runAsync(@NotNull Plugin owningPlugin, @NotNull Runnable task) {
        return new BukkitSchedulerTask(
                this.internal.runTaskAsynchronously(owningPlugin, task)
        );
    }

    @Override
    public @NotNull Task runLater(@NotNull Plugin owningPlugin, @NotNull Runnable task,
                                  long delayInTicks) {
        return new BukkitSchedulerTask(
                this.internal.runTaskLater(owningPlugin, task, delayInTicks)
        );
    }

    @Override
    public @NotNull Task runLaterAsync(@NotNull Plugin owningPlugin, @NotNull Runnable task,
                                       long delayInTicks) {
        return new BukkitSchedulerTask(
                this.internal.runTaskLaterAsynchronously(owningPlugin, task, delayInTicks)
        );
    }

    @Override
    public @NotNull Task runRepeated(@NotNull Plugin owningPlugin, @NotNull Runnable task,
                                     long delayInTicks, long repeatDelayInTicks) {
        return new BukkitSchedulerTask(
                this.internal.runTaskTimer(owningPlugin, task, repeatDelayInTicks, delayInTicks)
        );
    }

    @Override
    public @NotNull Task runRepeatedAsync(@NotNull Plugin owningPlugin, @NotNull Runnable task,
                                          long delayInTicks, long repeatDelayInTicks) {
        return new BukkitSchedulerTask(
                this.internal.runTaskTimerAsynchronously(owningPlugin, task, repeatDelayInTicks, delayInTicks)
        );
    }

    /**
     * An implementation of {@link Task} for Bukkit.
     */
    static class BukkitSchedulerTask implements Task {
        private final @NotNull BukkitTask internal;

        /**
         * Instantiates a new Bukkit scheduler task.
         *
         * @param internal the internal bukkit task
         */
        public BukkitSchedulerTask(final @NotNull BukkitTask internal) {
            this.internal = internal;
        }

        @Override
        public void cancel() {
            this.internal.cancel();
        }

        @Override
        public boolean isCancelled() {
            try {
                return this.internal.isCancelled();
            } catch (NoSuchMethodError e) {
                // Older versions did not have isCancelled method
                long period = new Refl<>(this.internal).getFieldObject("period");
                return period == -2;
            }
        }

        @Override
        public @NotNull Plugin getOwningPlugin() {
            return this.internal.getOwner();
        }
    }


}
