package it.angrybear.yagl.items;

import it.angrybear.yagl.TestUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MovablePersistentItemTest {

    @Test
    void testConstructors() {
        Constructor<?>[] constructors = MovablePersistentItem.class.getDeclaredConstructors();
        for (Constructor<?> c : constructors)
            assertDoesNotThrow(() -> c.newInstance(Arrays.stream(c.getParameterTypes())
                    .map(TestUtils::mockParameter).toArray(Object[]::new)));
    }
}