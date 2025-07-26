package it.fulminazzo.yagl.action;

import it.fulminazzo.yagl.event.ClickItemEvent;
import it.fulminazzo.yagl.gui.GUI;
import it.fulminazzo.yagl.viewer.Viewer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An implementation of {@link GUIItemAction} that opens the previous {@link GUI} of the {@link Viewer}, if available.
 */
public final class GUIItemBack implements GUIItemAction {

    @Override
    public void execute(final @NotNull ClickItemEvent event) {
        Viewer viewer = event.getViewer();
        GUI previousGUI = viewer.getPreviousGUI();
        if (previousGUI != null) previousGUI.open(viewer);
    }

    @Override
    public @Nullable String serialize() {
        return null;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof GUIItemBack;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
