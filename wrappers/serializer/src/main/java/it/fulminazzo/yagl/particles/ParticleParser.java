package it.fulminazzo.yagl.particles;

import it.fulminazzo.yagl.utils.EnumUtils;
import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yamlparser.configuration.ConfigurationSection;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.YAMLParser;

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
    protected BiFunctionException<IConfiguration, String, Particle, Exception> getLoader() {
        return (c, s) -> {
            ConfigurationSection particleSection = c.getConfigurationSection(s);
            if (particleSection == null) return null;
            String type = particleSection.getString("type");
            if (type == null) throw new IllegalArgumentException("'type' cannot be null");
            AParticleType<?> particleType;
            try {
                particleType = EnumUtils.valueOf(LegacyParticleType.class, type);
            } catch (IllegalArgumentException e) {
                particleType = EnumUtils.valueOf(ParticleType.class, type);
            }

            ParticleOption<?> option = null;

            Class<? extends ParticleOption<?>> optionType = particleType.getOptionType();
            if (optionType != null) option = particleSection.get("option", optionType);

            Refl<?> refl = new Refl<>(particleType);
            if (option == null) return refl.invokeMethod("create");
            else return refl.invokeMethod("create", option);
        };
    }

    @Override
    protected TriConsumer<IConfiguration, String, Particle> getDumper() {
        return (c, s, p) -> {
            c.set(s, null);
            if (p == null) return;
            ConfigurationSection particleSection = c.createSection(s);
            particleSection.set("type", p.getType());
            particleSection.set("option", new Refl<>(p).getFieldObject("option"));
        };
    }
}
