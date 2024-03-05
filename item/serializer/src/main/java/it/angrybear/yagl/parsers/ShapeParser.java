package it.angrybear.yagl.parsers;

import it.angrybear.yagl.items.recipes.ShapedRecipe;
import it.fulminazzo.yamlparser.parsers.CallableYAMLParser;

public class ShapeParser extends CallableYAMLParser<ShapedRecipe.Shape> {

    public ShapeParser() {
        super(ShapedRecipe.Shape.class, c -> new ShapedRecipe.Shape());
    }
}
