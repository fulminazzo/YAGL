package it.fulminazzo.yagl.particles;

import it.fulminazzo.yagl.items.AbstractItem;
import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.YAMLParser;

/**
 * A parser to serialize {@link ItemParticleOption}.
 * Should take priority over {@link ParticleOptionParser}.
 * It requires the <i>item:serializer</i> module to be added.
 */
@SuppressWarnings({"deprecation", "unchecked"})
public class ItemParticleOptionParser extends YAMLParser<ItemParticleOption<?>> {

    public ItemParticleOptionParser() {
        super((Class<ItemParticleOption<?>>) (Class<?>) ItemParticleOption.class);
    }

    @Override
    protected BiFunctionException<IConfiguration, String, ItemParticleOption<?>, Exception> getLoader() {
        return (c, s) -> {
            AbstractItem item = c.get(s, ReflectionUtils.getClass("it.fulminazzo.yagl.items.Item"));
            if (item == null) return null;
            return new ItemParticleOption<>(item);
        };
    }

    @Override
    protected TriConsumer<IConfiguration, String, ItemParticleOption<?>> getDumper() {
        return (c, s, o) -> c.set(s, o == null ? null : o.getOption());
    }
}
