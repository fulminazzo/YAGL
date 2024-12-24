package it.fulminazzo.yagl;

import it.fulminazzo.jbukkit.BukkitUtils;
import it.fulminazzo.jbukkit.annotations.Before1_;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class PotionWrapperTest extends BukkitUtils {

    @Test
    @Before1_(20.4)
    void testCreationOfPotionWrapperFromForeignObject() {
        super.setUp();
        assertThrowsExactly(IllegalArgumentException.class, () -> new PotionWrapper("Mock potion"));
    }

}
