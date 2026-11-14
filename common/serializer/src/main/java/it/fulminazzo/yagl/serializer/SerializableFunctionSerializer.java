package it.fulminazzo.yagl.serializer;

import it.fulminazzo.yagl.SerializableFunction;
import it.fulminazzo.yagl.naming.CaseConverter;
import it.fulminazzo.yagl.naming.Convention;
import it.fulminazzo.yagl.reflect.Reflect;
import it.fulminazzo.yagl.reflect.ReflectException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A special {@link Serializer} for {@link SerializableFunction} implementations.
 * <br>
 * The <b>serialization</b> part will use the following rules:
 * <ul>
 *     <li>if the value is of the same type as the one bound to this serializer,
 *     it will be serialized as a {@link String} using {@link SerializableFunction#serialize()};</li>
 *     <li>if the value is <b>not</b> of the same type (meaning it is a class implementing the interface),
 *     the {@link SerializableFunction#serialize()} method will be used to get its contents.
 *     <br>
 *     If it succeeds, it will be serialized as a {@link Map} containing the class type and the result of the serialization.
 *     <br>
 *     If it fails, it will be serialized as a {@link String} containing only the class type
 *     (assuming the contents are non-serializable or not of interest for the correct functioning of the object).</li>
 * </ul>
 * The <b>deserialization</b> part will use the following rules:
 * <ul>
 *     <li>if a type is specified, the serializer will look for a constructor function in the class
 *     and use it with the contents of the serialized object;</li>
 *     <li>if a type is not specified, then the serializer expects a {@link String}.
 *     <br>
 *     First, it looks up the corresponding class for the type. If it is found, a new empty instance is created.
 *     <br>
 *     If it is not found, the serializer will assume a general {@link SerializableFunction} was serialized,
 *     so it will attempt to deserialize it.
 *     </li>
 * </ul>
 *
 * @param <F> the type of the {@link SerializableFunction} to serialize
 * @see SerializableFunction
 */
@SuppressWarnings("unchecked")
@RequiredArgsConstructor
public final class SerializableFunctionSerializer<F extends SerializableFunction> implements Serializer<F> {
    private final @NotNull Class<F> type;

    @Override
    public @Nullable F deserialize(final @NotNull Node node) throws SerializationException {
        Object raw = node.getRaw();
        if (raw == null) return null;
        else if (raw instanceof String) {
            String typeName = (String) raw;
            Class<? extends F> type = toType(typeName);
            if (type != null) return Reflect.on(type).init().get();
            else try {
                return (F) SerializableFunction.deserialize(typeName);
            } catch (IOException | ClassNotFoundException e) {
                throw new SerializationException(e);
            }
        } else {
            DataNode dataNode = node.asDataNodeOrThrow();
            String typeName = dataNode.getOrThrow("type", String.class);
            Class<? extends F> type = toType(typeName);
            if (type == null)
                throw new SerializationException("Unknown %s type: %s", this.type.getSimpleName(), typeName);
            return Reflect.on(type).init(dataNode.getOrThrow("content", String.class)).get();
        }
    }

    @Override
    public @Nullable Object serialize(final @Nullable F value) throws SerializationException {
        if (value == null) return null;
        else try {
            Class<? extends F> valueType = (Class<? extends F>) value.getClass();
            if (valueType.isSynthetic()) return value.serialize();
            else {
                String valueTypeName = toTypeName(valueType);
                try {
                    String content = value.serialize();
                    Map<String, Object> result = new HashMap<>();
                    result.put("type", valueTypeName);
                    result.put("content", content);
                    return result;
                } catch (UnsupportedOperationException e) {
                    return valueTypeName;
                }
            }
        } catch (IOException e) {
            throw new SerializationException(e, "Could not serialize: %s", value);
        }
    }

    private @Nullable Class<? extends F> toType(@NotNull String typeName) {
        if (!typeName.contains(".")) {
            typeName = CaseConverter.convert(typeName.toLowerCase(Locale.ROOT), Convention.SNAKE_CASE, Convention.PASCAL_CASE);
            typeName = getPackageName() + typeName;
        }
        try {
            return Reflect.on(typeName).get();
        } catch (ReflectException e) {
            return null;
        }
    }

    private @NotNull String toTypeName(final @NotNull Class<? extends F> valueType) {
        String typeName = valueType.getCanonicalName().replace(getPackageName(), "");
        if (typeName.contains(".")) return typeName;
        else
            return CaseConverter.convert(typeName, Convention.PASCAL_CASE, Convention.SNAKE_CASE).toUpperCase(Locale.ROOT);
    }

    private @NotNull String getPackageName() {
        return type.getPackageName() + ".";
    }

}
