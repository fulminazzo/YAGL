package net.minecraft.server.v1_8_R3;

import it.fulminazzo.fulmicollection.objects.FieldEquable;
import lombok.Getter;

@Getter
public class Slot extends FieldEquable {
    private final IInventory inventory;
    private final int index;
    private final int x;
    private final int y;

    public Slot(IInventory inventory, int index, int x, int y) {
        this.inventory = inventory;
        this.index = index;
        this.x = x;
        this.y = y;
    }

}
