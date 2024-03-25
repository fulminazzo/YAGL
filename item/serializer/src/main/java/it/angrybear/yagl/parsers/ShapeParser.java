package it.angrybear.yagl.parsers;

import it.angrybear.yagl.items.recipes.ShapedRecipe;
import it.fulminazzo.yamlparser.parsers.CallableYAMLParser;

/**
 * A parser to serialize {@link it.angrybear.yagl.items.recipes.ShapedRecipe.Shape}.
 */
public class ShapeParser extends CallableYAMLParser<ShapedRecipe.Shape> {

    /**
     * Instantiates a new Shape parser.
     */
    public ShapeParser() {
        super(ShapedRecipe.Shape.class, c -> new ShapedRecipe.Shape());
    }
}
