package it.angrybear.yagl.particles;

import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yamlparser.configuration.ConfigurationSection;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.YAMLParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ParticleParser extends YAMLParser<Particle> {

    public ParticleParser() {
        super(Particle.class);
    }

    @Override
    protected BiFunctionException<@NotNull IConfiguration, @NotNull String, @Nullable Particle> getLoader() {
        return (c, s) -> {
            ConfigurationSection particleSection = c.getConfigurationSection(s);
            if (particleSection == null) return null;
            String type = particleSection.getString("type");
            if (type == null) throw new NullPointerException("'type' cannot be null");
            ParticleType<?> particleType = ParticleType.valueOf(type);
            if (particleType == null)
                throw new IllegalArgumentException(String.format("Could not find %s '%s'",
                        ParticleType.class.getSimpleName(), type));

            ParticleOption<?> option = null;

            Class<? extends ParticleOption<?>> optionType = particleType.getOptionType();
            if (optionType != null) option = particleSection.get("option", optionType);

            return new Refl<>(particleType).invokeMethod("createParticle", option);
        };
    }

    @Override
    protected TriConsumer<@NotNull IConfiguration, @NotNull String, @Nullable Particle> getDumper() {
        return (c, s, p) -> {
            c.set(s, null);
            if (p == null) return;
            ConfigurationSection particleSection = c.createSection(s);
            particleSection.set("type", p.getType());
            particleSection.set("option", new Refl<>(p).getFieldObject("option"));
        };
    }
}