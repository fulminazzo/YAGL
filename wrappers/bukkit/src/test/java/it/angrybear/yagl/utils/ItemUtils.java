package it.angrybear.yagl.utils;

import it.angrybear.yagl.items.AbstractItem;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("deprecation")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemUtils {

    public static ItemStack itemToItemStack(AbstractItem item) {
        return new ItemStack(Material.STONE, 7);
    }
}
