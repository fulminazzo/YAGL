package it.fulminazzo.yagl.parsers;

import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.yagl.guis.FullSizeGUI;
import it.fulminazzo.yagl.guis.GUI;
import it.fulminazzo.yagl.utils.ParserUtils;
import it.fulminazzo.yamlparser.configuration.ConfigurationSection;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.YAMLParser;

/**
 * A parser to serialize {@link FullSizeGUI}
 */
public class FullSizeGUIParser extends YAMLParser<FullSizeGUI> {

    /**
     * Instantiates a new Full size gui parser.
     *
     */
    public FullSizeGUIParser() {
        super(FullSizeGUI.class);
    }

    @Override
    protected BiFunctionException<IConfiguration, String, FullSizeGUI, Exception> getLoader() {
        return null;
    }

    @Override
    protected TriConsumer<IConfiguration, String, FullSizeGUI> getDumper() {
        return (c, s, g) -> {
            c.set(s, null);
            if (g == null) return;
            c.set(s, g.getUpperGUI());

            ConfigurationSection section = c.getConfigurationSection(s);

            section.set("contents", g.getContents());
            final String valueClass = "contents.value-class";
            if (section.contains(valueClass)) section.set(valueClass, null);

            section.set("gui-type", section.getString("type"));
            section.set("type", ParserUtils.classToType(GUI.class, g.getClass()));
        };
    }

}
