package it.angrybear.yagl.parsers;

import it.angrybear.yagl.guis.GUI;
import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ClassUtils;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.CallableYAMLParser;
import it.fulminazzo.yamlparser.utils.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class GUIParser extends CallableYAMLParser<GUI> {

    public GUIParser() {
        super(GUI.class, (c) -> {
            String type = c.getString("type");
            if (type == null) throw new NullPointerException("'type' cannot be null");
            Class<? extends GUI> clazz = typeToContentClass(type);
            Refl<? extends GUI> gui = new Refl<>(clazz, new Object[0]);
            return gui.getObject();
        });
    }

    @Override
    protected @NotNull BiFunctionException<@NotNull IConfiguration, @NotNull String, @Nullable GUI> getLoader() {
        return (c, s) -> {
            GUI g = super.getLoader().apply(c, s);
            Integer size = c.getInteger(s + ".size");
            if (size == null) throw new NullPointerException("'size' cannot be null");
            Refl<GUI> gui = new Refl<>(g);
            gui.setFieldObject("contents", gui.invokeMethod("createContents",
                    new Class[]{int.class, List.class},
                    size, gui.getFieldObject("contents")));
            return g;
        };
    }

    @Override
    protected @NotNull TriConsumer<@NotNull IConfiguration, @NotNull String, @Nullable GUI> getDumper() {
        return (c, s, g) -> {
            super.getDumper().accept(c, s, g);
            if (g == null) return;
            c.set(s + ".type", contentClassToType(g.getClass()));
            c.set(s + ".size", g.getSize());
        };
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends GUI> typeToContentClass(final @NotNull String type) {
        Class<?> mainClass = GUI.class;
        String packageName = mainClass.getPackage().getName();
        final @NotNull Set<Class<?>> classes = ClassUtils.findClassesInPackage(packageName);
        for (Class<?> clazz : classes)
            if (!mainClass.equals(clazz) && mainClass.isAssignableFrom(clazz)) {
                Class<? extends GUI> guiClazz = (Class<? extends GUI>) clazz;
                if (contentClassToType(guiClazz).equals(type)) return guiClazz;
            }
        throw new RuntimeException(String.format("Could not find corresponding %s class from type '%s'",
                GUI.class.getSimpleName(), type));
    }

    private static String contentClassToType(final @NotNull Class<? extends GUI> clazz) {
        String name = clazz.getSimpleName();
        final String guiContent = GUI.class.getSimpleName();
        if (name.endsWith(guiContent)) name = name.substring(0, name.length() - guiContent.length());
        name = FileUtils.formatStringToYaml(name);
        return name.replace("-", "_").toUpperCase();
    }
}
