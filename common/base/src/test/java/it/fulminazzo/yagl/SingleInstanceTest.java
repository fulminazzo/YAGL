package it.fulminazzo.yagl;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yagl.exceptions.InstanceAlreadyInitializedException;
import it.fulminazzo.yagl.exceptions.InstanceNotInitializedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SingleInstanceTest {

    @BeforeEach
    void setUp() {
        // Manually clear instances map to avoid conflicts
        new Refl<>(SingleInstance.class)
                .getFieldRefl("INSTANCES_MAP")
                .invokeMethod("clear");
    }

    @Test
    void testNormalBehaviour() {
        SingleInstance instance = new MockInstance();
        assertDoesNotThrow(() -> {
            instance.initialize();
            SingleInstance.getInstance(MockInstance.class);
            instance.terminate();
        });
    }

    @Test
    void testInitAlreadyInitialized() {
        SingleInstance instance = new MockInstance();
        instance.initialize();
        InstanceAlreadyInitializedException exception = assertThrowsExactly(
                InstanceAlreadyInitializedException.class, instance::initialize
        );
        assertEquals(instance, exception.getInstance());
    }

    @Test
    void testTerminateNotInitialized() {
        SingleInstance instance = new MockInstance();
        InstanceNotInitializedException exception = assertThrowsExactly(
                InstanceNotInitializedException.class, instance::terminate
        );
        assertEquals("Instance of type \"" + MockInstance.class.getSimpleName() +
                "\" has not been initialized yet", exception.getMessage());
    }

    @Test
    void testGetInstanceNotInitialized() {
        InstanceNotInitializedException exception = assertThrowsExactly(
                InstanceNotInitializedException.class, () -> SingleInstance.getInstance(MockInstance.class)
        );
        assertEquals("Instance of type \"" + MockInstance.class.getSimpleName() +
                "\" has not been initialized yet", exception.getMessage());
    }

    private static class MockInstance extends SingleInstance {

        public MockInstance() {

        }

    }

}