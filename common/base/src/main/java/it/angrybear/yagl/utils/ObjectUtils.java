package it.angrybear.yagl.utils;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.structures.Tuple;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The type Object utils.
 */
public class ObjectUtils {
    private static final String EMPTY_IDENTIFIER = "";

    /**
     * Prints the given object in a JSON format.
     * If the object (or an object contained in it) is "empty",
     * it will be printed as {@link #EMPTY_IDENTIFIER}.
     *
     * @param object the object
     * @return the output
     */
    public static String printAsJSON(@Nullable Object object) {
        if (object == null) return EMPTY_IDENTIFIER;
        else if (object instanceof Enum<?>) return ((Enum<?>) object).name();
        else if (object instanceof String) {
            String s = object.toString();
            if (s.isEmpty()) return EMPTY_IDENTIFIER;
            else return String.format("\"%s\"", s);
        } else if (object instanceof Number) {
            Number n = (Number) object;
            if (n.doubleValue() > 0) return n.toString();
            else return EMPTY_IDENTIFIER;
        } else if (ReflectionUtils.isPrimitiveOrWrapper(object.getClass())) return object.toString();
        else if (object instanceof Collection) {
            Collection<?> collection = (Collection<?>) object;
            String output = collection.stream().map(ObjectUtils::printAsJSON).collect(Collectors.joining(", "));
            if (output.matches("(, )*")) return EMPTY_IDENTIFIER;
            else return String.format("[%s]", output);
        } else if (!(object instanceof Map)) {
            Map<Object, Object> map = new LinkedHashMap<>();
            Refl<?> refl = new Refl<>(object);
            for (final Field field : refl.getNonStaticFields()) {
                Object obj = refl.getFieldObject(field);
                map.put(field.getName(), obj);
            }
            object = map;
        }
        Map<?, ?> map = (Map<?, ?>) object;
        StringBuilder output = new StringBuilder();
        map.entrySet().stream()
                .map(e -> new Tuple<>(printAsJSON(e.getKey()), printAsJSON(e.getValue())))
                .filter(t -> !t.getKey().equals(EMPTY_IDENTIFIER) && !t.getValue().equals(EMPTY_IDENTIFIER))
                .forEach(t -> output.append(t.getKey()).append(": ").append(t.getValue()).append(", "));
        String result = output.toString();
        if (result.matches("(: , )*")) return EMPTY_IDENTIFIER;
        else return String.format("{%s}", result.substring(0, result.length() - 2));
    }

}
