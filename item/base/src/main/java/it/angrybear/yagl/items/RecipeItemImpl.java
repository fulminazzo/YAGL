package it.angrybear.yagl.items;

import it.angrybear.yagl.items.recipes.Recipe;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * An implementation of {@link RecipeItem}.
 */
@Getter
class RecipeItemImpl extends ItemImpl implements RecipeItem {
    protected final List<Recipe> recipes = new ArrayList<>();

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
    public RecipeItem addRecipes(final Recipe @NotNull ... recipes) {
        Collections.addAll(this.recipes, recipes);
        return this;
    }

    @Override
    public RecipeItem clearRecipes(final Predicate<Recipe> predicate) {
        this.recipes.removeIf(predicate);
        return this;
    }

    @Override
    public void registerRecipes() {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public void unregisterRecipes() {
        throw new IllegalStateException("Not implemented");
    }
}
