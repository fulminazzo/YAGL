package it.angrybear.yagl.items;

/**
 * An enum used to identify the type of mobility intended for {@link PersistentItem}.
 */
public enum Mobility {
    /**
     * The item cannot be moved in any slot.
     */
    STATIC,
    /**
     * The item can be moved only in slots of the player's inventory.
     */
    INTERNAL
}
