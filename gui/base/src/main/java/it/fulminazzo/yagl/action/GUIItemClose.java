package it.fulminazzo.yagl.action;

import it.fulminazzo.yagl.event.ClickItemEvent;
import it.fulminazzo.yagl.gui.GUI;
import it.fulminazzo.yagl.viewer.Viewer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An implementation of {@link GUIItemAction} that closes the open {@link GUI} for the {@link Viewer}.
 */
public final class GUIItemClose implements GUIItemAction {

    @Override
    public void execute(final @NotNull ClickItemEvent event) {
        event.getViewer().closeGUI();
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
        return o instanceof GUIItemClose;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
