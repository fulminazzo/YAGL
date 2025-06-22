package it.fulminazzo.yagl;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.jbukkit.BukkitUtils;
import it.fulminazzo.jbukkit.annotations.Before1_;
import it.fulminazzo.yagl.item.BukkitItem;
import it.fulminazzo.yagl.item.Item;
import it.fulminazzo.yagl.item.field.ItemFlag;
import it.fulminazzo.yagl.item.recipe.ShapedRecipe;
import it.fulminazzo.yagl.item.recipe.ShapelessRecipe;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Before1_(8.8)
class ObsoleteItemAdapterTest extends BukkitUtils {

    @BeforeAll
    static void setAllUp() {
        setupServer();
        setupEnchantments();
    }

    @BeforeEach
    @Override
    protected void setUp() {
        super.setUp();
    }

    @Test
    void testItemConversion() {
        Item expected = Item.newItem("STONE").setAmount(2).setDurability(15)
                .setDisplayName("&7Cool stone").setLore("Click on this", "To be OP")
                .addEnchantment("INFINITY", 20)
                .addEnchantment("FLAME", 10)
                .addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS)
                .setUnbreakable(true);
        ItemStack itemStack = ItemAdapter.itemToItemStack(expected);

        assertEquals(expected, ItemAdapter.itemStackToItem(itemStack));
    }

    @Test
    void testShapedRecipeConversion() {
        org.bukkit.inventory.ShapedRecipe expected = new org.bukkit.inventory.ShapedRecipe(new ItemStack(Material.STONE));
        expected.shape("ABC", "DEF");

        Refl<?> r1 = new Refl<>(expected);

        Material[] materials = new Material[]{Material.IRON_INGOT, Material.GOLD_INGOT, Material.REDSTONE,
                Material.DIAMOND, Material.EMERALD, Material.INK_SACK};
        for (int i = 0; i < materials.length; i++)
            expected.setIngredient((char) ('A' + i), new ItemStack(materials[i]).getData());

        ShapedRecipe recipe = new ShapedRecipe("test")
                .setOutput(Item.newItem("STONE")).setShape(2, 3);
        for (int i = 0; i < materials.length; i++) recipe.setIngredient(i, BukkitItem.newItem(materials[i]));

        Refl<?> r2 = new Refl<>(ItemAdapter.recipeToMinecraft(recipe));

        for (Field field : r1.getNonStaticFields()) {
            Object obj1 = r1.getFieldObject(field);
            Object obj2 = r2.getFieldObject(field);
            if (obj1 != null && obj1.getClass().isArray())
                assertArrayEquals((Object[]) obj1, (Object[]) obj2);
            else assertEquals((Object) r1.getFieldObject(field), r2.getFieldObject(field));
        }
    }

    @Test
    void testShapelessRecipeConversion() {
        check();
        org.bukkit.inventory.ShapelessRecipe expected = new org.bukkit.inventory.ShapelessRecipe(new ItemStack(Material.STONE));
        expected.addIngredient(new ItemStack(Material.GRASS).getData());

        Refl<?> r1 = new Refl<>(expected);

        ShapelessRecipe recipe = new ShapelessRecipe("test")
                .setOutput(Item.newItem("STONE")).addIngredient(Item.newItem("GRASS"));

        Refl<?> r2 = new Refl<>(ItemAdapter.recipeToMinecraft(recipe));

        for (Field field : r1.getNonStaticFields())
            assertEquals((Object) r1.getFieldObject(field), r2.getFieldObject(field));
    }

}
