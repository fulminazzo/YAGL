package net.minecraft.server.v1_14_R1.containers;

import it.fulminazzo.jbukkit.BukkitUtils;
import lombok.Getter;
import org.bukkit.event.inventory.InventoryType;
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
    private final @NotNull InventoryType inventoryType;

    DefaultContainers() {
        BukkitUtils.setupServer();
        InventoryType inventoryType;
        try {
            inventoryType = InventoryType.valueOf(name());
        } catch (IllegalArgumentException e) {
            // Older version, did not have the inventoryType
            inventoryType = InventoryType.CREATIVE;
        }
        this.size = inventoryType.getDefaultSize();
        this.inventoryType = inventoryType;
    }

    DefaultContainers(final @NotNull String rawInventoryType) {
        BukkitUtils.setupServer();
        InventoryType inventoryType;
        try {
            inventoryType = InventoryType.valueOf(rawInventoryType);
        } catch (IllegalArgumentException e) {
            // Older version, did not have the inventoryType
            inventoryType = InventoryType.CREATIVE;
        }
        this.size = inventoryType.getDefaultSize();
        this.inventoryType = inventoryType;
    }

    DefaultContainers(final int size, final @NotNull String inventoryType) {
        BukkitUtils.setupServer();
        this.size = size;
        this.inventoryType = InventoryType.valueOf(inventoryType);
    }

}
