package it.angrybear.yagl.items;

import it.angrybear.yagl.items.recipes.Recipe;
import org.jetbrains.annotations.Nullable;

/**
 * An implementation of {@link Item} that supports a {@link Recipe}.
 */
public interface RecipeItem extends Item {

    /**
     * Sets recipe.
     *
     * @param recipe the recipe
     * @return the recipe
     */
    RecipeItem setRecipe(final @Nullable Recipe recipe);

    /**
     * Gets recipe.
     *
     * @return the recipe
     */
    @Nullable Recipe getRecipe();

    /**
     * Register recipe.
     */
    void registerRecipe();

    /**
     * Unregister recipe.
     */
    void unregisterRecipe();
}
