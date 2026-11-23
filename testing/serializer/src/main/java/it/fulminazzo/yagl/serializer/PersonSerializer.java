package it.fulminazzo.yagl.serializer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A serializer for {@link Person} objects.
 *
 * @see Person
 * @see ObjectSerializer
 */
final class PersonSerializer extends ObjectSerializer<Person> {
    static final @NotNull String NAME_PATH = "name";
    static final @NotNull String AGE_PATH = "age";

    @Override
    protected @Nullable Person initialize(final @NotNull DataNode node) throws SerializationException {
        String name = node.getOrNull(NAME_PATH, String.class);
        if (name == null) return null;
        else return new Person(name, node.getOrThrow(AGE_PATH, Integer.class));
    }

    @Override
    protected void loadData(final @NotNull Person value, final @NotNull DataNode node) {
        // nothing else to set, already covered in initialization
    }

    @Override
    protected void dumpData(final @NotNull Person value, final @NotNull DataNode output) throws SerializationException {
        output.set(NAME_PATH, value.getName());
        output.set(AGE_PATH, value.getAge());
        if (value.getAge() < 18) throw new SerializationException("Person is underage");
    }

}
