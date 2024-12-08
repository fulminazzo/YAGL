package it.fulminazzo.yagl.actions;

import org.jetbrains.annotations.NotNull;
import it.fulminazzo.yagl.guis.GUI;
import it.fulminazzo.yagl.viewers.Viewer;

/**
 * An implementation of {@link GUIAction} that executes the given command upon {@link #execute(Viewer, GUI)}.
 */
public class GUICommand extends CommandAction implements GUIAction {

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
