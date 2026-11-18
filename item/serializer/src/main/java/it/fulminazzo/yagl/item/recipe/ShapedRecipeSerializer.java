package it.fulminazzo.yagl.item.recipe;

import it.fulminazzo.yagl.item.Item;
import it.fulminazzo.yagl.serializer.DataNode;
import it.fulminazzo.yagl.serializer.ObjectSerializer;
import it.fulminazzo.yagl.serializer.SerializationException;
import it.fulminazzo.yagl.serializer.Serializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

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

    @SuppressWarnings("unchecked")
    @Override
    protected void loadData(final @NotNull ShapedRecipe value, final @NotNull DataNode node) throws SerializationException {
        Optional<Collection<?>> ingredientsOpt = (Optional<Collection<?>>) (Optional<?>) node.get(INGREDIENTS_PATH, Collection.class);
        if (ingredientsOpt.isPresent()) {
            Collection<Item> ingredients = mainSerializer().deserializeCollection(ingredientsOpt.get(), Item.class);
            if (ingredients != null) value.setIngredients(ingredients.toArray(new Item[0]));
        }
        Optional<?> shapeOpt = node.get(SHAPE_PATH, Object.class);
        if (shapeOpt.isPresent()) {
            ShapedRecipe.Shape shape = mainSerializer().deserialize(shapeOpt.get(), ShapedRecipe.Shape.class);
            if (shape != null) value.setShape(shape.getRows(), shape.getColumns());
        }
    }

    @Override
    protected void dumpData(final @NotNull ShapedRecipe value, final @NotNull Map<String, Object> output) throws SerializationException {
        output.put(INGREDIENTS_PATH, mainSerializer().serialize(value.getIngredients()));
        output.put(SHAPE_PATH, mainSerializer().serialize(value.getShape()));
    }

}
