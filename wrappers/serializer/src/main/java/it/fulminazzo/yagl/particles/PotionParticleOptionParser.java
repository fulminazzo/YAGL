package it.fulminazzo.yagl.particles;

import it.fulminazzo.yagl.wrappers.Potion;
import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.YAMLParser;

/**
 * A parser to serialize {@link PotionParticleOption}.
 * Should take priority over {@link ParticleOptionParser}.
 */
public final class PotionParticleOptionParser extends YAMLParser<PotionParticleOption> {

    /**
     * Instantiates a new Potion option parser.
     */
    public PotionParticleOptionParser() {
        super(PotionParticleOption.class);
    }

    @Override
    protected BiFunctionException<IConfiguration, String, PotionParticleOption, Exception> getLoader() {
        return (c, s) -> {
            Potion potion = c.get(s, Potion.class);
            if (potion == null) return null;
            else return new PotionParticleOption(potion);
        };
    }

    @Override
    protected TriConsumer<IConfiguration, String, PotionParticleOption> getDumper() {
        return (c, s, p) -> c.set(s, p == null ? null : p.getOption());
    }
}
