package it.fulminazzo.yagl.action.command;

import it.fulminazzo.yagl.action.GUIAction;
import it.fulminazzo.yagl.gui.GUI;
import it.fulminazzo.yagl.viewer.Viewer;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link GUIAction} that forces the execution of
 * the given command upon {@link #execute(Viewer, GUI)} from the console.
 */
public final class GUIConsoleCommand extends CommandAction implements GUIAction {
    private static final long serialVersionUID = 919413981368086969L;

    /**
     * Instantiates a new GUIConsoleCommand.
     *
     * @param command the command
     */
    public GUIConsoleCommand(final @NotNull String command) {
        super(command);
    }

    @Override
    public void execute(final @NotNull Viewer viewer, final @NotNull GUI gui) {
        viewer.consoleExecuteCommand(this.command);
    }

}
