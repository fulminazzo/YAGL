package it.fulminazzo.yagl.scheduler;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a general task.
 */
public interface Task {

    /**
     * Cancels the current task.
     */
    void cancel();

    /**
     * Checks if the task is cancelled.
     *
     * @return true if it is
     */
    boolean isCancelled();

    /**
     * Gets the plugin that generated this task.
     *
     * @return the owning plugin
     */
    @NotNull Plugin getOwningPlugin();

}
