package it.fulminazzo.yagl.parser;

import it.fulminazzo.yagl.content.GUIContent;
import it.fulminazzo.yagl.content.ItemGUIContent;
import it.fulminazzo.yagl.item.field.ItemFlag;
import it.fulminazzo.yagl.wrapper.Sound;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;
import it.fulminazzo.yamlparser.utils.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GUIContentParserTest {

    @BeforeAll
    static void setAllUp() {
        GUIYAGLParser.addAllParsers();
    }

    @Test
    void testSaveAndLoadItemGUIContent() throws IOException {
        GUIContent expected = ItemGUIContent.newInstance()
                .setMaterial("stone_sword").setAmount(1)
                .setDurability(1337).setDisplayName("&8Destroyer")
                .setLore("&eUse this sword to fight your enemies")
                .addEnchantment("unbreaking", 3)
                .addEnchantment("sharpness", 5)
                .addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_PLACED_ON)
                .setClickSound(new Sound("villager_happy", 10, 5))
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
