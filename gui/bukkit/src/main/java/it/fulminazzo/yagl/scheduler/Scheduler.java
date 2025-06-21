package it.fulminazzo.yagl.scheduler;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a general scheduler.
 */
public interface Scheduler {

    /**
     * Runs the given task immediately.
     *
     * @param owningPlugin the owning plugin
     * @param task         the task to execute
     * @return the task
     */
    @NotNull Task run(@NotNull Plugin owningPlugin, @NotNull Runnable task);

    /**
     * Runs the given task asynchronously and immediately.
     *
     * @param owningPlugin the owning plugin
     * @param task         the task to execute
     * @return the task
     */
    @NotNull Task runAsync(@NotNull Plugin owningPlugin, @NotNull Runnable task);

    /**
     * Runs the given task after a certain delay.
     *
     * @param owningPlugin the owning plugin
     * @param task         the task to execute
     * @param delayInTicks the delay after which to start executing the task
     * @return the task
     */
    @NotNull Task runLater(@NotNull Plugin owningPlugin, @NotNull Runnable task,
                           long delayInTicks);

    /**
     * Runs the given task asynchronously after a certain delay.
     *
     * @param owningPlugin the owning plugin
     * @param task         the task to execute
     * @param delayInTicks the delay after which to start executing the task
     * @return the task
     */
    @NotNull Task runLaterAsync(@NotNull Plugin owningPlugin, @NotNull Runnable task,
                           long delayInTicks);

    /**
     * Runs the given task after a certain delay and repeated in time.
     *
     * @param owningPlugin       the owning plugin
     * @param task               the task to execute
     * @param repeatDelayInTicks the delay between one repetition and another
     * @param delayInTicks       the delay after which to start executing the task
     * @return the task
     */
    @NotNull Task runRepeated(@NotNull Plugin owningPlugin, @NotNull Runnable task,
                              long delayInTicks, long repeatDelayInTicks);

    /**
     * Runs the given task asynchronously after a certain delay and repeated in time.
     *
     * @param owningPlugin       the owning plugin
     * @param task               the task to execute
     * @param repeatDelayInTicks the delay between one repetition and another
     * @param delayInTicks       the delay after which to start executing the task
     * @return the task
     */
    @NotNull Task runRepeatedAsync(@NotNull Plugin owningPlugin, @NotNull Runnable task,
                              long delayInTicks, long repeatDelayInTicks);

}
