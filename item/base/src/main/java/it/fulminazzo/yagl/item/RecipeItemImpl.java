package it.fulminazzo.yagl.item;

import it.fulminazzo.yagl.exception.NotImplementedException;
import it.fulminazzo.yagl.item.recipe.Recipe;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

/**
 * An implementation of {@link RecipeItem}.
 */
@EqualsAndHashCode(callSuper = true)
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
    public RecipeItemImpl(final @Nullable String material) {
        this(material, 1);
    }

    /**
     * Instantiates a new Recipe item.
     *
     * @param material the material
     * @param amount   the amount
     */
    public RecipeItemImpl(final @Nullable String material, final int amount) {
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
    public @NotNull RecipeItem setMaterial(final @NotNull String material) {
        return (RecipeItem) super.setMaterial(material);
    }

    @Override
    public @NotNull RecipeItem setAmount(final @Range(from = 1, to = Integer.MAX_VALUE) int amount) {
        return (RecipeItem) super.setAmount(amount);
    }

    @Override
    public @NotNull RecipeItem setDurability(final @Range(from = 0, to = Short.MAX_VALUE) int durability) {
        return (RecipeItem) super.setDurability(durability);
    }

    @Override
    public @NotNull RecipeItem setDisplayName(final @NotNull String displayName) {
        return (RecipeItem) super.setDisplayName(displayName);
    }

    @Override
    public @NotNull RecipeItem setUnbreakable(final boolean unbreakable) {
        return (RecipeItem) super.setUnbreakable(unbreakable);
    }

    @Override
    public @NotNull RecipeItem setCustomModelData(final int customModelData) {
        return (RecipeItem) super.setCustomModelData(customModelData);
    }

    @Override
    public @NotNull String toString() {
        String base = super.toString();
        return base.substring(0, base.length() - 1) + String.format(", recipes=%s}", this.recipes.size());
    }

}
