package it.angrybear.yagl.wrappers;

import it.angrybear.yagl.TestUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

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

}