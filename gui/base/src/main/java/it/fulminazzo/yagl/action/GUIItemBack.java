package it.fulminazzo.yagl.action;

import it.fulminazzo.yagl.content.GUIContent;
import it.fulminazzo.yagl.gui.GUI;
import it.fulminazzo.yagl.viewer.Viewer;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link GUIItemAction} that opens the previous {@link GUI} of the {@link Viewer}, if available.
 */
public final class GUIItemBack implements GUIItemAction {

    @Override
    public void execute(@NotNull Viewer viewer, @NotNull GUI gui, @NotNull GUIContent content) {
        GUI previousGUI = viewer.getPreviousGUI();
        if (previousGUI != null) previousGUI.open(viewer);
    }

    @Override
    public String serialize() {
        return null;
    }

}
