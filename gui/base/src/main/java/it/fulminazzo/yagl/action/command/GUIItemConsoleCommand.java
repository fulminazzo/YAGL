package it.fulminazzo.yagl.action.command;

import it.fulminazzo.yagl.action.GUIItemAction;
import it.fulminazzo.yagl.event.ClickItemEvent;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link GUIItemAction} that forces the execution of
 * the given command upon {@link #execute(ClickItemEvent)} from the console.
 */
public final class GUIItemConsoleCommand extends CommandAction implements GUIItemAction {
    private static final long serialVersionUID = -8651296695149463165L;

    /**
     * Instantiates a new GUIItemConsoleCommand.
     *
     * @param command the command
     */
    public GUIItemConsoleCommand(final @NotNull String command) {
        super(command);
    }

    @Override
    public void execute(final @NotNull ClickItemEvent event) {
        event.getViewer().consoleExecuteCommand(this.command);
    }

}
