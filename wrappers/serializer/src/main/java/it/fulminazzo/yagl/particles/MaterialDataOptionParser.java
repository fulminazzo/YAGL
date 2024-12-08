package it.fulminazzo.yagl.particles;

import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.fulmicollection.structures.tuples.Tuple;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.YAMLParser;

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
    protected BiFunctionException<IConfiguration, String, MaterialDataOption> getLoader() {
        return (c, s) -> {
            String raw = c.getString(s);
            if (raw == null) return null;
            return new MaterialDataOption(raw);
        };
    }

    @Override
    protected TriConsumer<IConfiguration, String, MaterialDataOption> getDumper() {
        return (c, s, b) -> {
            c.set(s, null);
            if (b == null) return;
            Tuple<String, Integer> o = b.getOption();
            c.set(s, String.format("%s[%s]", o.getKey(), o.getValue()));
        };
    }
}
