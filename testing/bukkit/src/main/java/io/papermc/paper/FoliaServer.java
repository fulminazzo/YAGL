package io.papermc.paper;

import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;

public class FoliaServer {

    public GlobalRegionScheduler getGlobalRegionScheduler() {
        return new GlobalRegionScheduler();
    }

}
