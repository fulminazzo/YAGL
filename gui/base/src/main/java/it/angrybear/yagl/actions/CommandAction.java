package it.angrybear.yagl.actions;

import org.jetbrains.annotations.NotNull;
import it.angrybear.yagl.SerializableFunction;
import it.angrybear.yagl.viewers.Viewer;

/**
 * An implementation of {@link SerializableFunction} that executes the given {@link #command} upon {@link #execute(Viewer)}.
 */
abstract class CommandAction implements SerializableFunction {
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
    public boolean equals(Object o) {
        if (o instanceof CommandAction)
            return getClass().equals(o.getClass()) && this.command.equals(((CommandAction) o).command);
        return super.equals(o);
    }

    @Override
    public String serialize() {
        return this.command;
    }
}
