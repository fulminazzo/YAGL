package it.fulminazzo.yagl.scheduler;

import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yagl.TestUtils;
import org.bukkit.Folia;
import org.bukkit.scheduler.FoliaTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.mockito.Mockito.*;

class FoliaSchedulerTest {
    private GlobalRegionScheduler actualScheduler;
    private FoliaScheduler scheduler;

    private ScheduledTask actualTask;
    private FoliaScheduler.FoliaSchedulerTask task;

    @BeforeEach
    void setUp() {
        this.actualScheduler = mock(GlobalRegionScheduler.class);

        this.scheduler = new FoliaScheduler();

        this.actualTask = mock(ScheduledTask.class);
        this.task = new FoliaScheduler.FoliaSchedulerTask(this.actualTask);
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

        new Refl<>(verify(this.actualScheduler)).invokeMethod(actualMethodName,
                method.getParameterTypes(),
                parameters);
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