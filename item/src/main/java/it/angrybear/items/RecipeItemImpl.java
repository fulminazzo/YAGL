package it.angrybear.items;

import it.angrybear.items.recipes.Recipe;
import lombok.Getter;
import lombok.Setter;

/**
 * An implementation of {@link RecipeItem}.
 */
@Getter
@Setter
class RecipeItemImpl extends ItemImpl implements RecipeItem {
    private Recipe recipe;

    @Override
    public void registerRecipe() {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public void unregisterRecipe() {
        throw new IllegalStateException("Not implemented");
    }
}
