package it.fulminazzo.yagl.parsers;

import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import it.fulminazzo.yagl.contents.GUIContent;
import it.fulminazzo.yagl.guis.FullSizeGUI;
import it.fulminazzo.yagl.guis.GUI;
import it.fulminazzo.yagl.guis.SearchGUI;
import it.fulminazzo.yagl.utils.ParserUtils;
import it.fulminazzo.yamlparser.configuration.ConfigurationSection;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.YAMLParser;

import java.util.List;

/**
 * A parser to serialize {@link FullSizeGUI}
 */
public class FullSizeGUIParser extends YAMLParser<FullSizeGUI> {

    /**
     * Instantiates a new Full size gui parser.
     *
     */
    public FullSizeGUIParser() {
        super(FullSizeGUI.class);
    }

    @Override
    protected BiFunctionException<IConfiguration, String, FullSizeGUI, Exception> getLoader() {
        return (c, s) -> {
            ConfigurationSection section = c.getConfigurationSection(s);
            if (section == null) return null;

            final String previousType = section.getString("type");

            final String guiType = section.getString("upper-gui-type");
            if (guiType == null) throw new IllegalArgumentException("'upper-gui-type' cannot be null");
            section.set("type", guiType);

            final GUI upperGUI = c.get(s, GUI.class).clear();

            final FullSizeGUI gui;

            // Section for SearchGUI
            final String searchFullSizeGUIName = SearchGUI.class.getCanonicalName() + ".SearchFullSizeGUI";
            Class<? extends FullSizeGUI> searchFullSizeGUI = ReflectionUtils.getClass(searchFullSizeGUIName);
            if (ParserUtils.classToType(GUI.class, searchFullSizeGUI).equals(previousType))
                gui = new Refl<FullSizeGUI>(searchFullSizeGUIName).getObject();
            else gui = new Refl<>(GUI.newFullSizeGUI(9))
                    .setFieldObject("upperGUI", upperGUI)
                    .getObject();

            ConfigurationSection contents = section.getConfigurationSection("contents");
            if (contents != null)
                for (String key : contents.getKeys(false)) {
                    int slot = Integer.parseInt(key);
                    List<GUIContent> guiContents = contents.getList(key, GUIContent.class);
                    if (guiContents != null) gui.setContents(slot, guiContents);
                }

            section.set("type", previousType);
            return gui;
        };
    }

    @Override
    protected TriConsumer<IConfiguration, String, FullSizeGUI> getDumper() {
        return (c, s, g) -> {
            c.set(s, null);
            if (g == null) return;
            c.set(s, g.getUpperGUI());

            ConfigurationSection section = c.getConfigurationSection(s);

            g.getFullContents().forEach((k, v) -> {
                if (!v.isEmpty()) section.setList("contents." + k, v);
            });

            section.set("upper-gui-type", section.getString("type"));
            section.set("type", ParserUtils.classToType(GUI.class, g.getClass()));
        };
    }

}
