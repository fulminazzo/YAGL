package it.fulminazzo.yagl.particle;

import it.fulminazzo.fulmicollection.objects.Refl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class BlockDataOptionTest {

    private static Object[][] testData() {
        return new Object[][]{
                new Object[]{"this:item:is:invalid", "INVALID_MATERIAL"},
                new Object[]{"  ", "INVALID_MATERIAL"},
                new Object[]{"minecraft:  ", "INVALID_MATERIAL"},
                new Object[]{"[this one too]", "INVALID_DATA"}
        };
    }

    @ParameterizedTest
    @MethodSource("testData")
    void testInvalidData(String raw, String field) {
        String minecraftId = new Refl<>(BlockDataOption.class).getFieldObject("MINECRAFT_IDENTIFIER");
        assertNotNull(minecraftId, "Could not find Minecraft identifier");
        Throwable throwable = assertThrowsExactly(IllegalArgumentException.class, () -> new BlockDataOption(raw));
        testThrowable(throwable, field, raw.contains(minecraftId) ? raw.substring(minecraftId.length()) : raw);
    }

    private void testThrowable(Throwable throwable, String fieldName, String value) {
        final Refl<?> refl = new Refl<>(BlockDataOption.class);
        final String actual = throwable.getMessage();
        assertNotNull(actual, String.format("'%s' message should not be null", throwable.getClass().getSimpleName()));
        final String expected = String.format((String) refl.getFieldObject(fieldName), value);
        assertEquals(expected, actual);
    }

    @Test
    void testEmptyNBT() {
        final String expected = "diamond_sword";
        BlockDataOption option = new BlockDataOption(expected + "[]");
        assertEquals(expected, option.getOption());
        assertEquals("", option.getNBT());
    }

}