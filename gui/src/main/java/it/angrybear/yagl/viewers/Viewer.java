package it.angrybear.yagl.viewers;

import it.angrybear.yagl.guis.GUI;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * The viewer of a GUI, usually a player.
 */
@Getter
public abstract class Viewer {
    protected final UUID uniqueId;
    protected final String name;

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
     * Opens gui.
     *
     * @param gui the gui
     */
    public abstract void openGUI(final @NotNull GUI gui);

    /**
     * Check if the viewer has the given permission
     *
     * @param permission the permission
     * @return true if they do
     */
    public abstract boolean hasPermission(final @NotNull String permission);
}
