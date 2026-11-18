package it.fulminazzo.yagl.particle;

import it.fulminazzo.yagl.serializer.DataNode;
import it.fulminazzo.yagl.serializer.ObjectSerializer;
import it.fulminazzo.yagl.serializer.SerializationException;
import it.fulminazzo.yagl.serializer.Serializer;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * A special {@link Serializer} for {@link ParticleOption} that have two or more properties.
 *
 * @param <P> the type of the particle option
 * @see Property
 * @see ParticleOption
 * @see Serializer
 */
@RequiredArgsConstructor
public final class PropertyParticleOptionSerializer<P extends ParticleOption<?>> extends ObjectSerializer<P> {
    private final @NotNull Map<String, Property<?, P>> properties = new HashMap<>();
    private final @NotNull Function<ValueMap, P> initializer;

    /**
     * Adds a new property to the serializer.
     *
     * @param name     the name of the property
     * @param property the property to add
     * @return this serializer
     */
    public @NotNull PropertyParticleOptionSerializer<P> addProperty(
            final @NotNull String name,
            final @NotNull Property<?, P> property
    ) {
        this.properties.put(name, property);
        return this;
    }

    @Override
    protected @Nullable P initialize(final @NotNull DataNode node) throws SerializationException {
        Map<String, Object> dataMap = new HashMap<>();
        for (Map.Entry<String, Property<?, P>> entry : this.properties.entrySet()) {
            Property<?, P> property = entry.getValue();
            Object value = node.getOrThrow(entry.getKey(), Object.class);
            value = mainSerializer().deserialize(value, property.getType());
            dataMap.put(entry.getKey(), value);
        }
        return this.initializer.apply(new ValueMap(dataMap));
    }

    @Override
    protected void loadData(final @NotNull P value, final @NotNull DataNode node) {
        // only final fields for now.
    }

    @Override
    protected void dumpData(final @NotNull P value, final @NotNull Map<String, Object> output) throws SerializationException {
        for (Map.Entry<String, Property<?, P>> entry : this.properties.entrySet()) {
            Object v = entry.getValue().get(value);
            v = mainSerializer().serialize(v);
            output.put(entry.getKey(), v);
        }
    }

}
