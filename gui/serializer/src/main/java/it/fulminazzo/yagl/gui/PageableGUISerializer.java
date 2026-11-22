package it.fulminazzo.yagl.gui;

import it.fulminazzo.yagl.content.GUIContent;
import it.fulminazzo.yagl.serializer.DataNode;
import it.fulminazzo.yagl.serializer.ObjectSerializer;
import it.fulminazzo.yagl.serializer.SerializationException;
import it.fulminazzo.yagl.serializer.TypedSerializer;
import it.fulminazzo.yagl.structure.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

/**
 * A serializer for {@link PageableGUI} objects.
 *
 * @see PageableGUI
 * @see ObjectSerializer
 */
public class PageableGUISerializer extends ObjectSerializer<PageableGUI> {
    static final @NotNull String GUI_TYPE_PATH = "guiType";
    static final @NotNull String GUI_SIZE_PATH = "guiSize";
    static final @NotNull String PAGES_PATH = "pages";
    static final @NotNull String GUI_PAGES_PATH = "guiPages";
    static final @NotNull String PREVIOUS_PAGE_PATH = "previousPage";
    static final @NotNull String NEXT_PAGE_PATH = "nextPage";
    static final @NotNull String SLOT_PATH = "slot";
    static final @NotNull String CONTENT_PATH = "content";

    @Override
    protected @Nullable PageableGUI initialize(final @NotNull DataNode node) throws SerializationException {
        Integer lowerGUISize = node.getOrNull(FullscreenGUISerializer.LOWER_GUI_SIZE_PATH, Integer.class);
        if (lowerGUISize == null)
            return GUISerializerUtils.callIfTypeOrSizeArePresent(node, GUI_TYPE_PATH, PageableGUI::newGUI, GUI_SIZE_PATH, PageableGUI::newGUI);
        else return GUISerializerUtils.callIfTypeOrSizeArePresent(
                node,
                FullscreenGUISerializer.UPPER_GUI_TYPE_PATH,
                t -> PageableGUI.newFullscreenGUI(t, lowerGUISize),
                FullscreenGUISerializer.UPPER_GUI_SIZE_PATH,
                s -> PageableGUI.newFullscreenGUI(s, lowerGUISize)
        );
    }

    @Override
    protected void loadData(final @NotNull PageableGUI value, final @NotNull DataNode node) throws SerializationException {
        node.get(PAGES_PATH, Integer.class).ifPresent(value::setPages);

        DataNode previousPageNode = node.getSubNode(PREVIOUS_PAGE_PATH).orElse(null);
        if (previousPageNode != null) {
            Integer slot = previousPageNode.getOrNull(SLOT_PATH, Integer.class);
            if (slot != null)
                previousPageNode.get(CONTENT_PATH, GUIContent.class).ifPresent(c -> value.setPreviousPage(slot, c));
        }

        DataNode nextPageNode = node.getSubNode(NEXT_PAGE_PATH).orElse(null);
        if (nextPageNode != null) {
            Integer slot = nextPageNode.getOrNull(SLOT_PATH, Integer.class);
            if (slot != null)
                nextPageNode.get(CONTENT_PATH, GUIContent.class).ifPresent(c -> value.setNextPage(slot, c));
        }

        DataNode guiPagesNode = node.getSubNode(GUI_PAGES_PATH).orElse(null);
        if (guiPagesNode != null)
            for (Object key : guiPagesNode.keys())
                try {
                    String rawPage = key.toString();
                    int page = Integer.parseInt(rawPage);

                    // Declared from the main GUI
                    GUI templateGUI = value.templateGUI;
                    guiPagesNode.set(
                            rawPage + "." + TypedSerializer.TYPE_PATH,
                            templateGUI.getClass().getSimpleName()
                                    .replace(GUI.class.getSimpleName(), "")
                                    .toUpperCase(Locale.ROOT)
                    );

                    if (templateGUI instanceof FullscreenGUI) {
                        FullscreenGUI fullscreenGUI = (FullscreenGUI) templateGUI;
                        GUISerializerUtils.dumpTypeOrNormalGUI(
                                fullscreenGUI.getUpperGUI(),
                                guiPagesNode,
                                rawPage + "." + FullscreenGUISerializer.UPPER_GUI_TYPE_PATH,
                                rawPage + "." + FullscreenGUISerializer.UPPER_GUI_SIZE_PATH
                        );
                        guiPagesNode.set(
                                rawPage + "." + FullscreenGUISerializer.LOWER_GUI_SIZE_PATH,
                                fullscreenGUI.getLowerGUI().size()
                        );
                    } else GUISerializerUtils.dumpTypeOrNormalGUI(templateGUI, guiPagesNode, rawPage);

                    guiPagesNode.get(rawPage, GUI.class)
                            .ifPresent(v -> value.getPage(page).copyFrom(v, true));
                } catch (NumberFormatException e) {
                    throw new SerializationException("Invalid page '%s'", key);
                }
    }

    @Override
    protected void dumpData(final @NotNull PageableGUI value, final @NotNull DataNode output) throws SerializationException {
        if (value.templateGUI instanceof FullscreenGUI) {
            FullscreenGUI fullscreenGUI = (FullscreenGUI) value.templateGUI;
            GUISerializerUtils.dumpTypeOrNormalGUI(
                    fullscreenGUI.getUpperGUI(),
                    output,
                    FullscreenGUISerializer.UPPER_GUI_TYPE_PATH,
                    FullscreenGUISerializer.UPPER_GUI_SIZE_PATH
            );
            output.set(FullscreenGUISerializer.LOWER_GUI_SIZE_PATH, fullscreenGUI.getLowerGUI().size());
        } else GUISerializerUtils.dumpTypeOrNormalGUI(value.templateGUI, output, GUI_TYPE_PATH, GUI_SIZE_PATH);
        output.set(PAGES_PATH, value.pages(), 0);

        Pair<Integer, GUIContent> previousPage = value.previousPage;
        if (previousPage.isPresent()) {
            output.set(PREVIOUS_PAGE_PATH + "." + SLOT_PATH, previousPage.getFirst());
            output.set(PREVIOUS_PAGE_PATH + "." + CONTENT_PATH, previousPage.getSecond());
        }

        Pair<Integer, GUIContent> nextPage = value.nextPage;
        if (nextPage.isPresent()) {
            output.set(NEXT_PAGE_PATH + "." + SLOT_PATH, nextPage.getFirst());
            output.set(NEXT_PAGE_PATH + "." + CONTENT_PATH, nextPage.getSecond());
        }

        for (int i = 0; i < value.pages(); i++) {
            String basePath = GUI_PAGES_PATH + "." + i;
            output.set(basePath, value.getPage(i));
            // Declared from the main GUI
            output.set(basePath + "." + TypedSerializer.TYPE_PATH, null);
            output.set(basePath + "." + DefaultGUISerializer.SIZE_PATH, null);
            output.set(basePath + "." + TypeGUISerializer.INVENTORY_TYPE_PATH, null);
            output.set(basePath + "." + FullscreenGUISerializer.UPPER_GUI_TYPE_PATH, null);
            output.set(basePath + "." + FullscreenGUISerializer.UPPER_GUI_SIZE_PATH, null);
            output.set(basePath + "." + FullscreenGUISerializer.LOWER_GUI_SIZE_PATH, null);
        }
    }

}
