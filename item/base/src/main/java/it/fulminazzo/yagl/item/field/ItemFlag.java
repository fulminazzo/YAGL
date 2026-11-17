package it.fulminazzo.yagl.item.field;

/**
 * An item flag is a special attribute that can be applied to an item in order to have a special behavior.
 * <br>
 * In this class you will find the <b>most common</b> and <b>widely used</b> item flags.
 * However, {@link it.fulminazzo.yagl.item.Item} provides support for <b>all</b> item flags of the Minecraft version
 * by specifying them with a <b>string</b> representation.
 *
 * @see it.fulminazzo.yagl.item.Item
 */
public enum ItemFlag {
    /**
     * Hides the attributes of the item (like damage or attack speed).
     */
    HIDE_ATTRIBUTES,
    /**
     * Hides the durability of the item.
     */
    HIDE_DESTROYS,
    /**
     * Hides the enchantments applied to the item.
     */
    HIDE_ENCHANTS,
    /**
     * Hides the text containing the blocks on which the item can be placed.
     */
    HIDE_PLACED_ON,
    /**
     * Hides the text that displays if the item is unbreakable.
     */
    HIDE_UNBREAKABLE

}
