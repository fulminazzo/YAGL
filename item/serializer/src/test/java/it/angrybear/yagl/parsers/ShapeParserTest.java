package it.angrybear.yagl.parsers;

import it.fulminazzo.yamlparser.configuration.IConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ShapeParserTest {

    @Test
    void testInvalidShape() {
        IConfiguration configuration = mock(IConfiguration.class);
        when(configuration.getString(any())).thenReturn("INVALID");
        assertThrowsExactly(IllegalArgumentException.class, () ->
                new ShapeParser().getLoader().apply(configuration, "a"));
    }
}