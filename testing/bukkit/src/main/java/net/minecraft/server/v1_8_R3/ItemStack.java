package net.minecraft.server.v1_8_R3;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Material;

@Getter
@EqualsAndHashCode
@ToString
public class ItemStack {
    protected final Material material;
    protected final int amount;

    public ItemStack(Material material, int amount) {
        this.material = material;
        this.amount = amount;
    }

}
