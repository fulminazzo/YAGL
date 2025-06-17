package it.fulminazzo.yagl.utils.legacy.containers;

import it.fulminazzo.jbukkit.inventory.MockInventoryView;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

import static org.mockito.Mockito.mock;

@Getter
public class Container {
    private final Containers type;
    private final Container container;
    private final Container inventory;

    private InventoryView openInventory;

    public Container() {
        this(DefaultContainers.GENERIC_9x3);
    }

    public Container(Containers type) {
        this(type, null, null);
    }

    public Container(Containers type, Container container, Container inventory) {
        this.type = type;
        this.container = container;
        this.inventory = inventory;
    }

    public void setOpenInventory(final @NotNull Inventory inventory) {
        this.openInventory = new MockInventoryView(inventory, mock(Player.class), "");
    }

}
