package it.fulminazzo.yagl.parser;

import it.fulminazzo.yagl.item.Item;
import it.fulminazzo.yagl.item.recipe.FurnaceRecipe;
import it.fulminazzo.yagl.item.recipe.Recipe;
import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yamlparser.configuration.IConfiguration;

import java.util.LinkedList;
import java.util.List;

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

    @Override
    protected BiFunctionException<IConfiguration, String, Recipe, Exception> getLoader() {
        return (c, s) -> {
            Recipe r = super.getLoader().apply(c, s);
            if (r == null) return null;
            if (r instanceof FurnaceRecipe) return r;
            List<Item> ingredients = c.getList(s + ".ingredients", Item.class);
            if (ingredients == null) ingredients = new LinkedList<>();
            return new Refl<>(r).setFieldObject("ingredients", ingredients).getObject();
        };
    }

    @Override
    protected TriConsumer<IConfiguration, String, Recipe> getDumper() {
        return super.getDumper().andThen((c, s, r) -> {
            if (r == null) return;
            if (!r.isEmpty()) c.set(s + ".ingredients.value-class", null);
        });
    }
}
