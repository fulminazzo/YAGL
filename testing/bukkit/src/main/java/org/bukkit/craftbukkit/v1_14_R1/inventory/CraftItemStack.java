package org.bukkit.craftbukkit.v1_14_R1.inventory;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
public class CraftItemStack {
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
