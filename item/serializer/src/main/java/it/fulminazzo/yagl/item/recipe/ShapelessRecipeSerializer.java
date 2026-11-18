package it.fulminazzo.yagl.item.recipe;

import it.fulminazzo.yagl.item.Item;
import it.fulminazzo.yagl.serializer.DataNode;
import it.fulminazzo.yagl.serializer.ObjectSerializer;
import it.fulminazzo.yagl.serializer.SerializationException;
import it.fulminazzo.yagl.serializer.Serializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * A {@link Serializer} for {@link ShapelessRecipe} objects.
 *
 * @see ShapelessRecipe
 * @see ObjectSerializer
 */
final class ShapelessRecipeSerializer extends ObjectSerializer<ShapelessRecipe> {
    static final @NotNull String INGREDIENTS_PATH = "ingredients";

    @Override
    protected @Nullable ShapelessRecipe initialize(final @NotNull DataNode node) throws SerializationException {
        return node.get(RecipeSerializer.ID_PATH, String.class).map(ShapelessRecipe::new).orElse(null);
    }

    @Override
    protected void loadData(final @NotNull ShapelessRecipe value, final @NotNull DataNode node) throws SerializationException {
        node.getCollection(INGREDIENTS_PATH, Item.class)
                .map(c -> c.toArray(new Item[0]))
                .ifPresent(value::addIngredients);
    }

    @Override
    protected void dumpData(final @NotNull ShapelessRecipe value, final @NotNull Map<String, Object> output) throws SerializationException {
        output.put(INGREDIENTS_PATH, mainSerializer().serialize(value.getIngredients()));
    }

}
