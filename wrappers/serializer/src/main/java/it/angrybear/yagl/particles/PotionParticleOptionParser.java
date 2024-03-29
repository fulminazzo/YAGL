package it.angrybear.yagl.particles;

import it.angrybear.yagl.wrappers.Potion;
import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.YAMLParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A parser to serialize {@link PotionParticleOption}.
 * Should take priority over {@link ParticleOptionParser}.
 */
public class PotionParticleOptionParser extends YAMLParser<PotionParticleOption> {

    /**
     * Instantiates a new Potion option parser.
     */
    public PotionParticleOptionParser() {
        super(PotionParticleOption.class);
    }

    @Override
    protected BiFunctionException<@NotNull IConfiguration, @NotNull String, @Nullable PotionParticleOption> getLoader() {
        return (c, s) -> {
            Potion potion = c.get(s, Potion.class);
            if (potion == null) return null;
            else return new PotionParticleOption(potion);
        };
    }

    @Override
    protected TriConsumer<@NotNull IConfiguration, @NotNull String, @Nullable PotionParticleOption> getDumper() {
        return (c, s, p) -> c.set(s, p == null ? null : p.getOption());
    }
}
