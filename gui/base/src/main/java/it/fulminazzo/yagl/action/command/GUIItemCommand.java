package it.fulminazzo.yagl.action.command;

import it.fulminazzo.yagl.action.GUIItemAction;
import it.fulminazzo.yagl.event.ClickItemEvent;
import org.jetbrains.annotations.NotNull;
import it.fulminazzo.yagl.content.GUIContent;
import it.fulminazzo.yagl.gui.GUI;
import it.fulminazzo.yagl.viewer.Viewer;

/**
 * An implementation of {@link GUIItemAction} that executes the given command upon {@link #execute(ClickItemEvent)}.
 */
public final class GUIItemCommand extends CommandAction implements GUIItemAction {
    private static final long serialVersionUID = 8198247757875424925L;

    /**
     * Instantiates a new GUIItemCommand.
     *
     * @param command the command
     */
    public GUIItemCommand(final @NotNull String command) {
        super(command);
    }

    @Override
    public void execute(final @NotNull ClickItemEvent event) {
        super.execute(event.getViewer());
    }

}
