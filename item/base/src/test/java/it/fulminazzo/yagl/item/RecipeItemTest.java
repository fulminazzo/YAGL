package it.fulminazzo.yagl.item;

import it.fulminazzo.yagl.exception.NotImplementedException;
import it.fulminazzo.yagl.item.field.ItemFlag;
import it.fulminazzo.yagl.item.recipe.FurnaceRecipe;
import it.fulminazzo.yagl.item.recipe.ShapedRecipe;
import it.fulminazzo.yagl.item.recipe.ShapelessRecipe;
import it.fulminazzo.yagl.wrapper.PotionEffect;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecipeItemTest {

    @Test
    void testCopy() {
        Item i1 = new RecipeItemImpl()
                .setMaterial("material")
                .setAmount(2)
                .setDurability(4)
                .setDisplayName("Hello world")
                .addLore("lore")
                .addEnchantments("enchant1", "enchant2")
                .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                .addPotionEffects(new PotionEffect("strength", 20, 2))
                .setCustomModelData(2)
                .setUnbreakable(true)
                .setRecipes(
                        new ShapelessRecipe("shapeless"),
                        new ShapedRecipe("shaped"),
                        new FurnaceRecipe("furnace")
                );
        Item i2 = i1.copy();
        assertEquals(i1, i2);
    }

    @Test
    void testClearRecipes() {
        assertFalse(Item.newRecipeItem().setRecipes(
                        new ShapelessRecipe("shapeless"),
                        new ShapedRecipe("shaped"),
                        new FurnaceRecipe("furnace"))
                .clearRecipes().iterator().hasNext(), "After calling clearRecipes, there should be no recipes saved");
    }

    @Test
    void recipeItemRegisterShouldBeUnimplemented() {
        assertThrowsExactly(NotImplementedException.class, () -> Item.newRecipeItem().registerRecipes());
    }

    @Test
    void recipeItemUnregisterShouldBeUnimplemented() {
        assertThrowsExactly(NotImplementedException.class, () -> Item.newRecipeItem().unregisterRecipes());
    }

}
