package it.angrybear.yagl.actions;

import org.jetbrains.annotations.NotNull;
import it.angrybear.yagl.contents.GUIContent;
import it.angrybear.yagl.guis.GUI;
import it.angrybear.yagl.viewers.Viewer;

/**
 * An implementation of {@link GUIItemAction} that executes the given command upon {@link #execute(Viewer, GUI, GUIContent)}.
 */
public class GUIItemCommand extends CommandAction implements GUIItemAction {

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
