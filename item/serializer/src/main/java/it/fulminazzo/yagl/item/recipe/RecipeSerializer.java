package it.fulminazzo.yagl.item.recipe;

import it.fulminazzo.yagl.item.Item;
import it.fulminazzo.yagl.serializer.DataNode;
import it.fulminazzo.yagl.serializer.SerializationException;
import it.fulminazzo.yagl.serializer.TypedSerializer;
import it.fulminazzo.yagl.serializer.YaglSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

/**
 * A {@link it.fulminazzo.yagl.serializer.Serializer} for {@link Recipe} implementations.
 *
 * @see Recipe
 * @see TypedSerializer
 */
public final class RecipeSerializer extends TypedSerializer<Recipe> {

    /**
     * Instantiates a new Recipe serializer.
     */
    public RecipeSerializer() {
        super(Recipe.class);
        registerSerializer(ShapedRecipe.class, new ShapedRecipeSerializer());
        registerSerializer(ShapelessRecipe.class, new ShapelessRecipeSerializer());
        registerSerializer(FurnaceRecipe.class, new FurnaceRecipeSerializer());
    }

    @Override
    protected void loadCommonData(final @NotNull Recipe value, final @NotNull DataNode node) throws SerializationException {
        Optional<?> outputOpt = node.get("output", Object.class);
        if (outputOpt.isPresent())
            value.setOutput(YaglSerializer.getInstance().deserialize(outputOpt.get(), Item.class));
    }

    @Override
    protected void dumpCommonData(final @NotNull Recipe value, final @NotNull Map<String, Object> output) throws SerializationException {
        output.put("id", value.getId());
        output.put("output", YaglSerializer.getInstance().serialize(value.getOutput()));
    }

}
