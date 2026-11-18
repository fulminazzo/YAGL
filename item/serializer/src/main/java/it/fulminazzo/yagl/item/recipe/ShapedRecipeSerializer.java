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
 * A {@link Serializer} for {@link ShapedRecipe} objects.
 *
 * @see ShapedRecipe
 * @see ObjectSerializer
 */
final class ShapedRecipeSerializer extends ObjectSerializer<ShapedRecipe> {
    static final @NotNull String SHAPE_PATH = "shape";
    static final @NotNull String INGREDIENTS_PATH = "ingredients";

    @Override
    protected @Nullable ShapedRecipe initialize(final @NotNull DataNode node) throws SerializationException {
        return node.get(RecipeSerializer.ID_PATH, String.class).map(ShapedRecipe::new).orElse(null);
    }

    @Override
    protected void loadData(final @NotNull ShapedRecipe value, final @NotNull DataNode node) throws SerializationException {
        node.getCollection(INGREDIENTS_PATH, Item.class)
                .map(c -> c.toArray(new Item[0]))
                .ifPresent(value::setIngredients);
        node.get(SHAPE_PATH, ShapedRecipe.Shape.class)
                .ifPresent(s -> value.setShape(s.getRows(), s.getColumns()));
    }

    @Override
    protected void dumpData(final @NotNull ShapedRecipe value, final @NotNull Map<String, Object> output) throws SerializationException {
        output.put(INGREDIENTS_PATH, mainSerializer().serialize(value.getIngredients()));
        output.put(SHAPE_PATH, mainSerializer().serialize(value.getShape()));
    }

}
