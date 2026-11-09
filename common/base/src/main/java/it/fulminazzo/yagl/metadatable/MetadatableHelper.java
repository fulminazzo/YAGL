package it.fulminazzo.yagl.metadatable;

import it.fulminazzo.yagl.reflect.Reflect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A support class for {@link Metadatable}.
 */
@SuppressWarnings("unchecked")
class MetadatableHelper implements Metadatable {
    private static final @NotNull ClassValue<@NotNull Field[]> APPLICABLE_FIELDS_CACHE = new ClassValue<>() {

        @Override
        protected @NotNull Field[] computeValue(final @NotNull Class<?> type) {
            return Reflect.on(type).getNonStaticFields().stream()
                    .filter(f -> !f.isAnnotationPresent(IgnoreApply.class))
                    .toArray(Field[]::new);
        }

    };

    private final @NotNull Metadatable internal;
    private final @NotNull Set<Object> visited;

    /**
     * Instantiates a new Metadatable helper.
     *
     * @param internal the internal
     */
    public MetadatableHelper(final @NotNull Metadatable internal) {
        this.internal = internal;
        this.visited = new HashSet<>();
    }

    @Override
    public <T> @Nullable T apply(final @Nullable T object) {
        Reflect reflect = Reflect.on(object);
        if (object == null || reflect.isPrimitive() || reflect.isWrapper()) return object;

        Class<?> objectClass = object.getClass();
        if (objectClass.isEnum() || objectClass.isSynthetic())
            return object;

        String packageName = objectClass.getPackage().getName();
        if (packageName.startsWith("java.") || packageName.startsWith("javax.") || packageName.startsWith("jdk."))
            return object;

        if (object instanceof String) return (T) applyString((String) object);
        else if (visited.contains(object)) return object;
        visited.add(object);

        if (objectClass.isArray()) return (T) applyArray((Object[]) object);
        else if (object instanceof Collection) return (T) applyCollection((Collection<?>) object);
        else if (object instanceof Map) return (T) applyMap((Map<?, ?>) object);
        else {
            for (Field field : APPLICABLE_FIELDS_CACHE.get(objectClass))
                reflect.set(field, apply(reflect.get(field).get()));
            return object;
        }
    }

    /**
     * Resolves variables for each of the key and value elements of a map.
     *
     * @param map the map
     * @param <K> the type of the keys
     * @param <V> the type of the values
     * @return the map (with updated elements)
     */
    <K, V> @NotNull Map<K, V> applyMap(final @NotNull Map<K, V> map) {
        final List<K> keys = new ArrayList<>(map.keySet());
        for (K key : keys) {
            if (key == null) continue;
            V value = map.get(key);
            if (value != null) {
                map.remove(key);
                map.put(apply(key), apply(value));
            }
        }
        return map;
    }

    /**
     * Resolves variables for each of the elements of a collection.
     *
     * @param collection the collection
     * @return the collection (with updated elements)
     */
    @NotNull Collection<?> applyCollection(final @NotNull Collection<?> collection) {
        Class<?> collectionType = collection.getClass();
        // In the case of creation with Arrays.asList()
        if (collectionType.getCanonicalName().equals(Arrays.class.getCanonicalName() + ".ArrayList"))
            collectionType = ArrayList.class;
        Class<Collection<Object>> finalClass = (Class<Collection<Object>>) collectionType;
        return collection.stream()
                .map(this::apply)
                .collect(Collectors.toCollection(() -> Reflect.on(finalClass).init().get()));
    }

    /**
     * Resolves variables for each of the elements of an array.
     *
     * @param array the array
     * @param <T>   the type of the elements
     * @return the array (with updated elements)
     */
    <T> @Nullable T @NotNull [] applyArray(final @Nullable T @NotNull [] array) {
        for (int i = 0; i < array.length; i++) array[i] = apply(array[i]);
        return array;
    }

    /**
     * Recursively resolves variables in a string.
     * Variables must be enclosed in angle brackets, e.g. {@code <variable>}.
     * If a variable is not found, or the brackets are escaped ({@code \<}), the variable is left unchanged.
     *
     * @param string the string to apply the metadatable to
     * @return the string with variables resolved
     */
    @NotNull String applyString(final @NotNull String string) {
        if (!string.contains("<")) return string;
        final Map<String, String> variables = variables();
        final StringBuilder builder = new StringBuilder();
        final char[] chars = string.toCharArray();
        boolean escaped = false;
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '\\') escaped = true;
            else if (c == '<' && !escaped) {
                StringBuilder variableBuilder = new StringBuilder("<");
                i++;
                for (; i < chars.length; i++) {
                    char d = chars[i];
                    variableBuilder.append(d);
                    if (d == '>') {
                        // Recursively resolve variables.
                        String variable = variableBuilder.toString();
                        String variableName;
                        while (variable.length() > 2 && variables.containsKey(variableName = variable.substring(1, variable.length() - 1))) {
                            variable = variables.get(variableName);
                        }
                        variableBuilder.setLength(0);
                        variableBuilder.append(variable);
                        break;
                    }
                }
                builder.append(variableBuilder);
                continue;
            } else if (escaped) escaped = false;
            builder.append(c);
        }
        return builder.toString();
    }

    @Override
    public @NotNull Map<String, String> variables() {
        return this.internal.variables();
    }

}
