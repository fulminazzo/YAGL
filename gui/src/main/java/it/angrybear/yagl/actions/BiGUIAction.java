package it.angrybear.yagl.actions;

import it.angrybear.yagl.guis.GUI;
import it.angrybear.yagl.viewers.Viewer;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * A general functional interface accepting a {@link Viewer} and two {@link GUI}s.
 */
public interface BiGUIAction extends Serializable {

    /**
     * Execute.
     *
     * @param viewer the viewer
     * @param gui1   the gui 1
     * @param gui2   the gui 2
     */
    void execute(final @NotNull Viewer viewer, final @NotNull GUI gui1, final @NotNull GUI gui2);
}
