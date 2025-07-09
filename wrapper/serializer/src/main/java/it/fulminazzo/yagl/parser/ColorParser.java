package it.fulminazzo.yagl.parser;

import it.fulminazzo.yagl.Color;
import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.YAMLParser;
import org.jetbrains.annotations.NotNull;

/**
 * A parser to serialize {@link Color}.
 */
public class ColorParser extends YAMLParser<Color> {

    /**
     * Instantiates a new Color parser.
     */
    public ColorParser() {
        super(Color.class);
    }

    @Override
    protected @NotNull BiFunctionException<IConfiguration, String, Color, Exception> getLoader() {
        return (c, s) -> {
            String name = c.getString(s);
            if (name == null) return null;
            Color o = Color.valueOf(name);
            if (o == null) o = Color.fromARGB(name);
            return o;
        };
    }

    @Override
    protected @NotNull TriConsumer<IConfiguration, String, Color> getDumper() {
        return (c, s, o) -> {
            c.set(s, null);
            if (o == null) return;
            String name = o.name();
            if (name != null) c.set(s, name);
            else c.set(s, o.toARGB());
        };
    }
}
