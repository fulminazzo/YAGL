package it.fulminazzo.yagl.parser;

import it.fulminazzo.yagl.content.GUIContent;
import it.fulminazzo.yagl.gui.GUI;
import it.fulminazzo.yagl.gui.PageableGUI;
import it.fulminazzo.yagl.util.ParserUtils;
import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.structures.tuples.Tuple;
import it.fulminazzo.yamlparser.configuration.ConfigurationSection;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A parser to serialize {@link PageableGUI}.
 */
@SuppressWarnings("DataFlowIssue")
public class PageableGUIParser extends TypedParser<PageableGUI> {
    private static final String[] IGNORE_FIELDS = new String[]{"type", "size"};

    /**
     * Instantiates a new Pageable gui parser.
     */
    public PageableGUIParser() {
        super(PageableGUI.class);
    }

    @Override
    protected @NotNull BiFunctionException<IConfiguration, String, PageableGUI, Exception> getLoader() {
        return (c, s) -> {
            ConfigurationSection section = c.getConfigurationSection(s);
            if (section == null) return null;

            final String previousType = section.getString("type");
            try {
                Integer pages = section.getInteger("pages");
                if (pages == null) pages = 1;
                final PageableGUI gui = (PageableGUI) getObjectFromType(GUI.class, section, (Object) null);

                final Refl<PageableGUI> refl = new Refl<>(gui);

                final String guiType = section.getString("gui-type");
                if (guiType == null) throw new IllegalArgumentException("'gui-type' cannot be null");
                section.set("type", guiType);

                final GUI templateGUI;
                try {
                    templateGUI = c.get(s, GUI.class);
                    templateGUI.variables().clear();
                } catch (Exception e) {
                    String message = String.format("Cannot properly load template GUI for PageableGUI: %s", e.getMessage());
                    throw new IllegalArgumentException(message);
                }
                refl.setFieldObject("templateGUI", templateGUI);

                try {
                    gui.setPages(pages);
                } catch (IllegalStateException e) {
                    // DataGUI does not support pages
                }

                ConfigurationSection previousPage = section.getConfigurationSection("previous_page");
                if (previousPage != null)
                    gui.setPreviousPage(previousPage.getInteger("slot"),
                            previousPage.get("content", GUIContent.class));

                ConfigurationSection nextPage = section.getConfigurationSection("next_page");
                if (nextPage != null)
                    gui.setNextPage(nextPage.getInteger("slot"),
                            nextPage.get("content", GUIContent.class));

                Map<?, ?> variables = section.get("variables", Map.class);
                if (variables != null) variables.forEach((k, v) -> {
                    if (k != null && v != null) gui.setVariable(k.toString(), v.toString());
                });

                final ConfigurationSection pagesSection = section.getConfigurationSection("gui-pages");
                if (pagesSection != null) loadPages(section, pagesSection, gui, IGNORE_FIELDS);

                return gui;
            } finally {
                section.set("type", previousType);
            }
        };
    }

    private void loadPages(final @NotNull ConfigurationSection guiSection,
                           final @NotNull ConfigurationSection pagesSection,
                           final @NotNull PageableGUI gui,
                           final String @NotNull ... ignoreFields) {
        final List<GUI> guiPages = new LinkedList<>();
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
    protected @NotNull TriConsumer<IConfiguration, String, PageableGUI> getDumper() {
        return (c, s, p) -> {
            c.set(s, null);
            if (p == null) return;
            Refl<PageableGUI> refl = new Refl<>(p);

            c.set(s, refl.getFieldObject("templateGUI"));
            ConfigurationSection section = c.getConfigurationSection(s);
            section.set("gui-type", section.getString("type"));
            section.set("type", ParserUtils.classToType(GUI.class, p.getClass()));
            section.set("size", p.size());
            // Set pages only if PageableGUI
            if (p.getClass().equals(PageableGUI.class)) section.set("pages", p.pages());

            Tuple<Integer, GUIContent> previousPage = refl.getFieldObject("previousPage");
            previousPage.ifPresent((i, g) -> {
                ConfigurationSection previousSection = section.createSection("previous_page");
                previousSection.set("slot", i);
                previousSection.set("content", g);
            });

            Tuple<Integer, GUIContent> nextPage = refl.getFieldObject("nextPage");
            nextPage.ifPresent((i, g) -> {
                ConfigurationSection nextSection = section.createSection("next_page");
                nextSection.set("slot", i);
                nextSection.set("content", g);
            });

            section.set("variables", p.variables());

            final ConfigurationSection pagesSection = section.createSection("gui-pages");
            dumpPages(pagesSection, p, IGNORE_FIELDS);
            if (pagesSection.getKeys().isEmpty()) section.set(pagesSection.getName(), null);
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
