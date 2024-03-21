package it.angrybear.yagl.parsers;

import it.angrybear.yagl.contents.GUIContent;
import it.angrybear.yagl.contents.ItemGUIContent;
import it.angrybear.yagl.items.fields.ItemFlag;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;
import it.fulminazzo.yamlparser.utils.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GUIContentParserTest {

    @Test
    void testSaveAndLoadItemGUIContent() throws IOException {
        YAGLParser.addAllParsers();
        GUIContent expected = (GUIContent) new ItemGUIContent()
                .setMaterial("STONE_SWORD").setAmount(1)
                .setDurability(1337).setDisplayName("&8Destroyer")
                .setLore("&eUse this sword to fight your enemies")
                .addEnchantment("UNBREAKING", 3)
                .addEnchantment("SHARPNESS", 5)
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