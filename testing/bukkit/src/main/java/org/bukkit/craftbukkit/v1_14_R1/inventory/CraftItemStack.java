package org.bukkit.craftbukkit.v1_14_R1.inventory;

import it.fulminazzo.fulmicollection.objects.FieldEquable;
import lombok.Getter;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
public class CraftItemStack extends FieldEquable {
    private final Material material;
    private final int amount;
    private final NBTTagCompound tag;

    public CraftItemStack(Material material, int amount) {
        this.material = material;
        this.amount = amount;
        this.tag = new NBTTagCompound();
    }

    public static CraftItemStack asNMSCopy(ItemStack itemStack) {
        return new CraftItemStack(itemStack.getType(), itemStack.getAmount());
    }

    public NBTTagCompound getOrCreateTag() {
        return this.tag;
    }

}
