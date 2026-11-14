package it.fulminazzo.yagl.serializer;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TypedSerializerTest extends SerializerTestHelper<TypedSerializerTest.Cat> {

    protected TypedSerializerTest() {
        super(Cat.class, new TypedSerializer<>(Cat.class) {

            @Override
            protected void loadCommonData(final @NotNull TypedSerializerTest.Cat value, final @NotNull DataNode node) throws SerializationException {
                node.get("age", Integer.class).ifPresent(value::setAge);
            }

            @Override
            protected void dumpCommonData(final @NotNull TypedSerializerTest.Cat value, final @NotNull Map<String, Object> output) {
                output.put("name", value.getName());
                output.put("age", value.getAge());
            }

            {
                registerSerializer(PersianCat.class, new ObjectSerializer<>() {
                    @Override
                    protected @Nullable PersianCat initialize(final @NotNull DataNode node) throws SerializationException {
                        return node.get("name", String.class).map(PersianCat::new).orElse(null);
                    }

                    @Override
                    protected void loadData(final @NotNull TypedSerializerTest.PersianCat value, final @NotNull DataNode node) throws SerializationException {
                        node.get("color", String.class).ifPresent(value::setColor);
                    }

                    @Override
                    protected void dumpData(final @NotNull TypedSerializerTest.PersianCat value, final @NotNull Map<String, Object> output) {
                        output.put("color", value.getColor());
                    }

                });
                registerSerializer(SiameseCat.class, new ObjectSerializer<>() {

                    @Override
                    protected @Nullable SiameseCat initialize(final @NotNull DataNode node) throws SerializationException {
                        String name = node.getOrNull("name", String.class);
                        if (name == null) return null;
                        return node.get("master", String.class).map(m -> new SiameseCat(name, m)).orElse(null);
                    }

                    @Override
                    protected void loadData(final @NotNull TypedSerializerTest.SiameseCat value, final @NotNull DataNode node) throws SerializationException {
                        node.get("region", String.class).ifPresent(value::setRegion);
                    }

                    @Override
                    protected void dumpData(final @NotNull TypedSerializerTest.SiameseCat value, final @NotNull Map<String, Object> output) {
                        output.put("master", value.getMaster());
                        output.put("region", value.getRegion());
                    }

                });
            }
        });
    }

    @Test
    void testDeserializePersian() throws IOException {
        Cat cat = load("persian");
        assertInstanceOf(PersianCat.class, cat);
        PersianCat persianCat = (PersianCat) cat;
        assertEquals("Garfield", persianCat.getName());
        assertEquals(15, persianCat.getAge());
        assertEquals("orange", persianCat.getColor());
    }

    @Test
    void testDeserializeSiamese() throws IOException {
        Cat cat = load("siamese");
        assertInstanceOf(SiameseCat.class, cat);
        SiameseCat siameseCat = (SiameseCat) cat;
        assertEquals("White", siameseCat.getName());
        assertEquals(5, siameseCat.getAge());
        assertEquals("Alex", siameseCat.getMaster());
        assertEquals("Italy", siameseCat.getRegion());
    }

    @Test
    void testSerializePersian() throws IOException {
        PersianCat persianCat = new PersianCat("Garfield");
        persianCat.setAge(15);
        persianCat.setColor("orange");

        Map<?, ?> raw = readRaw(dump(persianCat, "persian"));
        assertEquals(persianCat.getName(), raw.get("name"));
        assertEquals(persianCat.getAge(), raw.get("age"));
        assertEquals(persianCat.getColor(), raw.get("color"));
        assertEquals("PERSIAN", raw.get("type"));
    }

    @Test
    void testSerializeSiamese() throws IOException {
        SiameseCat siameseCat = new SiameseCat("White", "Alex");
        siameseCat.setAge(5);
        siameseCat.setRegion("Italy");

        Map<?, ?> raw = readRaw(dump(siameseCat, "siamese"));
        assertEquals(siameseCat.getName(), raw.get("name"));
        assertEquals(siameseCat.getAge(), raw.get("age"));
        assertEquals(siameseCat.getMaster(), raw.get("master"));
        assertEquals(siameseCat.getRegion(), raw.get("region"));
        assertEquals("SIAMESE", raw.get("type"));
    }

    @Test
    void testDeserializeThrowsIfInvalidTypeIsSpecified() {
        IOException exception = assertThrows(IOException.class, () -> load("invalid-type"));
        Throwable cause = exception.getCause();
        assertInstanceOf(SerializationException.class, cause);
        String message = cause.getMessage();
        assertTrue(message.contains("BRITISH"), "Invalid message: " + message);
    }

    @Test
    void testDeserializeThrowsIfNoTypeIsSpecified() {
        IOException exception = assertThrows(IOException.class, () -> load("missing-type"));
        Throwable cause = exception.getCause();
        assertInstanceOf(SerializationException.class, cause);
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    final static class PersianCat extends Cat {
        @NotNull String color;

        public PersianCat(final @NotNull String name) {
            super(name);
        }

    }


    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    final static class SiameseCat extends Cat {
        final @NotNull String master;
        @NotNull String region;

        public SiameseCat(final @NotNull String name, final @NotNull String master) {
            super(name);
            this.master = master;
        }

    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    abstract static class Cat {
        final @NotNull String name;
        int age;

    }

}