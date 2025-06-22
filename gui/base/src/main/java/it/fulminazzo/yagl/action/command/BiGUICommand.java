package it.fulminazzo.yagl.action.command;

import it.fulminazzo.yagl.action.BiGUIAction;
import org.jetbrains.annotations.NotNull;
import it.fulminazzo.yagl.gui.GUI;
import it.fulminazzo.yagl.viewer.Viewer;

/**
 * An implementation of {@link BiGUIAction} that executes the given command upon {@link #execute(Viewer, GUI, GUI)}.
 */
public final class BiGUICommand extends CommandAction implements BiGUIAction {

    /**
     * Instantiates a new BIGUICommand.
     *
     * @param command the command
     */
    public BiGUICommand(@NotNull String command) {
        super(command);
    }

    @Override
    public void execute(@NotNull Viewer viewer, @NotNull GUI gui1, @NotNull GUI gui2) {
        super.execute(viewer);
    }
}
