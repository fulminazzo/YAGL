package net.minecraft.server.v1_14_R1.containers;

import lombok.Getter;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

@Getter
public enum DefaultContainers implements Containers {

    GENERIC_9x1(9, InventoryType.CHEST),
    GENERIC_9x2(18, InventoryType.CHEST),
    GENERIC_9x3(InventoryType.CHEST),
    GENERIC_9x4(36, InventoryType.CHEST),
    GENERIC_9x5(45, InventoryType.CHEST),
    GENERIC_9x6(54, InventoryType.CHEST),
    GENERIC_3x3(InventoryType.DROPPER),
    ANVIL,
    BEACON,
    BLAST_FURNACE,
    BREWING_STAND(InventoryType.BREWING),
    CRAFTING,
    ENCHANTMENT(InventoryType.ENCHANTING),
    FURNACE,
    GRINDSTONE,
    HOPPER,
    LECTERN,
    LOOM,
    SHULKER_BOX,
    SMITHING,
    SMOKER,
    CARTOGRAPHY_TABLE(InventoryType.CARTOGRAPHY),
    STONECUTTER;

    private final int size;
    private final @NotNull InventoryType inventoryType;

    DefaultContainers() {
        InventoryType inventoryType = InventoryType.valueOf(name());
        this.size = inventoryType.getDefaultSize();
        this.inventoryType = inventoryType;
    }

    DefaultContainers(final @NotNull InventoryType inventoryType) {
        this(inventoryType.getDefaultSize(), inventoryType);
    }

    DefaultContainers(final int size, final @NotNull InventoryType inventoryType) {
        this.size = size;
        this.inventoryType = inventoryType;
    }

}
