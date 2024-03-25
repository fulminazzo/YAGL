package it.angrybear.yagl.particles;

import it.angrybear.yagl.utils.ParserUtils;
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

@SuppressWarnings({"rawtypes", "unchecked"})
public class ParticleOptionParser<P extends ParticleOption<?>> extends YAMLParser<P> {

    public ParticleOptionParser(@NotNull Class<P> pClass) {
        super(pClass);
    }

    @Override
    protected BiFunctionException<@NotNull IConfiguration, @NotNull String, @Nullable P> getLoader() {
        return (c, s) -> {
            ConfigurationSection section = c.getConfigurationSection(s);
            if (section == null) return null;
            String type = section.getString("type");
            if (type == null) throw new NullPointerException("'type' cannot be null");
            Class<?> clazz = ParserUtils.typeToClass(ParticleOption.class, type);
            Refl<?> reflP = new Refl<>(clazz, new Object[0]);

            @NotNull List<Field> fields = getFields(reflP);
            if (fields.size() == 1)
                loadField(reflP, section, "contents", fields.get(0));
            else {
                ConfigurationSection contentsSection = section.getConfigurationSection("contents");
                if (contentsSection != null) for (Field f : fields)
                    loadField(reflP, contentsSection, f.getName(), f);
            }

            return (P) reflP.getObject();
        };
    }

    private void loadField(Refl<?> reflP, ConfigurationSection section, String path, Field field) {
        Object object = section.get(path, field.getType());
        if (object instanceof String) {
            String string = object.toString();
            if (string.toLowerCase().matches("\\d+\\.\\d+f"))
                object = Float.valueOf(string.substring(0, string.length() - 1));
        }
        reflP.setFieldObject(field.getName(), object);
    }

    @Override
    protected TriConsumer<@NotNull IConfiguration, @NotNull String, @Nullable P> getDumper() {
        return (c, s, p) -> {
            c.set(s, null);
            if (p == null) return;
            ConfigurationSection section = c.createSection(s);
            String type = ParserUtils.classToType(ParticleOption.class, p.getClass());
            section.set("type", type);

            Refl<?> reflP = new Refl<>(p);
            @NotNull List<Field> fields = getFields(reflP);
            if (fields.size() == 1)
                saveField(reflP, section, "contents", fields.get(0));
            else {
                ConfigurationSection contentsSection = section.createSection("contents");
                for (Field f : fields)
                    saveField(reflP, contentsSection, f.getName(), f);
            }
        };
    }

    public static void addAllParsers() {
        @NotNull Set<Class<?>> classes = ClassUtils.findClassesInPackage(ParticleOption.class.getPackage().getName());
        for (Class<?> clazz : classes)
            if (!clazz.equals(ParticleOption.class) && ParticleOption.class.isAssignableFrom(clazz))
                FileConfiguration.addParsers(new ParticleOptionParser<>((Class<? extends ParticleOption>) clazz));
    }

    private void saveField(Refl<?> reflP, ConfigurationSection section, String path, Field field) {
        Object object = reflP.getFieldObject(field);
        if (object instanceof Float) object = object + "f";
        section.set(path, object);
    }

    private List<Field> getFields(Refl<?> reflP) {
        return reflP.getFields(f -> !Modifier.isStatic(f.getModifiers()) && !f.isAnnotationPresent(PreventSaving.class));
    }
}
