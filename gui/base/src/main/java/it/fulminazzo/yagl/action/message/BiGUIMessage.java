package it.fulminazzo.yagl.action.message;

import it.fulminazzo.yagl.action.BiGUIAction;
import it.fulminazzo.yagl.gui.GUI;
import it.fulminazzo.yagl.viewer.Viewer;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link BiGUIAction} that sends the given message to the viewer upon {@link #sendMessage(Viewer)}.
 */
public final class BiGUIMessage extends MessageAction implements BiGUIAction {
    private static final long serialVersionUID = 7058726246605217847L;

    /**
     * Instantiates a new BIGUIMessage.
     *
     * @param message the message
     */
    public BiGUIMessage(final @NotNull String message) {
        super(message);
    }

    @Override
    public void execute(final @NotNull Viewer viewer, final @NotNull GUI gui1, final @NotNull GUI gui2) {
        super.sendMessage(viewer);
    }

}
