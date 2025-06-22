package it.fulminazzo.yagl.item;

import it.fulminazzo.yagl.exception.NotImplementedException;
import it.fulminazzo.yagl.item.recipe.FurnaceRecipe;
import it.fulminazzo.yagl.item.recipe.ShapedRecipe;
import it.fulminazzo.yagl.item.recipe.ShapelessRecipe;
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