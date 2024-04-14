package it.angrybear.yagl.parsers;

import it.angrybear.yagl.contents.GUIContent;
import it.angrybear.yagl.guis.GUI;
import it.angrybear.yagl.guis.PageableGUI;
import it.angrybear.yagl.utils.ParserUtils;
import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.structures.Tuple;
import it.fulminazzo.yamlparser.configuration.ConfigurationSection;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.YAMLParser;

import java.util.Map;

@SuppressWarnings("DataFlowIssue")
public class PageableGUIParser extends YAMLParser<PageableGUI> {

    public PageableGUIParser() {
        super(PageableGUI.class);
    }

    @Override
    protected BiFunctionException<IConfiguration, String, PageableGUI> getLoader() {
        return (c, s) -> {
            ConfigurationSection section = c.getConfigurationSection(s);
            if (section == null) return null;
            Integer size = section.getInteger("size");
            if (size == null) throw new IllegalArgumentException("'size' cannot be null");
            PageableGUI gui = PageableGUI.newGUI(size);

            section.set("type", section.getString("gui-type"));
            GUI templateGUI = c.get(s, GUI.class);
            if (templateGUI == null)
                throw new IllegalArgumentException("Cannot properly load template GUI");
            new Refl<>(gui).setFieldObject("templateGUI", templateGUI);
            templateGUI.variables().clear();

            ConfigurationSection previousPage = section.getConfigurationSection("previous-page");
            if (previousPage != null)
                gui.setPreviousPage(previousPage.getInteger("slot"),
                        previousPage.get("content", GUIContent.class));

            ConfigurationSection nextPage = section.getConfigurationSection("next-page");
            if (nextPage != null)
                gui.setNextPage(nextPage.getInteger("slot"),
                        nextPage.get("content", GUIContent.class));

            Map<String, String> variables = section.get("variables", Map.class);
            if (variables != null) gui.variables().putAll(variables);

            return gui;
        };
    }

    @Override
    protected TriConsumer<IConfiguration, String, PageableGUI> getDumper() {
        return (c, s, p) -> {
            c.set(s, null);
            if (p == null) return;
            c.set(s, new Refl<>(p).getFieldObject("templateGUI"));
            ConfigurationSection section = c.getConfigurationSection(s);
            section.set("gui-type", section.getString("type"));
            section.set("type", ParserUtils.classToType(GUI.class, getOClass()));
            section.set("size", p.size());

            Refl<PageableGUI> refl = new Refl<>(p);

            Tuple<Integer, GUIContent> previousPage = refl.getFieldObject("previousPage");
            previousPage.ifPresent((i, g) -> {
                ConfigurationSection previousSection = section.createSection("previous-page");
                previousSection.set("slot", i);
                previousSection.set("content", g);
            });

            Tuple<Integer, GUIContent> nextPage = refl.getFieldObject("nextPage");
            nextPage.ifPresent((i, g) -> {
                ConfigurationSection nextSection = section.createSection("next-page");
                nextSection.set("slot", i);
                nextSection.set("content", g);
            });

            section.set("variables", p.variables());
        };
    }
}
