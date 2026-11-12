package it.fulminazzo.yagl.action.message;

import it.fulminazzo.yagl.action.GUIAction;
import it.fulminazzo.yagl.gui.GUI;
import it.fulminazzo.yagl.viewer.Viewer;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link GUIAction} that sends the given message to the viewer upon {@link #sendMessage(Viewer)}.
 */
public final class GUIMessage extends MessageAction implements GUIAction {
    private static final long serialVersionUID = 7150159587699086297L;

    /**
     * Instantiates a new GUIMessage.
     *
     * @param message the message
     */
    public GUIMessage(final @NotNull String message) {
        super(message);
    }

    @Override
    public void execute(final @NotNull Viewer viewer, final @NotNull GUI gui) {
        super.sendMessage(viewer);
    }

}
