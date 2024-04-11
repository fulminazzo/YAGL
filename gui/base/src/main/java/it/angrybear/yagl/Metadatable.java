package it.angrybear.yagl;

import it.fulminazzo.fulmicollection.objects.Refl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

/**
 * An interface that holds key-value pairs of {@link String}s.
 */
public interface Metadatable extends Iterable<String> {
    Function<@NotNull String, @NotNull String> VARIABLE_FORMAT = s -> "%" + s + "%";
    /**
     * A function used to parse the variables names.
     */
    Function<@NotNull String, @NotNull String> VARIABLE_PARSER = s -> {
        final String separator = "_";

        StringBuilder builder = new StringBuilder();
        for (String string : s.split("")) {
            if (string.matches("[A-Z]")) builder.append(separator);
            else if (string.matches("[\r\t\n -]")) {
                builder.append(separator);
                continue;
            }
            builder.append(string);
        }

        String output = builder.toString().toLowerCase();
        while (output.startsWith(separator))
            output = output.substring(separator.length());
        return output;
    };

    /**
     * Sets variable.
     *
     * @param name  the name
     * @param value the value
     * @return this metadatable
     */
    default @NotNull Metadatable setVariable(final @NotNull String name, final @NotNull String value) {
        variables().put(VARIABLE_PARSER.apply(name), value);
        return this;
    }

    /**
     * Unset variable metadatable.
     *
     * @param name the name
     * @return this metadatable
     */
    default @NotNull Metadatable unsetVariable(final @NotNull String name) {
        variables().remove(VARIABLE_PARSER.apply(name));
        return this;
    }

    /**
     * Gets variable.
     * Returns <i>null</i> in case of missing.
     *
     * @param name the name
     * @return the variable
     */
    default @Nullable String getVariable(final @NotNull String name) {
        return variables().get(VARIABLE_PARSER.apply(name));
    }

    /**
     * Applies all the current variables to the given object fields,
     * by replacing in every string all the variables in the format {@link #VARIABLE_FORMAT}.
     *
     * @param object the object
     * @return the object parsed
     */
    default Object apply(final Object object) {
        if (object == null) return null;

        final Refl<?> refl = new Refl<>(object);
        for (Field field : refl.getNonStaticFields())
            if (String.class.isAssignableFrom(field.getType())) {
                String o = refl.getFieldObject(field);
                if (o == null) continue;
                for (String v : this) {
                    String s = getVariable(v);
                    if (s != null)
                        o = o.replace(VARIABLE_FORMAT.apply(v), s);
                }
                refl.setFieldObject(field, o);
            }

        return refl.getObject();
    }

    /**
     * Gets the current variables in form of {@link Map}.
     *
     * @return the map
     */
    @NotNull Map<String, String> variables();

    @Override
    default @NotNull Iterator<String> iterator() {
        return variables().keySet().iterator();
    }
}
