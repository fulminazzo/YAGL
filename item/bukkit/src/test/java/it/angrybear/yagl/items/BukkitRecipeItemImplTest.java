package it.angrybear.yagl.items;

import it.angrybear.yagl.items.recipes.ShapelessRecipe;
import it.angrybear.yagl.utils.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BukkitRecipeItemImplTest {

    @Test
    void testRegisterAndUnregisterRecipe() {
        BukkitUtils.setupItemFactory();
        final String id = "test";
        final NamespacedKey key = new NamespacedKey("yagl", id);

        RecipeItem recipeItem = new BukkitRecipeItemImpl().setMaterial("STONE");
        recipeItem.setRecipes(new ShapelessRecipe(id).addIngredient(Item.newItem().setMaterial("STONE")));

        assertNull(Bukkit.getRecipe(key));

        recipeItem.registerRecipes();
        assertNotNull(Bukkit.getRecipe(key));

        recipeItem.unregisterRecipes();
        assertNull(Bukkit.getRecipe(key));
    }
}