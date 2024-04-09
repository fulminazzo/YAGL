package it.angrybear.yagl.parsers;

import it.fulminazzo.yamlparser.configuration.IConfiguration;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ShapeParserTest {

    @Test
    void testInvalidShape() {
        assertThrowsExactly(IllegalArgumentException.class, () ->
                new ShapeParser().getLoader().apply(prepareConfiguration("INVALID"),
                        "a"));
    }

    @Test
    void testLoadNull() throws Exception {
        assertNull(new ShapeParser().getLoader()
                .apply(prepareConfiguration(null), "shape"));
    }

    @Test
    void testSaveNull() {
        assertDoesNotThrow(() -> new ShapeParser().getDumper()
                .accept(prepareConfiguration(null), "shape", null));
    }

    private static @NotNull IConfiguration prepareConfiguration(String returnValue) {
        IConfiguration configuration = mock(IConfiguration.class);
        when(configuration.getString(any())).thenReturn(returnValue);
        return configuration;
    }
}