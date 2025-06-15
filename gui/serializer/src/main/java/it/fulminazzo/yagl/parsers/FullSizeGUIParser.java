package it.fulminazzo.yagl.parsers;

import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.yagl.guis.FullSizeGUI;
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
        return null;
    }

}
