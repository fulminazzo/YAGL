package it.angrybear.events.items.click;

import org.bukkit.event.inventory.ClickType;

/**
 * The interface Click event.
 */
interface ClickEvent {

    /**
     * Gets click type.
     *
     * @return the click type
     */
    ClickType getClickType();
}
