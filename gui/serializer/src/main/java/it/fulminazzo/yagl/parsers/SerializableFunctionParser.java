package it.fulminazzo.yagl.parsers;

import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.fulmicollection.structures.tuples.Singlet;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import it.fulminazzo.fulmicollection.utils.SerializeUtils;
import it.fulminazzo.yagl.SerializableFunction;
import it.fulminazzo.yagl.utils.ParserUtils;
import it.fulminazzo.yamlparser.configuration.ConfigurationSection;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.YAMLParser;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;

/**
 * A parser to serialize a general {@link SerializableFunction}.
 *
 * @param <F> the type of the {@link SerializableFunction}
 */
@SuppressWarnings("unchecked")
public class SerializableFunctionParser<F extends SerializableFunction> extends YAMLParser<F> {

    /**
     * Instantiates a new Serializable function parser.
     *
     * @param clazz the {@link SerializableFunction} class
     */
    public SerializableFunctionParser(final @NotNull Class<F> clazz) {
        super(clazz);
    }

    @Override
    protected BiFunctionException<IConfiguration, String, F> getLoader() {
        return (c, s) -> {
            ConfigurationSection section = c.getConfigurationSection(s);
            if (section == null) return null;
            // Get the type
            String type = section.getString("type");
            if (type == null) throw new IllegalArgumentException("'type' cannot be null");
            Class<?> clazz = typeToClass(getOClass(), type);
            // Get content
            String content = section.getString("content");
            try {
                Singlet<Object> object;
                if (content == null) {
                    Constructor<?> constructor = clazz.getConstructor();
                    object = ReflectionUtils.setAccessible(constructor).map(Constructor::newInstance);
                } else {
                    Constructor<?> constructor = clazz.getConstructor(String.class);
                    object = ReflectionUtils.setAccessible(constructor).map(o -> o.newInstance(content));
                }
                return (F) object.getValue();
            } catch (NoSuchMethodException e) {
                return SerializeUtils.deserializeFromBase64(content);
            }
        };
    }

    @Override
    protected TriConsumer<IConfiguration, String, F> getDumper() {
        return (c, s, f) -> {
            c.set(s, null);
            if (f == null) return;
            c.set(s + ".type", ParserUtils.classToType(getOClass(), (Class<? extends F>) f.getClass()));
            c.set(s + ".content", f.serialize());
        };
    }

    /**
     * Converts the given type to the corresponding class.
     *
     * @param <C>       the type of the class
     * @param mainClass the main class
     * @param type      the type
     * @return the class
     */
    protected static <C> Class<? extends C> typeToClass(final Class<C> mainClass, final @NotNull String type) {
        try {
            return ParserUtils.typeToClass(mainClass, type);
        } catch (RuntimeException e) {
            return mainClass;
        }
    }

}
