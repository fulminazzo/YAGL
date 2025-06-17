package it.fulminazzo.yagl.utils.legacy;

import it.fulminazzo.jbukkit.inventory.MockInventoryView;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

import static org.mockito.Mockito.mock;

@Getter
public class Container {
    private final Containers type;

    private InventoryView openInventory;

    public Container() {
        this(DefaultContainers.GENERIC_9x3);
    }

    public Container(Containers type) {
        this.type = type;
    }

    public void setOpenInventory(final @NotNull Inventory inventory) {
        this.openInventory = new MockInventoryView(inventory, mock(Player.class), "");
    }

    public interface Containers {

    }

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

}
