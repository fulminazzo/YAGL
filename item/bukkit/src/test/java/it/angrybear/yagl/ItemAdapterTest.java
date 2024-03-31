package it.angrybear.yagl;

import it.angrybear.yagl.items.BukkitItem;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ItemAdapterTest {

    @BeforeEach
    void setUp() {
        BukkitUtils.setupServer();
    }

    @Test
    void testItemConversion() {
        BukkitUtils.setupEnchantments();
        Item expected = Item.newItem("STONE").setAmount(2).setDurability(15)
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
        Material[] materials = new Material[]{Material.IRON_INGOT, Material.GOLD_INGOT, Material.REDSTONE,
                Material.DIAMOND, Material.EMERALD, Material.LAPIS_LAZULI};
        for (int i = 0; i < materials.length; i++)
            expected.setIngredient((char) ('A' + i), new RecipeChoice.ExactChoice(new ItemStack(materials[i])));

        it.angrybear.yagl.items.recipes.ShapedRecipe recipe = new it.angrybear.yagl.items.recipes.ShapedRecipe("test")
                .setOutput(Item.newItem("STONE")).setShape(2, 3);
        for (int i = 0; i < materials.length; i++) recipe.setIngredient(i, BukkitItem.newItem(materials[i]));

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
                .setOutput(Item.newItem("STONE")).addIngredient(Item.newItem("GRASS"));

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
                .setOutput(Item.newItem("STONE")).setIngredient(Item.newItem("COAL"))
                .setExperience(10).setCookingTime(20);

        Refl<?> r1 = new Refl<>(expected);
        Refl<?> r2 = new Refl<>(ItemAdapter.recipeToMinecraft(recipe));

        for (Field field : r1.getNonStaticFields())
            assertEquals((Object) r1.getFieldObject(field), r2.getFieldObject(field));
    }

    @Test
    void testResizeRecipe() {
        final String id = "test";
        final Material craftMaterial = Material.REDSTONE;
        final BukkitItem returnItem = BukkitItem.newItem(Material.REDSTONE_BLOCK);
        final int size = 4;

        ShapedRecipe expected = new ShapedRecipe(new NamespacedKey("yagl", id), returnItem.create());
        expected.shape("AB", "CD");
        for (int i = 0; i < size; i++) expected.setIngredient((char) ('A' + i), new RecipeChoice.ExactChoice(new ItemStack(craftMaterial)));

        it.angrybear.yagl.items.recipes.ShapedRecipe recipe = new it.angrybear.yagl.items.recipes.ShapedRecipe(id)
                .setOutput(returnItem).setShape(3, 3);
        for (int i = 0; i < 9; i++) recipe.setIngredient(i, BukkitItem.newItem(Material.COBBLESTONE));

        recipe.setShape(size / 2, size / 2);
        for (int i = 0; i < size; i++) recipe.setIngredient(i, BukkitItem.newItem(craftMaterial));

        assertEquals(size, recipe.size(), "Invalid recipe size");
        for (Item item : recipe)
            assertEquals(craftMaterial.name(), item.getMaterial(), String.format("Expected material %s", craftMaterial.name()));

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
    void testInvalidRecipeType() {
        assertThrowsExactly(IllegalArgumentException.class, () -> ItemAdapter.recipeToMinecraft(mock(it.angrybear.yagl.items.recipes.Recipe.class)));
    }
}