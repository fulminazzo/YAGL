package org.bukkit.craftbukkit.v1_14_R1.inventory;

import it.fulminazzo.jbukkit.inventory.MockInventoryView;
import lombok.Getter;
import net.minecraft.server.v1_14_R1.Container;
import net.minecraft.server.v1_14_R1.ContainerAnvil;
import net.minecraft.server.v1_14_R1.EntityPlayer;
import net.minecraft.server.v1_14_R1.containers.Containers;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

@Getter
public class CraftContainer {
    private final Inventory inventory;
    private final EntityPlayer owner;
    private final int windowId;

    private final Container delegate;
    private final Containers type;

    public CraftContainer(Inventory inventory, EntityPlayer owner, int windowId) {
        this.inventory = inventory;
        this.owner = owner;
        this.windowId = windowId;
        this.delegate = new ContainerAnvil(windowId);
        this.type = this.delegate.getType();
    }

    public InventoryView getBukkitView() {
        return new MockInventoryView(this.inventory, this.owner.getPlayer(), "Hello, world!");
    }

}
