package it.fulminazzo.yagl.items;

/**
 * Used by {@link PersistentItem} when the player dies.
 */
public enum DeathAction {
    /**
     * The item will be maintained in the player's inventory.
     */
    MAINTAIN,
    /**
     * The item will disappear from the player's inventory.
     */
    DISAPPEAR
}
