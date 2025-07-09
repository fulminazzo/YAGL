package it.fulminazzo.yagl.particle;

import it.fulminazzo.yagl.Color;
import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.YAMLParser;
import org.jetbrains.annotations.NotNull;

/**
 * A parser to serialize {@link ColorParticleOption}.
 * Should take priority over {@link ParticleOptionParser}.
 */
public class ColorOptionParser extends YAMLParser<ColorParticleOption> {

    /**
     * Instantiates a new Color option parser.
     */
    public ColorOptionParser() {
        super(ColorParticleOption.class);
    }

    @Override
    protected @NotNull BiFunctionException<IConfiguration, String, ColorParticleOption, Exception> getLoader() {
        return (c, s) -> {
            Color color = c.get(s, Color.class);
            if (color == null) return null;
            else return new ColorParticleOption(color);
        };
    }

    @Override
    protected @NotNull TriConsumer<IConfiguration, String, ColorParticleOption> getDumper() {
        return (c, s, p) -> c.set(s, p == null ? null : p.getOption());
    }

}
