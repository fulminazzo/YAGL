package it.fulminazzo.yagl.util;

import it.fulminazzo.yagl.reflect.Reflect;
import it.fulminazzo.yagl.reflect.ReflectException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A collection of utilities to work with enums.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnumUtils {
    private static final @NotNull Pattern UPPERCASE_REGEX = Pattern.compile("[A-Z]");

    /**
     * Uses the given class method <code>valueOf</code> to obtain the corresponding value with the given name.
     * If none is found, an {@link IllegalArgumentException} is thrown.
     *
     * @param <E>       the type parameter
     * @param enumClass the enum class
     * @param name      the name
     * @return the value
     */
    public static <E> @NotNull E valueOf(final @NotNull Class<E> enumClass, final @NotNull String name) {
        return valueOf(enumClass, name, "valueOf");
    }

    /**
     * Uses the given methods from the class to obtain the corresponding value with the given name.
     * If none is found, an {@link IllegalArgumentException} is thrown.
     *
     * @param <E>         the type parameter
     * @param enumClass   the enum class
     * @param name        the name
     * @param methodNames the method names
     * @return the value
     */
    public static <E> @NotNull E valueOf(final @NotNull Class<E> enumClass, final @NotNull String name, final String @NotNull ... methodNames) {
        final Reflect reflect = Reflect.on(enumClass);
        for (final String methodName : methodNames) {
            final Method method;
            try {
                method = reflect.getStaticMethod(enumClass, methodName, String.class);
            } catch (ReflectException e) {
                // Method was not found, move on
                continue;
            }
            try {
                E value = reflect.invoke(method, name.toUpperCase()).get();
                if (value != null) return value;
            } catch (ReflectException e) {
                Throwable cause = e.getCause();
                if (cause instanceof RuntimeException) throw (RuntimeException) cause;
                else throw e;
            }
        }
        final @NotNull String typeName = getTypeName(enumClass);
        throw new IllegalArgumentException(String.format("Could not find %s '%s'", typeName.toLowerCase(), name));
    }

    private static @NotNull String getTypeName(final @NotNull Class<?> clazz) {
        String typeName = clazz.getSimpleName();
        Matcher matcher = UPPERCASE_REGEX.matcher(typeName);
        while (matcher.find()) typeName = typeName.replaceAll(matcher.group(), " " + matcher.group());
        if (typeName.startsWith(" ")) typeName = typeName.substring(1);
        return typeName.toLowerCase();
    }

}
