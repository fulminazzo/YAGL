package net.minecraft.server.v1_8_R3;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter
public class Container {
    private final String type;
    private final int size;

    private final int windowId;

    private final @NotNull List<Slot> slots;

    private final @NotNull List<ItemStack> items;

    private final @NotNull List<EntityPlayer> slotListeners;

    private String title;

    private InventoryView bukkitView;

    private Container inventory;

    public Container() {
        this("minecraft:chest", 27, new Random().nextInt());
    }

    public Container(String type, int size, int windowId) {
        this.type = type;
        this.size = size;
        this.windowId = windowId;
        this.slots = new ArrayList<>();
        this.items = new ArrayList<>();
        this.slotListeners = new ArrayList<>();
    }

    public void addSlotListener(EntityPlayer entityPlayer) {
        this.slotListeners.add(entityPlayer);
    }

    public @NotNull Container setTitle(String title) {
        this.title = title;
        return this;
    }
}
