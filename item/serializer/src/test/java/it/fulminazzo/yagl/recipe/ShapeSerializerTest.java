package it.fulminazzo.yagl.recipe;

import it.fulminazzo.yagl.item.recipe.ShapedRecipe;
import it.fulminazzo.yagl.serializer.SerializationException;
import it.fulminazzo.yagl.serializer.SerializerTestHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ShapeSerializerTest extends SerializerTestHelper<ShapedRecipe.Shape> {

    protected ShapeSerializerTest() {
        super(ShapedRecipe.Shape.class, new ShapeSerializer());
    }

    @Test
    void testDeserialize() throws IOException {
        ShapedRecipe.Shape shape = load("shape");
        assertNotNull(shape);
        assertEquals(2, shape.getRows());
        assertEquals(3, shape.getColumns());
    }

    @Test
    void testInvalidFormatDeserialization() {
        IOException exception = assertThrows(IOException.class, () -> load("invalid_shape"));
        Throwable cause = exception.getCause();
        assertInstanceOf(SerializationException.class, cause);
    }

    @Test
    void testSerialize() throws IOException {
        ShapedRecipe.Shape shape = new ShapedRecipe.Shape(3, 2);
        String raw = readRawString(dump(shape, "shape"));
        assertEquals("3x2", raw);
    }

}