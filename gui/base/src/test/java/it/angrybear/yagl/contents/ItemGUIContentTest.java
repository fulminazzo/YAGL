package it.angrybear.yagl.contents;

import it.angrybear.yagl.TestUtils;
import it.angrybear.yagl.items.Item;
import it.angrybear.yagl.items.fields.ItemField;
import it.angrybear.yagl.items.fields.ItemFlag;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemGUIContentTest {

    @Test
    void testMetadatableReplacement() {
        GUIContent guiContent = new ItemGUIContent().setMaterial("stone")
                .setAmount(3).setDisplayName("Hello %var1%")
                .setLore("%lore1%", "%lore2%")
                .setVariable("var1", "world")
                .setVariable("lore1", "Hello friend")
                .setVariable("lore2", "Do you like this lore?");
        Item expected = Item.newItem().setMaterial("stone")
                .setAmount(3).setDisplayName("Hello world")
                .setLore("Hello friend", "Do you like this lore?");

        assertEquals(expected, guiContent.render());
    }

    @Test
    void testCopy() {
        ItemGUIContent expected = newItemGUIContent();
        ItemGUIContent actual = expected.copy();
        assertEquals(expected, actual);
        assertEquals((Item) new Refl<>(expected).getFieldObject("item"), new Refl<>(actual).getFieldObject("item"));
    }

    @Test
    @DisplayName("Item inherited methods should call actual methods of the inner item")
    void testItemMethods() throws InvocationTargetException, IllegalAccessException {
        ItemGUIContent actual = newItemGUIContent();
        Item expected = new Refl<>(actual).getFieldObject("item");

        for (Method method : Item.class.getDeclaredMethods())
            if (!Modifier.isStatic(method.getModifiers()) && !Item.class.isAssignableFrom(method.getReturnType())) {
                final Object[] params;
                if (method.getName().equals("isSimilar")) params = new Object[]{expected, new ItemField[0]};
                else params = Arrays.stream(method.getParameterTypes()).map(TestUtils::mockParameter).toArray(Object[]::new);

                Object obj1 = ReflectionUtils.setAccessible(method).invoke(actual, params);
                Object obj2 = ReflectionUtils.setAccessible(method).invoke(expected, params);
                assertEquals(obj2, obj1);
            }
    }

    private static ItemGUIContent newItemGUIContent() {
        return new ItemGUIContent()
                .setMaterial("stone_sword").setAmount(1)
                .setDurability(1337).setDisplayName("&8Destroyer")
                .setLore("&eUse this sword to fight your enemies")
                .addEnchantment("unbreaking", 3)
                .addEnchantment("sharpness", 5)
                .addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_PLACED_ON)
                .setCustomModelData(1);
    }

    @Test
    void testReturnTypesItem() {
        TestUtils.testReturnType(new ItemGUIContent(), Item.class, m -> m.getName().equals("copy"));
    }

    @Test
    void testReturnTypesGUIContent() {
        TestUtils.testReturnType(new ItemGUIContent(), GUIContent.class, m -> m.getName().equals("copyContent"));
    }
}