package it.fulminazzo.yagl.util;

import it.fulminazzo.fulmicollection.utils.ClassUtils;
import it.fulminazzo.yamlparser.utils.FileUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * The type Parser utils.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ParserUtils {

    /**
     * Converts the given string type to the corresponding class, based on the coreClass.
     * It gathers all the classes that extend <i>coreClass</i> in the same package.
     * Then, for each one of them, calls {@link #classToType(Class, Class)} and compares the result with the passed type.
     * If none matches, an {@link IllegalArgumentException} is thrown.
     *
     * @param <C>       the type parameter
     * @param coreClass the core class
     * @param toConvert the type to convert
     * @return the class
     */
    @SuppressWarnings("unchecked")
    public static <C> @NotNull Class<? extends C> typeToClass(final @NotNull Class<C> coreClass, final @NotNull String toConvert) {
        String packageName = coreClass.getPackage().getName();
        final @NotNull Set<Class<?>> classes = ClassUtils.findClassesInPackage(packageName);
        for (Class<?> clazz : classes)
            if (!coreClass.equals(clazz) && coreClass.isAssignableFrom(clazz)) {
                Class<? extends C> guiClazz = (Class<? extends C>) clazz;
                if (classToType(coreClass, guiClazz).equals(toConvert)) return guiClazz;
            }
        throw new IllegalArgumentException(String.format("Could not find corresponding %s class from type '%s'",
                coreClass.getSimpleName(), toConvert));
    }

    /**
     * Converts the given class to a corresponding type, based on the core class.
     * If the class to convert has the core class name in it, this will be stripped.
     *
     * @param <C>       the type parameter
     * @param coreClass the core class
     * @param toConvert the class to convert
     * @return the resulting type (in SCREAM_SNAKE_CASE)
     */
    public static <C> @NotNull String classToType(final @NotNull Class<C> coreClass, final @NotNull Class<? extends C> toConvert) {
        final String mainClassName = coreClass.getSimpleName();
        String name = toConvert.getSimpleName();
        if (name.startsWith(mainClassName)) name = name.substring(mainClassName.length());
        if (name.endsWith(mainClassName)) name = name.substring(0, name.length() - mainClassName.length());
        if (name.equals("Impl")) name = "default";
        name = FileUtils.formatStringToYaml(name);
        return name.replace("-", "_").toUpperCase();
    }

}
