package it.fulminazzo.yagl.items;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.jbukkit.BukkitUtils;
import it.fulminazzo.jbukkit.annotations.After1_;
import it.fulminazzo.yagl.items.recipes.ShapelessRecipe;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
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
        BukkitUtils.setupServer();
    }

    @Test
    void testRegisterAndUnregisterRecipe() {
        final String id = "test";
        final org.bukkit.NamespacedKey key = new org.bukkit.NamespacedKey("yagl", id);

        BukkitRecipeItem recipeItem = BukkitItem.newRecipeItem(Material.STONE);
        recipeItem.setRecipes(new ShapelessRecipe(id).addIngredient(Item.newItem("STONE")));

        assertNull(getRecipe(key));

        recipeItem.registerRecipes();
        assertNotNull(getRecipe(key));

        recipeItem.unregisterRecipes();
        assertNull(getRecipe(key));
    }

    private static org.bukkit.inventory.Recipe getRecipe(final org.bukkit.NamespacedKey key) {
        return Bukkit.getRecipesFor(new ItemStack(Material.STONE)).stream()
                .filter(r -> key.equals(new Refl<>(r).getFieldObject("key")))
                .findFirst().orElse(null);
    }

    @Test
    void testUnregisterNotRegisteredRecipe() {
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