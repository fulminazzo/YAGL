package it.fulminazzo.yagl.actions;

import it.fulminazzo.fulmicollection.objects.FieldEquable;
import it.fulminazzo.yagl.SerializableFunction;
import it.fulminazzo.yagl.utils.MessageUtils;
import it.fulminazzo.yagl.viewers.Viewer;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link SerializableFunction} that sends the given {@link #message} to the viewer upon {@link #execute(Viewer)}.
 */
abstract class MessageAction extends FieldEquable implements SerializableFunction {
    protected final String message;

    /**
     * Instantiates a new Message action.
     *
     * @param message the message
     */
    MessageAction(final @NotNull String message) {
        this.message = message;
    }

    /**
     * Execute.
     *
     * @param viewer the viewer
     */
    protected void execute(final @NotNull Viewer viewer) {
        viewer.sendMessage(MessageUtils.color(this.message));
    }

    @Override
    public String serialize() {
        return this.message;
    }
}
