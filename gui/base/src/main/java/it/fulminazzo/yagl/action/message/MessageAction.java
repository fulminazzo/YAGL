package it.fulminazzo.yagl.action.message;

import it.fulminazzo.yagl.SerializableFunction;
import it.fulminazzo.yagl.util.MessageUtils;
import it.fulminazzo.yagl.viewer.Viewer;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link SerializableFunction} that sends the given {@link #message} to the viewer upon {@link #sendMessage(Viewer)}.
 */
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public abstract class MessageAction implements SerializableFunction {
    private static final long serialVersionUID = -6021871694446968801L;

    protected final @NotNull String message;

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
