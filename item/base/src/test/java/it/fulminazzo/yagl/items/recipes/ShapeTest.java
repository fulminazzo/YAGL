package it.fulminazzo.yagl.items.recipes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ShapeTest {

    @Test
    void shapesShouldOnlyEqualWithSameRowsAndColumns() {
        ShapedRecipe.Shape s1 = new ShapedRecipe.Shape(3, 2);
        ShapedRecipe.Shape s2 = new ShapedRecipe.Shape(3, 2);
        ShapedRecipe.Shape s3 = new ShapedRecipe.Shape(2, 3);

        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
        assertNotEquals(s1, s3);
        assertNotEquals(s1.hashCode(), s3.hashCode());
    }

    @Test
    void testColumnsBounds() {
        assertThrowsExactly(IllegalArgumentException.class, () ->
                new ShapedRecipe.Shape().setColumns(ShapedRecipe.Shape.MIN_COLUMNS - 1));
        assertThrowsExactly(IllegalArgumentException.class, () ->
                new ShapedRecipe.Shape().setColumns(ShapedRecipe.Shape.MAX_COLUMNS + 1));
    }

    @Test
    void testRowsBounds() {
        assertThrowsExactly(IllegalArgumentException.class, () ->
                new ShapedRecipe.Shape().setRows(ShapedRecipe.Shape.MIN_ROWS - 1));
        assertThrowsExactly(IllegalArgumentException.class, () ->
                new ShapedRecipe.Shape().setRows(ShapedRecipe.Shape.MAX_ROWS + 1));
    }

    @Test
    void testShapeEquality() {
        assertEquals(new ShapedRecipe.Shape(3, 3), new ShapedRecipe.Shape(3, 3));
    }

    @Test
    void testShapeInequality() {
        assertNotEquals(new ShapedRecipe.Shape(3, 3), new ShapedRecipe.Shape(3, 2));
    }

    @Test
    void testShapeObjectInequality() {
        assertNotEquals(new ShapedRecipe.Shape(3, 3), new Object());
    }

    @Test
    void shapeToStringShouldContainRowsAndColumns() {
        ShapedRecipe.Shape shape = new ShapedRecipe.Shape(1, 2);
        String toString = shape.toString();
        assertTrue(toString.contains(shape.getColumns() + ""), "Shape#toString did not contain columns");
        assertTrue(toString.contains(shape.getRows() + ""), "Shape#toString did not contain rows");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, Integer.MIN_VALUE, ShapedRecipe.Shape.MAX_COLUMNS * ShapedRecipe.Shape.MAX_ROWS})
    void testShapeContain(int value) {
        assertFalse(new ShapedRecipe.Shape().contains(value), String.format("Shape should not contain value '%s'", value));
    }
}