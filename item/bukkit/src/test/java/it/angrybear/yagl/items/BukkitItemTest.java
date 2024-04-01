package it.angrybear.yagl.items;

import it.angrybear.yagl.items.fields.ItemFlag;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ExceptionUtils;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import it.fulminazzo.jbukkit.BukkitUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class BukkitItemTest {

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
        BukkitUtils.setupServer();
        BukkitUtils.setupEnchantments();

        ItemStack expected = new ItemStack(Material.STONE, 3);
        ItemMeta meta = expected.getItemMeta();
        meta.setDisplayName("Hello world");
        meta.setLore(Arrays.asList("An interesting lore..."));
        meta.addEnchant(Enchantment.CHANNELING, 3, true);
        expected.setItemMeta(meta);

        BukkitItem actual = BukkitItem.newItem(Material.STONE).setAmount(3)
                .setDisplayName("Hello world").addLore("An interesting lore...")
                .addEnchantment("channeling", 3);

        assertTrue(actual.isSimilar(expected));
    }

    @Test
    void testMetaCreation() {
        BukkitUtils.setupServer();
        ItemStack expected = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = expected.getItemMeta();
        ((EnchantmentStorageMeta) meta).addStoredEnchant(Enchantment.CHANNELING, 1, true);
        expected.setItemMeta(meta);

        ItemStack actual = BukkitItem.newItem(Material.ENCHANTED_BOOK)
                .create(EnchantmentStorageMeta.class, m -> m.addStoredEnchant(Enchantment.CHANNELING, 1, true));

        assertEquals(expected, actual);
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
                mockItem(new PersistentItem()),
                mockItem(new MovablePersistentItem())
        };
    }

    @ParameterizedTest
    @MethodSource("testItems")
    void testItemsReturnType(Item item) {
        final Refl<Item> itemRefl = new Refl<>(item);

        for (Method method : itemRefl.getNonStaticMethods()) {
            final Class<?>[] parameters = method.getParameterTypes();
            final String methodString = String.format("%s(%s)", method.getName(), Arrays.stream(parameters)
                            .map(Class::getSimpleName).collect(Collectors.joining(", ")));
            try {
                final Class<?> returnType = method.getReturnType();
                if (!Item.class.isAssignableFrom(returnType)) continue;
                if (method.equals(Item.class.getMethod("copy", Class.class))) continue;

                String errorMessage = String.format("Method '%s' of class '%s' did not return itself",
                        methodString, item.getClass().getSimpleName());

                Object[] mockParameters = Arrays.stream(parameters).map(this::mockParameter).toArray(Object[]::new);
                method.setAccessible(true);
                Object o = method.invoke(item, mockParameters);

                if (method.getName().equals("copy"))
                    assertInstanceOf(item.getClass(), o, String.format("Returned object from %s call should have been %s but was %s",
                            methodString, item.getClass(), o.getClass()));
                else assertEquals(item.hashCode(), o.hashCode(), errorMessage);
            } catch (Exception e) {
                System.err.printf("An exception occurred while testing method '%s'%n", methodString);
                ExceptionUtils.throwException(e);
            }
        }
    }

    private Object mockParameter(Class<?> clazz) {
        clazz = ReflectionUtils.getWrapperClass(clazz);
        if (Number.class.isAssignableFrom(clazz)) return 1;
        if (String.class.isAssignableFrom(clazz)) return "STONE";
        if (Boolean.class.isAssignableFrom(clazz)) return false;
        if (clazz.isEnum()) {
            Enum<?>[] enums = new Refl<>(clazz).invokeMethod("values");
            if (enums == null) return null;
            return enums[0];
        }
        if (clazz.isArray()) return Array.newInstance(clazz.componentType(), 0);
        if (Collection.class.isAssignableFrom(clazz)) return new ArrayList<>();
        return mock(clazz);
    }
}