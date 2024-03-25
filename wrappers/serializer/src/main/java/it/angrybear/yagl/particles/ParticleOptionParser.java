package it.angrybear.yagl.particles;

import it.angrybear.yagl.utils.ParserUtils;
import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yamlparser.configuration.ConfigurationSection;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.YAMLParser;
import it.fulminazzo.yamlparser.parsers.annotations.PreventSaving;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

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
            if (fields.size() == 1) {
                Field field = fields.get(0);
                reflP.setFieldObject(field, section.get("contents", field.getType()));
            } else {
                ConfigurationSection contentsSection = section.getConfigurationSection("contents");
                if (contentsSection != null) for (Field f : fields)
                    reflP.setFieldObject(f, contentsSection.get(f.getName(), f.getType()));
            }

            return (P) reflP.getObject();
        };
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
                section.set("contents", reflP.getFieldObject(fields.get(0)));
            else {
                ConfigurationSection contentsSection = section.createSection("contents");
                for (Field f : fields)
                    contentsSection.set(f.getName(), reflP.getFieldObject(f));
            }
        };
    }

    private List<Field> getFields(Refl<?> reflP) {
        return reflP.getFields(f -> !Modifier.isStatic(f.getModifiers()) && !f.isAnnotationPresent(PreventSaving.class));
    }
}
