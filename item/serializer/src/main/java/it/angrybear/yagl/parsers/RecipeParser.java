package it.angrybear.yagl.parsers;

import it.angrybear.yagl.items.Item;
import it.angrybear.yagl.items.recipes.FurnaceRecipe;
import it.angrybear.yagl.items.recipes.Recipe;
import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    protected BiFunctionException<IConfiguration, String, Recipe> getLoader() {
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
    protected @NotNull TriConsumer<@NotNull IConfiguration, @NotNull String, @Nullable Recipe> getDumper() {
        return super.getDumper().andThen((c, s, r) -> {
            if (r == null) return;
            if (!r.isEmpty()) c.set(s + ".ingredients.value-class", null);
        });
    }
}
