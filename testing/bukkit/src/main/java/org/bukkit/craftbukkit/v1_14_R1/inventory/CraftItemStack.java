package org.bukkit.craftbukkit.v1_14_R1.inventory;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@Getter
@EqualsAndHashCode
@ToString
public class CraftItemStack {
    private final Material material;
    private final int amount;
    private final @NotNull NBTTagCompound tag;

    public CraftItemStack(Material material, int amount) {
        this.material = material;
        this.amount = amount;
        this.tag = new NBTTagCompound();
    }

    public static @NotNull CraftItemStack asNMSCopy(@NotNull ItemStack itemStack) {
        return new CraftItemStack(itemStack.getType(), itemStack.getAmount());
    }

    public @NotNull NBTTagCompound getOrCreateTag() {
        return this.tag;
    }

}
