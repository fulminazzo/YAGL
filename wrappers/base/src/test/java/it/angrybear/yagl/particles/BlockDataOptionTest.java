package it.angrybear.yagl.particles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlockDataOptionTest {

    @Test
    void testInvalidMaterial() {
        assertThrowsExactly(IllegalArgumentException.class, () -> new BlockDataOption("this:item:is:invalid"));
    }
}