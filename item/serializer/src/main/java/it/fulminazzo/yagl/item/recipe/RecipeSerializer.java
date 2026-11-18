package it.fulminazzo.yagl.item.recipe;

import it.fulminazzo.yagl.item.Item;
import it.fulminazzo.yagl.serializer.DataNode;
import it.fulminazzo.yagl.serializer.SerializationException;
import it.fulminazzo.yagl.serializer.TypedSerializer;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link it.fulminazzo.yagl.serializer.Serializer} for {@link Recipe} implementations.
 *
 * @see Recipe
 * @see TypedSerializer
 */
public final class RecipeSerializer extends TypedSerializer<Recipe> {
    static final @NotNull String ID_PATH = "id";
    static final @NotNull String OUTPUT_PATH = "output";

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
        node.get(OUTPUT_PATH, Item.class).ifPresent(value::setOutput);
    }

    @Override
    protected void dumpCommonData(final @NotNull Recipe value, final @NotNull DataNode output) throws SerializationException {
        output.set(ID_PATH, value.getId());
        output.set(OUTPUT_PATH, value.getOutput());
    }

}
