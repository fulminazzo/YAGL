package it.fulminazzo.yagl.scheduler;

import it.fulminazzo.fulmicollection.objects.Refl;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * An implementation of {@link Scheduler} for Folia.
 */
final class FoliaScheduler implements Scheduler {
    private final @NotNull Refl<?> internal;

    /**
     * Instantiates a new Folia scheduler.
     */
    FoliaScheduler() {
        this.internal = Objects.requireNonNull(
                new Refl<>(Bukkit.getServer()).invokeMethodRefl("getGlobalRegionScheduler"),
                "Server is not running on Folia"
        );
    }

    @Override
    public @NotNull Task run(@NotNull Plugin owningPlugin, @NotNull Runnable task) {
        return new FoliaSchedulerTask(
                this.internal.invokeMethod("run", owningPlugin, runnableToConsumer(task))
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
                this.internal.invokeMethod("runDelayed", owningPlugin, runnableToConsumer(task), delayInTicks)
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
                this.internal.invokeMethod("runAtFixedRate", owningPlugin, runnableToConsumer(task), repeatDelayInTicks, delayInTicks)
        );
    }

    @Override
    public @NotNull Task runRepeatedAsync(@NotNull Plugin owningPlugin, @NotNull Runnable task,
                                          long delayInTicks, long repeatDelayInTicks) {
        return runRepeated(owningPlugin, task, delayInTicks, repeatDelayInTicks);
    }

    /**
     * Converts a Runnable to a consumer.
     *
     * @param task the runnable
     * @return the consumer
     */
    @NotNull Consumer<?> runnableToConsumer(@NotNull Runnable task) {
        return t -> task.run();
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
