package io.papermc.paper;

import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import org.jetbrains.annotations.NotNull;

public class FoliaServer {

    public @NotNull GlobalRegionScheduler getGlobalRegionScheduler() {
        return new GlobalRegionScheduler();
    }

}
