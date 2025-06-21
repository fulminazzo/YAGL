package it.fulminazzo.yagl.scheduler;

import it.fulminazzo.fulmicollection.objects.Refl;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.FoliaTask;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link Scheduler} for Folia.
 */
final class FoliaScheduler implements Scheduler {
    private final @NotNull org.bukkit.scheduler.FoliaScheduler internal;

    FoliaScheduler() {
        this.internal = Bukkit.getServer().getScheduler();
    }

    @Override
    public @NotNull Task run(@NotNull Plugin owningPlugin, @NotNull Runnable task) {
        return new FoliaSchedulerTask(
                this.internal.runTask(owningPlugin, task)
        );
    }

    @Override
    public @NotNull Task runAsync(@NotNull Plugin owningPlugin, @NotNull Runnable task) {
        return run(owningPlugin, task);
    }

    @Override
    public @NotNull Task runLater(@NotNull Plugin owningPlugin, @NotNull Runnable task,
                                  long delayInTicks) {
        return new FoliaSchedulerTask(
                this.internal.runTaskLater(owningPlugin, task, delayInTicks)
        );
    }

    @Override
    public @NotNull Task runLaterAsync(@NotNull Plugin owningPlugin, @NotNull Runnable task,
                                       long delayInTicks) {
        return runLater(owningPlugin, task, delayInTicks);
    }

    @Override
    public @NotNull Task runRepeated(@NotNull Plugin owningPlugin, @NotNull Runnable task,
                                     long delayInTicks, long repeatDelayInTicks) {
        return new FoliaSchedulerTask(
                this.internal.runTaskTimer(owningPlugin, task, repeatDelayInTicks, delayInTicks)
        );
    }

    @Override
    public @NotNull Task runRepeatedAsync(@NotNull Plugin owningPlugin, @NotNull Runnable task,
                                          long delayInTicks, long repeatDelayInTicks) {
        return runRepeated(owningPlugin, task, delayInTicks, repeatDelayInTicks);
    }

    /**
     * An implementation of {@link Task} for Folia.
     */
    static class FoliaSchedulerTask implements Task {
        private final @NotNull Refl<?> internal;

        /**
         * Instantiates a new Folia scheduler task.
         *
         * @param internal the internal bukkit task
         */
        public FoliaSchedulerTask(final @NotNull Object internal) {
            this.internal = new Refl<>(internal);
        }

        @Override
        public void cancel() {
            this.internal.invokeMethod("cancel");
        }

        @Override
        public boolean isCancelled() {
            return this.internal.invokeMethod("isCancelled");
        }

        @Override
        public @NotNull Plugin getOwningPlugin() {
            return this.internal.invokeMethod("getOwningPlugin");
        }
    }


}
