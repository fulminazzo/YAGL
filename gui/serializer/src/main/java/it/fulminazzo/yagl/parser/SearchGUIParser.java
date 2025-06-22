package it.fulminazzo.yagl.parser;

import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.yagl.gui.SearchGUI;
import it.fulminazzo.yamlparser.configuration.ConfigurationSection;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.YAMLParser;
import org.jetbrains.annotations.NotNull;

/**
 * A parser to serialize {@link SearchGUI}.
 */
public class SearchGUIParser extends YAMLParser<SearchGUI<?>> {
    private final @NotNull PageableGUIParser internalParser;

    /**
     * Instantiates a new Search gui parser.
     */
    @SuppressWarnings("unchecked")
    public SearchGUIParser() {
        super((Class<SearchGUI<?>>) (Class<?>) SearchGUI.class);
        this.internalParser = new PageableGUIParser();
    }

    @Override
    protected BiFunctionException<IConfiguration, String, SearchGUI<?>, Exception> getLoader() {
        return (c, s) -> {
            ConfigurationSection section = c.getConfigurationSection(s);
            if (section == null) return null;

            final Integer lowerGuiSize = section.getInteger("lower-gui-size");
            if (lowerGuiSize == null) throw new IllegalArgumentException("'lower-gui-size' cannot be null");

            // Save previous fixed variables

            // Set fixed variables

            SearchGUI<?> gui = (SearchGUI<?>) this.internalParser.load(c, s);

            // Restore fixed variables

            return gui;
        };
    }

    @Override
    protected TriConsumer<IConfiguration, String, SearchGUI<?>> getDumper() {
        return (c, s, g) -> {
            this.internalParser.dump(c, s, g);
            if (c.isConfigurationSection(s)) {
                // Remove fixed variables
            }
        };
    }

}
