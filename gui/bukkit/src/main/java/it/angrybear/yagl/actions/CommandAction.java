package it.angrybear.yagl.actions;

import it.angrybear.yagl.viewers.Viewer;
import org.jetbrains.annotations.NotNull;

abstract class CommandAction implements Action {
    protected final String command;

    CommandAction(final @NotNull String command) {
        this.command = command;
    }

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
