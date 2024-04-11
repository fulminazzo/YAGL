package it.angrybear.yagl.utils;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ExceptionUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Enum utils.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnumUtils {

    /**
     * Uses the given class method <code>valueOf</code> to obtain the corresponding value with the given name.
     * If none is found, an {@link IllegalArgumentException} is thrown.
     *
     * @param <E>         the type parameter
     * @param enumClass   the enum class
     * @param name        the name
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
        final Refl<Class<E>> enumRefl = new Refl<>(enumClass);
        for (final String methodName : methodNames)
            try {
                E object = enumRefl.invokeMethod(enumClass, methodName, name.toUpperCase());
                if (object != null) return object;
            } catch (RuntimeException e) {
                Throwable throwable = ExceptionUtils.unwrapRuntimeException(e);
                if (throwable instanceof IllegalArgumentException) {
                    // Check if an IllegalArgumentException stating a not found is thrown.
                    // If so, continue code execution.
                    final String message = throwable.getMessage();
                    if (message != null && message.contains(name.toUpperCase())) continue;
                } else if (!(throwable instanceof RuntimeException)) throwable = new RuntimeException(throwable);
                throw (RuntimeException) throwable;
            }
        final @NotNull String typeName = getTypeName(enumClass);
        throw new IllegalArgumentException(String.format("Could not find %s '%s'", typeName.toLowerCase(), name));
    }

    private static @NotNull String getTypeName(final @NotNull Class<?> clazz) {
        String typeName = clazz.getSimpleName();
        Matcher matcher = Pattern.compile("[A-Z]").matcher(typeName);
        while (matcher.find()) typeName = typeName.replaceAll(matcher.group(), " " + matcher.group());
        if (typeName.startsWith(" ")) typeName = typeName.substring(1);
        return typeName.toLowerCase();
    }
}
