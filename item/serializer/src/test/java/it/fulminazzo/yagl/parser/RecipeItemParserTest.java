package it.fulminazzo.yagl.parser;

import it.fulminazzo.yagl.item.Item;
import it.fulminazzo.yagl.item.field.ItemFlag;
import it.fulminazzo.yagl.item.recipe.FurnaceRecipe;
import it.fulminazzo.yagl.item.recipe.ShapedRecipe;
import it.fulminazzo.yagl.item.recipe.ShapelessRecipe;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;
import it.fulminazzo.yamlparser.utils.FileUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecipeItemParserTest {

    private static Object[] testItems() {
        return new Object[]{
                Item.newRecipeItem("stone").setAmount(2).setDurability(15)
                        .setDisplayName("&7Cool stone").setLore("Click on this", "To be OP")
                        .addEnchantment("enchant1", 10).addEnchantment("enchant2", 20)
                        .addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS)
                        .setUnbreakable(true)
                        .addRecipes(
                                new ShapedRecipe("shaped").setShape(2, 2)
                                        .setIngredient(0, Item.newItem().setMaterial("diamond"))
                                        .setIngredient(1, Item.newItem().setMaterial("redstone"))
                                        .setIngredient(2, Item.newItem().setMaterial("emerald"))
                                        .setIngredient(3, Item.newItem().setMaterial("gold_ingot")),
                                new ShapelessRecipe("shapeless").addIngredients(
                                        Item.newItem().setMaterial("stone"), Item.newItem().setMaterial("andesite"),
                                        Item.newItem().setMaterial("diorite"), Item.newItem().setMaterial("cobblestone")),
                                new FurnaceRecipe("furnace").setCookingTime(3).setExperience(10).setIngredient(
                                        Item.newItem().setMaterial("kelp"))
                        )
                        .setCustomModelData(7),
                new Refl<>(Item.newRecipeItem("stone").setAmount(2).setDurability(15)
                        .setDisplayName("&7Cool stone").setLore("Click on this", "To be OP")
                        .addEnchantment("enchant1", 10).addEnchantment("enchant2", 20)
                        .addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS)
                        .setUnbreakable(true)
                        .setCustomModelData(7))
                        .setFieldObject("recipes", null)
                        .getObject()
        };
    }

    @ParameterizedTest
    @MethodSource("testItems")
    void testSaveAndLoad(Item item) throws IOException {
        ItemYAGLParser.addAllParsers();

        File output = new File("build/resources/test/recipe-item.yml");
        if (output.exists()) FileUtils.deleteFile(output);
        FileUtils.createNewFile(output);
        FileConfiguration configuration = new FileConfiguration(output);
        configuration.set("item", item);
        if (!configuration.contains("item.recipes")) configuration.set("item.recipes", "mock");
        configuration.save();

        configuration = new FileConfiguration(output);
        Item item2 = configuration.get("item", Item.class);
        List<?> recipes1 = new Refl<>(item).getFieldObject("recipes");
        List<?> recipes2 = new Refl<>(item2).getFieldObject("recipes");
        assertNotNull(recipes2);
        if (recipes1 == null) assertTrue(recipes2.isEmpty(), "Loaded recipes should have been empty");
        else assertIterableEquals(recipes1, recipes2);
        assertEquals(item, item2);
    }
}