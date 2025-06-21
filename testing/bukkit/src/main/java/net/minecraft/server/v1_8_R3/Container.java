package net.minecraft.server.v1_8_R3;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.InventoryView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter
public class Container {
    private final String type;

    private final int windowId;

    private final List<Slot> slots;

    private final List<CraftItemStack> items;

    private final List<EntityPlayer> slotListeners;

    private InventoryView bukkitView;

    public Container() {
        this("minecraft:chest", 27, new Random().nextInt());
    }

    public Container(String type, int size, int windowId) {
        this.type = type;
        this.windowId = windowId;
        this.slots = new ArrayList<>();
        this.items = new ArrayList<>();
        for (int i = 0; i < size; i++) this.items.add(null);
        this.slotListeners = new ArrayList<>();
    }

}
