package net.minecraft.world.inventory;

import net.minecraft.server.v1_14_R1.containers.Containers;
import net.minecraft.server.v1_14_R1.containers.DefaultContainers;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.InventoryView;

import java.util.Random;

@Getter
@Setter
public abstract class Container {
    private final Containers type;
    private final int ignored;
    private final int id;

    private InventoryView openInventory;

    public Container() {
        this.type = DefaultContainers.GENERIC_9x3;
        this.ignored = -1;
        this.id = new Random().nextInt();
    }

    @Getter
    static class ContainerImpl extends Container {
        private final int anotherIgnored;

        public ContainerImpl() {
            super();
            this.anotherIgnored = -1;
        }
    }

    public static Container newContainer() {
        return new ContainerImpl();
    }

}
