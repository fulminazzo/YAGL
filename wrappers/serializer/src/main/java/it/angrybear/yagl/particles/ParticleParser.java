package it.angrybear.yagl.particles;

import it.angrybear.yagl.utils.EnumUtils;
import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yamlparser.configuration.ConfigurationSection;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.YAMLParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A parser to serialize {@link Particle}.
 */
public class ParticleParser extends YAMLParser<Particle> {

    /**
     * Instantiates a new Particle parser.
     */
    public ParticleParser() {
        super(Particle.class);
    }

    @Override
    protected @NotNull BiFunctionException<@NotNull IConfiguration, @NotNull String, @Nullable Particle> getLoader() {
        return (c, s) -> {
            ConfigurationSection particleSection = c.getConfigurationSection(s);
            if (particleSection == null) return null;
            String type = particleSection.getString("type");
            if (type == null) throw new NullPointerException("'type' cannot be null");
            ParticleType<?> particleType = EnumUtils.valueOf(ParticleType.class, type);

            ParticleOption<?> option = null;

            Class<? extends ParticleOption<?>> optionType = particleType.getOptionType();
            if (optionType != null) option = particleSection.get("option", optionType);

            return new Refl<>(particleType).invokeMethod("createParticle", option);
        };
    }

    @Override
    protected @NotNull TriConsumer<@NotNull IConfiguration, @NotNull String, @Nullable Particle> getDumper() {
        return (c, s, p) -> {
            c.set(s, null);
            if (p == null) return;
            ConfigurationSection particleSection = c.createSection(s);
            particleSection.set("type", p.getType());
            particleSection.set("option", new Refl<>(p).getFieldObject("option"));
        };
    }
}
