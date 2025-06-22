package it.fulminazzo.yagl.action.message;

import it.fulminazzo.yagl.action.GUIItemAction;
import it.fulminazzo.yagl.content.GUIContent;
import it.fulminazzo.yagl.gui.GUI;
import it.fulminazzo.yagl.viewer.Viewer;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link GUIItemAction} that sends the given message to the viewer upon {@link #sendMessage(Viewer)}.
 */
public final class GUIItemMessage extends MessageAction implements GUIItemAction {

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
        super.sendMessage(viewer);
    }
}
