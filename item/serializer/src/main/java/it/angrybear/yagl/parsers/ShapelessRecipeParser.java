package it.angrybear.yagl.parsers;

import it.angrybear.yagl.items.recipes.ShapelessRecipe;
import it.fulminazzo.yamlparser.parsers.CallableYAMLParser;

/**
 * A parser to serialize {@link ShapelessRecipe}.
 */
public class ShapelessRecipeParser extends CallableYAMLParser<ShapelessRecipe> {

    /**
     * Instantiates a new Shapeless recipe parser.
     */
    public ShapelessRecipeParser() {
        super(ShapelessRecipe.class, (c) -> new ShapelessRecipe(c.getName()));
    }
}
