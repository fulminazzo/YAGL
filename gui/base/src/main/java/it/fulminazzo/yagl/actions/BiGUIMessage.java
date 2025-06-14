package it.fulminazzo.yagl.actions;

import it.fulminazzo.yagl.guis.GUI;
import it.fulminazzo.yagl.viewers.Viewer;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link BiGUIAction} that sends the given message to the viewer upon {@link #execute(Viewer)}.
 */
public class BiGUIMessage extends MessageAction implements BiGUIAction {

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
        super.execute(viewer);
    }
}
