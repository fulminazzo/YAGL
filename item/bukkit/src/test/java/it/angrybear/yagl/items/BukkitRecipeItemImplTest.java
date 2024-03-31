package it.angrybear.yagl.items;

import it.angrybear.yagl.items.recipes.ShapelessRecipe;
import it.fulminazzo.jbukkit.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BukkitRecipeItemImplTest {

    @Test
    void testRegisterAndUnregisterRecipe() {
        BukkitUtils.setupServer();
        BukkitUtils.setupEnchantments();
        final String id = "test";
        final NamespacedKey key = new NamespacedKey("yagl", id);

        RecipeItem recipeItem = BukkitItem.newRecipeItem(Material.STONE);
        recipeItem.setRecipes(new ShapelessRecipe(id).addIngredient(Item.newItem("STONE")));

        assertNull(Bukkit.getRecipe(key));

        recipeItem.registerRecipes();
        assertNotNull(Bukkit.getRecipe(key));

        recipeItem.unregisterRecipes();
        assertNull(Bukkit.getRecipe(key));
    }
}