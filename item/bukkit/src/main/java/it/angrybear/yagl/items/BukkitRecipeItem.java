package it.angrybear.yagl.items;

import it.angrybear.yagl.items.recipes.Recipe;
import org.jetbrains.annotations.Nullable;

/**
 * An implementation of {@link RecipeItem} and {@link BukkitItem}
 */
public interface BukkitRecipeItem extends RecipeItem, BukkitItem {

    @Override
    BukkitRecipeItem setRecipe(@Nullable Recipe recipe);
}
