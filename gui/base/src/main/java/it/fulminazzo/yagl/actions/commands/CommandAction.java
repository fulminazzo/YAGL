package it.fulminazzo.yagl.actions.commands;

import it.fulminazzo.yagl.SerializableFunction;
import it.fulminazzo.yagl.viewers.Viewer;
import it.fulminazzo.fulmicollection.objects.FieldEquable;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link SerializableFunction} that executes the given {@link #command} upon {@link #execute(Viewer)}.
 */
public abstract class CommandAction extends FieldEquable implements SerializableFunction {
    protected final String command;

    /**
     * Instantiates a new Command action.
     *
     * @param command the command
     */
    CommandAction(final @NotNull String command) {
        this.command = command;
    }

    /**
     * Execute.
     *
     * @param viewer the viewer
     */
    public void execute(final @NotNull Viewer viewer) {
        viewer.executeCommand(this.command);
    }

    @Override
    public String serialize() {
        return this.command;
    }
}
