package it.fulminazzo.yagl.items;

import it.fulminazzo.yagl.exceptions.NotImplementedException;
import it.fulminazzo.yagl.items.recipes.Recipe;
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
    public RecipeItem addRecipes(final Recipe @NotNull ... recipes) {
        Collections.addAll(this.recipes, recipes);
        return this;
    }

    @Override
    public RecipeItem clearRecipes(final Predicate<Recipe> predicate) {
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
    public RecipeItem setMaterial(@NotNull String material) {
        return (RecipeItem) super.setMaterial(material);
    }

    @Override
    public RecipeItem setAmount(int amount) {
        return (RecipeItem) super.setAmount(amount);
    }

    @Override
    public RecipeItem setDurability(int durability) {
        return (RecipeItem) super.setDurability(durability);
    }

    @Override
    public RecipeItem setDisplayName(@NotNull String displayName) {
        return (RecipeItem) super.setDisplayName(displayName);
    }

    @Override
    public RecipeItem setUnbreakable(boolean unbreakable) {
        return (RecipeItem) super.setUnbreakable(unbreakable);
    }

    @Override
    public RecipeItem setCustomModelData(int customModelData) {
        return (RecipeItem) super.setCustomModelData(customModelData);
    }
}
