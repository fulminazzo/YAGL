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

    /**
     * Instantiates a new Recipe item.
     */
    public RecipeItemImpl() {
        super();
    }

    /**
     * Instantiates a new Recipe item.
     *
     * @param material the material
     */
    public RecipeItemImpl(String material) {
        super(material);
    }

    /**
     * Instantiates a new Recipe item.
     *
     * @param material the material
     * @param amount   the amount
     */
    public RecipeItemImpl(String material, int amount) {
        super(material, amount);
    }

    @Override
    public RecipeItem setRecipe(final @Nullable Recipe recipe) {
        this.recipe = recipe;
        if (recipe != null) recipe.setOutput(this);
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
