package it.fulminazzo.yagl.item;

import it.fulminazzo.yagl.item.field.ItemFlag;
import it.fulminazzo.yagl.wrapper.Enchantment;
import it.fulminazzo.yagl.wrapper.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Represents a special {@link Item} that supports customization capabilities.
 *
 * @param <I> the type of this custom item
 */
@SuppressWarnings("unchecked")
public abstract class CustomItem<I extends CustomItem<I>> extends ItemImpl {

    /**
     * Instantiates a new Custom item.
     */
    public CustomItem() {
        super();
    }

    /**
     * Instantiates a new Custom item.
     *
     * @param material the material
     */
    public CustomItem(final @Nullable String material) {
        super(material);
    }

    /**
     * Instantiates a new Custom item.
     *
     * @param material the material
     * @param amount   the amount
     */
    public CustomItem(final @Nullable String material, final int amount) {
        super(material, amount);
    }

    @Override
    public @NotNull I setMaterial(final @NotNull String material) {
        return (I) super.setMaterial(material);
    }

    @Override
    public @NotNull I setAmount(final int amount) {
        return (I) super.setAmount(amount);
    }

    @Override
    public @NotNull I setDurability(final int durability) {
        return (I) super.setDurability(durability);
    }

    @Override
    public @NotNull I setDisplayName(final @NotNull String displayName) {
        return (I) super.setDisplayName(displayName);
    }

    @Override
    public @NotNull I setUnbreakable(final boolean unbreakable) {
        return (I) super.setUnbreakable(unbreakable);
    }

    @Override
    public @NotNull I setCustomModelData(final int customModelData) {
        return (I) super.setCustomModelData(customModelData);
    }

    @Override
    public @NotNull I addLore(final String @NotNull ... lore) {
        return (I) super.addLore(lore);
    }

    @Override
    public @NotNull I addLore(final @NotNull Collection<String> lore) {
        return (I) super.addLore(lore);
    }

    @Override
    public @NotNull I removeLore(final String @NotNull ... lore) {
        return (I) super.removeLore(lore);
    }

    @Override
    public @NotNull I removeLore(final @NotNull Collection<String> lore) {
        return (I) super.removeLore(lore);
    }

    @Override
    public @NotNull I setLore(final String @NotNull ... lore) {
        return (I) super.setLore(lore);
    }

    @Override
    public @NotNull I setLore(final @NotNull Collection<String> lore) {
        return (I) super.setLore(lore);
    }

    @Override
    public @NotNull I addEnchantment(final @NotNull String enchantment, final int level) {
        return (I) super.addEnchantment(enchantment, level);
    }

    @Override
    public @NotNull I addEnchantments(final String @NotNull ... enchantments) {
        return (I) super.addEnchantments(enchantments);
    }

    @Override
    public @NotNull I addEnchantments(final Enchantment @NotNull ... enchantments) {
        return (I) super.addEnchantments(enchantments);
    }

    @Override
    public @NotNull I addEnchantments(final @NotNull Collection<Enchantment> enchantments) {
        return (I) super.addEnchantments(enchantments);
    }

    @Override
    public @NotNull I removeEnchantment(final @NotNull String enchantment, int level) {
        return (I) super.removeEnchantment(enchantment, level);
    }

    @Override
    public @NotNull I removeEnchantments(final String @NotNull ... enchantments) {
        return (I) super.removeEnchantments(enchantments);
    }

    @Override
    public @NotNull I removeEnchantments(final Enchantment @NotNull ... enchantments) {
        return (I) super.removeEnchantments(enchantments);
    }

    @Override
    public @NotNull I removeEnchantments(final @NotNull Collection<Enchantment> enchantments) {
        return (I) super.removeEnchantments(enchantments);
    }

    @Override
    public @NotNull I addItemFlags(final ItemFlag @NotNull ... itemFlags) {
        return (I) super.addItemFlags(itemFlags);
    }

    @Override
    public @NotNull I addItemFlags(final @NotNull Collection<ItemFlag> itemFlags) {
        return (I) super.addItemFlags(itemFlags);
    }

    @Override
    public @NotNull I removeItemFlags(final ItemFlag @NotNull ... itemFlags) {
        return (I) super.removeItemFlags(itemFlags);
    }

    @Override
    public @NotNull I removeItemFlags(final @NotNull Collection<ItemFlag> itemFlags) {
        return (I) super.removeItemFlags(itemFlags);
    }

    @Override
    public @NotNull I addPotionEffects(final PotionEffect @NotNull ... potionEffects) {
        return (I) super.addPotionEffects(potionEffects);
    }

    @Override
    public @NotNull I addPotionEffects(final @NotNull Collection<PotionEffect> potionEffects) {
        return (I) super.addPotionEffects(potionEffects);
    }

    @Override
    public @NotNull I removePotionEffects(final PotionEffect @NotNull ... potionEffects) {
        return (I) super.removePotionEffects(potionEffects);
    }

    @Override
    public @NotNull I removePotionEffects(final @NotNull Collection<PotionEffect> potionEffects) {
        return (I) super.removePotionEffects(potionEffects);
    }

    @Override
    public @NotNull I setPotionEffects(final PotionEffect @NotNull ... potionEffects) {
        return (I) super.setPotionEffects(potionEffects);
    }

    @Override
    public @NotNull I setPotionEffects(final @NotNull Collection<PotionEffect> potionEffects) {
        return (I) super.setPotionEffects(potionEffects);
    }

    @Override
    public @NotNull I copy() {
        return (I) super.copy();
    }

}
