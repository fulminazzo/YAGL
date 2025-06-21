package org.bukkit.craftbukkit.v1_8_R3.inventory;

import it.fulminazzo.fulmicollection.objects.FieldEquable;
import org.bukkit.inventory.ItemStack;

public class CraftItemStack extends FieldEquable {

    public static ItemStack asNMSCopy(ItemStack itemStack) {
        return new ItemStack(itemStack.getType(), itemStack.getAmount());
    }

}
