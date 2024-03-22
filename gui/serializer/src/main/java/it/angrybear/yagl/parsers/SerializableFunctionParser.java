package it.angrybear.yagl.parsers;

import it.angrybear.yagl.SerializableFunction;
import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.fulmicollection.utils.SerializeUtils;
import it.fulminazzo.yamlparser.configuration.ConfigurationSection;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.YAMLParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;

@SuppressWarnings("unchecked")
public class SerializableFunctionParser<F extends SerializableFunction> extends YAMLParser<F> {

    public SerializableFunctionParser(@NotNull Class<F> clazz) {
        super(clazz);
    }

    @Override
    protected @NotNull BiFunctionException<@NotNull IConfiguration, @NotNull String, @Nullable F> getLoader() {
        return (c, s) -> {
            ConfigurationSection section = c.getConfigurationSection(s);
            if (section == null) return null;
            // Get type
            String type = section.getString("type");
            if (type == null) throw new NullPointerException("'type' cannot be null");
            Class<?> clazz = typeToClass(getOClass(), type);
            // Get content
            String content = section.getString("content");
            if (content == null) throw new NullPointerException("'content' cannot be null");
            try {
                Constructor<?> constructor = clazz.getConstructor(String.class);
                return (F) constructor.newInstance(content);
            } catch (NoSuchMethodException e) {
                return SerializeUtils.deserializeFromBase64(content);
            }
        };
    }

    @Override
    protected @NotNull TriConsumer<@NotNull IConfiguration, @NotNull String, @Nullable F> getDumper() {
        return (c, s, f) -> {
            c.set(s, null);
            if (f == null) return;
            c.set(s + ".type", GUIElementParser.classToType(getOClass(), (Class<? extends F>) f.getClass()));
            c.set(s + ".content", f.serialize());
        };
    }
    
    protected static <C> Class<? extends C> typeToClass(final Class<C> mainClass, final @NotNull String type) {
        try {
            return GUIElementParser.typeToClass(mainClass, type);
        } catch (RuntimeException e) {
            return mainClass;
        }
    }
}
