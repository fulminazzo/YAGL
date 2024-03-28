package it.angrybear.yagl.particles;

import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.YAMLParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A parser to serialize {@link MaterialDataOption}.
 * Should take priority over {@link ParticleOptionParser}.
 */
public class MaterialDataOptionParser extends YAMLParser<MaterialDataOption> {

    /**
     * Instantiates a new Material data option parser.
     */
    public MaterialDataOptionParser() {
        super(MaterialDataOption.class);
    }

    @Override
    protected BiFunctionException<@NotNull IConfiguration, @NotNull String, @Nullable MaterialDataOption> getLoader() {
        return (c, s) -> {
            String raw = c.getString(s);
            if (raw == null) return null;
            return new MaterialDataOption(raw);
        };
    }

    @Override
    protected TriConsumer<@NotNull IConfiguration, @NotNull String, @Nullable MaterialDataOption> getDumper() {
        return (c, s, b) -> {
            c.set(s, null);
            if (b == null) return;
            c.set(s, b.getOption());
        };
    }
}