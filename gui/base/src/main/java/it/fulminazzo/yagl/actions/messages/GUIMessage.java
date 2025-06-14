package it.fulminazzo.yagl.actions.messages;

import it.fulminazzo.yagl.actions.GUIAction;
import it.fulminazzo.yagl.guis.GUI;
import it.fulminazzo.yagl.viewers.Viewer;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link GUIAction} that sends the given message to the viewer upon {@link #execute(Viewer)}.
 */
public class GUIMessage extends MessageAction implements GUIAction {

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
        super.execute(viewer);
    }
}
