package it.angrybear.yagl.parsers;

import it.angrybear.yagl.items.recipes.ShapedRecipe;
import it.fulminazzo.yamlparser.parsers.CallableYAMLParser;

public class ShapedRecipeParser extends CallableYAMLParser<ShapedRecipe> {

    public ShapedRecipeParser() {
        super(ShapedRecipe.class, (c) -> new ShapedRecipe(c.getName()));
    }
}
