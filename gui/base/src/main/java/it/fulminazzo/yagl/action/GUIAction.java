package it.fulminazzo.yagl.action;

import it.fulminazzo.yagl.SerializableFunction;
import it.fulminazzo.yagl.gui.GUI;
import org.jetbrains.annotations.NotNull;
import it.fulminazzo.yagl.viewer.Viewer;

/**
 * A general functional interface accepting a {@link Viewer} and a {@link GUI}.
 */
@FunctionalInterface
public interface GUIAction extends SerializableFunction {

    /**
     * Execute.
     *
     * @param viewer the viewer
     * @param gui    the gui
     */
    void execute(final @NotNull Viewer viewer, final @NotNull GUI gui);
}
