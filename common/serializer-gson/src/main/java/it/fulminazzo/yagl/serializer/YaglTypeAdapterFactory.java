package it.fulminazzo.yagl.serializer;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * Gson type adapter factory that delegates serialization and deserialization to the
 * shared {@link YaglSerializer} instance for types supported by YAGL
 * (and custom types, registered through {@link SerializersLoader}).
 * <br>
 * Usage:
 * <pre>{@code
 * Gson gson = new GsonBuilder()
 *         .registerTypeAdapterFactory(new YaglTypeAdapterFactory())
 *         .create();
 * }</pre>
 *
 * @see Serializer
 * @see SerializersLoader
 * @see YaglSerializer
 */
public final class YaglTypeAdapterFactory implements TypeAdapterFactory {
    private final @NotNull YaglSerializer yaglSerializer = YaglSerializer.getInstance();

    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(final @NotNull Gson gson, final @NotNull TypeToken<T> typeToken) {
        final Class<T> type = (Class<T>) typeToken.getRawType();
        if (!yaglSerializer.hasSerializer(type)) return null;
        final TypeAdapter<Object> raw = gson.getAdapter(Object.class);
        return new YaglTypeAdapter<T>(type, raw).nullSafe();
    }

    @RequiredArgsConstructor
    private final class YaglTypeAdapter<T> extends TypeAdapter<T> {
        private final @NotNull Class<T> type;
        private final @NotNull TypeAdapter<Object> raw;

        @Override
        public void write(final @NotNull JsonWriter out, final @Nullable T value) throws IOException {
            try {
                raw.write(out, yaglSerializer.serialize(value));
            } catch (SerializationException e) {
                throw new JsonIOException(e);
            }
        }

        @Override
        public T read(final @NotNull JsonReader in) throws IOException {
            try {
                return yaglSerializer.deserialize(raw.read(in), type);
            } catch (SerializationException e) {
                throw new JsonParseException(e);
            }
        }

    }

}