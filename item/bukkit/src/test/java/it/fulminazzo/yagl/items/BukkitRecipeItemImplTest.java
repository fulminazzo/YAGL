package it.fulminazzo.yagl.items;

import it.fulminazzo.yagl.items.recipes.ShapelessRecipe;
import it.fulminazzo.jbukkit.BukkitUtils;
import it.fulminazzo.jbukkit.annotations.After1_;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("deprecation")
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

    @Test
    void testUnregisterNotRegisteredRecipe() {
        BukkitUtils.setupServer();
        BukkitUtils.setupEnchantments();

        BukkitRecipeItem recipeItem = BukkitItem.newRecipeItem(Material.STONE);
        recipeItem.setRecipes(new ShapelessRecipe("test").addIngredient(Item.newItem("STONE")));

        BukkitItem.newRecipeItem("stone")
                .addRecipes(new ShapelessRecipe("test").addIngredient(Item.newItem("STONE")))
                .registerRecipes();

        assertEquals(1, countIterator(Bukkit.getServer().recipeIterator()), "Server should have one registered recipe");

        assertDoesNotThrow(recipeItem::unregisterRecipes);

        assertEquals(1, countIterator(Bukkit.getServer().recipeIterator()), "Server should still have registered recipe");

    }

    private int countIterator(Iterator<?> iterator) {
        int count;
        for (count = 0; iterator.hasNext(); iterator.next(), count++);
        return count;
    }
}