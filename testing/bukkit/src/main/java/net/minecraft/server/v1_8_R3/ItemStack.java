package net.minecraft.server.v1_8_R3;

import it.fulminazzo.fulmicollection.objects.FieldEquable;
import lombok.Getter;
import org.bukkit.Material;

@Getter
public class ItemStack extends FieldEquable {
    private final Material material;
    private final int amount;

    public ItemStack(Material material, int amount) {
        this.material = material;
        this.amount = amount;
    }

}
