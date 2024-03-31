package it.angrybear.yagl.items;

import it.angrybear.yagl.utils.BukkitUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BukkitItemTest {

    @Test
    void testMetaCreation() {
        BukkitUtils.setupItemFactory();
        ItemStack expected = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = expected.getItemMeta();
        ((EnchantmentStorageMeta) meta).addStoredEnchant(Enchantment.CHANNELING, 1, true);
        expected.setItemMeta(meta);

        ItemStack actual = BukkitItem.newItem()
                .setMaterial(Material.ENCHANTED_BOOK.name())
                .create(EnchantmentStorageMeta.class, m -> m.addStoredEnchant(Enchantment.CHANNELING, 1, true));

        assertEquals(expected, actual);
    }
}