package it.fulminazzo.yagl.serializer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class CatSerializer extends TypedSerializer<Cat> {

    public CatSerializer() {
        super(Cat.class);
    }

    @Override
    protected void loadCommonData(final @NotNull Cat value, final @NotNull DataNode node) throws SerializationException {
        node.get("age", Integer.class).ifPresent(value::setAge);
    }

    @Override
    protected void dumpCommonData(final @NotNull Cat value, final @NotNull DataNode output) throws SerializationException {
        output.set("name", value.getName());
        output.set("age", value.getAge());
    }

    {
        registerSerializer(PersianCat.class, new ObjectSerializer<>() {
            @Override
            protected @Nullable PersianCat initialize(final @NotNull DataNode node) throws SerializationException {
                return node.get("name", String.class).map(PersianCat::new).orElse(null);
            }

            @Override
            protected void loadData(final @NotNull PersianCat value, final @NotNull DataNode node) throws SerializationException {
                node.get("color", String.class).ifPresent(value::setColor);
            }

            @Override
            protected void dumpData(final @NotNull PersianCat value, final @NotNull DataNode output) throws SerializationException {
                output.set("color", value.getColor());
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
            protected void loadData(final @NotNull SiameseCat value, final @NotNull DataNode node) throws SerializationException {
                node.get("region", String.class).ifPresent(value::setRegion);
            }

            @Override
            protected void dumpData(final @NotNull SiameseCat value, final @NotNull DataNode output) throws SerializationException {
                output.set("master", value.getMaster());
                output.set("region", value.getRegion());
            }

        });
    }
}
