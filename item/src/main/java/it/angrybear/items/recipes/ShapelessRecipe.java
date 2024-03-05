package it.angrybear.items.recipes;

import it.angrybear.items.Item;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of {@link Recipe} to express the shapeless recipes in Minecraft.
 * An example of shapeless recipe is the yellow dye, which can be obtained by placing the flower in any position.
 */
public class ShapelessRecipe implements Recipe {
    private static final int MAX_SIZE = 9;
    @Getter
    private final String id;
    private final List<Item> ingredients;
    @Getter
    @Setter
    private Item output;

    /**
     * Instantiates a new Shapeless recipe.
     *
     * @param id the id
     */
    public ShapelessRecipe(final @NotNull String id) {
        this.id = id;
        this.ingredients = new ArrayList<>();
    }

    @Override
    public void setOutput(final @NotNull Item output) {
        this.output = output.copy(output.getClass());
    }

    /**
     * Add ingredients.
     *
     * @param items the items
     */
    public void addIngredients(final Item @NotNull ... items) {
        for (final Item item : items) addIngredient(item);
    }

    /**
     * Add ingredient.
     * If the size of {@link #ingredients} is superior to {@link #MAX_SIZE}, an {@link IllegalStateException} is thrown.
     *
     * @param item the item
     */
    public void addIngredient(final @NotNull Item item) {
        if (this.ingredients.size() >= MAX_SIZE)
            throw new IllegalStateException(String.format("Cannot add ingredient to ingredients as maximum size of %s has been reached", MAX_SIZE));
        this.ingredients.add(item.copy(item.getClass()));
    }

    /**
     * Remove ingredients.
     *
     * @param items the items
     */
    public void removeIngredients(final Item @NotNull ... items) {
        for (final Item item : items) removeIngredient(item);
    }

    /**
     * Remove ingredient.
     *
     * @param item the item
     */
    public void removeIngredient(final @NotNull Item item) {
        this.ingredients.removeIf(i -> i.equals(item));
    }

    @Override
    public List<Item> getIngredients() {
        return new ArrayList<>(this.ingredients);
    }
}
