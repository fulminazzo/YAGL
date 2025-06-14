package it.fulminazzo.yagl.actions;

import it.fulminazzo.yagl.contents.GUIContent;
import it.fulminazzo.yagl.guis.GUI;
import it.fulminazzo.yagl.viewers.Viewer;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link GUIItemAction} that sends the given message to the viewer upon {@link #execute(Viewer)}.
 */
public class GUIItemMessage extends MessageAction implements GUIItemAction {

    /**
     * Instantiates a new GUIItemMessage.
     *
     * @param message the message
     */
    public GUIItemMessage(@NotNull String message) {
        super(message);
    }

    @Override
    public void execute(@NotNull Viewer viewer, @NotNull GUI gui, @NotNull GUIContent content) {
        super.execute(viewer);
    }
}
