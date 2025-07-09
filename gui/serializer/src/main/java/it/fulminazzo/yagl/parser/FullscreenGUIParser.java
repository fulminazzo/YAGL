package it.fulminazzo.yagl.parser;

import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import it.fulminazzo.yagl.content.GUIContent;
import it.fulminazzo.yagl.gui.FullscreenGUI;
import it.fulminazzo.yagl.gui.GUI;
import it.fulminazzo.yagl.gui.ResizableGUI;
import it.fulminazzo.yagl.gui.SearchGUI;
import it.fulminazzo.yagl.util.ParserUtils;
import it.fulminazzo.yamlparser.configuration.ConfigurationSection;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.YAMLParser;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A parser to serialize {@link FullscreenGUI}
 */
public class FullscreenGUIParser extends YAMLParser<FullscreenGUI> {

    /**
     * Instantiates a new Full size gui parser.
     */
    public FullscreenGUIParser() {
        super(FullscreenGUI.class);
    }

    @Override
    protected @NotNull BiFunctionException<IConfiguration, String, FullscreenGUI, Exception> getLoader() {
        return (c, s) -> {
            ConfigurationSection section = c.getConfigurationSection(s);
            if (section == null) return null;

            final String previousType = section.getString("type");

            final String guiType = section.getString("upper-gui-type");
            if (guiType == null) throw new IllegalArgumentException("'upper-gui-type' cannot be null");
            section.set("type", guiType);

            final GUI upperGUI = c.get(s, GUI.class).clear();
            Refl<?> upperGUIRefl = new Refl<>(upperGUI);
            Set<Integer> upperGUIMovableSlots = upperGUIRefl.getFieldObject("movableSlots");
            upperGUIMovableSlots.removeIf(i -> i >= upperGUI.size());

            final FullscreenGUI gui;

            // Section for SearchGUI
            final String searchFullscreenGUIName = SearchGUI.class.getCanonicalName() + ".SearchFullscreenGUI";
            Class<? extends FullscreenGUI> searchFullscreenGUI = ReflectionUtils.getClass(searchFullscreenGUIName);
            if (ParserUtils.classToType(GUI.class, searchFullscreenGUI).equals(previousType))
                gui = new Refl<FullscreenGUI>(searchFullscreenGUIName).getObject();
            else gui = new Refl<>(GUI.newFullscreenGUI(9))
                    .setFieldObject("upperGUI", upperGUI)
                    .getObject();

            Integer lowerGuiSize = section.getInteger("lower-gui-size");
            if (lowerGuiSize != null)
                gui.getLowerGUI().resize(lowerGuiSize);

            ConfigurationSection contents = section.getConfigurationSection("contents");
            if (contents != null)
                for (String key : contents.getKeys(false)) {
                    int slot = Integer.parseInt(key);
                    List<GUIContent> guiContents = contents.getList(key, GUIContent.class);
                    if (guiContents != null) gui.setContents(slot, guiContents);
                }

            List<Integer> movableSlots = section.getList("movable-slots", Integer.class);
            if (movableSlots != null)
                movableSlots.forEach(i -> gui.setMovable(i, true));

            section.set("type", previousType);
            return gui;
        };
    }

    @SuppressWarnings("unchecked")
    @Override
    protected @NotNull TriConsumer<IConfiguration, String, FullscreenGUI> getDumper() {
        return (c, s, g) -> {
            c.set(s, null);
            if (g == null) return;
            GUI upperGUI = g.getUpperGUI();
            ResizableGUI lowerGUI = g.getLowerGUI();
            c.set(s, upperGUI);

            ConfigurationSection section = Objects.requireNonNull(c.getConfigurationSection(s));

            g.getFullContents().forEach((k, v) -> {
                if (!v.isEmpty()) section.setList("contents." + k, v);
            });

            section.set("upper-gui-type", section.getString("type"));
            section.set("movable-slots", Stream.concat(
                    ((Set<Integer>) new Refl<>(upperGUI).getFieldObject("movableSlots")).stream(),
                    ((Set<Integer>) new Refl<>(lowerGUI).getFieldObject("movableSlots")).stream()
                            .map(i -> i + upperGUI.size())
            ).collect(Collectors.toList()));
            section.set("lower-gui-size", lowerGUI.size());
            section.set("type", ParserUtils.classToType(GUI.class, g.getClass()));
        };
    }

}
