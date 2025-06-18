package it.fulminazzo.yagl.utils.legacy.containers;

import it.fulminazzo.jbukkit.inventory.MockInventoryView;
import lombok.Getter;
import net.minecraft.server.v1_14_R1.containers.Containers;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

import static org.mockito.Mockito.mock;

@Getter
public class Container {
    private final Containers type;
    private final Container container;

    private final int windowId;

    private InventoryView openInventory;

    public Container() {
        this(DefaultContainers.GENERIC_9x3);
    }

    public Container(Containers type) {
        this(type, null);
    }

    public Container(Containers type, Container container) {
        this.type = type;
        this.container = container;
        this.windowId = new Random().nextInt();
    }

    public void setOpenInventory(InventoryView openInventory) {
        this.openInventory = openInventory;
    }

    public void setOpenInventory(final @NotNull Inventory inventory) {
        this.openInventory = new MockInventoryView(inventory, mock(Player.class), "");
    }

}
