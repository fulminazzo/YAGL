package it.fulminazzo.yagl.actions;

import it.fulminazzo.yagl.guis.GUI;
import it.fulminazzo.yagl.viewers.Viewer;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link GUIAction} that forces the execution of
 * the given command upon {@link #execute(Viewer, GUI)} from the console.
 */
public class GUIConsoleCommand extends CommandAction implements GUIAction {

    /**
     * Instantiates a new GUIConsoleCommand.
     *
     * @param command the command
     */
    public GUIConsoleCommand(@NotNull String command) {
        super(command);
    }

    @Override
    public void execute(@NotNull Viewer viewer, @NotNull GUI gui) {
        viewer.consoleExecute(this.command);
    }
}
