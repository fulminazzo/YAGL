package io.papermc.paper.threadedregions.scheduler;

import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

@Getter
public class ScheduledTask {
    private final @NotNull Plugin owningPlugin;
    private final boolean repeatingTask;

    private int executionState;

    public ScheduledTask(@NotNull Plugin owningPlugin, boolean repeatingTask) {
        this.owningPlugin = owningPlugin;
        this.repeatingTask = repeatingTask;
        this.executionState = 1;
    }

    public int cancel() {
        this.executionState = -1;
        return this.executionState;
    }

    public boolean isCancelled() {
        return this.executionState == -1;
    }

}
