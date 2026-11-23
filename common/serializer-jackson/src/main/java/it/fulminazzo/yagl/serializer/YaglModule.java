package it.fulminazzo.yagl.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * Jackson module for YAGL serialization.
 * <br>
 * Allows to seamlessly serialize and deserialize YAGL objects
 * (and custom types, registered through {@link SerializersLoader})
 * with the Jackson API.
 * <br>
 * Usage:
 * <pre>{@code
 * ObjectMapper mapper = new ObjectMapper();
 * mapper.registerModule(new YaglModule());
 * }</pre>
 *
 * @see Serializer
 * @see SerializersLoader
 * @see YaglSerializer
 */
public final class YaglModule extends SimpleModule {
    private static final long serialVersionUID = -7705361287886716987L;

    private final @NotNull YaglSerializer yaglSerializer = YaglSerializer.getInstance();

    @Override
    public void setupModule(final @NotNull SetupContext context) {
        super.setupModule(context);
        context.addDeserializers(new Deserializers.Base() {

            @Override
            public JsonDeserializer<?> findBeanDeserializer(
                    final @NotNull JavaType type,
                    final @NotNull DeserializationConfig config,
                    final @NotNull BeanDescription beanDesc
            ) {
                return yaglSerializer.hasSerializer(type.getRawClass()) ? new YaglJsonDeserializer(type) : null;
            }

        });
        context.addSerializers(new Serializers.Base() {

            @Override
            public JsonSerializer<?> findSerializer(
                    final @NotNull SerializationConfig config,
                    final @NotNull JavaType type,
                    final @NotNull BeanDescription beanDesc
            ) {
                return yaglSerializer.hasSerializer(type.getRawClass()) ? new YaglJsonSerializer(type) : null;
            }

        });
    }

    private final class YaglJsonDeserializer extends StdDeserializer<Object> {
        private static final long serialVersionUID = 3036007525320137357L;

        public YaglJsonDeserializer(final @NotNull JavaType type) {
            super(type);
        }

        @Override
        public Object deserialize(
                final @NotNull JsonParser parser,
                final @NotNull DeserializationContext context) throws IOException {
            try {
                String currentKey = parser.currentName();
                if (currentKey == null) currentKey = "";
                JsonPointer pathWithoutKey = parser.getParsingContext().pathAsPointer().head();
                String pathString = pathWithoutKey == null ? "" : pathWithoutKey.toString();
                return yaglSerializer.deserialize(
                        parser.readValueAs(Object.class),
                        handledType(),
                        pathString,
                        currentKey
                );
            } catch (SerializationException e) {
                throw new IOException(e);
            }
        }

    }

    private final class YaglJsonSerializer extends StdSerializer<Object> {
        private static final long serialVersionUID = -7510439269379461174L;

        public YaglJsonSerializer(final @NotNull JavaType type) {
            super(type);
        }

        @Override
        public void serialize(
                final @Nullable Object value,
                final @NotNull JsonGenerator generator,
                final @NotNull SerializerProvider provider
        ) throws IOException {
            try {
                generator.writeObject(yaglSerializer.serialize(value));
            } catch (SerializationException e) {
                throw new IOException(e);
            }
        }

    }

}
