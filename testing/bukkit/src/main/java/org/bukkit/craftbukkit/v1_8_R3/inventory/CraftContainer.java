package org.bukkit.craftbukkit.v1_8_R3.inventory;

import it.fulminazzo.jbukkit.inventory.MockInventoryView;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.Container;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

@Getter
public class CraftContainer extends Container {
    private final Inventory bukkitInventory;
    private final Player owner;

    public CraftContainer(Inventory inventory, Player owner, int windowId) {
        this(inventory, owner, windowId, "minecraft:anvil", 3);
    }

    public CraftContainer(Inventory inventory, Player owner, int windowId, String type, int size) {
        super(type, size, windowId);
        this.bukkitInventory = inventory;
        this.owner = owner;

        setBukkitView(new MockInventoryView(this.bukkitInventory, this.owner, "Hello, world!"));
    }

}
