package it.fulminazzo.yagl.items.recipes;

import it.fulminazzo.yagl.items.Item;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of {@link Recipe} to express the shapeless recipes in Minecraft.
 * An example of shapeless recipe is the yellow dye, which can be obtained by placing the flower in any position.
 */
public final class ShapelessRecipe extends RecipeImpl {
    static final int MAX_SIZE = 9;
    private final @NotNull List<Item> ingredients;

    private ShapelessRecipe() {
        this("pending");
    }

    /**
     * Instantiates a new Shapeless recipe.
     *
     * @param id the id
     */
    public ShapelessRecipe(final @NotNull String id) {
        super(id);
        this.ingredients = new ArrayList<>();
    }

    @Override
    public @NotNull ShapelessRecipe setOutput(final @NotNull Item output) {
        this.output = output.copy(Item.class);
        return this;
    }

    /**
     * Add ingredients.
     *
     * @param items the items
     * @return this recipe
     */
    public @NotNull ShapelessRecipe addIngredients(final Item @NotNull ... items) {
        for (final Item item : items) addIngredient(item);
        return this;
    }

    /**
     * Add ingredient.
     * If the size of {@link #ingredients} is superior to {@link #MAX_SIZE}, an {@link IllegalStateException} is thrown.
     *
     * @param item the item
     * @return this recipe
     */
    public @NotNull ShapelessRecipe addIngredient(final @NotNull Item item) {
        if (this.ingredients.size() >= MAX_SIZE)
            throw new IllegalStateException(String.format("Cannot add ingredient to ingredients as maximum size of %s has been reached", MAX_SIZE));
        this.ingredients.add(item.copy(Item.class));
        return this;
    }

    /**
     * Remove ingredients.
     *
     * @param items the items
     * @return this recipe
     */
    public @NotNull ShapelessRecipe removeIngredients(final Item @NotNull ... items) {
        for (final Item item : items) removeIngredient(item);
        return this;
    }

    /**
     * Remove ingredient.
     *
     * @param item the item
     * @return this recipe
     */
    public @NotNull ShapelessRecipe removeIngredient(final @NotNull Item item) {
        this.ingredients.removeIf(i -> i.equals(item));
        return this;
    }

    @Override
    public @NotNull List<Item> getIngredients() {
        return new ArrayList<>(this.ingredients);
    }
}
