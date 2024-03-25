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
