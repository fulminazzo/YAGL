package it.fulminazzo.yagl.action.message;

import it.fulminazzo.yagl.action.BiGUIAction;
import it.fulminazzo.yagl.gui.GUI;
import it.fulminazzo.yagl.viewer.Viewer;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link BiGUIAction} that sends the given message to the viewer upon {@link #sendMessage(Viewer)}.
 */
public final class BiGUIMessage extends MessageAction implements BiGUIAction {

    /**
     * Instantiates a new BIGUIMessage.
     *
     * @param message the message
     */
    public BiGUIMessage(@NotNull String message) {
        super(message);
    }

    @Override
    public void execute(@NotNull Viewer viewer, @NotNull GUI gui1, @NotNull GUI gui2) {
        super.sendMessage(viewer);
    }
}
