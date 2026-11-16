package it.fulminazzo.yagl.recipe;

import it.fulminazzo.yagl.item.Item;
import it.fulminazzo.yagl.item.recipe.ShapedRecipe;
import it.fulminazzo.yagl.serializer.*;
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

    @Override
    protected @Nullable ShapedRecipe initialize(final @NotNull DataNode node) throws SerializationException {
        return node.get("id", String.class).map(ShapedRecipe::new).orElse(null);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void loadData(final @NotNull ShapedRecipe value, final @NotNull DataNode node) throws SerializationException {
        YaglSerializer mainSerializer = YaglSerializer.getInstance();
        Optional<Collection<?>> ingredientsOpt = (Optional<Collection<?>>) (Optional<?>) node.get("ingredients", Collection.class);
        if (ingredientsOpt.isPresent()) {
            Collection<Item> ingredients = mainSerializer.deserializeCollection(ingredientsOpt.get(), Item.class);
            if (ingredients != null) value.setIngredients(ingredients.toArray(new Item[0]));
        }
        Optional<?> shapeOpt = node.get("shape", Object.class);
        if (shapeOpt.isPresent()) {
            ShapedRecipe.Shape shape = mainSerializer.deserialize(shapeOpt.get(), ShapedRecipe.Shape.class);
            if (shape != null) value.setShape(shape.getRows(), shape.getColumns());
        }
    }

    @Override
    protected void dumpData(final @NotNull ShapedRecipe value, final @NotNull Map<String, Object> output) throws SerializationException {
        YaglSerializer mainSerializer = YaglSerializer.getInstance();
        output.put("ingredients", mainSerializer.serialize(value.getIngredients()));
        output.put("shape", mainSerializer.serialize(value.getShape()));
    }

}
