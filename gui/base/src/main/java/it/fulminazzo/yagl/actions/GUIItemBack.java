package it.fulminazzo.yagl.actions;

import it.fulminazzo.yagl.contents.GUIContent;
import it.fulminazzo.yagl.guis.GUI;
import it.fulminazzo.yagl.viewers.Viewer;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link GUIItemAction} that opens the previous {@link GUI} of the {@link Viewer}, if available.
 */
public class GUIItemBack implements GUIItemAction {

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
