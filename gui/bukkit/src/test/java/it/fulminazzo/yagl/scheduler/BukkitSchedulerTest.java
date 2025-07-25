package it.fulminazzo.yagl.scheduler;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.jbukkit.BukkitUtils;
import it.fulminazzo.jbukkit.annotations.After1_;
import it.fulminazzo.yagl.TestUtils;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_8_R3.scheduler.CraftTask;
import org.bukkit.scheduler.BukkitTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BukkitSchedulerTest extends BukkitUtils {
    private org.bukkit.scheduler.BukkitScheduler actualScheduler;
    private BukkitScheduler scheduler;

    private BukkitTask actualTask;
    private BukkitScheduler.BukkitSchedulerTask task;

    @BeforeEach
    protected void setUp() {
        super.setUp();
        this.actualScheduler = mock(org.bukkit.scheduler.BukkitScheduler.class);

        Server server = mock(Server.class);
        when(server.getScheduler()).thenReturn(this.actualScheduler);
        new Refl<>(Bukkit.class).setFieldObject("server", server);

        this.scheduler = new BukkitScheduler();

        this.actualTask = mock(BukkitTask.class);
        this.task = new BukkitScheduler.BukkitSchedulerTask(this.actualTask);
    }

    private static Object[][] bukkitSchedulerMethods() {
        return new Object[][]{
                new Object[]{"run", "runTask"},
                new Object[]{"runAsync", "runTaskAsynchronously"},
                new Object[]{"runLater", "runTaskLater"},
                new Object[]{"runLaterAsync", "runTaskLaterAsynchronously"},
                new Object[]{"runRepeated", "runTaskTimer"},
                new Object[]{"runRepeatedAsync", "runTaskTimerAsynchronously"},
        };
    }

    @ParameterizedTest
    @MethodSource("bukkitSchedulerMethods")
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

    private static Object[][] bukkitTaskMethods() {
        return new Object[][]{
                new Object[]{"cancel", "cancel"},
                new Object[]{"isCancelled", "isCancelled"},
                new Object[]{"getOwningPlugin", "getOwner"},
        };
    }

    @ParameterizedTest
    @MethodSource("bukkitTaskMethods")
    @After1_(12)
    void testTaskMethods(String methodName, String actualMethodName) {
        check();
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

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testLegacyIsCancelledMethod(boolean cancel) {
        BukkitTask actualTask = new CraftTask();
        BukkitScheduler.BukkitSchedulerTask task = new BukkitScheduler.BukkitSchedulerTask(actualTask);

        if (cancel) actualTask.cancel();

        assertEquals(cancel, task.isCancelled());
    }
    
}
