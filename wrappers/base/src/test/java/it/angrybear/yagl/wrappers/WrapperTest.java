package it.angrybear.yagl.wrappers;

import it.angrybear.yagl.TestUtils;
import it.fulminazzo.fulmicollection.interfaces.functions.ConsumerException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class WrapperTest {

    private static Wrapper[] testWrappers() {
        return new Wrapper[]{
                new Enchantment("ench"),
                new Potion("pot"),
                new PotionEffect("eff"),
                new Sound("snd")
        };
    }

    @ParameterizedTest
    @MethodSource("testWrappers")
    void testWrappersReturnType(Wrapper item) {
        TestUtils.testReturnType(item, Wrapper.class, null);
    }

    @Test
    void testValidValue() {
        assertDoesNotThrow(() -> new MockWrapper().setValue(5));
    }

    private static Object[][] getTestSetters() {
        return new Object[][]{
                new Object[]{(ConsumerException<MockWrapper>) wrapper -> wrapper.setValue(0), 1, 9},
                new Object[]{(ConsumerException<MockWrapper>) wrapper -> wrapper.setValue(10), 1, 9},
                new Object[]{(ConsumerException<MockWrapper>) wrapper -> wrapper.setMinOnly(0), 1, null},
                new Object[]{(ConsumerException<MockWrapper>) wrapper -> wrapper.setMaxOnly(10), null, 9},
        };
    }

    @ParameterizedTest
    @MethodSource("getTestSetters")
    void testSettersRanges(ConsumerException<MockWrapper> consumer, Integer min, Integer max) {
        final Class<? extends Exception> expected = IllegalArgumentException.class;
        try {
            MockWrapper wrapper = new MockWrapper();
            consumer.accept(wrapper);
            fail(String.format("Test should have thrown an %s", expected.getSimpleName()));
        } catch (Exception e) {
            assertInstanceOf(expected, e, String.format("Exception should have been %s, but was %s",
                    expected.getSimpleName(), e.getClass().getSimpleName()));

            final String message = e.getMessage();
            assertNotNull(message, "Error message should have not been null");

            if (min != null)
                assertTrue(message.contains(String.valueOf(min)), "Error message should have contained max value");
            else
                assertFalse(message.contains(String.valueOf(Integer.MIN_VALUE)), "Error message should have not contained min value");

            if (max != null)
                assertTrue(message.contains(String.valueOf(max)), "Error message should have contained min value");
            else
                assertFalse(message.contains(String.valueOf(Integer.MAX_VALUE)), "Error message should have not contained max value");
        }
    }

    @SuppressWarnings("unused")
    private static class MockWrapper extends Wrapper {
        @Range(min = 1, max = 9)
        private int value;
        @Range(min = 1)
        private int minOnly;
        @Range(max = 9)
        private int maxOnly;

        public void setValue(int value) {
            this.value = check(value);
        }
        public void setMinOnly(int minOnly) {
            this.minOnly = check(minOnly);
        }

        public void setMaxOnly(int maxOnly) {
            this.maxOnly = check(maxOnly);
        }

        @Override
        public String getName() {
            return null;
        }
    }
}