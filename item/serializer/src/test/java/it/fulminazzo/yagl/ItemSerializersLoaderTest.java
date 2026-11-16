package it.fulminazzo.yagl;

import it.fulminazzo.yagl.item.recipe.FurnaceRecipe;
import it.fulminazzo.yagl.item.recipe.Recipe;
import it.fulminazzo.yagl.item.recipe.ShapedRecipe;
import it.fulminazzo.yagl.item.recipe.ShapelessRecipe;
import it.fulminazzo.yagl.serializer.SerializationException;
import it.fulminazzo.yagl.serializer.YaglSerializer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ItemSerializersLoaderTest {

    private static Class<?>[] testTypes() {
        return new Class<?>[]{
                ShapedRecipe.Shape.class,
                Recipe.class,
                ShapedRecipe.class,
                ShapelessRecipe.class,
                FurnaceRecipe.class,
        };
    }

    @ParameterizedTest
    @MethodSource("testTypes")
    void testThatSerializerIsRegisteredFor(final @NotNull Class<?> type) throws SerializationException {
        assertNotNull(YaglSerializer.getInstance().getSerializer(type));
    }

}
