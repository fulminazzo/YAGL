package it.angrybear.yagl.items;

import it.angrybear.yagl.ItemAdapter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * An implementation of {@link Item} for Bukkit.
 */
public interface BukkitItem extends Item {

    @Override
    BukkitItem setMaterial(@NotNull String material);

    @Override
    BukkitItem setAmount(final int amount);

    @Override
    BukkitItem setDurability(final int durability);

    @Override
    BukkitItem setDisplayName(final @NotNull String displayName);

    @Override
    BukkitItem setLore(final @NotNull Collection<String> lore);

    @Override
    BukkitItem setUnbreakable(final boolean unbreakable);

    @Override
    BukkitItem copy();

    /**
     * Compares the given {@link ItemStack} with the one created from {@link #create()}.
     *
     * @param itemStack the item stack
     * @return true if they are similar (uses {@link ItemStack#isSimilar(ItemStack)})
     */
    default boolean isSimilar(final ItemStack itemStack) {
        return create().isSimilar(itemStack);
    }

    /**
     * Create an item stack from this item.
     *
     * @return the item stack
     */
    @NotNull
    default ItemStack create() {
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
    default <M extends ItemMeta> @NotNull ItemStack create(Class<M> itemMetaClass, final Consumer<M> metaFunction) {
        if (getMaterial() == null) throw new NullPointerException("Cannot create item from null material");
        ItemStack itemStack = ItemAdapter.itemToItemStack(this);
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

    /**
     * Creates a new item to be used.
     *
     * @return the item
     */
    static BukkitItem newItem() {
        return new BukkitItemImpl();
    }

    /**
     * Creates a new recipe item to be used.
     *
     * @return the item
     */
    static RecipeItem newRecipeItem() {
        return new BukkitRecipeItemImpl();
    }
}
