package it.fulminazzo.yagl.viewers;

import it.fulminazzo.yagl.guis.GUI;
import it.fulminazzo.yagl.wrappers.Sound;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * The viewer of a GUI, usually a player.
 */
@Getter
public abstract class Viewer {
    protected final UUID uniqueId;
    protected final String name;
    protected GUI previousGUI;
    protected GUI openGUI;
    /**
     * Represents the next {@link GUI} that will be immediately open.
     * Usually set only during a change GUI event.
     */
    protected GUI nextGUI;

    /**
     * Instantiates a new Viewer.
     *
     * @param uniqueId the unique id
     * @param name     the name
     */
    protected Viewer(final UUID uniqueId, final String name) {
        this.uniqueId = uniqueId;
        this.name = name;
    }

    /**
     * Checks if the current viewer has a GUI opened.
     *
     * @return true if they do
     */
    public boolean hasOpenGUI() {
        return this.openGUI != null;
    }

    /**
     * Plays the given sound for the player.
     *
     * @param sound the sound
     */
    public abstract void playSound(final @NotNull Sound sound);

    /**
     * Sends the given message to the player.
     *
     * @param message the message
     */
    public abstract void sendMessage(final @NotNull String message);

    /**
     * Execute command.
     *
     * @param command the command
     */
    public abstract void executeCommand(final @NotNull String command);

    /**
     * Forces the execution of the given command from the console.
     *
     * @param command the command
     */
    public abstract void consoleExecuteCommand(final @NotNull String command);

    /**
     * Check if the viewer has the given permission
     *
     * @param permission the permission
     * @return true if they do
     */
    public abstract boolean hasPermission(final @NotNull String permission);

    /**
     * Closes the currently open GUI.
     */
    public abstract void closeGUI();
}
