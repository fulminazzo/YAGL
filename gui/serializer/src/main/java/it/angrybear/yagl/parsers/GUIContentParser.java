package it.angrybear.yagl.parsers;

import it.angrybear.yagl.contents.GUIContent;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ClassUtils;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.CallableYAMLParser;
import it.fulminazzo.yamlparser.utils.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class GUIContentParser extends CallableYAMLParser<GUIContent> {

    public GUIContentParser() {
        super(GUIContent.class, (c) -> {
            String type = c.getString("type");
            if (type == null) throw new NullPointerException("'type' cannot be null");
            Class<? extends GUIContent> clazz = typeToContentClass(type);
            return new Refl<>(clazz, new Object[0]).getObject();
        });
    }

    @Override
    protected @NotNull TriConsumer<@NotNull IConfiguration, @NotNull String, @Nullable GUIContent> getDumper() {
        return (c, s, g) -> {
            super.getDumper().accept(c, s, g);
            if (g == null) return;
            c.set(s + ".type", contentClassToType(g.getClass()));
        };
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends GUIContent> typeToContentClass(final @NotNull String type) {
        Class<?> mainClass = GUIContent.class;
        String packageName = mainClass.getPackage().getName();
        final @NotNull Set<Class<?>> classes = ClassUtils.findClassesInPackage(packageName);
        for (Class<?> clazz : classes)
            if (!mainClass.equals(clazz) && mainClass.isAssignableFrom(clazz)) {
                Class<? extends GUIContent> guiClazz = (Class<? extends GUIContent>) clazz;
                if (contentClassToType(guiClazz).equals(type)) return guiClazz;
            }
        throw new RuntimeException(String.format("Could not find corresponding %s class from type '%s'",
                GUIContent.class.getSimpleName(), type));
    }

    private static String contentClassToType(final @NotNull Class<? extends GUIContent> clazz) {
        String name = clazz.getSimpleName();
        final String guiContent = GUIContent.class.getSimpleName();
        if (name.endsWith(guiContent)) name = name.substring(0, name.length() - guiContent.length());
        name = FileUtils.formatStringToYaml(name);
        return name.replace("-", "_").toUpperCase();
    }
}
