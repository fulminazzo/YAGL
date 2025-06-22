package it.fulminazzo.yagl.actions.commands;

import it.fulminazzo.yagl.actions.BiGUIAction;
import org.jetbrains.annotations.NotNull;
import it.fulminazzo.yagl.guis.GUI;
import it.fulminazzo.yagl.viewers.Viewer;

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
