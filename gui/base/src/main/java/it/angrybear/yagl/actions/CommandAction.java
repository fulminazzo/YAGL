package it.angrybear.yagl.actions;

import it.angrybear.yagl.SerializableFunction;
import it.angrybear.yagl.viewers.Viewer;
import it.fulminazzo.fulmicollection.objects.FieldEquable;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link SerializableFunction} that executes the given {@link #command} upon {@link #execute(Viewer)}.
 */
abstract class CommandAction extends FieldEquable implements SerializableFunction {
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
    protected void execute(final @NotNull Viewer viewer) {
        viewer.executeCommand(this.command);
    }

    @Override
    public String serialize() {
        return this.command;
    }
}
