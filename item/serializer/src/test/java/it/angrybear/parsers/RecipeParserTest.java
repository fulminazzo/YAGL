package it.angrybear.parsers;

import it.angrybear.items.Item;
import it.angrybear.items.recipes.FurnaceRecipe;
import it.angrybear.items.recipes.Recipe;
import it.angrybear.items.recipes.ShapedRecipe;
import it.angrybear.items.recipes.ShapelessRecipe;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;
import it.fulminazzo.yamlparser.utils.FileUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
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
        for (int i = 0; i < 9; i++) shapelessRecipe.addIngredient(mock);
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

        File output = new File("build/resources/test/recipe.yml");
        if (output.exists()) FileUtils.deleteFile(output);
        FileUtils.createNewFile(output);

        FileConfiguration configuration = new FileConfiguration(output);
        configuration.set("recipe", recipe);
        configuration.save();

        configuration = new FileConfiguration(output);
        Recipe recipe2 = configuration.get("recipe", recipe.getClass());

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