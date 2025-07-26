package it.fulminazzo.yagl.action.message;

import it.fulminazzo.yagl.action.GUIItemAction;
import it.fulminazzo.yagl.event.ClickItemEvent;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link GUIItemAction} that sends the given message to the viewer upon {@link #execute(ClickItemEvent)}.
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
    public void execute(final @NotNull ClickItemEvent event) {
        super.sendMessage(event.getViewer());
    }

}
