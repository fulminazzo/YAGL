package it.fulminazzo.yagl.event;

import it.fulminazzo.yagl.gui.GUI;
import it.fulminazzo.yagl.viewer.Viewer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the event triggered by {@link it.fulminazzo.yagl.GUIAdapter#updatePlayerGUI(GUI, Viewer)}.
 */
@Getter
@Setter
public final class FullscreenGUIUpdateEvent extends InventoryEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled;

    /**
     * Instantiates a new Fullscreen gui update event.
     *
     * @param transaction the associated inventory view
     */
    public FullscreenGUIUpdateEvent(final @NotNull InventoryView transaction) {
        super(transaction);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Gets handler list.
     *
     * @return the handler list
     */
    public static @NotNull HandlerList getHandlerList() {
        return handlers;
    }

}
