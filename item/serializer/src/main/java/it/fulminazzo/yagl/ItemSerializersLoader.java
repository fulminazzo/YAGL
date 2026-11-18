package it.fulminazzo.yagl;

import it.fulminazzo.yagl.item.ItemComponent;
import it.fulminazzo.yagl.item.ItemComponentSerializer;
import it.fulminazzo.yagl.item.recipe.Recipe;
import it.fulminazzo.yagl.item.recipe.RecipeSerializer;
import it.fulminazzo.yagl.item.recipe.ShapeSerializer;
import it.fulminazzo.yagl.item.recipe.ShapedRecipe;
import it.fulminazzo.yagl.serializer.SerializersLoader;
import it.fulminazzo.yagl.serializer.YaglSerializer;
import org.jetbrains.annotations.NotNull;

public final class ItemSerializersLoader implements SerializersLoader {
    private static final @NotNull ItemComponentSerializer ITEM_COMPONENT_SERIALIZER = new ItemComponentSerializer();

    @Override
    public void load(final @NotNull YaglSerializer mainSerializer) {
        mainSerializer
                .registerSerializer(ShapedRecipe.Shape.class, new ShapeSerializer())
                .registerSerializer(Recipe.class, new RecipeSerializer())
                .registerSerializer(ItemComponent.class, getItemComponentSerializer())
        ;
    }

    public static @NotNull ItemComponentSerializer getItemComponentSerializer() {
        return ITEM_COMPONENT_SERIALIZER;
    }

}
