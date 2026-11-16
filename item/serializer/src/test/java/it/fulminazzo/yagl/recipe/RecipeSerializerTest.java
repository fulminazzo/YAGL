package it.fulminazzo.yagl.recipe;

import it.fulminazzo.yagl.item.Item;
import it.fulminazzo.yagl.item.recipe.FurnaceRecipe;
import it.fulminazzo.yagl.item.recipe.Recipe;
import it.fulminazzo.yagl.item.recipe.ShapedRecipe;
import it.fulminazzo.yagl.item.recipe.ShapelessRecipe;
import it.fulminazzo.yagl.serializer.SerializerTestHelper;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RecipeSerializerTest extends SerializerTestHelper<Recipe> {

    protected RecipeSerializerTest() {
        super(Recipe.class, new RecipeSerializer());
    }

    @ParameterizedTest
    @MethodSource("testData")
    void testDeserialize(final @NotNull Recipe expected, final @NotNull Map<String, Object> map) throws IOException {
        String fileName = expected.getClass().getSimpleName().toLowerCase(Locale.ROOT);
        Recipe actual = load(fileName);
        assertNotNull(actual);
        assertEquals(expected.getClass(), actual.getClass());
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getOutput().getMaterial(), actual.getOutput().getMaterial());
    }

    @ParameterizedTest
    @MethodSource("testData")
    void testSerialize(final @NotNull Recipe recipe, final @NotNull Map<String, Object> expectedMap) throws IOException {
        String fileName = recipe.getClass().getSimpleName().toLowerCase(Locale.ROOT);
        Map<?, ?> actualMap = readRawMap(dump(recipe, fileName));
        assertEquals(expectedMap, actualMap);
    }

    private static @NotNull Object[][] testData() {
        Map<String, Object> outputMap = new HashMap<>();
        outputMap.put("material", "STONE");
        outputMap.put("amount", 1);

        Map<String, Object> shapedIngredient = new HashMap<>();
        shapedIngredient.put("material", "COBBLESTONE");
        shapedIngredient.put("amount", 1);

        Map<String, Object> shapelessIngredient = new HashMap<>();
        shapelessIngredient.put("material", "ORANGE_TULIP");
        shapelessIngredient.put("amount", 1);

        Map<String, Object> furnaceIngredient = new HashMap<>();
        furnaceIngredient.put("material", "RAW_IRON");
        furnaceIngredient.put("amount", 1);

        Map<String, Object> shapedMap = new HashMap<>();
        shapedMap.put("type", "SHAPED");
        shapedMap.put("id", "test_shaped");
        shapedMap.put("output", outputMap);
        shapedMap.put("ingredients", List.of(shapedIngredient));
        shapedMap.put("shape", "1x1");

        Map<String, Object> shapelessMap = new HashMap<>();
        shapelessMap.put("type", "SHAPELESS");
        shapelessMap.put("id", "test_shapeless");
        shapelessMap.put("output", outputMap);
        shapelessMap.put("ingredients", List.of(shapelessIngredient));

        Map<String, Object> furnaceMap = new HashMap<>();
        furnaceMap.put("type", "FURNACE");
        furnaceMap.put("id", "test_furnace");
        furnaceMap.put("output", outputMap);
        furnaceMap.put("ingredient", furnaceIngredient);
        furnaceMap.put("experience", 0.1);
        furnaceMap.put("cookingTime", 10.0);

        return new Object[][]{
                new Object[]{
                        new ShapedRecipe("test_shaped")
                                .setShape(1, 1)
                                .setIngredient(0, Item.newItem("COBBLESTONE").setAmount(1))
                                .setOutput(Item.newItem("STONE").setAmount(1)),
                        shapedMap
                },
                new Object[]{
                        new ShapelessRecipe("test_shapeless")
                                .addIngredient(Item.newItem("ORANGE_TULIP").setAmount(1))
                                .setOutput(Item.newItem("STONE").setAmount(1)),
                        shapelessMap
                },
                new Object[]{
                        new FurnaceRecipe("test_furnace")
                                .setIngredient(Item.newItem("RAW_IRON").setAmount(1))
                                .setExperience(0.1f)
                                .setCookingTime(10.0)
                                .setOutput(Item.newItem("STONE").setAmount(1)),
                        furnaceMap
                }
        };
    }

}
