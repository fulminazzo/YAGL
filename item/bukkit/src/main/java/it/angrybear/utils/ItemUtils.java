package it.angrybear.utils;

import it.angrybear.items.BukkitItem;
import it.angrybear.items.Enchantment;
import it.angrybear.items.Item;
import it.angrybear.items.ItemFlag;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A collection of utilities for {@link Item}.
 */
public class ItemUtils {

    /**
     * Converts the given {@link ItemStack} to an {@link Item}.
     *
     * @param itemStack the item stack
     * @return the item
     */
    public static @Nullable Item itemStackToItem(final @Nullable ItemStack itemStack) {
        if (itemStack == null) return null;
        Item item = new BukkitItem()
                .setMaterial(itemStack.getType().name())
                .setAmount(itemStack.getAmount());
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            String displayName = meta.getDisplayName();
            if (displayName != null) item.setDisplayName(displayName);
            List<String> lore = meta.getLore();
            if (lore != null) item.setLore(lore);
            meta.getEnchants().forEach((e, l) -> item.addEnchantments(new Enchantment(e.getKey().getKey(), l)));
            meta.getItemFlags().forEach(f -> item.addItemFlags(ItemFlag.valueOf(f.name())));
            if (meta instanceof Damageable) item.setDurability((short) ((Damageable) meta).getDamage());
            item.setUnbreakable(meta.isUnbreakable());
        }
        return item;
    }

    /**
     * Converts the given {@link Item} to an {@link ItemStack}.
     *
     * @param item the item
     * @return the item stack
     */
    public static @Nullable ItemStack itemToItemStack(final @Nullable Item item) {
        if (item == null) return null;
        ItemStack itemStack = new ItemStack(Material.valueOf(item.getMaterial()), item.getAmount());
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(item.getDisplayName());
            meta.setLore(item.getLore());
            item.getEnchantments().forEach(e -> {
                NamespacedKey name = new NamespacedKey("minecraft", e.getEnchantment());
                org.bukkit.enchantments.Enchantment enchantment = org.bukkit.enchantments.Enchantment.getByKey(name);
                meta.addEnchant(enchantment, e.getLevel(), true);
            });
            item.getItemFlags().forEach(f -> meta.addItemFlags(org.bukkit.inventory.ItemFlag.valueOf(f.name())));
            if (meta instanceof Damageable) ((Damageable) meta).setDamage(item.getDurability());
            meta.setUnbreakable(item.isUnbreakable());
            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }
}
