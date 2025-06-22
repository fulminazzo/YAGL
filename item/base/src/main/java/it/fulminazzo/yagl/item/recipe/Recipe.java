package it.fulminazzo.yagl.item.recipe;

import it.fulminazzo.yagl.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

/**
 * A wrapper for Minecraft recipes.
 */
public interface Recipe extends Iterable<Item> {

    /**
     * Gets id.
     *
     * @return the id
     */
    @NotNull String getId();

    /**
     * Gets ingredients.
     *
     * @return the ingredients
     */
    @NotNull List<Item> getIngredients();

    /**
     * Sets output.
     *
     * @param item the item
     * @return this recipe
     */
    @NotNull Recipe setOutput(final @NotNull Item item);

    /**
     * Gets output.
     *
     * @return the output
     */
    @NotNull Item getOutput();

    /**
     * Gets the number of ingredients in this recipe.
     *
     * @return the size
     */
    default int size() {
        return getIngredients().size();
    }

    /**
     * Check if the current recipe has no ingredient set.
     *
     * @return true if {@link #size()} is 0
     */
    default boolean isEmpty() {
        return size() == 0;
    }

    @Override
    default @NotNull Iterator<Item> iterator() {
        return getIngredients().iterator();
    }
}
