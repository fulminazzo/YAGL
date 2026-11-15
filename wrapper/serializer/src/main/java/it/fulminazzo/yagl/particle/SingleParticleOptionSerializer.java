package it.fulminazzo.yagl.particle;

import it.fulminazzo.yagl.serializer.Node;
import it.fulminazzo.yagl.serializer.SerializationException;
import it.fulminazzo.yagl.serializer.Serializer;
import it.fulminazzo.yagl.serializer.YaglSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.function.Function;

/**
 * A general {@link Serializer} for {@link ParticleOption} objects that only contain <b>one</b> value.
 *
 * @param <T> the type of the value
 * @param <P> the type of the particle option
 */
public final class SingleParticleOptionSerializer<T, P extends ParticleOption<T>> implements Serializer<P> {
    private final @NotNull Class<T> fieldType;
    private final @NotNull Function<T, P> initializer;

    /**
     * Instantiates a new Single particle option serializer.
     *
     * @param optionType  the option type
     * @param initializer the function to initialize a new instance of the particle option
     */
    @SuppressWarnings("unchecked")
    public SingleParticleOptionSerializer(final @NotNull Class<P> optionType, final @NotNull Function<T, P> initializer) {
        this.fieldType = (Class<T>) Arrays.stream(optionType.getDeclaredFields())
                .filter(f -> !Modifier.isStatic(f.getModifiers()))
                .findFirst()
                .map(Field::getType)
                .orElseThrow(() -> new IllegalArgumentException("No type field found in " + optionType));
        this.initializer = initializer;
    }

    @Override
    public @Nullable P deserialize(final @NotNull Node node) throws SerializationException {
        T option = YaglSerializer.getInstance().deserialize(node.getRaw(), this.fieldType);
        return this.initializer.apply(option);
    }

    @Override
    public @Nullable Object serialize(final @Nullable P value) throws SerializationException {
        return value == null ? null : YaglSerializer.getInstance().serialize(value.getOption());
    }

}
