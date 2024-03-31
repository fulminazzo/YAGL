package it.angrybear.yagl.parsers;

import it.angrybear.yagl.items.recipes.Recipe;

/**
 * A parser to serialize {@link Recipe} and derivatives.
 */
public class RecipeParser extends TypedParser<Recipe> {

    /**
     * Instantiates a new Recipe parser.
     */
    public RecipeParser() {
        super(Recipe.class);
    }
}
