package it.fulminazzo.yagl.metadatable;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A support class for {@link Metadatable}.
 */
class MetadatableHelper implements Metadatable {
    /**
     * A function used to convert the variables in {@link #apply(Object)}.
     */
    private static final @NotNull Function<@NotNull String, @NotNull String> VARIABLE_FORMAT = s -> "<" + s + ">";

    private final @NotNull Metadatable internal;
    private final @NotNull List<Object> alreadyApplied;

    /**
     * Instantiates a new Metadatable helper.
     *
     * @param internal the internal
     */
    public MetadatableHelper(final @NotNull Metadatable internal) {
        this.internal = internal;
        this.alreadyApplied = new ArrayList<>();
    }

    /**
     * Applies all the current variables to the given object fields,
     * by replacing in every string all the variables in the format {@link #VARIABLE_FORMAT}.
     *
     * @param <T>    the type parameter
     * @param object the object
     * @return the object parsed
     */
    @SuppressWarnings("unchecked")
    public <T> T apply(final T object) {
        if (object == null) return null;

        if (this.alreadyApplied.contains(object)) return object;
        this.alreadyApplied.add(object);

        if (object.getClass().isEnum()) return object;
        else if (object.getClass().isArray()) {
            Object[] array = (Object[]) object;
            for (Object o : array) apply(o);
        }
        else if (object instanceof String) return (T) apply((String) object);
        else if (object instanceof Collection) return (T) apply((Collection<Object>) object);
        else if (object instanceof Map) return (T) apply((Map<Object, Object>) object);
        else if (!ReflectionUtils.isPrimitiveOrWrapper(object.getClass())) {
            final Refl<T> refl = new Refl<>(object);
            for (Field field : refl.getNonStaticFields())
                ReflectionUtils.setAccessible(field)
                        .map(f -> f.get(object))
                        .filter(o -> !o.toString().contains("Lambda"))
                        .ifPresent(o -> field.set(object, apply(o)));
        }
        return object;
    }

    /**
     * Applies all the current variables to the given string.
     *
     * @param string the string
     * @return the string parsed
     */
    String apply(@NotNull String string) {
        for (final String v : variables().keySet()) {
            final String s = getVariable(v);
            if (s == null) continue;
            final Matcher matcher = Pattern.compile("([^\\\\]|^)" + VARIABLE_FORMAT.apply(v)).matcher(string);
            while (matcher.find())
                string = string.substring(0, matcher.start()) + matcher.group(1) + s +
                        string.substring(matcher.end());
        }
        return string;
    }

    /**
     * Applies all the current variables to the given collection.
     *
     * @param collection the collection
     * @return the collection parsed
     */
    @SuppressWarnings("unchecked")
    @NotNull Collection<Object> apply(Collection<Object> collection) {
        Class<?> clazz = collection.getClass();
        // In the case of creation with Arrays.asList()
        if (clazz.getCanonicalName().equals(Arrays.class.getCanonicalName() + ".ArrayList"))
            clazz = ArrayList.class;
        Class<Collection<Object>> finalClass = (Class<Collection<Object>>) clazz;
        return collection.stream().map(this::apply)
                .collect(Collectors.toCollection(() -> new Refl<>(finalClass, new Object[0]).getObject()));
    }

    /**
     * Applies all the current variables to the given map.
     *
     * @param map the map
     * @return the map parsed
     */
    @NotNull Map<Object, Object> apply(final @NotNull Map<Object, Object> map) {
        final List<Object> keys = new ArrayList<>(map.keySet());
        for (Object key : keys) {
            if (key == null) continue;
            Object value = map.get(key);
            if (value != null) {
                map.remove(key);
                map.put(apply(key), apply(value));
            }
        }
        return map;
    }

    @Override
    public @NotNull Map<String, String> variables() {
        return this.internal.variables();
    }

}
