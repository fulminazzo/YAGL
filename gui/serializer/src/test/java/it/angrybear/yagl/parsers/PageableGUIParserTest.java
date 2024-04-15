package it.angrybear.yagl.parsers;

import it.angrybear.yagl.ParserTestHelper;
import it.angrybear.yagl.guis.GUI;
import it.angrybear.yagl.guis.PageableGUI;
import it.angrybear.yagl.items.Item;
import it.angrybear.yagl.items.fields.ItemFlag;
import it.fulminazzo.yamlparser.configuration.ConfigurationSection;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.utils.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    void testSizeNotSpecified() {
        IConfiguration configuration = getConfiguration(c -> {});
        Throwable throwable = assertThrowsExactly(IllegalArgumentException.class, () ->
                getLoader().apply(configuration, "gui"));
        checkMessage(throwable, "size");
    }

    @Test
    void testPagesNotSpecified() {
        IConfiguration configuration = getConfiguration(c -> {
            c.set("size", 3);
        });
        Throwable throwable = assertThrowsExactly(IllegalArgumentException.class, () ->
                getLoader().apply(configuration, "gui"));
        checkMessage(throwable, "pages");
    }

    @Test
    void testGUITypeNotSpecified() {
        IConfiguration configuration = getConfiguration(c -> {
            c.set("size", 3);
            c.set("pages", 3);
        });
        Throwable throwable = assertThrowsExactly(IllegalArgumentException.class, () ->
                getLoader().apply(configuration, "gui"));
        checkMessage(throwable, "gui-type");
    }

    @Test
    void testInvalidTemplateGUI() {
        IConfiguration configuration = getConfiguration(c -> {
            c.set("size", 3);
            c.set("pages", 3);
            c.set("gui-type", "DEFAULT");
        });
        Throwable throwable = assertThrowsExactly(IllegalArgumentException.class, () ->
                getLoader().apply(configuration, "gui"));
        checkMessage(throwable, "template");
    }

    private void checkMessage(Throwable throwable, String tmp) {
        String message = throwable.getMessage();
        assertNotNull(message, "Message of exception should not be null");
        assertTrue(message.contains(tmp), String.format("'%s' did not contain '%s'", message, tmp));
    }

    private IConfiguration getConfiguration(Consumer<IConfiguration> function) {
        FileConfiguration configuration = new FileConfiguration(new ByteArrayInputStream(new byte[0]));
        ConfigurationSection section = configuration.createSection("gui");
        function.accept(section);
        return configuration;
    }

    @Override
    protected Class<?> getParser() {
        return PageableGUIParser.class;
    }
}