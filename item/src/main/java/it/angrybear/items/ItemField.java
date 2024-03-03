package it.angrybear.items;

/**
 * This object represents all the default fields of {@link Item}.
 * Use these in {@link Item#isSimilar(Item, ItemField...)}.
 */
public enum ItemField {
    MATERIAL, AMOUNT, DURABILITY,
    DISPLAY_NAME, LORE, ENCHANTMENTS,
    ITEM_FLAGS, UNBREAKABLE
}
