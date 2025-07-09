package it.fulminazzo.yagl.item;

import it.fulminazzo.yagl.ItemAdapter;
import it.fulminazzo.yagl.item.field.ItemFlag;
import it.fulminazzo.yagl.wrapper.Enchantment;
import it.fulminazzo.yagl.wrapper.PotionEffect;
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
     * Uses the values previously set in {@link #setMetadata(Class, Consumer)}.
     *
     * @return the item stack
     */
    @NotNull ItemStack create();

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
        if (getMaterial() == null) throw new IllegalArgumentException("Cannot create item from null material. Item: " + this);
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
     * Sets the {@link ItemMeta} data for the current item.
     * These will then be used by {@link #create()} to create the corresponding {@link ItemStack},
     * as if {@link #create(Class, Consumer)} was invoked.
     *
     * @param <M>           the type of the item meta
     * @param itemMetaClass the item meta class
     * @param metaFunction  the meta function
     * @return this item
     */
    <M extends ItemMeta> @NotNull BukkitItem setMetadata(final @Nullable Class<M> itemMetaClass, final @Nullable Consumer<M> metaFunction);

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
    default @NotNull BukkitItem addLore(final String @NotNull ... lore) {
        return (BukkitItem) Item.super.addLore(lore);
    }

    @Override
    default @NotNull BukkitItem addLore(final @NotNull Collection<String> lore) {
        return (BukkitItem) Item.super.addLore(lore);
    }

    @Override
    default @NotNull BukkitItem removeLore(final String @NotNull ... lore) {
        return (BukkitItem) Item.super.removeLore(lore);
    }

    @Override
    default @NotNull BukkitItem removeLore(final @NotNull Collection<String> lore) {
        return (BukkitItem) Item.super.removeLore(lore);
    }

    @Override
    default @NotNull BukkitItem setLore(final String @NotNull ... lore) {
        return (BukkitItem) Item.super.setLore(lore);
    }

    @Override
    default @NotNull BukkitItem addEnchantment(final @NotNull String enchantment, final int level) {
        return (BukkitItem) Item.super.addEnchantment(enchantment, level);
    }

    @Override
    default @NotNull BukkitItem addEnchantments(final String @NotNull ... enchantments) {
        return (BukkitItem) Item.super.addEnchantments(enchantments);
    }

    @Override
    default @NotNull BukkitItem addEnchantments(final Enchantment @NotNull ... enchantments) {
        return (BukkitItem) Item.super.addEnchantments(enchantments);
    }

    @Override
    default @NotNull BukkitItem addEnchantments(final @NotNull Collection<Enchantment> enchantments) {
        return (BukkitItem) Item.super.addEnchantments(enchantments);
    }

    @Override
    default @NotNull BukkitItem removeEnchantment(final @NotNull String enchantment, final int level) {
        return (BukkitItem) Item.super.removeEnchantment(enchantment, level);
    }

    @Override
    default @NotNull BukkitItem removeEnchantments(final String @NotNull ... enchantments) {
        return (BukkitItem) Item.super.removeEnchantments(enchantments);
    }

    @Override
    default @NotNull BukkitItem removeEnchantments(final Enchantment @NotNull ... enchantments) {
        return (BukkitItem) Item.super.removeEnchantments(enchantments);
    }

    @Override
    default @NotNull BukkitItem removeEnchantments(final @NotNull Collection<Enchantment> enchantments) {
        return (BukkitItem) Item.super.removeEnchantments(enchantments);
    }

    @Override
    default @NotNull BukkitItem addItemFlags(final ItemFlag @NotNull ... itemFlags) {
        return (BukkitItem) Item.super.addItemFlags(itemFlags);
    }

    @Override
    default @NotNull BukkitItem addItemFlags(final @NotNull Collection<ItemFlag> itemFlags) {
        return (BukkitItem) Item.super.addItemFlags(itemFlags);
    }

    @Override
    default @NotNull BukkitItem removeItemFlags(final ItemFlag @NotNull ... itemFlags) {
        return (BukkitItem) Item.super.removeItemFlags(itemFlags);
    }

    @Override
    default @NotNull BukkitItem removeItemFlags(final @NotNull Collection<ItemFlag> itemFlags) {
        return (BukkitItem) Item.super.removeItemFlags(itemFlags);
    }

    @Override
    @NotNull BukkitItem setCustomModelData(final int customModelData);

    @Override
    @NotNull BukkitItem setMaterial(@NotNull String material);

    @Override
    @NotNull BukkitItem setAmount(final int amount);

    @Override
    @NotNull BukkitItem setDurability(final int durability);

    @Override
    @NotNull BukkitItem setDisplayName(final @NotNull String displayName);

    @Override
    @NotNull BukkitItem setLore(final @NotNull Collection<String> lore);

    @Override
    @NotNull BukkitItem setUnbreakable(final boolean unbreakable);

    @Override
    default @NotNull BukkitItem addPotionEffects(final PotionEffect @NotNull ... potionEffects) {
        return (BukkitItem) Item.super.addPotionEffects(potionEffects);
    }

    @Override
    default @NotNull BukkitItem addPotionEffects(final @NotNull Collection<PotionEffect> potionEffects) {
        return (BukkitItem) Item.super.addPotionEffects(potionEffects);
    }

    @Override
    default @NotNull BukkitItem removePotionEffects(final PotionEffect @NotNull ... potionEffects) {
        return (BukkitItem) Item.super.removePotionEffects(potionEffects);
    }

    @Override
    default @NotNull BukkitItem removePotionEffects(final @NotNull Collection<PotionEffect> potionEffects) {
        return (BukkitItem) Item.super.removePotionEffects(potionEffects);
    }

    @Override
    default @NotNull BukkitItem setPotionEffects(final PotionEffect @NotNull ... potionEffects) {
        return (BukkitItem) Item.super.setPotionEffects(potionEffects);
    }

    @Override
    default @NotNull BukkitItem setPotionEffects(final @NotNull Collection<PotionEffect> potionEffects) {
        return (BukkitItem) Item.super.setPotionEffects(potionEffects);
    }

    @Override
    @NotNull BukkitItem copy();

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
