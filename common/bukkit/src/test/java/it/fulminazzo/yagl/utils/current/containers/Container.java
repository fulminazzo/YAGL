package it.fulminazzo.yagl.utils.current.containers;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.InventoryView;

import java.util.Random;

@Getter
@Setter
public abstract class Container {
    private final Containers containers;
    private final int ignored;
    private final int id;

    private InventoryView openInventory;

    public Container() {
        this.containers = Containers.GENERIC9x3;
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
