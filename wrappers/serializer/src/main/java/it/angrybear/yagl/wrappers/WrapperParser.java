package it.angrybear.yagl.wrappers;

import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ClassUtils;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.YAMLParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    public WrapperParser(Class<W> clazz) {
        super(clazz);
    }

    @Override
    protected BiFunctionException<@NotNull IConfiguration, @NotNull String, @Nullable W> getLoader() {
        return (c, s) -> {
            String raw = c.getString(s);
            if (raw == null || raw.trim().isEmpty()) return null;
            String[] tmp = raw.split(":");
            Constructor<W> constructor = (Constructor<W>) Arrays.stream(getOClass().getConstructors())
                    .filter(t -> t.getParameterCount() <= tmp.length)
                    .min(Comparator.comparing(t -> -t.getParameterCount())).orElse(null);
            if (constructor == null) {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < tmp.length; i++) builder.append("?, ");
                throw new NoSuchMethodException(String.format("Could not find method %s(%s)",
                        getOClass().getSimpleName(), builder.substring(0, Math.max(0, builder.length() - 2))));
            }

            Object[] parameters = new Object[tmp.length];

            Class<?>[] types = constructor.getParameterTypes();
            for (int i = 0; i < types.length; i++) {
                Class<?> type = types[i];
                if (!ReflectionUtils.isPrimitiveOrWrapper(type))
                    throw new IllegalArgumentException(String.format("Cannot parse type %s", type.getCanonicalName()));
                type = ReflectionUtils.getWrapperClass(type);
                String t = tmp[i];
                if (type.equals(String.class)) parameters[i] = t;
                else parameters[i] = new Refl<>(type).invokeMethod("valueOf", t);
            }

            return new Refl<>(getOClass(), parameters).getObject();
        };
    }

    @Override
    protected TriConsumer<@NotNull IConfiguration, @NotNull String, @Nullable W> getDumper() {
        return (c, s, w) -> {
            c.set(s, null);
            if (w == null) return;
            StringBuilder tmp = new StringBuilder();
            Refl<?> wRefl = new Refl<>(w);
            for (Field field : wRefl.getNonStaticFields()) {
                Object o = wRefl.getFieldObject(field);
                tmp.append(o == null ? "" : o.toString()).append(":");
            }
            c.set(s, tmp.substring(0, Math.max(0, tmp.length() - 1)));
        };
    }

    /**
     * Adds all the parsers in the {@link it.angrybear.yagl.wrappers} package as {@link WrapperParser}s.
     */
    public static void addAllParsers() {
        @NotNull Set<Class<?>> classes = ClassUtils.findClassesInPackage(Wrapper.class.getPackage().getName());
        for (Class<?> clazz : classes)
            if (!clazz.equals(Wrapper.class) && Wrapper.class.isAssignableFrom(clazz))
                FileConfiguration.addParsers(new WrapperParser<>((Class<? extends Wrapper>) clazz));
    }
}
