package it.angrybear.yagl.items.recipes;

import it.angrybear.yagl.items.Item;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A wrapper for Minecraft recipes.
 */
public interface Recipe {

    /**
     * Gets id.
     *
     * @return the id
     */
    String getId();

    /**
     * Gets ingredients.
     *
     * @return the ingredients
     */
    List<Item> getIngredients();

    /**
     * Sets output.
     *
     * @param item the item
     * @return this recipe
     */
    Recipe setOutput(final @NotNull Item item);

    /**
     * Gets output.
     *
     * @return the output
     */
    Item getOutput();
}
