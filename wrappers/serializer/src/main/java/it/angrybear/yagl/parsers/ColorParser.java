package it.angrybear.yagl.parsers;

import it.angrybear.yagl.Color;
import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.YAMLParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ColorParser extends YAMLParser<Color> {

    public ColorParser() {
        super(Color.class);
    }

    @Override
    protected BiFunctionException<@NotNull IConfiguration, @NotNull String, @Nullable Color> getLoader() {
        return (c, s) -> {
            String name = c.getString(s);
            if (name == null) return null;
            Color o = Color.valueOf(name);
            if (o == null) o = Color.fromARGB(name);
            return o;
        };
    }

    @Override
    protected TriConsumer<@NotNull IConfiguration, @NotNull String, @Nullable Color> getDumper() {
        return (c, s, o) -> {
            c.set(s, null);
            if (o == null) return;
            String name = o.name();
            if (name != null) c.set(s, name);
            else c.set(s, o.toARGB());
        };
    }
}
