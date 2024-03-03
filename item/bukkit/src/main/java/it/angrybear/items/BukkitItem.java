package it.angrybear.items;

import it.angrybear.utils.ItemUtils;
import it.angrybear.utils.MaterialUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * An implementation of {@link Item} for Bukkit.
 */
public class BukkitItem extends ItemImpl {

    @Override
    public BukkitItem setMaterial(@NotNull String material) {
        MaterialUtils.getMaterial(material, true);
        return (BukkitItem) super.setMaterial(material);
    }

    @Override
    public BukkitItem setAmount(final int amount) {
        return (BukkitItem) super.setAmount(amount);
    }

    @Override
    public BukkitItem setDurability(final int durability) {
        return (BukkitItem) super.setDurability(durability);
    }

    @Override
    public BukkitItem setDisplayName(final @NotNull String displayName) {
        return (BukkitItem) super.setDisplayName(displayName);
    }

    @Override
    public BukkitItem setLore(final @NotNull Collection<String> lore) {
        return (BukkitItem) super.setLore(lore);
    }

    @Override
    public BukkitItem setUnbreakable(final boolean unbreakable) {
        return (BukkitItem) super.setUnbreakable(unbreakable);
    }

    /**
     * Create an item stack from this item.
     *
     * @return the item stack
     */
    public @NotNull ItemStack create() {
        return create(null, null);
    }

    /**
     * Create an item stack from this item.
     *
     * @param <M>           the type parameter
     * @param itemMetaClass the type of the item meta
     * @param metaFunction  the item meta function
     * @return the item stack
     */
    public <M extends ItemMeta> @NotNull ItemStack create(Class<M> itemMetaClass, final Consumer<M> metaFunction) {
        if (getMaterial() == null) throw new NullPointerException("Cannot create item from null material");
        ItemStack itemStack = ItemUtils.itemToItemStack(this);
        if (itemStack == null) throw new IllegalStateException("Unreachable code");
        if (metaFunction != null) {
            ItemMeta meta = itemStack.getItemMeta();
            if (meta != null) {
                metaFunction.accept(itemMetaClass.cast(meta));
                itemStack.setItemMeta(meta);
            }
        }
        return itemStack;
    }
}
