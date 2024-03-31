package it.angrybear.yagl;

import it.angrybear.yagl.items.Item;
import it.angrybear.yagl.items.fields.ItemFlag;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.jbukkit.BukkitUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemAdapterTest {

    @BeforeEach
    void setUp() {
        BukkitUtils.setupServer();
    }

    @Test
    void testItemConversion() {
        BukkitUtils.setupEnchantments();
        Item expected = Item.newItem().setMaterial("STONE").setAmount(2).setDurability(15)
                .setDisplayName("&7Cool stone").setLore("Click on this", "To be OP")
                .addEnchantment("flame", 10)
                .addEnchantment("infinity", 20)
                .addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS)
                .setUnbreakable(true)
                .setCustomModelData(7);
        ItemStack itemStack = ItemAdapter.itemToItemStack(expected);

        assertEquals(expected, ItemAdapter.itemStackToItem(itemStack));
    }

    @Test
    void testShapedRecipeConversion() {
        ShapedRecipe expected = new ShapedRecipe(new NamespacedKey("yagl", "test"),
                new ItemStack(Material.STONE));
        expected.shape("ABC", "DEF");
        expected.setIngredient('A', new RecipeChoice.ExactChoice(new ItemStack(Material.DIAMOND)));
        expected.setIngredient('B', new RecipeChoice.ExactChoice(new ItemStack(Material.EMERALD)));
        expected.setIngredient('C', new RecipeChoice.ExactChoice(new ItemStack(Material.STONE)));
        expected.setIngredient('D', new RecipeChoice.ExactChoice(new ItemStack(Material.OAK_LEAVES)));
        expected.setIngredient('E', new RecipeChoice.ExactChoice(new ItemStack(Material.COBBLED_DEEPSLATE)));
        expected.setIngredient('F', new RecipeChoice.ExactChoice(new ItemStack(Material.LAPIS_BLOCK)));

        it.angrybear.yagl.items.recipes.ShapedRecipe recipe = new it.angrybear.yagl.items.recipes.ShapedRecipe("test")
                .setOutput(Item.newItem().setMaterial("STONE")).setShape(2, 3)
                .setIngredient(0, Item.newItem().setMaterial("DIAMOND"))
                .setIngredient(1, Item.newItem().setMaterial("EMERALD"))
                .setIngredient(2, Item.newItem().setMaterial("STONE"))
                .setIngredient(3, Item.newItem().setMaterial("OAK_LEAVES"))
                .setIngredient(4, Item.newItem().setMaterial("COBBLED_DEEPSLATE"))
                .setIngredient(5, Item.newItem().setMaterial("LAPIS_BLOCK"));

        Refl<?> r1 = new Refl<>(expected);
        Refl<?> r2 = new Refl<>(ItemAdapter.recipeToMinecraft(recipe));

        for (Field field : r1.getNonStaticFields()) {
            Object obj1 = r1.getFieldObject(field), obj2 = r2.getFieldObject(field);
            if (obj1 != null && obj1.getClass().isArray())
                assertArrayEquals((Object[]) obj1, (Object[]) obj2);
            else assertEquals((Object) r1.getFieldObject(field), r2.getFieldObject(field));
        }
    }

    @Test
    void testShapelessRecipeConversion() {
        ShapelessRecipe expected = new ShapelessRecipe(new NamespacedKey("yagl", "test"),
                new ItemStack(Material.STONE));
        expected.addIngredient(new RecipeChoice.ExactChoice(new ItemStack(Material.GRASS)));

        it.angrybear.yagl.items.recipes.ShapelessRecipe recipe = new it.angrybear.yagl.items.recipes.ShapelessRecipe("test")
                .setOutput(Item.newItem().setMaterial("STONE")).addIngredient(Item.newItem().setMaterial("GRASS"));

        Refl<?> r1 = new Refl<>(expected);
        Refl<?> r2 = new Refl<>(ItemAdapter.recipeToMinecraft(recipe));

        for (Field field : r1.getNonStaticFields())
            assertEquals((Object) r1.getFieldObject(field), r2.getFieldObject(field));
    }

    @Test
    void testFurnaceRecipeConversion() {
        FurnaceRecipe expected = new FurnaceRecipe(new NamespacedKey("yagl", "test"),
                new ItemStack(Material.STONE), Material.COAL, 10, 20);
        new Refl<>(expected).setFieldObject("ingredient", new RecipeChoice.ExactChoice(new ItemStack(Material.COAL)));

        it.angrybear.yagl.items.recipes.FurnaceRecipe recipe = new it.angrybear.yagl.items.recipes.FurnaceRecipe("test")
                .setOutput(Item.newItem().setMaterial("STONE")).setIngredient(Item.newItem().setMaterial("COAL"))
                .setExperience(10).setCookingTime(20);

        Refl<?> r1 = new Refl<>(expected);
        Refl<?> r2 = new Refl<>(ItemAdapter.recipeToMinecraft(recipe));

        for (Field field : r1.getNonStaticFields())
            assertEquals((Object) r1.getFieldObject(field), r2.getFieldObject(field));
    }
}