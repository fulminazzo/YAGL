package it.fulminazzo.yagl.parsers;

import it.fulminazzo.yagl.contents.GUIContent;
import it.fulminazzo.yagl.contents.ItemGUIContent;
import it.fulminazzo.yagl.items.fields.ItemFlag;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;
import it.fulminazzo.yamlparser.utils.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GUIContentParserTest {

    @Test
    void testSaveAndLoadItemGUIContent() throws IOException {
        GUIYAGLParser.addAllParsers();
        GUIContent expected = ItemGUIContent.newInstance()
                .setMaterial("stone_sword").setAmount(1)
                .setDurability(1337).setDisplayName("&8Destroyer")
                .setLore("&eUse this sword to fight your enemies")
                .addEnchantment("unbreaking", 3)
                .addEnchantment("sharpness", 5)
                .addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_PLACED_ON)
                .setCustomModelData(1);
        File file = new File("build/resources/test/gui-content.yml");
        if (file.exists()) FileUtils.deleteFile(file);
        FileUtils.createNewFile(file);
        FileConfiguration configuration = new FileConfiguration(file);
        configuration.set("content", expected);
        configuration.save();

        configuration = new FileConfiguration(file);
        GUIContent actual = configuration.get("content", GUIContent.class);
        assertNotNull(actual);
        assertEquals(expected.render(), actual.render());
    }
}