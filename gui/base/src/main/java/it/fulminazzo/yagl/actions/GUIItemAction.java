package it.fulminazzo.yagl.actions;

import it.fulminazzo.yagl.SerializableFunction;
import it.fulminazzo.yagl.contents.GUIContent;
import it.fulminazzo.yagl.guis.GUI;
import org.jetbrains.annotations.NotNull;
import it.fulminazzo.yagl.viewers.Viewer;

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