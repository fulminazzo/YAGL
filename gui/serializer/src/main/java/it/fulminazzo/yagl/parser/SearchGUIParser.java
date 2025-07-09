package it.fulminazzo.yagl.parser;

import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.yagl.gui.GUI;
import it.fulminazzo.yagl.gui.GUIType;
import it.fulminazzo.yagl.gui.SearchGUI;
import it.fulminazzo.yagl.gui.TypeGUI;
import it.fulminazzo.yagl.util.ParserUtils;
import it.fulminazzo.yamlparser.configuration.ConfigurationSection;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.YAMLParser;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * A parser to serialize {@link SearchGUI}.
 */
@SuppressWarnings("unchecked")
public class SearchGUIParser extends YAMLParser<SearchGUI<?>> {
    private static final @NotNull Map<String, Object> FIXED_VARIABLES = new HashMap<>();

    static {
        FIXED_VARIABLES.put("inventory-type", GUIType.ANVIL.name());
        FIXED_VARIABLES.put("size", GUIType.ANVIL.getSize());
        FIXED_VARIABLES.put("upper-gui-type", ParserUtils.classToType(GUI.class, TypeGUI.class));
        FIXED_VARIABLES.put("gui-type", ParserUtils.classToType(
                GUI.class, (Class<? extends GUI>) SearchGUI.class.getDeclaredClasses()[0]
        ));
    }

    private final @NotNull PageableGUIParser internalParser;

    /**
     * Instantiates a new Search gui parser.
     */
    public SearchGUIParser() {
        super((Class<SearchGUI<?>>) (Class<?>) SearchGUI.class);
        this.internalParser = new PageableGUIParser();
    }

    @Override
    protected @NotNull BiFunctionException<IConfiguration, String, SearchGUI<?>, Exception> getLoader() {
        return (c, s) -> {
            ConfigurationSection section = c.getConfigurationSection(s);
            if (section == null) return null;

            final Integer lowerGuiSize = section.getInteger("lower-gui-size");
            if (lowerGuiSize == null) throw new IllegalArgumentException("'lower-gui-size' cannot be null");

            Map<String, Object> previous = new HashMap<>();
            FIXED_VARIABLES.forEach((k, v) -> {
                previous.put(k, section.getObject(k));
                section.set(k, v);
            });

            SearchGUI<?> gui = (SearchGUI<?>) this.internalParser.load(c, s);

            previous.forEach(section::set);

            return gui;
        };
    }

    @Override
    protected @NotNull TriConsumer<IConfiguration, String, SearchGUI<?>> getDumper() {
        return (c, s, g) -> {
            this.internalParser.dump(c, s, g);
            if (c.isConfigurationSection(s))
                FIXED_VARIABLES.forEach((k, v) -> c.set(s + "." + k, null));
        };
    }

}
