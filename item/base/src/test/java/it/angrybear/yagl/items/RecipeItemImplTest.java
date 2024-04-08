package it.angrybear.yagl.items;

import it.angrybear.yagl.exceptions.NotImplemented;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecipeItemImplTest {

    @Test
    void recipeItemRegisterShouldBeUnimplemented() {
        assertThrowsExactly(NotImplemented.class, () -> Item.newRecipeItem().registerRecipes());
    }

    @Test
    void recipeItemUnregisterShouldBeUnimplemented() {
        assertThrowsExactly(NotImplemented.class, () -> Item.newRecipeItem().registerRecipes());
    }
}