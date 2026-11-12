package it.fulminazzo.yagl.wrapper;

import it.fulminazzo.yagl.TestUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;

class WrapperTest {

    private static Wrapper[] testWrappers() {
        return new Wrapper[]{
                new Enchantment("ench"),
                new Potion("pot"),
                new PotionEffect("eff"),
                new Sound("snd")
        };
    }

    @SuppressWarnings("unchecked")
    @ParameterizedTest
    @MethodSource("testWrappers")
    void testWrappersReturnType(Wrapper item) {
        TestUtils.testReturnType(item, (Class<? super Wrapper>) item.getClass(), null);
    }

    @Test
    void testIsSimilarNull() {
        Wrapper w1 = new MockWrapper();
        assertFalse(w1.isSimilar(null), "Wrapper should not be similar to null");
    }

    @Test
    void testIsSimilarOtherClass() {
        Wrapper w1 = new MockWrapper();
        assertFalse(w1.isSimilar(mock(Wrapper.class)), "Wrapper should not be similar to a wrapper of different class");
    }

    @Getter
    @Setter
    @EqualsAndHashCode(callSuper = false)
    @ToString
    private static class MockWrapper extends Wrapper {
        private int value;
        private int minOnly;
        private int maxOnly;
        private int free;

        @Override
        public @NotNull String getName() {
            return "aiden";
        }

    }

}
