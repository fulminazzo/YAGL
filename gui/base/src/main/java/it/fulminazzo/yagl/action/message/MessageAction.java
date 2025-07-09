package it.fulminazzo.yagl.action.message;

import it.fulminazzo.fulmicollection.objects.FieldEquable;
import it.fulminazzo.yagl.SerializableFunction;
import it.fulminazzo.yagl.util.MessageUtils;
import it.fulminazzo.yagl.viewer.Viewer;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link SerializableFunction} that sends the given {@link #message} to the viewer upon {@link #sendMessage(Viewer)}.
 */
public abstract class MessageAction extends FieldEquable implements SerializableFunction {
    protected final @NotNull String message;

    /**
     * Instantiates a new Message action.
     *
     * @param message the message
     */
    MessageAction(final @NotNull String message) {
        this.message = message;
    }

    /**
     * Send message.
     *
     * @param viewer the viewer
     */
    public void sendMessage(final @NotNull Viewer viewer) {
        viewer.sendMessage(MessageUtils.color(this.message));
    }

    @Override
    public @NotNull String serialize() {
        return this.message;
    }
}
