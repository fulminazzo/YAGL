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
    public CustomItem(final String material) {
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
    public @NotNull I setMaterial(@NotNull String material) {
        return (I) super.setMaterial(material);
    }

    @Override
    public @NotNull I setAmount(int amount) {
        return (I) super.setAmount(amount);
    }

    @Override
    public @NotNull I setDurability(int durability) {
        return (I) super.setDurability(durability);
    }

    @Override
    public @NotNull I setDisplayName(@NotNull String displayName) {
        return (I) super.setDisplayName(displayName);
    }

    @Override
    public @NotNull I setUnbreakable(boolean unbreakable) {
        return (I) super.setUnbreakable(unbreakable);
    }

    @Override
    public @NotNull I setCustomModelData(int customModelData) {
        return (I) super.setCustomModelData(customModelData);
    }

    @Override
    public @NotNull I addLore(String @NotNull ... lore) {
        return (I) super.addLore(lore);
    }

    @Override
    public @NotNull I addLore(@NotNull Collection<String> lore) {
        return (I) super.addLore(lore);
    }

    @Override
    public @NotNull I removeLore(String @NotNull ... lore) {
        return (I) super.removeLore(lore);
    }

    @Override
    public @NotNull I removeLore(@NotNull Collection<String> lore) {
        return (I) super.removeLore(lore);
    }

    @Override
    public @NotNull I setLore(String @NotNull ... lore) {
        return (I) super.setLore(lore);
    }

    @Override
    public @NotNull I setLore(@NotNull Collection<String> lore) {
        return (I) super.setLore(lore);
    }

    @Override
    public @NotNull I addEnchantment(@NotNull String enchantment, int level) {
        return (I) super.addEnchantment(enchantment, level);
    }

    @Override
    public @NotNull I addEnchantments(String @NotNull ... enchantments) {
        return (I) super.addEnchantments(enchantments);
    }

    @Override
    public @NotNull I addEnchantments(Enchantment @NotNull ... enchantments) {
        return (I) super.addEnchantments(enchantments);
    }

    @Override
    public @NotNull I addEnchantments(@NotNull Collection<Enchantment> enchantments) {
        return (I) super.addEnchantments(enchantments);
    }

    @Override
    public @NotNull I removeEnchantment(@NotNull String enchantment, int level) {
        return (I) super.removeEnchantment(enchantment, level);
    }

    @Override
    public @NotNull I removeEnchantments(String @NotNull ... enchantments) {
        return (I) super.removeEnchantments(enchantments);
    }

    @Override
    public @NotNull I removeEnchantments(Enchantment @NotNull ... enchantments) {
        return (I) super.removeEnchantments(enchantments);
    }

    @Override
    public @NotNull I removeEnchantments(@NotNull Collection<Enchantment> enchantments) {
        return (I) super.removeEnchantments(enchantments);
    }

    @Override
    public @NotNull I addItemFlags(ItemFlag @NotNull ... itemFlags) {
        return (I) super.addItemFlags(itemFlags);
    }

    @Override
    public @NotNull I addItemFlags(@NotNull Collection<ItemFlag> itemFlags) {
        return (I) super.addItemFlags(itemFlags);
    }

    @Override
    public @NotNull I removeItemFlags(ItemFlag @NotNull ... itemFlags) {
        return (I) super.removeItemFlags(itemFlags);
    }

    @Override
    public @NotNull I removeItemFlags(@NotNull Collection<ItemFlag> itemFlags) {
        return (I) super.removeItemFlags(itemFlags);
    }

    @Override
    public @NotNull I addPotionEffects(PotionEffect @NotNull ... potionEffects) {
        return (I) super.addPotionEffects(potionEffects);
    }

    @Override
    public @NotNull I addPotionEffects(@NotNull Collection<PotionEffect> potionEffects) {
        return (I) super.addPotionEffects(potionEffects);
    }

    @Override
    public @NotNull I removePotionEffects(PotionEffect @NotNull ... potionEffects) {
        return (I) super.removePotionEffects(potionEffects);
    }

    @Override
    public @NotNull I removePotionEffects(@NotNull Collection<PotionEffect> potionEffects) {
        return (I) super.removePotionEffects(potionEffects);
    }

    @Override
    public @NotNull I setPotionEffects(PotionEffect @NotNull ... potionEffects) {
        return (I) super.setPotionEffects(potionEffects);
    }

    @Override
    public @NotNull I setPotionEffects(@NotNull Collection<PotionEffect> potionEffects) {
        return (I) super.setPotionEffects(potionEffects);
    }

    @Override
    public @NotNull I copy() {
        return (I) super.copy();
    }

}
