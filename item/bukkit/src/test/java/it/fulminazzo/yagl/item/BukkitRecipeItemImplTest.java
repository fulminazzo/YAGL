package it.fulminazzo.yagl.item;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.jbukkit.BukkitUtils;
import it.fulminazzo.jbukkit.annotations.After1_;
import it.fulminazzo.yagl.item.recipe.ShapelessRecipe;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("deprecation")
@After1_(13)
class BukkitRecipeItemImplTest extends BukkitUtils {

    @BeforeAll
    static void setAllUp() {
        setupServer();
    }

    @BeforeEach
    @Override
    protected void setUp() {
        super.setUp();
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

    private static org.bukkit.inventory.Recipe getRecipe(final Object key) {
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

    @Test
    void testInternalMetaCreation() {
        ItemStack expected = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = expected.getItemMeta();
        ((EnchantmentStorageMeta) meta).addStoredEnchant(Enchantment.SILK_TOUCH, 1, true);
        expected.setItemMeta(meta);

        ItemStack actual = BukkitItem.newItem(Material.ENCHANTED_BOOK)
                .copy(BukkitRecipeItem.class)
                .setMetadata(EnchantmentStorageMeta.class, m -> m.addStoredEnchant(Enchantment.SILK_TOUCH, 1, true))
                .create();

        assertEquals(expected, actual);
    }

    private int countIterator(Iterator<?> iterator) {
        int count;
        for (count = 0; iterator.hasNext(); iterator.next(), count++);
        return count;
    }

}