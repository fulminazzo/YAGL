package it.fulminazzo.yagl.items;

import it.fulminazzo.yagl.exceptions.NotImplementedException;
import it.fulminazzo.yagl.items.recipes.FurnaceRecipe;
import it.fulminazzo.yagl.items.recipes.ShapedRecipe;
import it.fulminazzo.yagl.items.recipes.ShapelessRecipe;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecipeItemTest {

    @Test
    void testClearRecipes() {
        assertFalse(Item.newRecipeItem().setRecipes(
                        new ShapelessRecipe("shapeless"),
                        new ShapedRecipe("shaped"),
                        new FurnaceRecipe("furnace"))
                .clearRecipes().iterator().hasNext(), "After calling clearRecipes, there should be no recipes saved");
    }

    @Test
    void recipeItemRegisterShouldBeUnimplemented() {
        assertThrowsExactly(NotImplementedException.class, () -> Item.newRecipeItem().registerRecipes());
    }

    @Test
    void recipeItemUnregisterShouldBeUnimplemented() {
        assertThrowsExactly(NotImplementedException.class, () -> Item.newRecipeItem().unregisterRecipes());
    }
}