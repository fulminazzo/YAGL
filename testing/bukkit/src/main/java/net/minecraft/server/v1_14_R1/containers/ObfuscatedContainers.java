package net.minecraft.server.v1_14_R1.containers;

import lombok.Getter;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

@Getter
public enum ObfuscatedContainers implements Containers {

    A(9, "CHEST"),
    B(18, "CHEST"),
    C(27, "CHEST"),
    D(36, "CHEST"),
    E(45, "CHEST"),
    F(54, "CHEST");

    private final int size;
    private final @NotNull String rawInventoryType;

    ObfuscatedContainers(final int size, final @NotNull String rawInventoryType) {
        this.size = size;
        this.rawInventoryType = rawInventoryType;
    }

    public @NotNull InventoryType getInventoryType() {
        return InventoryType.valueOf(this.rawInventoryType);
    }

}
