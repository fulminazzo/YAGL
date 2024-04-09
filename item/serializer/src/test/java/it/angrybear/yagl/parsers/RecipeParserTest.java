package it.angrybear.yagl.parsers;

import it.angrybear.yagl.ParserTestHelper;
import it.angrybear.yagl.items.Item;
import it.angrybear.yagl.items.recipes.FurnaceRecipe;
import it.angrybear.yagl.items.recipes.Recipe;
import it.angrybear.yagl.items.recipes.ShapedRecipe;
import it.angrybear.yagl.items.recipes.ShapelessRecipe;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;
import it.fulminazzo.yamlparser.utils.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RecipeParserTest extends ParserTestHelper<Recipe> {

    private static Recipe[] getRecipes() {
        Item mock = Item.newItem().setMaterial("stone");
        List<Recipe> recipes = new LinkedList<>();
        Item[] ingredients = new Item[9];
        Arrays.fill(ingredients, mock);

        recipes.add(new ShapedRecipe("sd-recipe1"));
        ShapedRecipe shapedRecipe = new ShapedRecipe("sd-recipe2")
                .setShape(3, 3)
                .setIngredients(ingredients).setOutput(mock);
        recipes.add(shapedRecipe);

        recipes.add(new ShapelessRecipe("sl-recipe1"));
        ShapelessRecipe shapelessRecipe = new ShapelessRecipe("sl-recipe2")
                .addIngredients(ingredients).setOutput(mock);
        recipes.add(shapelessRecipe);

        recipes.add(new FurnaceRecipe("f-recipe1"));
        FurnaceRecipe furnaceRecipe = new FurnaceRecipe("f-recipe2")
                .setIngredient(mock).setOutput(mock)
                .setExperience(1337F).setCookingTime(777);
        recipes.add(furnaceRecipe);

        return recipes.toArray(new Recipe[0]);
    }

    @ParameterizedTest
    @MethodSource("getRecipes")
    void testRecipe(Recipe recipe) throws IOException {
        ItemYAGLParser.addAllParsers();
        final String path = FileUtils.formatStringToYaml(recipe.getClass().getSimpleName());

        File output = new File("build/resources/test/recipe.yml");
        if (!output.exists()) FileUtils.createNewFile(output);

        FileConfiguration configuration = new FileConfiguration(output);
        configuration.set(path, recipe);
        configuration.save();

        configuration = new FileConfiguration(output);
        Recipe recipe2 = configuration.get(path, Recipe.class);

        for (final Field field : recipe.getClass().getDeclaredFields()) {
            Object obj1 = ReflectionUtils.get(field, recipe);
            Object obj2 = ReflectionUtils.get(field, recipe2);
            assertEquals(obj1, obj2);
        }
    }

    @Test
    void testNullIngredientsList() throws IOException {
        ItemYAGLParser.addAllParsers();
        final String path = "null-recipes";

        Recipe recipe = new ShapedRecipe("recipe");
        new Refl<>(recipe).setFieldObject("ingredients", null);

        File output = new File("build/resources/test/recipe.yml");
        if (!output.exists()) FileUtils.createNewFile(output);

        FileConfiguration configuration = new FileConfiguration(output);
        configuration.set(path, recipe);
        configuration.save();

        configuration = new FileConfiguration(output);
        Recipe recipe2 = configuration.get(path, Recipe.class);

        assertNotNull(new Refl<>(recipe2).getFieldObject("ingredients"),
                "Recipe2 ingredients list");
    }

    @Override
    protected Class<?> getParser() {
        return RecipeParser.class;
    }
}