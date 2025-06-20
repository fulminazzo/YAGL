package org.bukkit.craftbukkit.v1_14_R1.inventory;

import lombok.Getter;
import net.minecraft.server.v1_14_R1.Container;
import net.minecraft.server.v1_14_R1.ContainerAnvil;
import net.minecraft.server.v1_14_R1.EntityPlayer;
import org.bukkit.inventory.Inventory;

@Getter
public class CraftContainer {
    private final Inventory inventory;
    private final EntityPlayer owner;

    private final Container delegate;

    public CraftContainer(Inventory inventory, EntityPlayer owner, int windowId) {
        this.inventory = inventory;
        this.owner = owner;
        this.delegate = new ContainerAnvil(windowId);
    }

}
