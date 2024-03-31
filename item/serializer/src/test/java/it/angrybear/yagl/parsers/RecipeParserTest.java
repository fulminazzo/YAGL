package it.angrybear.yagl.parsers;

import it.angrybear.yagl.items.Item;
import it.angrybear.yagl.items.recipes.FurnaceRecipe;
import it.angrybear.yagl.items.recipes.Recipe;
import it.angrybear.yagl.items.recipes.ShapedRecipe;
import it.angrybear.yagl.items.recipes.ShapelessRecipe;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;
import it.fulminazzo.yamlparser.utils.FileUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RecipeParserTest {

    private static Recipe[] getRecipes() {
        Item mock = Item.newItem().setMaterial("STONE");
        List<Recipe> recipes = new LinkedList<>();

        recipes.add(new ShapedRecipe("sd-recipe1"));
        ShapedRecipe shapedRecipe = new ShapedRecipe("sd-recipe2");
        shapedRecipe.setShape(3, 3);
        for (int i = 0; i < 9; i++) shapedRecipe.setIngredient(i, mock);
        shapedRecipe.setOutput(mock);
        recipes.add(shapedRecipe);

        recipes.add(new ShapelessRecipe("sl-recipe1"));
        ShapelessRecipe shapelessRecipe = new ShapelessRecipe("sl-recipe2");
        Item[] tmp = new Item[9];
        Arrays.fill(tmp, mock);
        shapelessRecipe.addIngredients(tmp);
        shapelessRecipe.setOutput(mock);
        recipes.add(shapelessRecipe);

        recipes.add(new FurnaceRecipe("f-recipe1"));
        FurnaceRecipe furnaceRecipe = new FurnaceRecipe("f-recipe2");
        furnaceRecipe.setIngredient(mock);
        furnaceRecipe.setOutput(mock);
        furnaceRecipe.setExperience(1337F);
        furnaceRecipe.setCookingTime(777);
        recipes.add(furnaceRecipe);

        return recipes.toArray(new Recipe[0]);
    }

    @ParameterizedTest
    @MethodSource("getRecipes")
    void testRecipe(Recipe recipe) throws IOException {
        YAGLParser.addAllParsers();
        final String path = FileUtils.formatStringToYaml(recipe.getClass().getSimpleName());

        File output = new File("build/resources/test/recipe.yml");
        if (!output.exists()) FileUtils.createNewFile(output);

        FileConfiguration configuration = new FileConfiguration(output);
        configuration.set(path, recipe);
        configuration.save();

        configuration = new FileConfiguration(output);
        Recipe recipe2 = configuration.get(path, Recipe.class);

        for (final Field field : recipe.getClass().getDeclaredFields())
            try {
                field.setAccessible(true);
                Object obj1 = field.get(recipe);
                Object obj2 = field.get(recipe2);
                assertEquals(obj1, obj2);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
    }
}