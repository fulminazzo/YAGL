package it.angrybear.yagl.guis;

import lombok.Getter;

/**
 * An enum used to create specific types of GUIs in Minecraft.
 */
@Getter
public enum GUIType {
    CHEST(27),
    DISPENSER(9),
    DROPPER(9),
    FURNACE(3),
    WORKBENCH(10),
    ENCHANTING(2),
    BREWING(5),
    PLAYER(41),
    ENDER_CHEST(27),
    ANVIL(3),
    SMITHING(3),
    BEACON(1),
    HOPPER(5),
    SHULKER_BOX(27),
    BARREL(27),
    BLAST_FURNACE(3),
    LECTERN(1),
    SMOKER(3),
    LOOM(4),
    CARTOGRAPHY(3),
    GRINDSTONE(3),
    STONECUTTER(2),
    ;

    private final int size;

    GUIType(int size) {
        this.size = size;
    }
}
