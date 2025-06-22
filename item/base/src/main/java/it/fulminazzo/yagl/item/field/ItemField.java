package it.fulminazzo.yagl.item.field;

import it.fulminazzo.yagl.item.Item;

/**
 * This object represents all the default fields of {@link Item}.
 * Use these in {@link Item#isSimilar(Item, ItemField...)}.
 */
public enum ItemField {
    MATERIAL, AMOUNT, DURABILITY,
    DISPLAY_NAME, LORE, ENCHANTMENTS,
    ITEM_FLAGS, UNBREAKABLE
}
