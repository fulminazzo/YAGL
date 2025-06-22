package it.fulminazzo.yagl;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import it.fulminazzo.jbukkit.BukkitUtils;
import it.fulminazzo.jbukkit.annotations.After1_;
import it.fulminazzo.jbukkit.inventory.meta.MockItemMeta;
import it.fulminazzo.yagl.item.BukkitItem;
import it.fulminazzo.yagl.item.Item;
import it.fulminazzo.yagl.item.field.ItemFlag;
import it.fulminazzo.yagl.item.recipe.FurnaceRecipe;
import it.fulminazzo.yagl.item.recipe.Recipe;
import it.fulminazzo.yagl.item.recipe.ShapedRecipe;
import it.fulminazzo.yagl.item.recipe.ShapelessRecipe;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;

import java.lang.reflect.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemAdapterTest extends BukkitUtils {

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
    @After1_(14)
    void testItem() {
        check();
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
    @After1_(14)
    void testItemStackNullDisplayNameAndLore() {
        check();

        ItemStack itemStack = mock(ItemStack.class);
        // Necessary to avoid null checks
        ItemMeta itemMeta = mock(MockItemMeta.class, CALLS_REAL_METHODS);
        new Refl<>(itemMeta)
                .setFieldObject("enchants", new HashMap<>())
                .setFieldObject("itemFlags", new HashSet<>());
        when(itemMeta.getDisplayName()).thenReturn(null);

        when(itemStack.getType()).thenReturn(Material.STONE);
        when(itemStack.getAmount()).thenReturn(1);
        when(itemStack.getItemMeta()).thenReturn(itemMeta);

        assertEquals(Item.newItem("STONE"), ItemAdapter.itemStackToItem(itemStack));
    }

    private ItemFactory mockNullItemFactory() {
        ItemFactory itemFactory = mock(ItemFactory.class);
        when(itemFactory.getItemMeta(any(Material.class))).thenReturn(null);
        return itemFactory;
    }

    @Nested
    @DisplayName("ItemStack to Item conversion")
    class ItemStackToItem {

        @Test
        void testNullItemStackShouldReturnNull() {
            assertNull(ItemAdapter.itemStackToItem(null));
        }

        @Test
        void testNullItemMeta() {
            when(Bukkit.getServer().getItemFactory()).thenAnswer(a -> mockNullItemFactory());
            assertDoesNotThrow(() -> ItemAdapter.itemStackToItem(new ItemStack(Material.STONE)));
        }

        @Test
        void testNullDisplayNameAndLore() {
            assertDoesNotThrow(() -> ItemAdapter.itemStackToItem(new ItemStack(Material.STONE)));
        }

        @Test
        void testNullFieldsItem() {
            // setupServer necessary here to avoid mocking issues
            setupServer();

            BukkitItem expected = BukkitItem.newItem(Material.STONE);
            ItemStack itemStack = new ItemStack(Material.STONE);
            ItemMeta meta = new MockItemMeta();
            meta.setDisplayName(null);
            meta.setLore(null);
            try {
                meta.setCustomModelData(-1);
                expected.setCustomModelData(-1);
            } catch (NoSuchMethodError ignored) {}
            itemStack.setItemMeta(meta);
            assertEquals(expected, ItemAdapter.itemStackToItem(itemStack));
        }

        @Test
        void testNullMetaItem() {
            ItemStack itemStack = new ItemStack(Material.STONE);
            itemStack.setItemMeta(null);
            assertEquals(BukkitItem.newItem(Material.STONE), ItemAdapter.itemStackToItem(itemStack));
        }

    }

    @Nested
    @DisplayName("Item to ItemStack conversion")
    class ItemToItemStack {

        @Test
        void testNullItemShouldReturnNull() {
            assertNull(ItemAdapter.itemToItemStack(null));
        }

        @Test
        void testItemWithNoMaterial() {
            assertThrowsExactly(IllegalArgumentException.class, () -> ItemAdapter.itemToItemStack(Item.newItem()));
        }

        @Test
        @After1_(11)
        void testNotDamageableItemMeta() {
            check();
            ItemFactory itemFactory = mock(ItemFactory.class);
            when(itemFactory.getItemMeta(any(Material.class))).thenReturn(mock(ItemMeta.class));

            when(Bukkit.getServer().getItemFactory()).thenReturn(itemFactory);
            assertDoesNotThrow(() -> ItemAdapter.itemToItemStack(BukkitItem.newItem(Material.STONE)
                    .setDurability(2)));
        }

        @Test
        void testNullItemMeta() {
            when(Bukkit.getServer().getItemFactory()).thenAnswer(a -> mockNullItemFactory());
            assertDoesNotThrow(() -> ItemAdapter.itemToItemStack(BukkitItem.newItem(Material.STONE)));
        }

    }

    @SuppressWarnings("deprecation")
    @Nested
    class RecipeConversion {
        public static final String RECIPE_CHOICE_CLASS = "org.bukkit.inventory.RecipeChoice.ExactChoice";

        @Test
        void testNullRecipe() throws InvocationTargetException, IllegalAccessException {
            final String methodName = "recipeToMinecraft";
            Refl<Class<ItemAdapter>> adapter = new Refl<>(ItemAdapter.class);
            @NotNull List<Method> methods = adapter.getMethods(m -> m.getName().equals(methodName));
            assertEquals(4, methods.size(), String.format("Could not find all '%s' methods", methodName));
            for (Method method : methods) {
                Object result = ReflectionUtils.setAccessibleOrThrow(method).invoke(ItemAdapter.class, (Object) null);
                assertNull(result);
            }
        }

        private Object newRecipeChoice(final ItemStack @NotNull ... args) {
            return new Refl<>(RECIPE_CHOICE_CLASS, (Object) args).getObject();
        }

        @Test
        @After1_(13)
        void testShapedRecipe() {
            check();
            org.bukkit.inventory.ShapedRecipe expected = new org.bukkit.inventory.ShapedRecipe(
                    new org.bukkit.NamespacedKey("yagl", "test"),
                    new ItemStack(Material.STONE)
            );
            expected.shape("ABC", "DEF");
            Material[] materials = new Material[]{Material.IRON_INGOT, Material.GOLD_INGOT, Material.REDSTONE,
                    Material.DIAMOND, Material.EMERALD, Material.LAPIS_LAZULI};
            Refl<?> r1 = new Refl<>(expected);
            for (int i = 0; i < materials.length; i++)
                r1.invokeMethod("setIngredient", (char) ('A' + i), newRecipeChoice(new ItemStack(materials[i])));
            r1.invokeMethod("setIngredient",
                    new Class[]{char.class, ReflectionUtils.getClass(RECIPE_CHOICE_CLASS)},
                    (char) ('A' + 3), null);

            ShapedRecipe recipe = new ShapedRecipe("test")
                    .setOutput(Item.newItem("STONE")).setShape(2, 3);
            for (int i = 0; i < materials.length; i++) recipe.setIngredient(i, BukkitItem.newItem(materials[i]));
            recipe.setIngredient(3, null);

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
        @After1_(13)
        void testShapelessRecipe() {
            check();
            org.bukkit.inventory.ShapelessRecipe expected = new org.bukkit.inventory.ShapelessRecipe(
                    new org.bukkit.NamespacedKey("yagl", "test"),
                    new ItemStack(Material.STONE)
            );
            Refl<?> r1 = new Refl<>(expected);
            r1.invokeMethod("addIngredient", newRecipeChoice(new ItemStack(Material.DIAMOND)));

            ShapelessRecipe recipe = new ShapelessRecipe("test")
                    .setOutput(Item.newItem("STONE")).addIngredient(Item.newItem("DIAMOND"));

            Refl<?> r2 = new Refl<>(ItemAdapter.recipeToMinecraft(recipe));

            for (Field field : r1.getNonStaticFields())
                assertEquals((Object) r1.getFieldObject(field), r2.getFieldObject(field));
        }

        @Test
        @After1_(13)
        void testFurnaceRecipe() {
            check();
            org.bukkit.inventory.FurnaceRecipe expected = new org.bukkit.inventory.FurnaceRecipe(
                    new org.bukkit.NamespacedKey("yagl", "test"),
                    new ItemStack(Material.STONE), Material.COAL, 10, 20
            );
            Refl<?> r1 = new Refl<>(expected);
            r1.setFieldObject("ingredient", newRecipeChoice(new ItemStack(Material.COAL)));

            FurnaceRecipe recipe = new FurnaceRecipe("test")
                    .setOutput(Item.newItem("STONE")).setIngredient(Item.newItem("COAL"))
                    .setExperience(10).setCookingTime(1);

            Refl<?> r2 = new Refl<>(ItemAdapter.recipeToMinecraft(recipe));

            for (Field field : r1.getNonStaticFields())
                assertEquals((Object) r1.getFieldObject(field), r2.getFieldObject(field));
        }

        @Test
        @After1_(13)
        void testResizeRecipe() {
            check();
            final String id = "test";
            final Material craftMaterial = Material.REDSTONE;
            final BukkitItem returnItem = BukkitItem.newItem(Material.REDSTONE_BLOCK);
            final int size = 4;

            org.bukkit.inventory.ShapedRecipe expected = new org.bukkit.inventory.ShapedRecipe(
                    new org.bukkit.NamespacedKey("yagl", id),
                    returnItem.create()
            );
            expected.shape("AB", "CD");
            Refl<?> r1 = new Refl<>(expected);
            for (int i = 0; i < size; i++)
                r1.invokeMethod("setIngredient", (char) ('A' + i), newRecipeChoice(new ItemStack(craftMaterial)));

            ShapedRecipe recipe = new ShapedRecipe(id)
                    .setOutput(returnItem).setShape(3, 3);
            for (int i = 0; i < 9; i++) recipe.setIngredient(i, BukkitItem.newItem(Material.COBBLESTONE));

            recipe.setShape(size / 2, size / 2);
            for (int i = 0; i < size; i++) recipe.setIngredient(i, BukkitItem.newItem(craftMaterial));

            assertEquals(size, recipe.size(), "Invalid recipe size");
            for (Item item : recipe)
                assertEquals(craftMaterial.name(), item.getMaterial(), String.format("Expected material %s", craftMaterial.name()));

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
        void testInvalidRecipeType() {
            assertThrowsExactly(IllegalArgumentException.class, () -> ItemAdapter.recipeToMinecraft(mock(Recipe.class)));
        }

    }

}
