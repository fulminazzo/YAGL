package it.angrybear.yagl.items;

import it.angrybear.yagl.items.recipes.ShapelessRecipe;
import it.fulminazzo.jbukkit.BukkitUtils;
import it.fulminazzo.jbukkit.annotations.After1_;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@After1_(13)
class BukkitRecipeItemImplTest extends BukkitUtils {

    @BeforeEach
    @Override
    protected void setUp() {
        super.setUp();
    }

    @Test
    void testRegisterAndUnregisterRecipe() {
        BukkitUtils.setupServer();
        BukkitUtils.setupEnchantments();
        final String id = "test";
        final org.bukkit.NamespacedKey key = new org.bukkit.NamespacedKey("yagl", id);

        BukkitRecipeItem recipeItem = BukkitItem.newRecipeItem(Material.STONE);
        recipeItem.setRecipes(new ShapelessRecipe(id).addIngredient(Item.newItem("STONE")));

        assertNull(Bukkit.getRecipe(key));

        recipeItem.registerRecipes();
        assertNotNull(Bukkit.getRecipe(key));

        recipeItem.unregisterRecipes();
        assertNull(Bukkit.getRecipe(key));
    }
}