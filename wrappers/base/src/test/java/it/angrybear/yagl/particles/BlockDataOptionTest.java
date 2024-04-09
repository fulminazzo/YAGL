package it.angrybear.yagl.particles;

import it.fulminazzo.fulmicollection.objects.Refl;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class BlockDataOptionTest {

    private static Object[][] testData() {
        return new Object[][]{
                new Object[]{"this:item:is:invalid", "INVALID_MATERIAL"},
                new Object[]{"[this one too]", "INVALID_DATA"}
        };
    }

    @ParameterizedTest
    @MethodSource("testData")
    void testInvalidData(String raw, String field) {
        Throwable throwable = assertThrowsExactly(IllegalArgumentException.class, () -> new BlockDataOption(raw));
        testThrowable(throwable, field, raw);
    }

    private void testThrowable(Throwable throwable, String fieldName, String value) {
        final Refl<?> refl = new Refl<>(BlockDataOption.class);
        final String actual = throwable.getMessage();
        assertNotNull(actual, String.format("'%s' message should not be null", throwable.getClass().getSimpleName()));
        final String expected = String.format((String) refl.getFieldObject(fieldName), value);
        assertEquals(expected, actual);
    }
}