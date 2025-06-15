package it.fulminazzo.yagl.parsers;

import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yagl.contents.GUIContent;
import it.fulminazzo.yagl.guis.FullSizeGUI;
import it.fulminazzo.yagl.guis.GUI;
import it.fulminazzo.yagl.utils.ParserUtils;
import it.fulminazzo.yamlparser.configuration.ConfigurationSection;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.YAMLParser;

import java.util.ArrayList;
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

            final String guiType = section.getString("gui-type");
            if (guiType == null) throw new IllegalArgumentException("'gui-type' cannot be null");
            section.set("type", guiType);

            List<GUIContent> contents = section.getList("contents", GUIContent.class);
            section.set("contents", new ArrayList<>());

            final GUI upperGUI = c.get(s, GUI.class).clear();

            final FullSizeGUI gui = new Refl<>(GUI.newFullSizeGUI(9))
                    .setFieldObject("upperGUI", upperGUI)
                    .getObject();

            if (contents != null) gui.addContent(contents.toArray(new GUIContent[0]));

            section.set("contents", contents);
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

            section.set("gui-type", section.getString("type"));
            section.set("type", ParserUtils.classToType(GUI.class, g.getClass()));
        };
    }

}
