package it.fulminazzo.yagl.parsers;

import it.fulminazzo.yagl.ParserTestHelper;
import it.fulminazzo.yagl.items.recipes.ShapedRecipe;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class ShapeParserTest extends ParserTestHelper<ShapedRecipe.Shape> {

    @Test
    void testInvalidShape() {
        assertThrowsExactly(IllegalArgumentException.class, () ->
                getLoader().apply(prepareConfiguration("INVALID"), "a"));
    }

    @Override
    protected Class<?> getParser() {
        return ShapeParser.class;
    }
}