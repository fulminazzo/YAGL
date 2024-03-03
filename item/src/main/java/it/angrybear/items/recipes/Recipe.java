package it.angrybear.items.recipes;

import it.angrybear.items.Item;

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
     * Gets output.
     *
     * @return the output
     */
    Item getOutput();
}
