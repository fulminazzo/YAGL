package it.angrybear.parsers;

import it.angrybear.items.recipes.ShapedRecipe;
import it.fulminazzo.yamlparser.parsers.CallableYAMLParser;

public class ShapedRecipeParser extends CallableYAMLParser<ShapedRecipe> {

    public ShapedRecipeParser() {
        super(ShapedRecipe.class, (c) -> new ShapedRecipe(c.getName()));
    }
}
