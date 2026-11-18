package it.fulminazzo.yagl;

import it.fulminazzo.yagl.item.recipe.Recipe;
import it.fulminazzo.yagl.item.recipe.ShapedRecipe;
import it.fulminazzo.yagl.item.recipe.RecipeSerializer;
import it.fulminazzo.yagl.item.recipe.ShapeSerializer;
import it.fulminazzo.yagl.serializer.SerializersLoader;
import it.fulminazzo.yagl.serializer.YaglSerializer;
import org.jetbrains.annotations.NotNull;

public final class ItemSerializersLoader implements SerializersLoader {

    @Override
    public void load(final @NotNull YaglSerializer mainSerializer) {
        mainSerializer
                .registerSerializer(ShapedRecipe.Shape.class, new ShapeSerializer())
                .registerSerializer(Recipe.class, new RecipeSerializer())
        ;
    }

}
