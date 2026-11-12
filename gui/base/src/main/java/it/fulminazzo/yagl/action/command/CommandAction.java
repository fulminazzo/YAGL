package it.fulminazzo.yagl.action.command;

import it.fulminazzo.yagl.SerializableFunction;
import it.fulminazzo.yagl.viewer.Viewer;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link SerializableFunction} that executes the given {@link #command} upon {@link #execute(Viewer)}.
 */
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public abstract class CommandAction implements SerializableFunction {
    private static final long serialVersionUID = -6444604818044458039L;

    protected final @NotNull String command;

    /**
     * Executes the action for the given viewer.
     *
     * @param viewer the viewer
     */
    public void execute(final @NotNull Viewer viewer) {
        viewer.executeCommand(this.command);
    }

    @Override
    public @NotNull String serialize() {
        return this.command;
    }

}
