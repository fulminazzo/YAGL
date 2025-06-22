package net.minecraft.server.v1_14_R1.containers;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public enum DefaultContainers implements Containers {

    GENERIC_9x1(9, "CHEST"),
    GENERIC_9x2(18, "CHEST"),
    GENERIC_9x3("CHEST"),
    GENERIC_9x4(36, "CHEST"),
    GENERIC_9x5(45, "CHEST"),
    GENERIC_9x6(54, "CHEST"),
    GENERIC_3x3("DROPPER"),
    ANVIL,
    BEACON,
    BLAST_FURNACE,
    BREWING_STAND("BREWING"),
    CRAFTING,
    ENCHANTMENT("ENCHANTING"),
    FURNACE,
    GRINDSTONE,
    HOPPER,
    LECTERN,
    LOOM,
    SHULKER_BOX,
    SMITHING,
    SMOKER,
    CARTOGRAPHY_TABLE("CARTOGRAPHY"),
    STONECUTTER;

    private final int size;
    private final @NotNull String rawInventoryType;

    DefaultContainers() {
        this.size = -1;
        this.rawInventoryType = name();
    }

    DefaultContainers(final @NotNull String rawInventoryType) {
        this.size = -1;
        this.rawInventoryType = rawInventoryType;
    }

    DefaultContainers(final int size, final @NotNull String rawInventoryType) {
        this.size = size;
        this.rawInventoryType = rawInventoryType;
    }

}
