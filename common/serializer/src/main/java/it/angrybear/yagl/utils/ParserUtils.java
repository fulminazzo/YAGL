package it.angrybear.yagl.utils;

import it.fulminazzo.fulmicollection.utils.ClassUtils;
import it.fulminazzo.yamlparser.utils.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public final class ParserUtils {

    @SuppressWarnings("unchecked")
    public static <C> Class<? extends C> typeToClass(final Class<C> coreClass, final @NotNull String toConvert) {
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

    public static <C> String classToType(final Class<C> coreClass, final @NotNull Class<? extends C> toConvert) {
        final String mainClassName = coreClass.getSimpleName();
        String name = toConvert.getSimpleName();
        if (name.contains("$")) name = mainClassName;
        else if (name.endsWith(mainClassName)) name = name.substring(0, name.length() - mainClassName.length());
        name = FileUtils.formatStringToYaml(name);
        return name.replace("-", "_").toUpperCase();
    }
}
