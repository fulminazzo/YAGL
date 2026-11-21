package it.fulminazzo.yagl.item.recipe;

import it.fulminazzo.yagl.item.Item;
import it.fulminazzo.yagl.serializer.DataNode;
import it.fulminazzo.yagl.serializer.ObjectSerializer;
import it.fulminazzo.yagl.serializer.SerializationException;
import it.fulminazzo.yagl.serializer.Serializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A serializer for {@link FurnaceRecipe} objects.
 *
 * @see FurnaceRecipe
 * @see ObjectSerializer
 */
final class FurnaceRecipeSerializer extends ObjectSerializer<FurnaceRecipe> {
    static final @NotNull String INGREDIENT_PATH = "ingredient";
    static final @NotNull String EXPERIENCE_PATH = "experience";
    static final @NotNull String COOKING_TIME_PATH = "cookingTime";

    @Override
    protected @Nullable FurnaceRecipe initialize(final @NotNull DataNode node) throws SerializationException {
        return node.get(RecipeSerializer.ID_PATH, String.class).map(FurnaceRecipe::new).orElse(null);
    }

    @Override
    protected void loadData(final @NotNull FurnaceRecipe value, final @NotNull DataNode node) throws SerializationException {
        node.get(INGREDIENT_PATH, Item.class).ifPresent(value::setIngredient);
        node.get(EXPERIENCE_PATH, Float.class).ifPresent(value::setExperience);
        node.get(COOKING_TIME_PATH, Double.class).ifPresent(value::setCookingTime);
    }

    @Override
    protected void dumpData(final @NotNull FurnaceRecipe value, final @NotNull DataNode output) throws SerializationException {
        List<Item> ingredients = value.getIngredients();
        if (!ingredients.isEmpty()) output.set(INGREDIENT_PATH, ingredients.get(0));
        output.set(EXPERIENCE_PATH, value.getExperience(), 0);
        output.set(COOKING_TIME_PATH, value.getCookingTime(), 0);
    }

}
