package it.angrybear.yagl.actions;

import it.angrybear.yagl.parsers.CommandActionParser;
import it.fulminazzo.fulmicollection.utils.ClassUtils;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Modifier;
import java.util.Set;

public class ActionParsers {

    public static void addParsers() {
        final String packageName = ActionParsers.class.getPackage().getName();
        try {
            Class<?> commandAction = Class.forName(packageName + ".CommandAction");
            @NotNull Set<Class<?>> classes = ClassUtils.findClassesInPackage(packageName);
            for (Class<?> clazz : classes)
                if (!Modifier.isAbstract(clazz.getModifiers()) && commandAction.isAssignableFrom(clazz))
                    FileConfiguration.addParsers(new CommandActionParser<>(clazz));
        } catch (ClassNotFoundException ignored) {}
    }
}
