package it.angrybear.parsers;

import it.angrybear.items.recipes.ShapedRecipe;
import it.fulminazzo.yamlparser.parsers.CallableYAMLParser;

public class ShapeParser extends CallableYAMLParser<ShapedRecipe.Shape> {

    public ShapeParser() {
        super(ShapedRecipe.Shape.class, c -> new ShapedRecipe.Shape());
    }
}
