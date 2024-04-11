package it.angrybear.yagl.actions;

import org.jetbrains.annotations.NotNull;
import it.angrybear.yagl.guis.GUI;
import it.angrybear.yagl.viewers.Viewer;

/**
 * An implementation of {@link BiGUIAction} that executes the given command upon {@link #execute(Viewer, GUI, GUI)}.
 */
public class BiGUICommand extends CommandAction implements BiGUIAction {

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
