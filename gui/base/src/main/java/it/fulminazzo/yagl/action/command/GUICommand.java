package it.fulminazzo.yagl.action.command;

import it.fulminazzo.yagl.action.GUIAction;
import it.fulminazzo.yagl.gui.GUI;
import it.fulminazzo.yagl.viewer.Viewer;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link GUIAction} that executes the given command upon {@link #execute(Viewer, GUI)}.
 */
public final class GUICommand extends CommandAction implements GUIAction {
    private static final long serialVersionUID = 5815978468410590987L;

    /**
     * Instantiates a new GUICommand.
     *
     * @param command the command
     */
    public GUICommand(final @NotNull String command) {
        super(command);
    }

    @Override
    public void execute(final @NotNull Viewer viewer, final @NotNull GUI gui) {
        super.execute(viewer);
    }

}
