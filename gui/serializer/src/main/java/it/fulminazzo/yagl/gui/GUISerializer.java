package it.fulminazzo.yagl.gui;

import it.fulminazzo.yagl.action.BiGUIAction;
import it.fulminazzo.yagl.action.GUIAction;
import it.fulminazzo.yagl.content.GUIContent;
import it.fulminazzo.yagl.serializer.DataNode;
import it.fulminazzo.yagl.serializer.SerializationException;
import it.fulminazzo.yagl.serializer.TypedSerializer;
import org.jetbrains.annotations.NotNull;

/**
 * A serializer for {@link GUI} implementations.
 *
 * @see GUI
 * @see TypedSerializer
 */
public final class GUISerializer extends TypedSerializer<GUI> {
    static final @NotNull String TITLE_PATH = "title";
    static final @NotNull String CONTENTS_PATH = "contents";
    static final @NotNull String MOVABLE_SLOTS_PATH = "movableSlots";
    static final @NotNull String VARIABLES_PATH = "variables";
    static final @NotNull String CLICK_OUTSIDE_ACTION_PATH = "clickOutsideAction";
    static final @NotNull String OPEN_GUI_ACTION_PATH = "openGuiAction";
    static final @NotNull String CLOSE_GUI_ACTION_PATH = "closeGuiAction";
    static final @NotNull String CHANGE_GUI_ACTION_PATH = "changeGuiAction";

    public GUISerializer() {
        super(GUI.class);
        registerSerializer(DefaultGUI.class, new DefaultGUISerializer());
        registerSerializer(TypeGUI.class, new TypeGUISerializer());
        registerSerializer(ResizableGUI.class, new ResizableGUISerializer());
        registerSerializer(FullscreenGUI.class, new FullscreenGUISerializer());
        registerSerializer(PageableGUI.class, new PageableGUISerializer());
    }

    @Override
    protected void loadCommonData(final @NotNull GUI value, final @NotNull DataNode node) throws SerializationException {
        node.get(TITLE_PATH, String.class).ifPresent(value::setTitle);
        node.getCollection(MOVABLE_SLOTS_PATH, Integer.class)
                .ifPresent(v -> v.forEach(s -> value.setMovable(s, true)));
        node.getMap(VARIABLES_PATH, String.class, String.class)
                .ifPresent(v -> v.forEach(value::setVariable));
        node.get(CLICK_OUTSIDE_ACTION_PATH, GUIAction.class).ifPresent(value::onClickOutside);
        node.get(OPEN_GUI_ACTION_PATH, GUIAction.class).ifPresent(value::onOpenGUI);
        node.get(CLOSE_GUI_ACTION_PATH, GUIAction.class).ifPresent(value::onCloseGUI);
        node.get(CHANGE_GUI_ACTION_PATH, BiGUIAction.class).ifPresent(value::onChangeGUI);

        DataNode contentsNode = node.getSubNode(CONTENTS_PATH).orElse(null);
        if (contentsNode != null)
            for (Object key : contentsNode.keys())
                try {
                    String rawSlot = key.toString();
                    int slot = Integer.parseInt(rawSlot);
                    contentsNode.getCollection(rawSlot, GUIContent.class)
                            .ifPresent(v -> value.setContents(slot, v));
                } catch (NumberFormatException e) {
                    throw new SerializationException("Invalid slot '%s'", key);
                }
    }

    @Override
    protected void dumpCommonData(final @NotNull GUI value, final @NotNull DataNode output) throws SerializationException {
        output.set(TITLE_PATH, value.getTitle());
        output.set(MOVABLE_SLOTS_PATH, value.getMovableSlots());
        output.set(VARIABLES_PATH, value.variables());
        GUIAction clickOutsideAction = value.clickOutsideAction().orElse(null);
        if (clickOutsideAction != null) output.set(CLICK_OUTSIDE_ACTION_PATH, clickOutsideAction);
        GUIAction openGUIAction = value.openGUIAction().orElse(null);
        if (openGUIAction != null) output.set(OPEN_GUI_ACTION_PATH, openGUIAction);
        GUIAction closeGUIAction = value.closeGUIAction().orElse(null);
        if (closeGUIAction != null) output.set(CLOSE_GUI_ACTION_PATH, closeGUIAction);
        BiGUIAction changeGUIAction = value.changeGUIAction().orElse(null);
        if (changeGUIAction != null) output.set(CHANGE_GUI_ACTION_PATH, changeGUIAction);

        for (int i = 0; i < value.size(); i++)
            output.set(CONTENTS_PATH + "." + i, value.getContents(i));
    }

}
