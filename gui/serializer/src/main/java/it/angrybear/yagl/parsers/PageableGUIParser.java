package it.angrybear.yagl.parsers;

import it.angrybear.yagl.guis.GUI;
import it.angrybear.yagl.guis.PageableGUI;
import it.angrybear.yagl.utils.ParserUtils;
import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.yamlparser.configuration.ConfigurationSection;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.YAMLParser;

public class PageableGUIParser extends YAMLParser<PageableGUI> {

    public PageableGUIParser() {
        super(PageableGUI.class);
    }

    @Override
    protected BiFunctionException<IConfiguration, String, PageableGUI> getLoader() {
        return (c, s) -> {
            ConfigurationSection section = c.getConfigurationSection(s);
            if (section == null) return null;
            Integer size = section.getInteger("size");
            if (size == null) throw new IllegalArgumentException("'size' cannot be null");
            return PageableGUI.newGUI(size);
        };
    }

    @Override
    protected TriConsumer<IConfiguration, String, PageableGUI> getDumper() {
        return (c, s, p) -> {
            c.set(s, null);
            if (p == null) return;
            ConfigurationSection section = c.createSection(s);
            section.set("type", ParserUtils.classToType(GUI.class, getOClass()));
            section.set("size", p.size());
        };
    }
}
