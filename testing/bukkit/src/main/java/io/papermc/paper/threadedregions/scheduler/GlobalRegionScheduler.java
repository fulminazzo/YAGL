package io.papermc.paper.threadedregions.scheduler;

import it.fulminazzo.yagl.exception.NotImplementedException;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class GlobalRegionScheduler {

    public void cancelTasks(@NotNull Plugin plugin) {
        throw new NotImplementedException();
    }

    public void execute(@NotNull Plugin plugin, @NotNull Runnable run) {
        throw new NotImplementedException();
    }

    public @NotNull ScheduledTask run(@NotNull Plugin plugin, @NotNull Consumer<ScheduledTask> task) {
        return new ScheduledTask(plugin, false);
    }

    public @NotNull ScheduledTask runAtFixedRate(@NotNull Plugin plugin, @NotNull Consumer<ScheduledTask> task,
                                                 long initialDelayTicks, long periodTicks) {
        return new ScheduledTask(plugin, true);
    }

    public @NotNull ScheduledTask runDelayed(@NotNull Plugin plugin, @NotNull Consumer<ScheduledTask> task,
                                             long delayTicks) {
        return new ScheduledTask(plugin, false);
    }

}
