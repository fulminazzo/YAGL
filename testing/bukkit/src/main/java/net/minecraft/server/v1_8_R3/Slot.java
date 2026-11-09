package net.minecraft.server.v1_8_R3;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class Slot {
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
