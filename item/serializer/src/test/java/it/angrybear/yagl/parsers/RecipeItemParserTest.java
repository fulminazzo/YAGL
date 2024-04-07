package it.angrybear.yagl.parsers;

import it.angrybear.yagl.items.Item;
import it.angrybear.yagl.items.fields.ItemFlag;
import it.angrybear.yagl.items.recipes.FurnaceRecipe;
import it.angrybear.yagl.items.recipes.ShapedRecipe;
import it.angrybear.yagl.items.recipes.ShapelessRecipe;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;
import it.fulminazzo.yamlparser.utils.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class RecipeItemParserTest {

    @Test
    void testSaveAndLoad() throws IOException {
        ItemYAGLParser.addAllParsers();

        File output = new File("build/resources/test/recipe-item.yml");
        if (output.exists()) FileUtils.deleteFile(output);
        FileUtils.createNewFile(output);
        Item item = Item.newRecipeItem("stone").setAmount(2).setDurability(15)
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
                .setCustomModelData(7);
        FileConfiguration configuration = new FileConfiguration(output);
        configuration.set("item", item);
        configuration.save();

        configuration = new FileConfiguration(output);
        Item item2 = configuration.get("item", Item.class);
        assertIterableEquals(new Refl<>(item).getFieldObject("recipes"), new Refl<>(item2).getFieldObject("recipes"));
        assertEquals(item, item2);
    }
}