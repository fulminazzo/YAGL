package org.bukkit.craftbukkit.v1_8_R3.inventory;

import it.fulminazzo.jbukkit.inventory.MockInventoryView;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.Container;
import net.minecraft.server.v1_8_R3.ContainerAnvil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

@Getter
public class CraftContainer extends Container {
    private final Inventory inventory;
    private final Player owner;

    public CraftContainer(Inventory inventory, Player owner, int windowId) {
        this(inventory, owner, windowId, new ContainerAnvil(windowId));
    }

    public CraftContainer(Inventory inventory, Player owner, int windowId, Container delegate) {
        super(delegate.getType(), 0, windowId);
        this.inventory = inventory;
        this.owner = owner;

        setBukkitView(new MockInventoryView(this.inventory, this.owner, "Hello, world!"));
    }

}
