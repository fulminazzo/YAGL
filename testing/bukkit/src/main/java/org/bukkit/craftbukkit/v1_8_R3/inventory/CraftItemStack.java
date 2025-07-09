package org.bukkit.craftbukkit.v1_8_R3.inventory;

import it.fulminazzo.fulmicollection.objects.FieldEquable;
import net.minecraft.server.v1_8_R3.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CraftItemStack extends FieldEquable {

    public static @NotNull ItemStack asNMSCopy(org.bukkit.inventory.@NotNull ItemStack itemStack) {
        return new ItemStack(itemStack.getType(), itemStack.getAmount());
    }

}
