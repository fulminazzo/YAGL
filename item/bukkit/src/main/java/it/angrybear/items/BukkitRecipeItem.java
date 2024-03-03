package it.angrybear.items;

import it.angrybear.items.recipes.Recipe;
import org.jetbrains.annotations.Nullable;

/**
 * An implementation of {@link RecipeItem} and {@link BukkitItem}
 */
public interface BukkitRecipeItem extends RecipeItem, BukkitItem {

    @Override
    BukkitRecipeItem setRecipe(@Nullable Recipe recipe);
}
