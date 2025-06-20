package net.minecraft.server.v1_14_R1.containers;

import lombok.Getter;
import net.minecraft.server.v1_14_R1.Container;

@Getter
public class InventoryContainer extends Container {
    private final Container inventory;

    public InventoryContainer(Containers type, Container container, Container inventory) {
        super(type, container);
        this.inventory = inventory;
    }

}
