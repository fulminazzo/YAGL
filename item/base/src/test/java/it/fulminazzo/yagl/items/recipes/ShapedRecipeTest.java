package it.fulminazzo.yagl.items.recipes;

import it.fulminazzo.yagl.items.Item;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class ShapedRecipeTest {

    @Test
    void testSetIngredientsBasedOnShape() {
        for (int rows = 1; rows <= 3; rows++)
            for (int columns = 1; columns <= 3; columns++) {
                ShapedRecipe recipe = new ShapedRecipe("test").setShape(rows, columns);
                for (int i = 0; i < rows * columns; i++) {
                    int finalI = i;
                    assertDoesNotThrow(() -> recipe.setIngredient(finalI, Item.newItem()));
                }
            }
    }

    @Test
    void testInvalidSet() {
        ShapedRecipe shapedRecipe = new ShapedRecipe("test").setShape(1, 1);
        assertThrowsExactly(IllegalArgumentException.class, () -> shapedRecipe.setIngredient(4, Item.newItem()));
    }

    @Test
    void testInvalidSetOfIngredients() {
        ShapedRecipe shapedRecipe = new ShapedRecipe("test").setShape(1, 1);
        Item[] items = new Item[4];
        Arrays.fill(items, Item.newItem());
        assertThrowsExactly(IllegalArgumentException.class, () -> shapedRecipe.setIngredients(items));
    }
}
