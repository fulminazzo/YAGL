package org.bukkit.craftbukkit.v1_8_R3.inventory;

import it.fulminazzo.fulmicollection.objects.FieldEquable;
import net.minecraft.server.v1_8_R3.ItemStack;

public class CraftItemStack extends FieldEquable {

    public static ItemStack asNMSCopy(org.bukkit.inventory.ItemStack itemStack) {
        return new ItemStack(itemStack.getType(), itemStack.getAmount());
    }

}
