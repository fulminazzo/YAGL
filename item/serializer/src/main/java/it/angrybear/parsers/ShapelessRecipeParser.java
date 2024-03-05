package it.angrybear.parsers;

import it.angrybear.items.recipes.ShapelessRecipe;
import it.fulminazzo.yamlparser.parsers.CallableYAMLParser;

public class ShapelessRecipeParser extends CallableYAMLParser<ShapelessRecipe> {

    public ShapelessRecipeParser() {
        super(ShapelessRecipe.class, (c) -> new ShapelessRecipe(c.getName()));
    }
}
