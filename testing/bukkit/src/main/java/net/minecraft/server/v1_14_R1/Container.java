package net.minecraft.server.v1_14_R1;

import it.fulminazzo.jbukkit.inventory.MockInventoryView;
import lombok.Getter;
import net.minecraft.server.v1_14_R1.containers.Containers;
import net.minecraft.server.v1_14_R1.containers.DefaultContainers;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.mock;

@Getter
public class Container {
    private final Containers type;
    private final Container container;

    private final int windowId;

    private final List<CraftItemStack> items;

    private final List<EntityPlayer> slotListener;

    private InventoryView bukkitView;

    public Container() {
        this(DefaultContainers.GENERIC_9x3);
    }

    public Container(Containers type) {
        this(type, null);
    }

    public Container(Containers type, int windowId) {
        this(type, null, windowId);
    }

    public Container(Containers type, Container container) {
        this(type, container, new Random().nextInt());
    }

    public Container(Containers type, Container container, int windowId) {
        this.type = type;
        this.container = container;
        this.windowId = windowId;
        this.items = new ArrayList<>();
        for (int i = 0; i < type.getSize(); i++) this.items.add(null);
        this.slotListener = new ArrayList<>();
    }

    public void setBukkitView(InventoryView bukkitView) {
        this.bukkitView = bukkitView;
    }

    public void setBukkitView(final @NotNull Inventory inventory) {
        this.bukkitView = new MockInventoryView(inventory, mock(Player.class), "");
    }

    public void setItem(final int slot, CraftItemStack itemStack) {
        this.items.set(slot, itemStack);
    }

    public void addSlotListener(EntityPlayer entityPlayer) {
        this.slotListener.add(entityPlayer);
    }

}
