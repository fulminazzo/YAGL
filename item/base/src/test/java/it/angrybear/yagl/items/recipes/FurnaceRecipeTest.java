package it.angrybear.yagl.items.recipes;

import it.angrybear.yagl.Constants;
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