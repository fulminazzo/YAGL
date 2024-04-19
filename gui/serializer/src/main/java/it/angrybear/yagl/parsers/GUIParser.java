package it.angrybear.yagl.parsers;

import it.angrybear.yagl.guis.GUI;
import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import it.fulminazzo.yamlparser.configuration.ConfigurationSection;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.YAMLParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A parser to serialize {@link GUI}.
 */
public class GUIParser extends TypedParser<GUI> {

    /**
     * Instantiates a new Gui parser.
     */
    public GUIParser() {
        super(GUI.class);
    }

    @Override
    protected BiFunctionException<IConfiguration, String, GUI> getLoader() {
        return (c, s) -> {
            ConfigurationSection section = c.getConfigurationSection(s);
            if (section == null) return null;
            GUI tmp = new Refl<>(this).getFieldRefl("function").invokeMethod("apply", section);
            YAMLParser<? extends GUI> parser = getSpecificGUIParser(tmp);
            if (!parser.equals(this)) return parser.load(c, s);
            GUI g = super.getLoader().apply(c, s);
            Integer size = c.getInteger(s + ".size");
            if (size == null) throw new IllegalArgumentException("'size' cannot be null");
            Refl<GUI> gui = new Refl<>(g);
            Class<?> contentsClass = ReflectionUtils.getClass(GUI.class.getCanonicalName() + "Impl.Contents");
            @Nullable List<?> contents = c.getList(s + ".contents", contentsClass);
            gui.setFieldObject("contents", gui.invokeMethod("createContents",
                    new Class[]{int.class, List.class},
                    size, contents));
            return g;
        };
    }

    @Override
    protected TriConsumer<IConfiguration, String, GUI> getDumper() {
        return (c, s, g) -> {
            c.set(s, null);
            if (g == null) return;
            YAMLParser<GUI> parser = getSpecificGUIParser(g);
            if (!this.equals(parser)) parser.dump(c, s, g);
            else {
                super.getDumper().accept(c, s, g);
                final String valueClass = s + ".contents.value-class";
                if (c.contains(valueClass)) c.set(valueClass, null);
                c.set(s + ".size", g.size());
            }
        };
    }

    @SuppressWarnings("unchecked")
    private YAMLParser<GUI> getSpecificGUIParser(final @NotNull GUI gui) {
        FileConfiguration.removeParsers(this);
        YAMLParser<? extends @NotNull GUI> parser = FileConfiguration.getParser(gui.getClass());
        FileConfiguration.addParsers(this);
        if (parser == null || !getOClass().isAssignableFrom(parser.getOClass())) return this;
        return (YAMLParser<GUI>) parser;
    }

}
