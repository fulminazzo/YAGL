package it.fulminazzo.yagl.utils.legacy;

import lombok.Getter;

@Getter
public class Container {
    private final Containers type;

    public Container() {
        this(Containers.GENERIC_9x3);
    }

    public Container(Containers type) {
        this.type = type;
    }

    public enum Containers {

        GENERIC_9x1,
        GENERIC_9x2,
        GENERIC_9x3,
        GENERIC_9x4,
        GENERIC_9x5,
        GENERIC_9x6,
        GENERIC_3x3,
        CRAFTER_3x3,
        ANVIL,
        BEACON,
        BLAST_FURNACE,
        BREWING_STAND,
        CRAFTING,
        ENCHANTMENT,
        FURNACE,
        GRINDSTONE,
        HOPPER,
        LECTERN,
        LOOM,
        MERCHANT,
        SHULKER_BOX,
        SMITHING,
        SMOKER,
        CARTOGRAPHY_TABLE,
        STONECUTTER

    }

}
