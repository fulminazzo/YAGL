package it.fulminazzo.yagl.action;

import it.fulminazzo.yagl.SerializableFunction;
import it.fulminazzo.yagl.content.GUIContent;
import it.fulminazzo.yagl.event.ClickItemEvent;
import it.fulminazzo.yagl.event.ClickType;
import it.fulminazzo.yagl.gui.GUI;
import it.fulminazzo.yagl.viewer.Viewer;
import org.jetbrains.annotations.NotNull;

/**
 * A general functional interface accepting a {@link ClickItemEvent}.
 */
@FunctionalInterface
public interface GUIItemAction extends SerializableFunction {

    /**
     * Execute.
     *
     * @param event the event
     */
    void execute(final @NotNull ClickItemEvent event);

    /**
     * Execute.
     *
     * @param viewer  the viewer
     * @param gui     the gui
     * @param content the content
     */
    default void execute(final @NotNull Viewer viewer, final @NotNull GUI gui, final @NotNull GUIContent content) {
        execute(ClickItemEvent.builder()
                .viewer(viewer)
                .gui(gui)
                .content(content)
                .clickType(ClickType.LEFT)
                .build());
    }

}
