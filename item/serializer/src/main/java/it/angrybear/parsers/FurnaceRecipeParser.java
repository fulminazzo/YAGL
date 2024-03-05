package it.angrybear.parsers;

import it.angrybear.items.recipes.FurnaceRecipe;
import it.fulminazzo.yamlparser.parsers.CallableYAMLParser;

public class FurnaceRecipeParser extends CallableYAMLParser<FurnaceRecipe> {

    public FurnaceRecipeParser() {
        super(FurnaceRecipe.class, (c) -> new FurnaceRecipe(c.getName()));
    }
}
