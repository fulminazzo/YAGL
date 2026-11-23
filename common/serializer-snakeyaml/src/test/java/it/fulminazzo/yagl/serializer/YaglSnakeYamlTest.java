package it.fulminazzo.yagl.serializer;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class YaglSnakeYamlTest {
    private final @NotNull Person person = new Person("Alex", 24);
    private final @NotNull Map<?, ?> data = Map.of(
            PersonSerializer.NAME_PATH, person.getName(),
            PersonSerializer.AGE_PATH, person.getAge()
    );

    private @NotNull Yaml yaml;

    @BeforeEach
    void setup() {
        LoaderOptions loaderOptions = new LoaderOptions();
        DumperOptions dumperOptions = new DumperOptions();

        YaglConstructor constructor = new YaglConstructor(loaderOptions);
        YaglRepresenter representer = new YaglRepresenter(dumperOptions);

        yaml = new Yaml(constructor, representer, dumperOptions, loaderOptions);
    }

    @Test
    void testDeserialize() {
        final String serializedPerson = yaml.dump(data);
        final Person deserializedPerson = yaml.loadAs(serializedPerson, Person.class);
        assertNotNull(deserializedPerson);
        assertEquals(person.getName(), deserializedPerson.getName());
        assertEquals(person.getAge(), deserializedPerson.getAge());
    }

    @Test
    void testSerializationExceptionOnDeserializeIsRethrownAsYAMLException() {
        final String serializedPerson = yaml.dump(Map.of(PersonSerializer.NAME_PATH, person.getName()));
        Exception exception = assertThrowsExactly(YAMLException.class, () -> yaml.loadAs(serializedPerson, Person.class));
        String message = exception.getMessage();
        assertTrue(
                message.contains("Could not find required key 'age' in path ''"),
                "Exception message should contain serialization exception class name, but was: " + message
        );
    }

    @Test
    void testSerialize() {
        final String serializedPerson = yaml.dump(person);
        assertNotNull(serializedPerson);
        final Map<?, ?> loaded = yaml.load(serializedPerson);
        assertNotNull(loaded);
        assertEquals(person.getName(), loaded.get(PersonSerializer.NAME_PATH));
        assertEquals(person.getAge(), loaded.get(PersonSerializer.AGE_PATH));
    }

    @Test
    void testSerializationExceptionOnSerializeIsRethrownAsYAMLException() {
        final Person underagePerson = new Person("John", 17);
        Exception exception = assertThrowsExactly(YAMLException.class, () -> yaml.dump(underagePerson));
        assertTrue(
                exception.getMessage().contains(SerializationException.class.getName()),
                "Exception message should contain serialization exception class name, but was: " + exception.getMessage()
        );
    }

}
