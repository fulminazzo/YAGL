package it.angrybear.yagl.parsers;

import it.angrybear.yagl.items.recipes.ShapedRecipe;
import it.fulminazzo.yamlparser.parsers.CallableYAMLParser;

/**
 * A parser to serialize {@link ShapedRecipe}.
 */
public class ShapedRecipeParser extends CallableYAMLParser<ShapedRecipe> {

    /**
     * Instantiates a new Shaped recipe parser.
     */
    public ShapedRecipeParser() {
        super(ShapedRecipe.class, (c) -> new ShapedRecipe(c.getName()));
    }
}
