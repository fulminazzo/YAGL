package it.fulminazzo.yagl.actions.commands;

import it.fulminazzo.yagl.actions.GUIItemAction;
import it.fulminazzo.yagl.contents.GUIContent;
import it.fulminazzo.yagl.guis.GUI;
import it.fulminazzo.yagl.viewers.Viewer;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link GUIItemAction} that forces the execution of
 * the given command upon {@link #execute(Viewer, GUI, GUIContent)} from the console.
 */
public class GUIItemConsoleCommand extends CommandAction implements GUIItemAction {

    /**
     * Instantiates a new GUIItemConsoleCommand.
     *
     * @param command the command
     */
    public GUIItemConsoleCommand(@NotNull String command) {
        super(command);
    }

    @Override
    public void execute(@NotNull Viewer viewer, @NotNull GUI gui, @NotNull GUIContent content) {
        viewer.consoleExecuteCommand(this.command);
    }
}
