package it.angrybear.yagl.items;

import it.angrybear.yagl.ItemAdapter;
import it.angrybear.yagl.items.fields.ItemFlag;
import it.angrybear.yagl.wrappers.Enchantment;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * An implementation of {@link Item} for Bukkit.
 */
public interface BukkitItem extends Item {

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
    @SuppressWarnings("DataFlowIssue")
    default <M extends ItemMeta> @NotNull ItemStack create(@Nullable Class<M> itemMetaClass, final @Nullable Consumer<M> metaFunction) {
        if (getMaterial() == null) throw new IllegalArgumentException("Cannot create item from null material");
        ItemStack itemStack = ItemAdapter.itemToItemStack(this);
        if (itemMetaClass != null && metaFunction != null) {
            ItemMeta meta = itemStack.getItemMeta();
            if (meta != null) {
                metaFunction.accept(itemMetaClass.cast(meta));
                itemStack.setItemMeta(meta);
            }
        }
        return itemStack;
    }

    /**
     * Sets material.
     *
     * @param material the material
     * @return this item
     */
    default BukkitItem setMaterial(final @NotNull Material material) {
        return setMaterial(material.name());
    }

    @Override
    default BukkitItem addLore(final String @NotNull ... lore) {
        return (BukkitItem) Item.super.addLore(lore);
    }

    @Override
    default BukkitItem addLore(final @NotNull Collection<String> lore) {
        return (BukkitItem) Item.super.addLore(lore);
    }

    @Override
    default BukkitItem removeLore(final String @NotNull ... lore) {
        return (BukkitItem) Item.super.removeLore(lore);
    }

    @Override
    default BukkitItem removeLore(final @NotNull Collection<String> lore) {
        return (BukkitItem) Item.super.removeLore(lore);
    }

    @Override
    default BukkitItem setLore(final String @NotNull ... lore) {
        return (BukkitItem) Item.super.setLore(lore);
    }

    @Override
    default BukkitItem addEnchantment(final @NotNull String enchantment, final int level) {
        return (BukkitItem) Item.super.addEnchantment(enchantment, level);
    }

    @Override
    default BukkitItem addEnchantments(final String @NotNull ... enchantments) {
        return (BukkitItem) Item.super.addEnchantments(enchantments);
    }

    @Override
    default BukkitItem addEnchantments(final Enchantment @NotNull ... enchantments) {
        return (BukkitItem) Item.super.addEnchantments(enchantments);
    }

    @Override
    default BukkitItem addEnchantments(final @NotNull Collection<Enchantment> enchantments) {
        return (BukkitItem) Item.super.addEnchantments(enchantments);
    }

    @Override
    default BukkitItem removeEnchantment(final @NotNull String enchantment, final int level) {
        return (BukkitItem) Item.super.removeEnchantment(enchantment, level);
    }

    @Override
    default BukkitItem removeEnchantments(final String @NotNull ... enchantments) {
        return (BukkitItem) Item.super.removeEnchantments(enchantments);
    }

    @Override
    default BukkitItem removeEnchantments(final Enchantment @NotNull ... enchantments) {
        return (BukkitItem) Item.super.removeEnchantments(enchantments);
    }

    @Override
    default BukkitItem removeEnchantments(final @NotNull Collection<Enchantment> enchantments) {
        return (BukkitItem) Item.super.removeEnchantments(enchantments);
    }

    @Override
    default BukkitItem addItemFlags(final ItemFlag @NotNull ... itemFlags) {
        return (BukkitItem) Item.super.addItemFlags(itemFlags);
    }

    @Override
    default BukkitItem addItemFlags(final @NotNull Collection<ItemFlag> itemFlags) {
        return (BukkitItem) Item.super.addItemFlags(itemFlags);
    }

    @Override
    default BukkitItem removeItemFlags(final ItemFlag @NotNull ... itemFlags) {
        return (BukkitItem) Item.super.removeItemFlags(itemFlags);
    }

    @Override
    default BukkitItem removeItemFlags(final @NotNull Collection<ItemFlag> itemFlags) {
        return (BukkitItem) Item.super.removeItemFlags(itemFlags);
    }

    @Override
    BukkitItem setCustomModelData(final int customModelData);

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
     * Creates a new item to be used.
     *
     * @return the item
     */
    static @NotNull BukkitItem newItem() {
        return new BukkitItemImpl();
    }

    /**
     * Creates a new item to be used with the given material.
     *
     * @param material the material
     * @return the item
     */
    static @NotNull BukkitItem newItem(final @NotNull String material) {
        return newItem().setMaterial(material);
    }

    /**
     * Creates a new item to be used with the given material.
     *
     * @param material the material
     * @return the item
     */
    static @NotNull BukkitItem newItem(final @NotNull Material material) {
        return newItem(material.name());
    }

    /**
     * Creates a new recipe item to be used.
     *
     * @return the item
     */
    static @NotNull BukkitRecipeItem newRecipeItem() {
        return new BukkitRecipeItemImpl();
    }

    /**
     * Creates a new recipe item to be used with the given material.
     *
     * @param material the material
     * @return the item
     */
    static @NotNull BukkitRecipeItem newRecipeItem(final @NotNull String material) {
        return newRecipeItem().setMaterial(material);
    }

    /**
     * Creates a new recipe item to be used with the given material.
     *
     * @param material the material
     * @return the item
     */
    static @NotNull BukkitRecipeItem newRecipeItem(final @NotNull Material material) {
        return newRecipeItem(material.name());
    }
}
