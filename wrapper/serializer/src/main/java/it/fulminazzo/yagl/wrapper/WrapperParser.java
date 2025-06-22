package it.fulminazzo.yagl.wrapper;

import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ClassUtils;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.YAMLParser;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;

/**
 * A parser to serialize a generic {@link Wrapper} object.
 *
 * @param <W> the type parameter
 */
@SuppressWarnings("unchecked")
public class WrapperParser<W extends Wrapper> extends YAMLParser<W> {

    /**
     * Instantiates a new Wrapper parser.
     *
     * @param clazz the class of the {@link Wrapper} to serialize
     */
    public WrapperParser(@NotNull Class<W> clazz) {
        super(clazz);
    }

    @Override
    protected BiFunctionException<IConfiguration, String, W, Exception> getLoader() {
        return (c, s) -> {
            String raw = c.getString(s);
            if (raw == null || raw.trim().isEmpty()) return null;
            else return parseWrapperFromString(raw, getOClass());
        };
    }

    /**
     * Converts the given string to an instance of the given wrapper class by using the most appropriate constructor.
     *
     * @param <W>   the type of the wrapper
     * @param raw   the string to convert from
     * @param clazz the class of the wrapper
     * @return the wrapper
     * @throws NoSuchMethodException an exception thrown in case the constructor cannot be found
     */
    public static <W extends Wrapper> @NotNull W parseWrapperFromString(final @NotNull String raw, final @NotNull Class<W> clazz) throws NoSuchMethodException {
        String[] rawData = raw.split(":");
        Constructor<W> constructor = findConstructorFromRaw(rawData, clazz);
        Object[] parameters = initializeParameters(rawData, constructor);
        return new Refl<>(clazz, parameters).getObject();
    }

    private static <W extends Wrapper> @NotNull Constructor<W> findConstructorFromRaw(final String @NotNull [] rawData, final @NotNull Class<W> clazz) throws NoSuchMethodException {
        Constructor<W> constructor = (Constructor<W>) Arrays.stream(clazz.getConstructors())
                .filter(t -> t.getParameterCount() <= rawData.length)
                .min(Comparator.comparing(t -> -t.getParameterCount())).orElse(null);
        if (constructor == null) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < rawData.length; i++) builder.append("?, ");
            throw new NoSuchMethodException(String.format("Could not find method %s(%s)",
                    clazz.getSimpleName(), builder.substring(0, Math.max(0, builder.length() - 2))));
        }
        return constructor;
    }

    private static <W extends Wrapper> Object @NotNull [] initializeParameters(final String @NotNull [] rawData,
                                                                               final @NotNull Constructor<W> constructor) {
        Object[] parameters = new Object[rawData.length];
        Class<?>[] types = constructor.getParameterTypes();
        for (int i = 0; i < types.length; i++) {
            Class<?> type = types[i];
            if (!ReflectionUtils.isPrimitiveOrWrapper(type))
                throw new IllegalArgumentException(String.format("Cannot parse type %s", type.getCanonicalName()));
            else {
                type = ReflectionUtils.getWrapperClass(type);
                String t = rawData[i];
                if (type.equals(String.class)) parameters[i] = t;
                else parameters[i] = new Refl<>(type).invokeMethod("valueOf", t);
            }
        }
        return parameters;
    }

    @Override
    protected TriConsumer<IConfiguration, String, W> getDumper() {
        return (c, s, w) -> {
            c.set(s, null);
            if (w == null) return;
            StringBuilder tmp = new StringBuilder();
            Refl<?> wRefl = new Refl<>(w);
            for (Field field : wRefl.getNonStaticFields()) {
                Object o = wRefl.getFieldObject(field);
                String result = o == null ? "" : o.toString();
                if (o instanceof String) result = result.toLowerCase();
                tmp.append(result).append(":");
            }
            c.set(s, tmp.substring(0, Math.max(0, tmp.length() - 1)));
        };
    }

    /**
     * Adds all the parsers in the {@link it.fulminazzo.yagl.wrapper} package as {@link WrapperParser}s.
     */
    public static void addAllParsers() {
        @NotNull Set<Class<?>> classes = ClassUtils.findClassesInPackage(Wrapper.class.getPackage().getName());
        for (Class<?> clazz : classes)
            if (!clazz.equals(Wrapper.class) && Wrapper.class.isAssignableFrom(clazz))
                FileConfiguration.addParsers(new WrapperParser<>((Class<? extends Wrapper>) clazz));
    }
}
