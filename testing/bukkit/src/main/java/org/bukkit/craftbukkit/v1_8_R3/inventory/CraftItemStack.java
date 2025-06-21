package org.bukkit.craftbukkit.v1_8_R3.inventory;

import it.fulminazzo.fulmicollection.objects.FieldEquable;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
public class CraftItemStack extends FieldEquable {
    private final Material material;
    private final int amount;

    public CraftItemStack(Material material, int amount) {
        this.material = material;
        this.amount = amount;
    }

    public static CraftItemStack asNMSCopy(ItemStack itemStack) {
        return new CraftItemStack(itemStack.getType(), itemStack.getAmount());
    }

}
