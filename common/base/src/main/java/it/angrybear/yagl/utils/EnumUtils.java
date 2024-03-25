package it.angrybear.yagl.utils;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ExceptionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Enum utils.
 */
public class EnumUtils {//

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
        try {
            final Refl<Class<E>> enumRefl = new Refl<>(enumClass);
            E object = null;
            for (final String methodName : methodNames)
                try {
                    object = enumRefl.invokeMethod(enumClass, methodName, name.toUpperCase());
                    if (object != null) break;
                } catch (RuntimeException e) {
                    Throwable throwable = ExceptionUtils.unwrapRuntimeException(e);
                    if (!(throwable instanceof IllegalArgumentException)) ExceptionUtils.throwException(throwable);
                }
            if (object == null) throw new IllegalArgumentException();
            return object;
        } catch (IllegalArgumentException e) {
            String typeName = enumClass.getSimpleName();
            Matcher matcher = Pattern.compile("[A-Z]").matcher(typeName);
            while (matcher.find())
                typeName = typeName.replaceAll(matcher.group(), " " + matcher.group());
            while (typeName.startsWith(" ")) typeName = typeName.substring(1);
            throw new IllegalArgumentException(String.format("Could not find %s '%s'", typeName.toLowerCase(), name));
        }
    }
}
