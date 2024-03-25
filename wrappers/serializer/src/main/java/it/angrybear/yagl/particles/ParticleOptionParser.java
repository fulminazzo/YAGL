package it.angrybear.yagl.particles;

import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ClassUtils;
import it.fulminazzo.yamlparser.configuration.ConfigurationSection;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.YAMLParser;
import it.fulminazzo.yamlparser.parsers.annotations.PreventSaving;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;

/**
 * A parser to serialize a generic {@link ParticleOption} object.
 *
 * @param <P> the type parameter
 */
@SuppressWarnings("unchecked")
public class ParticleOptionParser<P extends ParticleOption<?>> extends YAMLParser<P> {

    /**
     * Instantiates a new Particle option parser.
     *
     * @param pClass the class of the {@link ParticleOption} to serialize
     */
    public ParticleOptionParser(@NotNull Class<P> pClass) {
        super(pClass);
    }

    @Override
    protected @NotNull BiFunctionException<@NotNull IConfiguration, @NotNull String, @Nullable P> getLoader() {
        return (c, s) -> {
            Refl<?> reflP = new Refl<>(getOClass(), new Object[0]);

            @NotNull List<Field> fields = getFields(reflP);
            if (fields.size() == 1) loadField(reflP, c, s, fields.get(0));
            else {
                ConfigurationSection section = c.getConfigurationSection(s);
                if (section != null) for (Field f : fields)
                    loadField(reflP, section, f.getName(), f);
            }

            return (P) reflP.getObject();
        };
    }

    private void loadField(@NotNull Refl<?> reflP, @NotNull IConfiguration section, @NotNull String path, @NotNull Field field) {
        Object object = section.get(path, field.getType());
        if (object instanceof String) {
            String string = object.toString();
            if (string.toLowerCase().matches("\\d+\\.\\d+f"))
                object = Float.valueOf(string.substring(0, string.length() - 1));
        }
        reflP.setFieldObject(field.getName(), object);
    }

    @Override
    protected @NotNull TriConsumer<@NotNull IConfiguration, @NotNull String, @Nullable P> getDumper() {
        return (c, s, p) -> {
            c.set(s, null);
            if (p == null) return;

            Refl<?> reflP = new Refl<>(p);
            @NotNull List<Field> fields = getFields(reflP);
            if (fields.size() == 1) saveField(reflP, c, s, fields.get(0));
            else {
                ConfigurationSection section = c.createSection(s);
                for (Field f : fields)
                    saveField(reflP, section, f.getName(), f);
            }
        };
    }

    /**
     * Adds all the parsers in the {@link it.angrybear.yagl.particles} package as {@link ParticleOptionParser}s.
     */
    public static void addAllParsers() {
        @NotNull Set<Class<?>> classes = ClassUtils.findClassesInPackage(ParticleOption.class.getPackage().getName());
        for (Class<?> clazz : classes)
            if (!clazz.equals(ParticleOption.class) && ParticleOption.class.isAssignableFrom(clazz))
                FileConfiguration.addParsers(new ParticleOptionParser<>((Class<? extends ParticleOption<?>>) clazz));
    }

    private void saveField(@NotNull Refl<?> reflP, @NotNull IConfiguration section, @NotNull String path, @NotNull Field field) {
        Object object = reflP.getFieldObject(field);
        if (object instanceof Float) object = object + "f";
        section.set(path, object);
    }

    private @NotNull List<Field> getFields(@NotNull Refl<?> reflP) {
        return reflP.getFields(f -> !Modifier.isStatic(f.getModifiers()) && !f.isAnnotationPresent(PreventSaving.class));
    }
}
