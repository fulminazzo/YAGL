package it.fulminazzo.yagl.actions;

import it.fulminazzo.yagl.guis.GUI;
import it.fulminazzo.yagl.viewers.Viewer;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link BiGUIAction} that forces the execution of
 * the given command upon {@link #execute(Viewer, GUI, GUI)} from the console.
 */
public class BiGUIConsoleCommand extends CommandAction implements BiGUIAction {

    /**
     * Instantiates a new BiGUIConsoleCommand.
     *
     * @param command the command
     */
    public BiGUIConsoleCommand(@NotNull String command) {
        super(command);
    }

    @Override
    public void execute(@NotNull Viewer viewer, @NotNull GUI gui1, @NotNull GUI gui2) {
        viewer.consoleExecute(this.command);
    }
}
