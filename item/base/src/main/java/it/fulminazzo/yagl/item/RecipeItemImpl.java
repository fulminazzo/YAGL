package it.fulminazzo.yagl.item;

import it.fulminazzo.yagl.exception.NotImplementedException;
import it.fulminazzo.yagl.item.recipe.Recipe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

/**
 * An implementation of {@link RecipeItem}.
 */
class RecipeItemImpl extends ItemImpl implements RecipeItem {
    protected final List<Recipe> recipes = new ArrayList<>();

    /**
     * Instantiates a new Recipe item.
     */
    public RecipeItemImpl() {
        this(null);
    }

    /**
     * Instantiates a new Recipe item.
     *
     * @param material the material
     */
    public RecipeItemImpl(String material) {
        this(material, 1);
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
    public @NotNull RecipeItem addRecipes(final Recipe @NotNull ... recipes) {
        Collections.addAll(this.recipes, recipes);
        return this;
    }

    @Override
    public @NotNull RecipeItem clearRecipes(final @NotNull Predicate<Recipe> predicate) {
        this.recipes.removeIf(predicate);
        return this;
    }

    @NotNull
    @Override
    public Iterator<Recipe> iterator() {
        return this.recipes.iterator();
    }

    @Override
    public void registerRecipes() {
        throw new NotImplementedException();
    }

    @Override
    public void unregisterRecipes() {
        throw new NotImplementedException();
    }

    @Override
    public @NotNull RecipeItem setMaterial(@NotNull String material) {
        return (RecipeItem) super.setMaterial(material);
    }

    @Override
    public @NotNull RecipeItem setAmount(int amount) {
        return (RecipeItem) super.setAmount(amount);
    }

    @Override
    public @NotNull RecipeItem setDurability(int durability) {
        return (RecipeItem) super.setDurability(durability);
    }

    @Override
    public @NotNull RecipeItem setDisplayName(@NotNull String displayName) {
        return (RecipeItem) super.setDisplayName(displayName);
    }

    @Override
    public @NotNull RecipeItem setUnbreakable(boolean unbreakable) {
        return (RecipeItem) super.setUnbreakable(unbreakable);
    }

    @Override
    public @NotNull RecipeItem setCustomModelData(int customModelData) {
        return (RecipeItem) super.setCustomModelData(customModelData);
    }
}
