package it.angrybear.yagl.utils;

import it.angrybear.yagl.items.AbstractItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class ItemUtils {

    private ItemUtils() {}

    public static ItemStack itemToItemStack(AbstractItem item) {
        return new ItemStack(Material.STONE, 7);
    }
}
