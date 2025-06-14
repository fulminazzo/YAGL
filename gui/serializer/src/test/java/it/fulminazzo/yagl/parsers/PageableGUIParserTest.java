package it.fulminazzo.yagl.parsers;

import it.fulminazzo.yagl.ParserTestHelper;
import it.fulminazzo.yagl.guis.GUI;
import it.fulminazzo.yagl.guis.PageableGUI;
import it.fulminazzo.yagl.items.Item;
import it.fulminazzo.yagl.items.fields.ItemFlag;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yamlparser.configuration.ConfigurationSection;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.utils.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.invocation.InvocationOnMock;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PageableGUIParserTest extends ParserTestHelper<PageableGUI> {

    @Test
    void testSaveAndLoadPageableGUI() throws IOException {
        GUIYAGLParser.addAllParsers();
        PageableGUI expected = PageableGUI.newGUI(9)
                .setPages(4)
                .setPreviousPage(1, Item.newItem("paper")
                        .setDisplayName("&ePrevious Page")
                        .addEnchantment("unbreaking", 3)
                        .addItemFlags(ItemFlag.HIDE_UNBREAKABLE))
                .setNextPage(1, Item.newItem("paper")
                        .setDisplayName("&eNext Page")
                        .addEnchantment("unbreaking", 3)
                        .addItemFlags(ItemFlag.HIDE_UNBREAKABLE))
                .setVariable("var1", "hello")
                .setVariable("var2", "world");
        expected.getPage(0).setContents(2, Item.newItem("stone"));
        expected.getPage(2).setVariable("hello", "world");
        expected.getPage(3).setMovable(3, true);
        GUIParserTest.setupContents(expected);

        File file = new File("build/resources/test/pageable-gui.yml");
        if (!file.exists()) FileUtils.createNewFile(file);
        FileConfiguration configuration = new FileConfiguration(file);
        final String path = expected.getClass().getSimpleName().toLowerCase();
        configuration.set(path, expected);
        configuration.save();

        configuration = new FileConfiguration(file);
        GUI actual = configuration.get(path, GUI.class);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void testSaveAndLoadPageableGUIStripped() throws IOException {
        GUIYAGLParser.addAllParsers();
        PageableGUI expected = PageableGUI.newGUI(9).setPages(0);

        File file = new File("build/resources/test/pageable-gui-stripped.yml");
        if (!file.exists()) FileUtils.createNewFile(file);
        FileConfiguration configuration = new FileConfiguration(file);
        final String path = expected.getClass().getSimpleName().toLowerCase();
        configuration.set(path, expected);
        configuration.save();

        configuration = new FileConfiguration(file);
        GUI actual = configuration.get(path, GUI.class);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void testPagesNotSpecified() {
        IConfiguration configuration = getConfiguration(c -> {
            c.set("type", "PAGEABLE");
            c.set("size", 3);
        });
        Throwable throwable = assertThrowsExactly(IllegalArgumentException.class, () ->
                getLoader().apply(configuration, "gui"));
        checkMessage(throwable, "pages");
    }

    @Test
    void testGUITypeNotSpecified() {
        IConfiguration configuration = getConfiguration(c -> {
            c.set("type", "PAGEABLE");
            c.set("size", 9);
            c.set("pages", 3);
        });
        Throwable throwable = assertThrowsExactly(IllegalArgumentException.class, () ->
                getLoader().apply(configuration, "gui"));
        checkMessage(throwable, "gui-type");
    }

    @Test
    void testInvalidTemplateGUI() {
        IConfiguration configuration = getConfiguration(c -> {
            c.set("type", "PAGEABLE");
            c.set("size", 9);
            c.set("pages", 3);
            c.set("gui-type", "DEFAULT");
        });
        Throwable throwable = assertThrowsExactly(IllegalArgumentException.class, () ->
                getLoader().apply(configuration, "gui"));
        checkMessage(throwable, "template");
    }

    private static Object[][] getVariableMaps() {
        return new Object[][]{
                new Object[]{
                        new HashMap<Object, Object>(){{
                            put(null, "do-not-pick");
                            put("do-not-pick", null);
                            put(1, "hello");
                            put(true, "world");
                        }},
                        new HashMap<String, String>(){{
                            put("1", "hello");
                            put("true", "world");
                        }}
                },
                new Object[]{
                        null,
                        new HashMap<>()
                }
        };
    }

    @ParameterizedTest
    @MethodSource("getVariableMaps")
    void testVariableMaps(Map<Object, Object> variables, Object expected) throws Exception {
        GUIYAGLParser.addAllParsers();
        IConfiguration configuration = new FileConfiguration(new ByteArrayInputStream(new byte[0]));
        ConfigurationSection section = mock(ConfigurationSection.class, InvocationOnMock::callRealMethod);
        new Refl<>(section).setFieldObject("map", new HashMap<>())
                .setFieldObject("name", "gui")
                .setFieldObject("parent", configuration);
        when(section.getName()).thenReturn("gui");
        doAnswer(a -> variables == null ? null : new HashMap<>(variables))
                .when(section).get("variables", Map.class);
        configuration.toMap().put("gui", section);
        section.set("type", "PAGEABLE");
        section.set("size", 9);
        section.set("pages", 3);
        section.set("gui-type", "DEFAULT");
        GUI gui = getLoader().apply(configuration, "gui");
        assertEquals(expected, gui.variables());
    }

    private void checkMessage(Throwable throwable, String tmp) {
        String message = throwable.getMessage();
        assertNotNull(message, "Message of exception should not be null");
        assertTrue(message.contains(tmp), String.format("'%s' did not contain '%s'", message, tmp));
    }

    private IConfiguration getConfiguration(Consumer<IConfiguration> function) {
        IConfiguration configuration = mock(IConfiguration.class);
        ConfigurationSection section = new ConfigurationSection(configuration, "gui");
        when(configuration.getConfigurationSection(anyString())).thenReturn(section);
        function.accept(section);
        return configuration;
    }

    @Override
    protected Class<?> getParser() {
        return PageableGUIParser.class;
    }
}