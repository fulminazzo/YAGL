package it.angrybear.yagl.parsers;

import it.angrybear.yagl.items.recipes.FurnaceRecipe;
import it.fulminazzo.yamlparser.parsers.CallableYAMLParser;

/**
 * A parser to serialize {@link FurnaceRecipe}.
 */
public class FurnaceRecipeParser extends CallableYAMLParser<FurnaceRecipe> {

    /**
     * Instantiates a new Furnace recipe parser.
     */
    public FurnaceRecipeParser() {
        super(FurnaceRecipe.class, (c) -> new FurnaceRecipe(c.getName()));
    }
}
