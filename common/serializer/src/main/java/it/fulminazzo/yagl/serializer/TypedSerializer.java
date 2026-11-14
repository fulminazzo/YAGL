package it.fulminazzo.yagl.serializer;

import it.fulminazzo.yagl.structure.BiMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * An implementation of {@link ObjectSerializer} that redirects serialization to specific
 * serializers based on the provided type.
 * <br>
 * This is useful when having multiple implementations of one interface or abstract class
 * which are not directly exposed to the end user.
 * <br>
 * Let's consider the following scenario:
 * <ul>
 *     <li>an abstract class {@code Cat};</li>
 *     <li>the implementation {@code PersianCat};</li>
 *     <li>another implementation {@code SiameseCat}.</li>
 * </ul>
 * And two {@link ObjectSerializer}s for {@code PersianCat} and {@code SiameseCat}.
 * <br>
 * The {@code TypedSerializer} for {@code Cat} will do the following:
 * <ul>
 *     <li>on <b>serialization</b>, it will save the <b>type</b> of the object (either "PERSIAN" or "SIAMESE")
 *     along with other data;</li>
 *     <li>on <b>deserialization</b>, it will use the <b>type</b> of the object to choose the correct serializer.</li>
 * </ul>
 * In this way, it is possible to serialize and deserialize general {@code Cat} objects,
 * without exposing the actual implementation details.
 *
 * @param <T> the type of the object to serialize
 */
@SuppressWarnings("unchecked")
public class TypedSerializer<T> extends ObjectSerializer<T> {
    private final @NotNull BiMap<String, Class<? extends T>> typeClassNames = new BiMap<>();
    private final @NotNull Map<Class<? extends T>, ObjectSerializer<? extends T>> classSerializers = new HashMap<>();

    private final @NotNull Class<T> type;

    /**
     * Instantiates a new Typed serializer.
     *
     * @param type the general type of the serializer
     */
    public TypedSerializer(final @NotNull Class<T> type) {
        this.type = type;
    }

    /**
     * Registers a new serializer for the given type.
     * The name associated to the type is the simple name of the class, stripped of the common type name.
     * <br>
     * For example, if {@code PageableGUI} extends {@code GUI}, the name will be "Pageable".
     *
     * @param type       the actual type
     * @param serializer the serializer to use for the type
     * @param <C>        the type registered
     * @return this serializer
     */
    public <C extends T> @NotNull TypedSerializer<T> registerSerializer(
            final @NotNull Class<C> type,
            final @NotNull ObjectSerializer<C> serializer
    ) {
        return registerSerializer(type.getSimpleName().replace(this.type.getSimpleName(), ""), type, serializer);
    }

    /**
     * Registers a new serializer for the given type.
     *
     * @param typeName   the name to associate to the type
     * @param type       the actual type
     * @param serializer the serializer to use for the type
     * @param <C>        the type registered
     * @return this serializer
     */
    public <C extends T> @NotNull TypedSerializer<T> registerSerializer(
            final @NotNull String typeName,
            final @NotNull Class<C> type,
            final @NotNull ObjectSerializer<C> serializer
    ) {
        String actualType = typeName.toUpperCase(Locale.ROOT);
        typeClassNames.put(actualType, type);
        classSerializers.put(type, serializer);
        return this;
    }

    /**
     * Loads data shared across all type implementations from the given node into the given value.
     *
     * @param value the value to load data into
     * @param node  the node to get data from
     * @throws SerializationException in case of any deserialization errors
     */
    @SuppressWarnings("unused")
    protected void loadCommonData(final @NotNull T value, final @NotNull DataNode node) throws SerializationException {

    }

    /**
     * Dumps data shared across all type implementations from the given value into the given output map.
     *
     * @param value  the value to dump data from
     * @param output the output map to dump data into
     * @throws SerializationException in case of any serialization errors
     */
    @SuppressWarnings("unused")
    protected void dumpCommonData(final @NotNull T value, final @NotNull Map<String, Object> output) throws SerializationException {

    }

    private @NotNull ObjectSerializer<T> getSerializer(final @NotNull T value) {
        return (ObjectSerializer<T>) classSerializers.get(value.getClass());
    }

    @Override
    protected @Nullable T initialize(final @NotNull DataNode node) throws SerializationException {
        String typeName = node.getOrThrow("type", String.class);
        Class<? extends T> clazz = typeClassNames.get(typeName);
        if (clazz == null)
            throw new SerializationException("Unknown %s type: %s", type.getSimpleName(), typeName);
        return classSerializers.get(clazz).initialize(node);
    }

    @Override
    protected void loadData(final @NotNull T value, final @NotNull DataNode node) throws SerializationException {
        loadCommonData(value, node);
        getSerializer(value).loadData(value, node);
    }

    @Override
    protected void dumpData(final @NotNull T value, final @NotNull Map<String, Object> output) throws SerializationException {
        dumpCommonData(value, output);
        getSerializer(value).dumpData(value, output);
        output.put("type", typeClassNames.getByValue((Class<? extends T>) value.getClass()));
    }

}
