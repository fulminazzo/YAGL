package it.fulminazzo.yagl.content;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import it.fulminazzo.yagl.TestUtils;
import it.fulminazzo.yagl.action.GUIItemAction;
import it.fulminazzo.yagl.action.command.GUIItemCommand;
import it.fulminazzo.yagl.content.requirement.PermissionRequirement;
import it.fulminazzo.yagl.content.requirement.RequirementChecker;
import it.fulminazzo.yagl.item.Item;
import it.fulminazzo.yagl.item.field.ItemField;
import it.fulminazzo.yagl.item.field.ItemFlag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemGUIContentTest {

    @Test
    void testMetadatableReplacement() {
        GUIContent guiContent = ItemGUIContent.newInstance("stone")
                .setAmount(3).setDisplayName("Hello <var1>")
                .setLore("<lore1>", "<lore2>")
                .setVariable("var1", "world")
                .setVariable("lore1", "Hello friend")
                .setVariable("lore2", "Do you like this lore?");
        Item expected = Item.newItem().setMaterial("stone")
                .setAmount(3).setDisplayName("Hello world")
                .setLore("Hello friend", "Do you like this lore?");

        assertEquals(expected, guiContent.render());
    }

    private static Object[][] actions() {
        return new Object[][]{
                new Object[]{null, null},
                new Object[]{
                        (GUIItemAction) (v, g, c) -> System.out.println("Hello, world"),
                        (RequirementChecker) (v) -> false
                },
                new Object[]{
                        new GUIItemCommand("say Hello, world"),
                        new PermissionRequirement("permission")
                }
        };
    }

    @ParameterizedTest
    @MethodSource("actions")
    void testCopy(GUIItemAction action, RequirementChecker requirement) {
        ItemGUIContent expected = newInstance().onClickItem(action).setViewRequirements(requirement);
        ItemGUIContent actual = expected.copy().onClickItem(action).setViewRequirements(requirement);
        assertEquals("value", actual.getVariable("name"));
        assertEquals(expected, actual);
        assertEquals((Item) new Refl<>(expected).getFieldObject("item"), new Refl<>(actual).getFieldObject("item"));
    }

    @Test
    @DisplayName("Item inherited methods should call actual methods of the inner item")
    void testItemMethods() throws InvocationTargetException, IllegalAccessException {
        ItemGUIContent actual = newInstance();
        Item expected = new Refl<>(actual).getFieldObject("item");

        for (Method method : Item.class.getDeclaredMethods())
            if (!Modifier.isStatic(method.getModifiers()) && !Item.class.isAssignableFrom(method.getReturnType())) {
                final Object[] params;
                if (method.getName().equals("isSimilar")) params = new Object[]{expected, new ItemField[0]};
                else params = Arrays.stream(method.getParameterTypes()).map(TestUtils::mockParameter).toArray(Object[]::new);

                Object obj1 = ReflectionUtils.setAccessibleOrThrow(method).invoke(actual, params);
                Object obj2 = ReflectionUtils.setAccessibleOrThrow(method).invoke(expected, params);
                assertEquals(obj2, obj1);
            }
    }

    private static ItemGUIContent newInstance() {
        return ItemGUIContent.newInstance()
                .setMaterial("stone_sword").setAmount(1)
                .setDurability(1337).setDisplayName("&8Destroyer")
                .setLore("&eUse this sword to fight your enemies")
                .addEnchantment("unbreaking", 3)
                .addEnchantment("sharpness", 5)
                .addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_PLACED_ON)
                .setCustomModelData(1)
                .setVariable("name", "value");
    }

    @Test
    void testReturnTypesItem() {
        TestUtils.testReturnType(ItemGUIContent.newInstance(), Item.class, m -> m.getName().equals("copy"));
    }

    @Test
    void testReturnTypesGUIContent() {
        TestUtils.testReturnType(ItemGUIContent.newInstance(), GUIContent.class, m -> m.getName().equals("copyContent"));
    }

}
