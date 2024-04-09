package it.angrybear.yagl.parsers;

import it.angrybear.yagl.ParserTestHelper;
import it.angrybear.yagl.items.recipes.ShapedRecipe;
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