package it.fulminazzo.yagl.serializer;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SerializableFunctionSerializerTest extends SerializerTestHelper<GreetFunction> {
    private static final @NotNull String TYPE_CUSTOM_GREET = "CUSTOM_GREET";
    private static final @NotNull String TYPE_ITALIAN_GREET = "ITALIAN_GREET";

    private SerializableFunctionSerializerTest() {
        super(GreetFunction.class, new SerializableFunctionSerializer<>(GreetFunction.class));
    }

    @Test
    void testDeserializeGreet() throws IOException {
        GreetFunction function = load("greet");
        assertNotNull(function);
        assertEquals("Hello!", function.greet());
    }

    @Test
    void testDeserializeCustomGreet() throws IOException {
        GreetFunction function = load("custom_greet");
        assertInstanceOf(CustomGreet.class, function);
        assertEquals("Hello!", function.greet());
    }

    @Test
    void testDeserializeItalianGreet() throws IOException {
        GreetFunction function = load("italian_greet");
        assertInstanceOf(ItalianGreet.class, function);
        assertEquals("Ciao!", function.greet());
    }

    @Test
    void testSerializeGreet() throws IOException {
        GreetFunction function = () -> "Hello!";

        File file = dump(function, "greet");
        try (FileInputStream input = new FileInputStream(file)) {
            String raw = new String(input.readAllBytes());
            assertEquals(
                    function.serialize(),
                    raw.replace("--- ", "").strip().replace("\"", "")
            );
        }
    }

    @Test
    void testSerializeCustomGreet() throws IOException {
        CustomGreet function = new CustomGreet("Hello!");

        Map<?, ?> raw = readRaw(dump(function, "custom_greet"));
        assertEquals(TYPE_CUSTOM_GREET, raw.get("type"));
        assertEquals("Hello!", raw.get("content"));
    }

    @Test
    void testSerializeItalianGreet() throws IOException {
        ItalianGreet function = new ItalianGreet();

        File dumped = dump(function, "italian_greet");
        Object raw = new com.fasterxml.jackson.dataformat.yaml.YAMLMapper().readValue(dumped, Object.class);
        assertEquals(TYPE_ITALIAN_GREET, raw);
    }

    @Test
    void testDeserializeThrowsIfInvalidSerializationString() {
        assertThrows(IllegalArgumentException.class, () -> load("invalid_serialization"));
    }

    @Test
    void testDeserializeThrowsIfUnknownType() {
        IOException exception = assertThrows(IOException.class, () -> load("unknown_type"));
        Throwable cause = exception.getCause();
        assertInstanceOf(SerializationException.class, cause);
        String message = cause.getMessage();
        assertTrue(message.contains("UNKNOWN_GREET"), "Invalid message: " + message);
    }

    @Test
    void testSerializeThrowsIfIOException() {
        GreetFunction function = () -> {
            System.out.println(this);
            return "";
        };
        assertThrows(IOException.class, () -> dump(function, "failing_greet"));
    }

}