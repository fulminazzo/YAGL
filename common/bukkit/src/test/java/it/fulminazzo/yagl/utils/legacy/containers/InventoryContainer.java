package it.fulminazzo.yagl.utils.legacy.containers;

import lombok.Getter;

@Getter
public class InventoryContainer extends Container {
    private final Container inventory;

    public InventoryContainer(Containers type, Container container, Container inventory) {
        super(type, container);
        this.inventory = inventory;
    }

}
