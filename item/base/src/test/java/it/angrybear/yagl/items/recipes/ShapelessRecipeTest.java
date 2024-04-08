package it.angrybear.yagl.items.recipes;

import it.angrybear.yagl.items.Item;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShapelessRecipeTest {

    @Test
    void testAddTooManyItems() {
        Item[] items = new Item[ShapelessRecipe.MAX_SIZE + 1];
        Arrays.fill(items, Item.newItem());
        assertThrowsExactly(IllegalStateException.class, () -> new ShapelessRecipe("tmp").addIngredients(items));
    }

    @Test
    void testRemoveItems() {
        List<Item> items = new LinkedList<>();
        for (int i = 0; i < ShapelessRecipe.MAX_SIZE; i++) items.add(Item.newItem("MOCK_MATERIAL_" + i));
        ShapelessRecipe recipe = new ShapelessRecipe("tmp")
                .addIngredients(items.toArray(new Item[0]))
                .removeIngredients(items.toArray(new Item[0]));
        assertTrue(recipe.isEmpty(), "Recipe should have been empty after removing items");
    }
}