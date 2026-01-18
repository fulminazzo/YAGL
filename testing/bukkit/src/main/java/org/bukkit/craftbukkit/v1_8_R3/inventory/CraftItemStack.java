package org.bukkit.craftbukkit.v1_8_R3.inventory;

import lombok.Getter;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@Getter
public class CraftItemStack extends net.minecraft.server.v1_8_R3.ItemStack {
    private final @NotNull NBTTagCompound tag;

    public CraftItemStack(Material material, int amount) {
        super(material, amount);
        this.tag = new NBTTagCompound();
    }

    public static @NotNull CraftItemStack asNMSCopy(@NotNull ItemStack itemStack) {
        return new CraftItemStack(itemStack.getType(), itemStack.getAmount());
    }

    public @NotNull NBTTagCompound getOrCreateTag() {
        return this.tag;
    }

}
