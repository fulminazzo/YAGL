package it.angrybear.yagl.parsers;

import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ClassUtils;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.CallableYAMLParser;
import it.fulminazzo.yamlparser.utils.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * This parser wraps acts as a bridge for common GUI elements (like {@link it.angrybear.yagl.guis.GUI} and {@link it.angrybear.yagl.contents.GUIContent})
 * that use a 'type' field to recognize the actual type of the object.
 *
 * @param <C> the type parameter
 */
abstract class GUIElementParser<C> extends CallableYAMLParser<C> {

    /**
     * Instantiates a new Gui element parser.
     *
     * @param clazz the clazz
     */
    public GUIElementParser(final @NotNull Class<C> clazz) {
        super(clazz, (c) -> {
            String type = c.getString("type");
            if (type == null) throw new NullPointerException("'type' cannot be null");
            Class<? extends C> clz = typeToClass(clazz, type);
            return new Refl<>(clz, new Object[0]).getObject();
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    protected @NotNull TriConsumer<@NotNull IConfiguration, @NotNull String, @Nullable C> getDumper() {
        return (c, s, g) -> {
            super.getDumper().accept(c, s, g);
            if (g == null) return;
            c.set(s + ".type", classToType(getOClass(), (Class<? extends C>) g.getClass()));
        };
    }

    @SuppressWarnings("unchecked")
    protected static <C> Class<? extends C> typeToClass(final Class<C> mainClass, final @NotNull String type) {
        String packageName = mainClass.getPackage().getName();
        final @NotNull Set<Class<?>> classes = ClassUtils.findClassesInPackage(packageName);
        for (Class<?> clazz : classes)
            if (!mainClass.equals(clazz) && mainClass.isAssignableFrom(clazz)) {
                Class<? extends C> guiClazz = (Class<? extends C>) clazz;
                if (classToType(mainClass, guiClazz).equals(type)) return guiClazz;
            }
        throw new RuntimeException(String.format("Could not find corresponding %s class from type '%s'",
                mainClass.getSimpleName(), type));
    }

    protected static <C> String classToType(final Class<C> mainClass, final @NotNull Class<? extends C> clazz) {
        final String mainClassName = mainClass.getSimpleName();
        String name = clazz.getSimpleName();
        if (name.contains("$")) name = mainClassName;
        else if (name.endsWith(mainClassName)) name = name.substring(0, name.length() - mainClassName.length());
        name = FileUtils.formatStringToYaml(name);
        return name.replace("-", "_").toUpperCase();
    }
}
