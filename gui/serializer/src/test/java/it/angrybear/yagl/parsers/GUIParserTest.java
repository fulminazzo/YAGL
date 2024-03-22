package it.angrybear.yagl.parsers;

import it.angrybear.yagl.actions.BiGUICommand;
import it.angrybear.yagl.actions.GUICommand;
import it.angrybear.yagl.actions.GUIItemCommand;
import it.angrybear.yagl.contents.GUIContent;
import it.angrybear.yagl.contents.ItemGUIContent;
import it.angrybear.yagl.contents.PermissionRequirementChecker;
import it.angrybear.yagl.guis.ContentsParser;
import it.angrybear.yagl.guis.GUI;
import it.angrybear.yagl.guis.GUIType;
import it.angrybear.yagl.guis.TypeGUI;
import it.angrybear.yagl.items.Item;
import it.angrybear.yagl.viewers.Viewer;
import it.fulminazzo.fulmicollection.objects.Refl;
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
    void testSaveAndLoadOfSpecialActionsAndRequirements() throws IOException {
        YAGLParser.addAllParsers();
        GUIContent expectedContent = new ItemGUIContent()
                .setMaterial("STONE")
                .onClickItem(new GUIItemCommand("command"))
                .setViewRequirements(new PermissionRequirementChecker("permission"));
        GUI expected = GUI.newGUI(9)
                .onChangeGUI(new BiGUICommand("command"))
                .onCloseGUI(Viewer::openGUI)
                .onOpenGUI(new GUICommand("command"))
                .setContents(0, expectedContent);
        File file = new File("build/resources/test/actions-and-requirements.yml");
        if (file.exists()) FileUtils.deleteFile(file);
        FileUtils.createNewFile(file);
        FileConfiguration configuration = new FileConfiguration(file);
        configuration.set("gui", expected);
        configuration.save();

        configuration = new FileConfiguration(file);
        GUI actual = configuration.get("gui", GUI.class);

        assertNotNull(actual);
        assertEquals(expected.changeGUIAction().orElse(null), actual.changeGUIAction().orElse(null));
        assertNotNull(expected.closeGUIAction().orElse(null));
        assertEquals(expected.openGUIAction().orElse(null), actual.openGUIAction().orElse(null));

        @NotNull List<GUIContent> contents = actual.getContents(0);
        assertFalse(contents.isEmpty());
        GUIContent content = contents.get(0);
        assertNotNull(content);
        assertEquals(expectedContent.clickItemAction(), content.clickItemAction());
        assertEquals((Object) new Refl<>(expectedContent).getFieldObject("requirements"),
                new Refl<>(content).getFieldObject("requirements"));
    }

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