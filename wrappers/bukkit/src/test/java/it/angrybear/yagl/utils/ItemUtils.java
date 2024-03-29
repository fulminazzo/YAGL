package it.angrybear.yagl.utils;

import it.angrybear.yagl.items.Item;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemUtils {

    public static ItemStack itemToItemStack(Item item) {
        return new ItemStack(Material.STONE, 7);
    }
}
