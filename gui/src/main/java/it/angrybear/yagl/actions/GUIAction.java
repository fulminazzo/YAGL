package it.angrybear.yagl.actions;

import it.angrybear.yagl.SerializableFunction;
import it.angrybear.yagl.guis.GUI;
import it.angrybear.yagl.viewers.Viewer;
import org.jetbrains.annotations.NotNull;

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
