package it.fulminazzo.yagl.scheduler;

import io.papermc.paper.FoliaServer;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import it.fulminazzo.yagl.TestUtils;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.*;

class SchedulerTest {

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testGetSchedulerReturnsAppropriateScheduler(boolean isFolia) {
        TestUtils.mockReflectionUtils(() -> {
            FoliaServer server = mock(FoliaServer.class, withSettings().extraInterfaces(Server.class));
            new Refl<>(Bukkit.class).setFieldObject("server", server);

            when(ReflectionUtils.getClass(GlobalRegionScheduler.class.getCanonicalName())).thenAnswer(a -> {
                if (isFolia) return a.callRealMethod();
                else throw new IllegalArgumentException("Class not found!");
            });

            Scheduler scheduler = Scheduler.getScheduler();
            Class<? extends Scheduler> expected = isFolia ? FoliaScheduler.class : BukkitScheduler.class;

            assertInstanceOf(expected, scheduler);
        });
    }

}