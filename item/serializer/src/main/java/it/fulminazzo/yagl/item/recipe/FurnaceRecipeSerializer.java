package it.fulminazzo.yagl.item.recipe;

import it.fulminazzo.yagl.item.Item;
import it.fulminazzo.yagl.serializer.DataNode;
import it.fulminazzo.yagl.serializer.ObjectSerializer;
import it.fulminazzo.yagl.serializer.SerializationException;
import it.fulminazzo.yagl.serializer.Serializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A {@link Serializer} for {@link FurnaceRecipe} objects.
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
        Optional<?> ingredientOpt = node.get(INGREDIENT_PATH, Object.class);
        if (ingredientOpt.isPresent()) {
            Item ingredient = mainSerializer().deserialize(ingredientOpt.get(), Item.class);
            if (ingredient != null) value.setIngredient(ingredient);
        }
        node.get(EXPERIENCE_PATH, Float.class).ifPresent(value::setExperience);
        node.get(COOKING_TIME_PATH, Double.class).ifPresent(value::setCookingTime);
    }

    @Override
    protected void dumpData(final @NotNull FurnaceRecipe value, final @NotNull Map<String, Object> output) throws SerializationException {
        List<Item> ingredients = value.getIngredients();
        if (!ingredients.isEmpty()) output.put(INGREDIENT_PATH, mainSerializer().serialize(ingredients.get(0)));
        output.put(EXPERIENCE_PATH, value.getExperience());
        output.put(COOKING_TIME_PATH, value.getCookingTime());
    }

}
