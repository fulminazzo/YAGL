package it.angrybear.yagl.events.items.interact;

import org.bukkit.event.block.Action;

/**
 * The interface Interact event.
 */
interface InteractEvent {

    /**
     * Gets interact action.
     *
     * @return the interact action
     */
    Action getInteractAction();
}
