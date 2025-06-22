package it.fulminazzo.yagl.action;

import it.fulminazzo.yagl.SerializableFunction;
import it.fulminazzo.yagl.gui.GUI;
import org.jetbrains.annotations.NotNull;
import it.fulminazzo.yagl.viewer.Viewer;

/**
 * A general functional interface accepting a {@link Viewer} and two {@link GUI}s.
 */
@FunctionalInterface
public interface BiGUIAction extends SerializableFunction {

    /**
     * Execute.
     *
     * @param viewer the viewer
     * @param gui1   the gui 1
     * @param gui2   the gui 2
     */
    void execute(final @NotNull Viewer viewer, final @NotNull GUI gui1, final @NotNull GUI gui2);
}
