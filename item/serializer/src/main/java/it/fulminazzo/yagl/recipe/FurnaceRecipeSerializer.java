package it.fulminazzo.yagl.recipe;

import it.fulminazzo.yagl.item.Item;
import it.fulminazzo.yagl.item.recipe.FurnaceRecipe;
import it.fulminazzo.yagl.serializer.*;
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

    @Override
    protected @Nullable FurnaceRecipe initialize(final @NotNull DataNode node) throws SerializationException {
        return node.get("id", String.class).map(FurnaceRecipe::new).orElse(null);
    }

    @Override
    protected void loadData(final @NotNull FurnaceRecipe value, final @NotNull DataNode node) throws SerializationException {
        YaglSerializer mainSerializer = YaglSerializer.getInstance();
        Optional<?> ingredientOpt = node.get("ingredient", Object.class);
        if (ingredientOpt.isPresent()) {
            Item ingredient = mainSerializer.deserialize(ingredientOpt.get(), Item.class);
            if (ingredient != null) value.setIngredient(ingredient);
        }
        node.get("experience", Float.class).ifPresent(value::setExperience);
        node.get("cookingTime", Double.class).ifPresent(value::setCookingTime);
    }

    @Override
    protected void dumpData(final @NotNull FurnaceRecipe value, final @NotNull Map<String, Object> output) throws SerializationException {
        YaglSerializer mainSerializer = YaglSerializer.getInstance();
        List<Item> ingredients = value.getIngredients();
        if (!ingredients.isEmpty()) output.put("ingredient", mainSerializer.serialize(ingredients.get(0)));
        output.put("experience", value.getExperience());
        output.put("cookingTime", value.getCookingTime());
    }

}
