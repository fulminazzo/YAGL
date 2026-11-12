package it.fulminazzo.yagl.content;

import it.fulminazzo.yagl.item.Constants;
import it.fulminazzo.yagl.item.Item;
import it.fulminazzo.yagl.item.field.ItemField;
import it.fulminazzo.yagl.item.field.ItemFlag;
import it.fulminazzo.yagl.wrapper.Enchantment;
import it.fulminazzo.yagl.wrapper.PotionEffect;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * A special type of {@link CustomGUIContent} which acts as a wrapper for a {@link Item}.
 * The item can later be used for rendering purposes.
 *
 * @param <C> the type of this content (for method chaining)
 */
@SuppressWarnings("unchecked")
@EqualsAndHashCode(callSuper = true)
public abstract class CustomItemGUIContent<C extends CustomItemGUIContent<C>>
        extends CustomGUIContent<C>
        implements Item {
    protected final @NotNull Item item;

    protected CustomItemGUIContent() {
        this(Item.newItem());
    }

    protected CustomItemGUIContent(final @NotNull String material) {
        this(Item.newItem(material));
    }

    protected CustomItemGUIContent(final @NotNull Item item) {
        this.item = item;
    }

    @Override
    public @NotNull <I extends Item> I copy(final @NotNull Class<I> type) {
        I copy = item.copy(type);
        if (copy instanceof GUIContent) copyDataTo((GUIContent) copy);
        return copy;
    }

    @Override
    public @NotNull C setMaterial(final @NotNull String material) {
        this.item.setMaterial(material);
        return (C) this;
    }

    @Override
    public @Nullable String getMaterial() {
        return this.item.getMaterial();
    }

    @Override
    public @NotNull C setAmount(final @Range(from = Constants.MIN_AMOUNT, to = Integer.MAX_VALUE) int amount) {
        this.item.setAmount(amount);
        return (C) this;
    }

    @Override
    public @Range(from = Constants.MIN_AMOUNT, to = Integer.MAX_VALUE) int getAmount() {
        return this.item.getAmount();
    }

    @Override
    public @NotNull C setDurability(final @Range(from = Constants.MIN_DURABILITY, to = Short.MAX_VALUE) int durability) {
        this.item.setDurability(durability);
        return (C) this;
    }

    @Override
    public @Range(from = Constants.MIN_DURABILITY, to = Short.MAX_VALUE) int getDurability() {
        return this.item.getDurability();
    }

    @Override
    public @NotNull C setDisplayName(final @NotNull String displayName) {
        this.item.setDisplayName(displayName);
        return (C) this;
    }

    @Override
    public @NotNull String getDisplayName() {
        return this.item.getDisplayName();
    }

    @Override
    public @NotNull List<String> getLore() {
        return this.item.getLore();
    }

    @Override
    public @NotNull Set<Enchantment> getEnchantments() {
        return this.item.getEnchantments();
    }

    @Override
    public @NotNull Set<ItemFlag> getItemFlags() {
        return this.item.getItemFlags();
    }

    @Override
    public @NotNull Set<PotionEffect> getPotionEffects() {
        return this.item.getPotionEffects();
    }

    @Override
    public @NotNull C setUnbreakable(final boolean unbreakable) {
        this.item.setUnbreakable(unbreakable);
        return (C) this;
    }

    @Override
    public boolean isUnbreakable() {
        return this.item.isUnbreakable();
    }

    @Override
    public @NotNull C setCustomModelData(final int customModelData) {
        this.item.setCustomModelData(customModelData);
        return (C) this;
    }

    @Override
    public int getCustomModelData() {
        return this.item.getCustomModelData();
    }

    @Override
    public boolean isSimilar(final @Nullable Item item, final ItemField @NotNull ... ignore) {
        return this.item.isSimilar(item, ignore);
    }

    @Override
    public @NotNull C addLore(final String @NotNull ... lore) {
        return (C) Item.super.addLore(lore);
    }

    @Override
    public @NotNull C addLore(final @NotNull Collection<String> lore) {
        return (C) Item.super.addLore(lore);
    }

    @Override
    public @NotNull C removeLore(final String @NotNull ... lore) {
        return (C) Item.super.removeLore(lore);
    }

    @Override
    public @NotNull C removeLore(final @NotNull Collection<String> lore) {
        return (C) Item.super.removeLore(lore);
    }

    @Override
    public @NotNull C setLore(final String @NotNull ... lore) {
        return (C) Item.super.setLore(lore);
    }

    @Override
    public @NotNull C setLore(final @NotNull Collection<String> lore) {
        return (C) Item.super.setLore(lore);
    }

    @Override
    public @NotNull C addEnchantment(final @NotNull String enchantment, final int level) {
        return (C) Item.super.addEnchantment(enchantment, level);
    }

    @Override
    public @NotNull C addEnchantments(final String @NotNull ... enchantments) {
        return (C) Item.super.addEnchantments(enchantments);
    }

    @Override
    public @NotNull C addEnchantments(final Enchantment @NotNull ... enchantments) {
        return (C) Item.super.addEnchantments(enchantments);
    }

    @Override
    public @NotNull C addEnchantments(final @NotNull Collection<Enchantment> enchantments) {
        return (C) Item.super.addEnchantments(enchantments);
    }

    @Override
    public @NotNull C removeEnchantment(final @NotNull String enchantment, final int level) {
        return (C) Item.super.removeEnchantment(enchantment, level);
    }

    @Override
    public @NotNull C removeEnchantments(final String @NotNull ... enchantments) {
        return (C) Item.super.removeEnchantments(enchantments);
    }

    @Override
    public @NotNull C removeEnchantments(final Enchantment @NotNull ... enchantments) {
        return (C) Item.super.removeEnchantments(enchantments);
    }

    @Override
    public @NotNull C removeEnchantments(final @NotNull Collection<Enchantment> enchantments) {
        return (C) Item.super.removeEnchantments(enchantments);
    }

    @Override
    public @NotNull C addItemFlags(final ItemFlag @NotNull ... itemFlags) {
        return (C) Item.super.addItemFlags(itemFlags);
    }

    @Override
    public @NotNull C addItemFlags(final @NotNull Collection<ItemFlag> itemFlags) {
        return (C) Item.super.addItemFlags(itemFlags);
    }

    @Override
    public @NotNull C removeItemFlags(final ItemFlag @NotNull ... itemFlags) {
        return (C) Item.super.removeItemFlags(itemFlags);
    }

    @Override
    public @NotNull C removeItemFlags(final @NotNull Collection<ItemFlag> itemFlags) {
        return (C) Item.super.removeItemFlags(itemFlags);
    }

    @Override
    public @NotNull C addPotionEffects(final PotionEffect @NotNull ... potionEffects) {
        return (C) Item.super.addPotionEffects(potionEffects);
    }

    @Override
    public @NotNull C addPotionEffects(final @NotNull Collection<PotionEffect> potionEffects) {
        return (C) Item.super.addPotionEffects(potionEffects);
    }

    @Override
    public @NotNull C removePotionEffects(final PotionEffect @NotNull ... potionEffects) {
        return (C) Item.super.removePotionEffects(potionEffects);
    }

    @Override
    public @NotNull C removePotionEffects(final @NotNull Collection<PotionEffect> potionEffects) {
        return (C) Item.super.removePotionEffects(potionEffects);
    }

    @Override
    public @NotNull C setPotionEffects(final PotionEffect @NotNull ... potionEffects) {
        return (C) Item.super.setPotionEffects(potionEffects);
    }

    @Override
    public @NotNull C setPotionEffects(final @NotNull Collection<PotionEffect> potionEffects) {
        return (C) Item.super.setPotionEffects(potionEffects);
    }

    @Override
    public @NotNull C copy() {
        return (C) super.copy();
    }

    @Override
    public @NotNull String toString() {
        return item.toString();
    }

}
