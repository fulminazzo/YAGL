package org.bukkit.craftbukkit.v1_8_R3.scheduler;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

@Getter
@Setter
public class CraftTask implements BukkitTask {
    private Plugin owner;
    private int taskId;
    private boolean sync;
    private long period;

    @Override
    public void cancel() {
        this.period = -2;
    }

    public boolean isCancelled() {
        throw new NoSuchMethodError("Could not find isCancelled method");
    }

}
