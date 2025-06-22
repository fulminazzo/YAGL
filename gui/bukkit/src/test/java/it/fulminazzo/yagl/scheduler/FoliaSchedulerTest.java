package it.fulminazzo.yagl.scheduler;

import io.papermc.paper.FoliaServer;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yagl.TestUtils;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class FoliaSchedulerTest {
    private GlobalRegionScheduler actualScheduler;
    private FoliaScheduler scheduler;

    private ScheduledTask actualTask;
    private FoliaScheduler.FoliaSchedulerTask task;

    @BeforeEach
    void setUp() {
        this.actualScheduler = mock(GlobalRegionScheduler.class);

        FoliaServer server = mock(FoliaServer.class, withSettings().extraInterfaces(Server.class));
        when(server.getGlobalRegionScheduler()).thenReturn(this.actualScheduler);
        new Refl<>(Bukkit.class).setFieldObject("server", server);

        this.scheduler = spy(FoliaScheduler.class);
        when(this.scheduler.runnableToConsumer(any())).thenReturn(null);

        this.actualTask = mock(ScheduledTask.class);
        this.task = new FoliaScheduler.FoliaSchedulerTask(this.actualTask);
    }

    @Test
    void testRunnableToConsumer() {
        FoliaScheduler scheduler = new FoliaScheduler();
        AtomicBoolean called = new AtomicBoolean(false);

        scheduler.runnableToConsumer(() -> called.set(true)).accept(null);

        assertTrue(called.get(), "Runnable not called");
    }

    private static Object[][] foliaSchedulerMethods() {
        return new Object[][]{
                new Object[]{"run", "run"},
                new Object[]{"runAsync", "run"},
                new Object[]{"runLater", "runDelayed"},
                new Object[]{"runLaterAsync", "runDelayed"},
                new Object[]{"runRepeated", "runAtFixedRate"},
                new Object[]{"runRepeatedAsync", "runAtFixedRate"},
        };
    }

    @ParameterizedTest
    @MethodSource("foliaSchedulerMethods")
    void testSchedulerMethods(String methodName, String actualMethodName) {
        Refl<?> refl = new Refl<>(this.scheduler);

        Method method = refl.getMethods(m -> m.getName().equals(methodName)).get(0);
        Object[] parameters = Arrays.stream(method.getParameterTypes())
                .map(TestUtils::mockParameter)
                .toArray();

        refl.invokeMethod(methodName, method.getParameterTypes(), parameters);

        Class<?>[] parameterTypes = Arrays.stream(method.getParameterTypes())
                .map(c -> Runnable.class.isAssignableFrom(c) ? Consumer.class : c)
                .toArray(Class[]::new);

        Object[] expectedParameters = Arrays.stream(parameters)
                .map(p -> p instanceof Runnable ? null : p)
                .toArray();

        new Refl<>(verify(this.actualScheduler)).invokeMethod(actualMethodName,
                parameterTypes,
                expectedParameters);
    }

    private static Object[][] foliaTaskMethods() {
        return new Object[][]{
                new Object[]{"cancel", "cancel"},
                new Object[]{"isCancelled", "isCancelled"},
                new Object[]{"getOwningPlugin", "getOwningPlugin"},
        };
    }

    @ParameterizedTest
    @MethodSource("foliaTaskMethods")
    void testTaskMethods(String methodName, String actualMethodName) {
        Refl<?> refl = new Refl<>(this.task);

        Method method = refl.getMethods(m -> m.getName().equals(methodName)).get(0);
        Object[] parameters = Arrays.stream(method.getParameterTypes())
                .map(TestUtils::mockParameter)
                .toArray();

        refl.invokeMethod(methodName, method.getParameterTypes(), parameters);

        new Refl<>(verify(this.actualTask)).invokeMethod(actualMethodName,
                method.getParameterTypes(),
                parameters);
    }

}
