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
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("DataFlowIssue")
public class PageableGUIParser extends YAMLParser<PageableGUI> {
    private static final String[] IGNORE_FIELDS = new String[]{"type", "size"};

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
            Integer pages = section.getInteger("pages");
            if (pages == null) throw new IllegalArgumentException("'pages' cannot be null");
            PageableGUI gui = PageableGUI.newGUI(size).setPages(pages);

            section.set("type", section.getString("gui-type"));
            GUI templateGUI = c.get(s, GUI.class);
            if (templateGUI == null)
                throw new IllegalArgumentException("Cannot properly load template GUI");
            Refl<PageableGUI> refl = new Refl<>(gui);
            refl.setFieldObject("templateGUI", templateGUI);
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

            final ConfigurationSection pagesSection = section.getConfigurationSection("gui-pages");
            loadPages(section, pagesSection, gui, IGNORE_FIELDS);

            return gui;
        };
    }

    private void loadPages(final @NotNull ConfigurationSection guiSection,
                           final @NotNull ConfigurationSection pagesSection,
                           final @NotNull PageableGUI gui,
                           final String @NotNull ... ignoreFields) {
        List<GUI> guiPages = new LinkedList<>();
        for (int i = 0; i < gui.pages(); i++) {
            final String path = String.valueOf(i);
            ConfigurationSection pageSection = pagesSection.getConfigurationSection(path);
            if (pageSection == null) guiPages.add(gui.getPage(i + 1));
            else {
                for (String s : ignoreFields) pageSection.set(s, guiSection.getObject(s));
                guiPages.add(pagesSection.get(path, GUI.class));
                for (String s : ignoreFields) pageSection.set(s, null);
            }
        }
        new Refl<>(gui).setFieldObject("pages", guiPages);
    }

    @Override
    protected TriConsumer<IConfiguration, String, PageableGUI> getDumper() {
        return (c, s, p) -> {
            c.set(s, null);
            if (p == null) return;
            Refl<PageableGUI> refl = new Refl<>(p);

            c.set(s, refl.getFieldObject("templateGUI"));
            ConfigurationSection section = c.getConfigurationSection(s);
            section.set("gui-type", section.getString("type"));
            section.set("type", ParserUtils.classToType(GUI.class, getOClass()));
            section.set("size", p.size());
            section.set("pages", p.pages());

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

            final ConfigurationSection pagesSection = section.createSection("gui-pages");
            dumpPages(pagesSection, p, IGNORE_FIELDS);
        };
    }

    private void dumpPages(final @NotNull ConfigurationSection pagesSection, final @NotNull PageableGUI gui,
                           final String @NotNull ... ignoreFields) {
        final List<GUI> guiPages = new Refl<>(gui).getFieldObject("pages");
        for (int i = 0; i < guiPages.size(); i++) {
            final GUI page = guiPages.get(i);
            final String path = String.valueOf(i);
            pagesSection.set(path, page);

            final ConfigurationSection pageSection = pagesSection.getConfigurationSection(path);

            for (String s : ignoreFields) pageSection.set(s, null);
            final Set<String> keys = pageSection.getKeys();
            for (String k : keys) {
                Object object = pageSection.getObject(k);
                if (object instanceof ConfigurationSection && ((ConfigurationSection) object).getKeys().isEmpty())
                    pageSection.set(k, null);
                else if (object instanceof List && ((List<?>) object).isEmpty())
                    pageSection.set(k, null);
            }

            if (pageSection.getKeys().isEmpty()) pagesSection.set(pageSection.getName(), null);
        }
    }
}
