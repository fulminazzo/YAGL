package it.fulminazzo.yagl.item;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.jbukkit.BukkitUtils;
import it.fulminazzo.yagl.ItemAdapter;
import it.fulminazzo.yagl.TestUtils;
import it.fulminazzo.yagl.item.field.ItemFlag;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BukkitItemTest {

    @BeforeAll
    static void setAllUp() {
        BukkitUtils.setupServer();
        BukkitUtils.setupEnchantments();
    }

    private static Item[] getTestMaterialItems() {
        return new Item[]{
                BukkitItem.newItem().setMaterial("DIAMOND_SWORD"),
                BukkitItem.newItem().setMaterial(Material.DIAMOND_SWORD),
                BukkitItem.newItem("DIAMOND_SWORD"),
                BukkitItem.newItem(Material.DIAMOND_SWORD),
                BukkitItem.newRecipeItem().setMaterial("DIAMOND_SWORD"),
                BukkitItem.newRecipeItem().setMaterial(Material.DIAMOND_SWORD),
                BukkitItem.newRecipeItem("DIAMOND_SWORD"),
                BukkitItem.newRecipeItem(Material.DIAMOND_SWORD),
        };
    }

    @ParameterizedTest
    @MethodSource("getTestMaterialItems")
    void testSetMaterial(Item actual) {
        Item expected = Item.newItem().setMaterial("DIAMOND_SWORD");
        assertEquals(expected.getMaterial(), actual.getMaterial());
    }

    @Test
    void testIsSimilar() {
        ItemStack expected = new ItemStack(Material.STONE, 3);
        ItemMeta meta = expected.getItemMeta();
        meta.setDisplayName("Hello world");
        meta.setLore(Collections.singletonList("An interesting lore..."));
        meta.addEnchant(Enchantment.SILK_TOUCH, 3, true);
        expected.setItemMeta(meta);

        BukkitItem actual = BukkitItem.newItem(Material.STONE).setAmount(3)
                .setDisplayName("Hello world").addLore("An interesting lore...")
                .addEnchantment("silk_touch", 3);

        assertTrue(actual.isSimilar(expected), String.format("%s should have been similar to %s", actual, expected));
    }

    @Test
    void testInternalMetaCreation() {
        ItemStack expected = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = expected.getItemMeta();
        ((EnchantmentStorageMeta) meta).addStoredEnchant(Enchantment.SILK_TOUCH, 1, true);
        expected.setItemMeta(meta);

        ItemStack actual = BukkitItem.newItem(Material.ENCHANTED_BOOK)
                .setMetadata(EnchantmentStorageMeta.class, m -> m.addStoredEnchant(Enchantment.SILK_TOUCH, 1, true))
                .create();

        assertEquals(expected, actual);
    }

    @Test
    void testCopyCopiesInternalMeta() {
        Class<ItemMeta> clazz = ItemMeta.class;
        Consumer<ItemMeta> consumer = c -> {};

        BukkitItem src = BukkitItem.newItem().setMetadata(clazz, consumer);
        BukkitItem dst = src.copy();

        Refl<?> srcRefl = new Refl<>(src);
        Refl<?> dstRefl = new Refl<>(dst);

        assertEquals((Class<?>) srcRefl.getFieldObject("itemMetaClass"), dstRefl.getFieldObject("itemMetaClass"));
        assertEquals((Consumer<?>) srcRefl.getFieldObject("metaFunction"), dstRefl.getFieldObject("metaFunction"));
    }

    @Test
    void testMetaCreation() {
        ItemStack expected = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = expected.getItemMeta();
        ((EnchantmentStorageMeta) meta).addStoredEnchant(Enchantment.SILK_TOUCH, 1, true);
        expected.setItemMeta(meta);

        ItemStack actual = BukkitItem.newItem(Material.ENCHANTED_BOOK)
                .create(EnchantmentStorageMeta.class, m -> m.addStoredEnchant(Enchantment.SILK_TOUCH, 1, true));

        assertEquals(expected, actual);
    }

    @Test
    void testCreateNullMaterial() {
        assertThrowsExactly(IllegalArgumentException.class, () -> BukkitItem.newItem().create());
    }

    @Test
    void testCreateWithNullMetaClass() {
        ItemStack expected = new ItemStack(Material.STONE);
        ItemStack actual = BukkitItem.newItem("stone").create(null, m -> m.setDisplayName("Robert"));
        assertEquals(expected, actual);
    }

    @Test
    void testCreateWithNullMetaFunction() {
        ItemStack expected = new ItemStack(Material.STONE);
        ItemStack actual = BukkitItem.newItem("stone").create(ItemMeta.class, null);
        assertEquals(expected, actual);
    }

    @Test
    void testCreateWithNullMeta() {
        ItemStack expected = new ItemStack(Material.STONE);
        expected.setItemMeta(null);
        try (MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class);
             MockedStatic<ItemAdapter> itemAdapter = mockStatic(ItemAdapter.class)) {
            ItemFactory mockFactory = mock(ItemFactory.class);
            when(mockFactory.getItemMeta(any())).thenReturn(null);
            bukkit.when(Bukkit::getItemFactory).thenReturn(mockFactory);
            itemAdapter.when(() -> ItemAdapter.itemToItemStack(any())).thenReturn(expected);

            ItemStack actual = BukkitItem.newItem("stone").create(ItemMeta.class, m -> m.setDisplayName("Hello world"));
            assertEquals(expected, actual);
        }
    }

    private static Item mockItem(Item item) {
        return item.setMaterial("STONE").setAmount(2).setDurability(15)
                .setDisplayName("&7Cool stone").setLore("Click on this", "To be OP")
                .addEnchantment("flame", 10).addEnchantment("unbreaking", 20)
                .addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS)
                .setUnbreakable(true)
                .setCustomModelData(7);
    }

    private static Item[] testItems() {
        return new Item[]{
                mockItem(Item.newItem()),
                mockItem(Item.newRecipeItem()),
                mockItem(BukkitItem.newItem()),
                mockItem(BukkitItem.newRecipeItem()),
                mockItem(PersistentItem.newItem())
        };
    }

    @ParameterizedTest
    @MethodSource("testItems")
    void testItemsReturnType(Item item) throws NoSuchMethodException {
        final Method copyMethod = Item.class.getMethod("copy", Class.class);
        TestUtils.testReturnType(item, Item.class, m -> m.equals(copyMethod));
    }
}