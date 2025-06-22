package it.fulminazzo.yagl.item.recipe;

import it.fulminazzo.yagl.Constants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FurnaceRecipeTest {

    @Test
    void cookingTimeInTicksShouldBeNatural() {
        final double cookingTime = 15.75891554289;
        FurnaceRecipe recipe = new FurnaceRecipe("id").setCookingTime(cookingTime);
        assertEquals((long) (cookingTime * Constants.TICKS_IN_SECOND), recipe.getCookingTimeInTicks());
    }
}