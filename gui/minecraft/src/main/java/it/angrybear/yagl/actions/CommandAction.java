package it.angrybear.yagl.actions;

import it.angrybear.yagl.viewers.Viewer;
import org.jetbrains.annotations.NotNull;

abstract class CommandAction {
    protected final String command;

    CommandAction(final @NotNull String command) {
        this.command = command;
    }

    protected void execute(final @NotNull Viewer viewer) {
        viewer.executeCommand(this.command);
    }
}
