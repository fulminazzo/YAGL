package it.fulminazzo.yagl.item.recipe;

import it.fulminazzo.yagl.item.Item;
import it.fulminazzo.yagl.serializer.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * A {@link Serializer} for {@link ShapelessRecipe} objects.
 *
 * @see ShapelessRecipe
 * @see ObjectSerializer
 */
final class ShapelessRecipeSerializer extends ObjectSerializer<ShapelessRecipe> {

    @Override
    protected @Nullable ShapelessRecipe initialize(final @NotNull DataNode node) throws SerializationException {
        return node.get("id", String.class).map(ShapelessRecipe::new).orElse(null);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void loadData(final @NotNull ShapelessRecipe value, final @NotNull DataNode node) throws SerializationException {
        YaglSerializer mainSerializer = YaglSerializer.getInstance();
        Optional<Collection<?>> ingredientsOpt = (Optional<Collection<?>>) (Optional<?>) node.get("ingredients", Collection.class);
        if (ingredientsOpt.isPresent()) {
            Collection<Item> ingredients = mainSerializer.deserializeCollection(ingredientsOpt.get(), Item.class);
            if (ingredients != null) value.addIngredients(ingredients.toArray(new Item[0]));
        }
    }

    @Override
    protected void dumpData(final @NotNull ShapelessRecipe value, final @NotNull Map<String, Object> output) throws SerializationException {
        YaglSerializer mainSerializer = YaglSerializer.getInstance();
        output.put("ingredients", mainSerializer.serialize(value.getIngredients()));
    }

}
