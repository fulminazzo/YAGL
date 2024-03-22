package it.angrybear.yagl.actions;

import it.angrybear.yagl.SerializableFunction;
import it.angrybear.yagl.contents.GUIContent;
import it.angrybear.yagl.guis.GUI;
import it.angrybear.yagl.viewers.Viewer;
import org.jetbrains.annotations.NotNull;

/**
 * A general functional interface accepting a {@link Viewer}, {@link GUI} and a {@link GUIContent}.
 */
@FunctionalInterface
public interface GUIItemAction extends SerializableFunction {

    /**
     * Execute.
     *
     * @param viewer  the viewer
     * @param gui     the gui
     * @param content the content
     */
    void execute(final @NotNull Viewer viewer, final @NotNull GUI gui, final @NotNull GUIContent content);
}
