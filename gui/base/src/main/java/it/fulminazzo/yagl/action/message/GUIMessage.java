package it.fulminazzo.yagl.action.message;

import it.fulminazzo.yagl.action.GUIAction;
import it.fulminazzo.yagl.gui.GUI;
import it.fulminazzo.yagl.viewer.Viewer;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link GUIAction} that sends the given message to the viewer upon {@link #sendMessage(Viewer)}.
 */
public final class GUIMessage extends MessageAction implements GUIAction {

    /**
     * Instantiates a new GUIMessage.
     *
     * @param message the message
     */
    public GUIMessage(@NotNull String message) {
        super(message);
    }

    @Override
    public void execute(@NotNull Viewer viewer, @NotNull GUI gui) {
        super.sendMessage(viewer);
    }
}
