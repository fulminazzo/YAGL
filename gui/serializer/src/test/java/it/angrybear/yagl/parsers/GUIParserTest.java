package it.angrybear.yagl.parsers;

import it.angrybear.yagl.contents.GUIContent;
import it.angrybear.yagl.guis.ContentsParser;
import it.angrybear.yagl.guis.GUI;
import it.angrybear.yagl.guis.GUIType;
import it.angrybear.yagl.guis.TypeGUI;
import it.angrybear.yagl.items.Item;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;
import it.fulminazzo.yamlparser.utils.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GUIParserTest {

    @Test
    void testSaveAndLoadResizableGUI() throws IOException {
        FileConfiguration.addParsers(new ContentsParser());
        YAGLParser.addAllParsers();
        TypeGUI expected = (TypeGUI) GUI.newGUI(GUIType.BARREL)
                .setContents(0, Item.newItem()
                        .setMaterial("STONE_SWORD").setAmount(1)
                        .setDurability(1337).setDisplayName("First")
                        .setCustomModelData(1))
                .setContents(1, Item.newItem()
                        .setMaterial("STONE_SWORD").setAmount(1)
                        .setDurability(1337).setDisplayName("Second")
                        .setCustomModelData(1))
                .setContents(4, Item.newItem()
                                .setMaterial("STONE_SWORD").setAmount(1)
                                .setDurability(1337).setDisplayName("Third-1")
                                .setCustomModelData(1),
                        Item.newItem()
                                .setMaterial("STONE_SWORD").setAmount(1)
                                .setDurability(1337).setDisplayName("Third-2")
                                .setCustomModelData(1))
                .setMovable(3, true).setMovable(7, true);
        File file = new File("build/resources/test/gui.yml");
        if (file.exists()) FileUtils.deleteFile(file);
        FileUtils.createNewFile(file);
        FileConfiguration configuration = new FileConfiguration(file);
        configuration.set("gui", expected);
        configuration.save();

        configuration = new FileConfiguration(file);
        TypeGUI actual = (TypeGUI) configuration.get("gui", GUI.class);
        assertNotNull(actual);
        assertEquals(expected.getInventoryType(), actual.getInventoryType());

        @NotNull List<GUIContent> expectedContents = expected.getContents();
        @NotNull List<GUIContent> actualContents = actual.getContents();
        assertEquals(expectedContents.size(), actualContents.size());

        for (int i = 0; i < actualContents.size(); i++) {
            GUIContent exp = expectedContents.get(i);
            GUIContent act = actualContents.get(i);
            if (exp == null) assertNull(act);
            else assertEquals(exp.render(), act.render());
        }

        assertIterableEquals(expected.getMovableSlots(), actual.getMovableSlots());
    }
}