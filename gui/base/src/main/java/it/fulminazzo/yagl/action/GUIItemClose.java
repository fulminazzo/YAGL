package it.fulminazzo.yagl.action;

import it.fulminazzo.yagl.event.ClickItemEvent;
import it.fulminazzo.yagl.gui.GUI;
import it.fulminazzo.yagl.viewer.Viewer;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link GUIItemAction} that closes the open {@link GUI} for the {@link Viewer}.
 */
public final class GUIItemClose implements GUIItemAction {
    private static final long serialVersionUID = 1354944856286643090L;

    @Override
    public void execute(final @NotNull ClickItemEvent event) {
        event.getViewer().closeGUI();
    }

    @Override
    public @NotNull String serialize() {
        throw new UnsupportedOperationException("This action cannot be serialized");
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
