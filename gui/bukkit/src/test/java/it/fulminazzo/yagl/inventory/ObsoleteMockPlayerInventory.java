package it.fulminazzo.yagl.inventory;

import it.fulminazzo.jbukkit.inventory.MockPlayerInventory;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.IInventory;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Getter
public class ObsoleteMockPlayerInventory extends MockPlayerInventory {
    private final IInventory inventory;

    public ObsoleteMockPlayerInventory(@NotNull Player holder) {
        super(holder);
        this.inventory = new IInventory();
    }

}
