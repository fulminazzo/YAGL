package it.fulminazzo.yagl.action.command;

import it.fulminazzo.yagl.action.GUIAction;
import org.jetbrains.annotations.NotNull;
import it.fulminazzo.yagl.gui.GUI;
import it.fulminazzo.yagl.viewer.Viewer;

/**
 * An implementation of {@link GUIAction} that executes the given command upon {@link #execute(Viewer, GUI)}.
 */
public final class GUICommand extends CommandAction implements GUIAction {

    /**
     * Instantiates a new GUICommand.
     *
     * @param command the command
     */
    public GUICommand(@NotNull String command) {
        super(command);
    }

    @Override
    public void execute(@NotNull Viewer viewer, @NotNull GUI gui) {
        super.execute(viewer);
    }
}
