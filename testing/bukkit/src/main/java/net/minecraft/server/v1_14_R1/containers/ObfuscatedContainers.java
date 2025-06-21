package net.minecraft.server.v1_14_R1.containers;

import lombok.Getter;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

@Getter
public enum ObfuscatedContainers implements Containers {

    A(9, InventoryType.CHEST),
    B(18, InventoryType.CHEST),
    C(27, InventoryType.CHEST),
    D(36, InventoryType.CHEST),
    E(45, InventoryType.CHEST),
    F(54, InventoryType.CHEST);

    private final int size;
    private final @NotNull InventoryType inventoryType;

    ObfuscatedContainers(final int size, final @NotNull InventoryType inventoryType) {
        this.size = size;
        this.inventoryType = inventoryType;
    }

}
