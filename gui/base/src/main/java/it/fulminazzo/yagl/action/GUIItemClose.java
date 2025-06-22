package it.fulminazzo.yagl.action;

import it.fulminazzo.yagl.content.GUIContent;
import it.fulminazzo.yagl.gui.GUI;
import it.fulminazzo.yagl.viewer.Viewer;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link GUIItemAction} that closes the open {@link GUI} for the {@link Viewer}.
 */
public final class GUIItemClose implements GUIItemAction {

    @Override
    public void execute(@NotNull Viewer viewer, @NotNull GUI gui, @NotNull GUIContent content) {
        viewer.closeGUI();
    }

    @Override
    public String serialize() {
        return null;
    }

}
