package it.fulminazzo.yagl.action.command;

import it.fulminazzo.yagl.action.BiGUIAction;
import it.fulminazzo.yagl.gui.GUI;
import it.fulminazzo.yagl.viewer.Viewer;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link BiGUIAction} that executes the given command upon {@link #execute(Viewer, GUI, GUI)}.
 */
public final class BiGUICommand extends CommandAction implements BiGUIAction {
    private static final long serialVersionUID = -1363194488408299505L;

    /**
     * Instantiates a new BIGUICommand.
     *
     * @param command the command
     */
    public BiGUICommand(final @NotNull String command) {
        super(command);
    }

    @Override
    public void execute(final @NotNull Viewer viewer, final @NotNull GUI gui1, final @NotNull GUI gui2) {
        super.execute(viewer);
    }

}
