package it.fulminazzo.yagl.action.command;

import it.fulminazzo.yagl.action.BiGUIAction;
import it.fulminazzo.yagl.gui.GUI;
import it.fulminazzo.yagl.viewer.Viewer;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link BiGUIAction} that forces the execution of
 * the given command upon {@link #execute(Viewer, GUI, GUI)} from the console.
 */
public final class BiGUIConsoleCommand extends CommandAction implements BiGUIAction {
    private static final long serialVersionUID = -1286603964635643982L;

    /**
     * Instantiates a new BiGUIConsoleCommand.
     *
     * @param command the command
     */
    public BiGUIConsoleCommand(final @NotNull String command) {
        super(command);
    }

    @Override
    public void execute(final @NotNull Viewer viewer, final @NotNull GUI gui1, final @NotNull GUI gui2) {
        viewer.consoleExecuteCommand(this.command);
    }
}
