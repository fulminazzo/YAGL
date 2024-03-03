package it.angrybear.items;

import it.angrybear.items.recipes.Recipe;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

/**
 * An implementation of {@link RecipeItem}.
 */
@Getter
class RecipeItemImpl extends ItemImpl implements RecipeItem {
    protected Recipe recipe;

    @Override
    public RecipeItem setRecipe(@Nullable Recipe recipe) {
        this.recipe = recipe;
        return this;
    }

    @Override
    public void registerRecipe() {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public void unregisterRecipe() {
        throw new IllegalStateException("Not implemented");
    }
}
