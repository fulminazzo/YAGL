package it.angrybear.yagl.contents;

import it.angrybear.yagl.TestUtils;
import it.angrybear.yagl.items.Item;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
    void testReturnTypes() {
        TestUtils.testReturnType(new ItemGUIContent(), GUIContent.class, m ->
                m.getName().equals("copyContent"));
    }
}