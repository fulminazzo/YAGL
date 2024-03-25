package it.angrybear.yagl.items;

import it.angrybear.yagl.items.recipes.Recipe;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/**
 * An implementation of {@link Item} that supports a {@link Recipe}.
 */
public interface RecipeItem extends Item {

    default RecipeItem setRecipes(final Recipe @NotNull ... recipes) {
        return clearRecipes().addRecipes(recipes);
    }

    RecipeItem addRecipes(final Recipe @NotNull ... recipes);

    RecipeItem clearRecipes(final Predicate<Recipe> predicate);

    default RecipeItem clearRecipes() {
        return clearRecipes(t -> true);
    }

    void registerRecipes();

    void unregisterRecipes();
}
