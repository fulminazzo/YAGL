package it.fulminazzo.yagl.action.command;

import it.fulminazzo.yagl.action.GUIItemAction;
import org.jetbrains.annotations.NotNull;
import it.fulminazzo.yagl.content.GUIContent;
import it.fulminazzo.yagl.gui.GUI;
import it.fulminazzo.yagl.viewer.Viewer;

/**
 * An implementation of {@link GUIItemAction} that executes the given command upon {@link #execute(Viewer, GUI, GUIContent)}.
 */
public final class GUIItemCommand extends CommandAction implements GUIItemAction {

    /**
     * Instantiates a new GUIItemCommand.
     *
     * @param command the command
     */
    public GUIItemCommand(@NotNull String command) {
        super(command);
    }

    @Override
    public void execute(@NotNull Viewer viewer, @NotNull GUI gui, @NotNull GUIContent content) {
        super.execute(viewer);
    }
}
