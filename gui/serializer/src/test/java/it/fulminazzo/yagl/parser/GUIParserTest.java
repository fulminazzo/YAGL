package it.fulminazzo.yagl.parser;

import it.fulminazzo.yagl.ParserTestHelper;
import it.fulminazzo.yagl.action.command.*;
import it.fulminazzo.yagl.content.GUIContent;
import it.fulminazzo.yagl.content.ItemGUIContent;
import it.fulminazzo.yagl.content.requirement.PermissionRequirement;
import it.fulminazzo.yagl.gui.GUI;
import it.fulminazzo.yagl.gui.GUIType;
import it.fulminazzo.yagl.item.Item;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import it.fulminazzo.yamlparser.configuration.ConfigurationSection;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;
import it.fulminazzo.yamlparser.utils.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GUIParserTest extends ParserTestHelper<GUI> {

    @BeforeAll
    static void setAllUp() {
        GUIYAGLParser.addAllParsers();
    }

    @Test
    void testSaveAndLoadOfSpecialActionsAndRequirements() throws IOException {
        GUIContent expectedContent1 = ItemGUIContent.newInstance()
                .setMaterial("stone")
                .onClickItem(new GUIItemCommand("command"))
                .setViewRequirements(new PermissionRequirement("permission"));
        GUIContent expectedContent2 = ItemGUIContent.newInstance()
                .setMaterial("stone")
                .onClickItem(new GUIItemConsoleCommand("command"))
                .setViewRequirements(new PermissionRequirement("permission"));
        GUI expected = GUI.newGUI(9)
                .onChangeGUI(new BiGUICommand("command"))
                .onCloseGUI((v, g) -> v.executeCommand("help"))
                .onClickOutside(new GUIConsoleCommand("command"))
                .onOpenGUI(new GUICommand("command"))
                .setContents(0, expectedContent1)
                .setContents(1, expectedContent2);
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
        assertEquals((Object) new Refl<>(expectedContent1).getFieldObject("requirements"),
                new Refl<>(content).getFieldObject("requirements"));
        assertEquals((Object) new Refl<>(expectedContent2).getFieldObject("requirements"),
                new Refl<>(content).getFieldObject("requirements"));
    }

    private static GUI[] getGUIs() {
        return new GUI[]{GUI.newResizableGUI(9), GUI.newGUI(9), GUI.newGUI(GUIType.CHEST)};
    }

    @ParameterizedTest
    @MethodSource("getGUIs")
    void testSaveAndLoadGUI(GUI expected) throws IOException {
        setupContents(expected);

        File file = new File("build/resources/test/gui.yml");
        if (!file.exists()) FileUtils.createNewFile(file);
        FileConfiguration configuration = new FileConfiguration(file);
        final String path = expected.getClass().getSimpleName().toLowerCase();
        configuration.set(path, expected);
        configuration.save();

        configuration = new FileConfiguration(file);
        GUI actual = configuration.get(path, GUI.class);
        assertNotNull(actual);

        for (final Field field : new Refl<>(expected).getNonStaticFields())
            if (!field.getName().equals("contents")) {
                Object obj1 = ReflectionUtils.getOrThrow(field, expected);
                Object obj2 = ReflectionUtils.getOrThrow(field, actual);
                assertEquals(obj1, obj2);
            }

        @NotNull List<GUIContent> expectedContents = expected.getContents();
        @NotNull List<GUIContent> actualContents = actual.getContents();
        assertEquals(expectedContents.size(), actualContents.size());

        for (int i = 0; i < actualContents.size(); i++) {
            GUIContent exp = expectedContents.get(i);
            GUIContent act = actualContents.get(i);
            if (exp == null) assertNull(act);
            else assertEquals(exp.render(), act.render());
        }
    }

    public static void setupContents(GUI expected) {
        expected.setContents(0, Item.newItem()
                        .setMaterial("stone_sword").setAmount(1)
                        .setDurability(1337).setDisplayName("First")
                        .setCustomModelData(1))
                .setContents(1, Item.newItem()
                        .setMaterial("stone_sword").setAmount(1)
                        .setDurability(1337).setDisplayName("Second")
                        .setCustomModelData(1))
                .setContents(4, Item.newItem()
                                .setMaterial("stone_sword").setAmount(1)
                                .setDurability(1337).setDisplayName("Third-1")
                                .setCustomModelData(1),
                        Item.newItem()
                                .setMaterial("stone_sword").setAmount(1)
                                .setDurability(1337).setDisplayName("Third-2")
                                .setCustomModelData(1))
                .setMovable(3, true).setMovable(7, true);
    }

    @Test
    void testInvalidSize() {
        final String path = "gui";
        final GUI gui = GUI.newGUI(9);
        ConfigurationSection section = new ConfigurationSection(null, "section");
        getDumper().accept(section, path, gui);
        section.set(path + ".size", null);

        assertThrowsExactly(IllegalArgumentException.class, () -> getLoader().apply(section, path));
    }

    @Test
    void testDumpOfGUIWithParser() {
        FileConfiguration.addParsers(new MockGUIParser());
        ConfigurationSection section = new ConfigurationSection(null, "main");
        GUI gui = new MockGUIParser.MockGUI();
        getDumper().accept(section, "gui", gui);
        assertEquals(section.getObject("gui"), gui.getTitle());
    }

    @Override
    protected Class<?> getParser() {
        return GUIParser.class;
    }

}