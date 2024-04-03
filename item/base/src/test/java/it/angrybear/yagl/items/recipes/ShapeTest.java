package it.angrybear.yagl.items.recipes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShapeTest {

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
}