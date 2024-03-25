package it.angrybear.yagl.items.fields;

import it.angrybear.yagl.items.Item;

/**
 * This object represents all the default fields of {@link Item}.
 * Use these in {@link Item#isSimilar(Item, ItemField...)}.
 */
public enum ItemField {
    MATERIAL, AMOUNT, DURABILITY,
    DISPLAY_NAME, LORE, ENCHANTMENTS,
    ITEM_FLAGS, UNBREAKABLE
}
