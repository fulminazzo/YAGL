package it.fulminazzo.yagl.actions;

import it.fulminazzo.yagl.SerializableFunction;
import it.fulminazzo.yagl.guis.GUI;
import org.jetbrains.annotations.NotNull;
import it.fulminazzo.yagl.viewers.Viewer;

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
