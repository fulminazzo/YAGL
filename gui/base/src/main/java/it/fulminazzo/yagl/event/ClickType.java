package it.fulminazzo.yagl.event;

/**
 * Represents how a content was clicked.
 */
public enum ClickType {
    /**
     * The left (or primary) mouse button.
     */
    LEFT,
    /**
     * Holding shift while pressing the left mouse button.
     */
    SHIFT_LEFT,
    /**
     * The right mouse button.
     */
    RIGHT,
    /**
     * Holding shift while pressing the right mouse button.
     */
    SHIFT_RIGHT,
    /**
     * The middle mouse button, or a "scroll wheel click".
     */
    MIDDLE,
    /**
     * One of the number keys 1-9, correspond to slots on the hotbar.
     */
    NUMBER_KEY,
    /**
     * Pressing the left mouse button twice in quick succession.
     */
    DOUBLE_CLICK,
    /**
     * The "Drop" key (defaults to Q).
     */
    DROP,
    /**
     * Holding Ctrl while pressing the "Drop" key (defaults to Q).
     */
    CONTROL_DROP

}
