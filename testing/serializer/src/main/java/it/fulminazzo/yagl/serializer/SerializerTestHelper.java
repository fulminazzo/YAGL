package it.fulminazzo.yagl.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

/**
 * A helper class for testing {@link Serializer}s objects.
 */
public abstract class SerializerTestHelper<T> {
    private static final @NotNull String BASE_PATH = "build/resources/test/serializer";
    private static final @NotNull String LOAD_DIRECTORY_NAME = "load";
    private static final @NotNull String DUMP_DIRECTORY_NAME = "dump";

    protected final @NotNull Class<T> type;
    protected final @NotNull Serializer<T> serializer;
    protected final @NotNull File workingDir;
    private final @NotNull ObjectMapper mapper;

    /**
     * Instantiates a new Serializer test helper.
     *
     * @param type       the type of the object to serialize
     * @param serializer the serializer to test
     */
    protected SerializerTestHelper(final @NotNull Class<T> type, final @NotNull Serializer<T> serializer) {
        this.type = type;
        this.serializer = serializer;

        String dirPath = getClass().getCanonicalName()
                .replace(SerializerTestHelper.class.getPackage().getName() + ".", "")
                .replace(".", File.separator)
                .toLowerCase(Locale.ROOT);
        this.workingDir = new File(BASE_PATH + File.separator + dirPath);

        this.mapper = new YAMLMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(new SerializerBounderSerializer(type, serializer));
        module.addDeserializer(type, new SerializerBoundDeserializer(type, serializer));
        this.mapper.registerModule(module);
    }

    @BeforeEach
    public void setup() {
        File dumpDir = new File(workingDir, DUMP_DIRECTORY_NAME);
        if (dumpDir.isDirectory()) {
            Arrays.stream(dumpDir.listFiles()).forEach(File::delete);
            dumpDir.delete();
        }
    }

    /**
     * Deserializes the given file.
     *
     * @param fileName the name of the file to deserialize
     * @return the deserialized object
     */
    protected @Nullable T load(final @NotNull String fileName) throws IOException {
        File file = new File(workingDir, LOAD_DIRECTORY_NAME + File.separator + fileName + ".yml");
        return mapper.readValue(file, type);
    }

    /**
     * Serializes the given object to a file with the given name.
     *
     * @param object   the object to serialize
     * @param fileName the name of the file to serialize the object to
     * @return the file that the object was serialized to
     */
    protected @NotNull File dump(final @Nullable T object, final @NotNull String fileName) throws IOException {
        File file = new File(workingDir, DUMP_DIRECTORY_NAME + File.separator + fileName + ".yml");
        file.getParentFile().mkdirs();
        mapper.writeValue(file, object);
        return file;
    }

    private class SerializerBoundDeserializer extends StdDeserializer<T> {
        private static final long serialVersionUID = -7239482593165710566L;

        private final @NotNull Serializer<T> serializer;

        public SerializerBoundDeserializer(final @NotNull Class<T> type, final @NotNull Serializer<T> serializer) {
            super(type);
            this.serializer = serializer;
        }

        @Override
        public T deserialize(final @NotNull JsonParser parser,
                             final @NotNull DeserializationContext context) throws IOException {
            try {
                String currentKey = parser.currentName();
                if (currentKey == null) currentKey = "";
                JsonPointer pathWithoutKey = parser.getParsingContext().pathAsPointer().head();
                String pathString = pathWithoutKey == null ? "" : pathWithoutKey.toString();
                Node node = new Node(
                        parser.readValueAs(Object.class),
                        pathString,
                        currentKey
                );
                return serializer.deserialize(node);
            } catch (SerializationException e) {
                throw new IOException(e);
            }
        }

    }

    private class SerializerBounderSerializer extends StdSerializer<T> {
        private static final long serialVersionUID = 563295772298400251L;

        private final @NotNull Serializer<T> serializer;

        public SerializerBounderSerializer(final @NotNull Class<T> type, final @NotNull Serializer<T> serializer) {
            super(type);
            this.serializer = serializer;
        }

        @Override
        public void serialize(final @Nullable T value,
                              final @NotNull JsonGenerator generator,
                              final @NotNull SerializerProvider provider) throws IOException {
            try {
                generator.writeObject(serializer.serialize(value));
            } catch (SerializationException e) {
                throw new IOException(e);
            }
        }

    }

}
