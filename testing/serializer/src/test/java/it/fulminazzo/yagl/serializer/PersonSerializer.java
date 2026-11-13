package it.fulminazzo.yagl.serializer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

final class PersonSerializer implements Serializer<Person> {

    @Override
    public @Nullable Person deserialize(final @NotNull Node node) throws SerializationException {
        Optional<DataNode> dataNodeOpt = node.asDataNode();
        if (dataNodeOpt.isEmpty()) return null;
        DataNode dataNode = dataNodeOpt.get();
        return new Person(
                dataNode.getOrThrow("name", String.class),
                dataNode.getOrThrow("age", Integer.class)
        );
    }

    @Override
    public @Nullable Object serialize(final @Nullable Person value) {
        if (value == null) return null;
        Map<String, Object> data = new HashMap<>();
        data.put("name", value.getName());
        data.put("age", value.getAge());
        return data;
    }

}
