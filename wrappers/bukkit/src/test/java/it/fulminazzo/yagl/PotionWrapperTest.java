package it.fulminazzo.yagl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class PotionWrapperTest {

    @Test
    void testCreationOfPotionWrapperFromForeignObject() {
        assertThrowsExactly(IllegalArgumentException.class, () -> new PotionWrapper("Mock potion"));
    }

}
