package it.fulminazzo.yagl.inventory;

import it.fulminazzo.jbukkit.inventory.MockInventory;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.IInventory;

@Getter
public class ObsoleteMockInventory extends MockInventory {
    private final IInventory inventory;

    public ObsoleteMockInventory(int size) {
        super(size);
        this.inventory = new IInventory();
    }


}
