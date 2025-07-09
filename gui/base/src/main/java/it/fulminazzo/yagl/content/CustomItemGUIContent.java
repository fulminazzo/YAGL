package it.fulminazzo.yagl.content;

import it.fulminazzo.yagl.item.Item;
import it.fulminazzo.yagl.item.field.ItemField;
import it.fulminazzo.yagl.item.field.ItemFlag;
import it.fulminazzo.yagl.wrapper.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    public @NotNull C setMaterial(@NotNull String material) {
        this.item.setMaterial(material);
        return (C) this;
    }

    @Override
    public @Nullable String getMaterial() {
        return this.item.getMaterial();
    }

    @Override
    public @NotNull C setAmount(int amount) {
        this.item.setAmount(amount);
        return (C) this;
    }

    @Override
    public int getAmount() {
        return this.item.getAmount();
    }

    @Override
    public @NotNull C setDurability(int durability) {
        this.item.setDurability(durability);
        return (C) this;
    }

    @Override
    public int getDurability() {
        return this.item.getDurability();
    }

    @Override
    public @NotNull C setDisplayName(@NotNull String displayName) {
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
    public @NotNull C setUnbreakable(boolean unbreakable) {
        this.item.setUnbreakable(unbreakable);
        return (C) this;
    }

    @Override
    public boolean isUnbreakable() {
        return this.item.isUnbreakable();
    }

    @Override
    public @NotNull C setCustomModelData(int customModelData) {
        this.item.setCustomModelData(customModelData);
        return (C) this;
    }

    @Override
    public int getCustomModelData() {
        return this.item.getCustomModelData();
    }

    @Override
    public boolean isSimilar(@Nullable Item item, ItemField @NotNull ... ignore) {
        return this.item.isSimilar(item, ignore);
    }

    @Override
    public @NotNull C addLore(String @NotNull ... lore) {
        return (C) Item.super.addLore(lore);
    }

    @Override
    public @NotNull C addLore(@NotNull Collection<String> lore) {
        return (C) Item.super.addLore(lore);
    }

    @Override
    public @NotNull C removeLore(String @NotNull ... lore) {
        return (C) Item.super.removeLore(lore);
    }

    @Override
    public @NotNull C removeLore(@NotNull Collection<String> lore) {
        return (C) Item.super.removeLore(lore);
    }

    @Override
    public @NotNull C setLore(String @NotNull ... lore) {
        return (C) Item.super.setLore(lore);
    }

    @Override
    public @NotNull C setLore(@NotNull Collection<String> lore) {
        return (C) Item.super.setLore(lore);
    }

    @Override
    public @NotNull C addEnchantment(@NotNull String enchantment, int level) {
        return (C) Item.super.addEnchantment(enchantment, level);
    }

    @Override
    public @NotNull C addEnchantments(String @NotNull ... enchantments) {
        return (C) Item.super.addEnchantments(enchantments);
    }

    @Override
    public @NotNull C addEnchantments(Enchantment @NotNull ... enchantments) {
        return (C) Item.super.addEnchantments(enchantments);
    }

    @Override
    public @NotNull C addEnchantments(@NotNull Collection<Enchantment> enchantments) {
        return (C) Item.super.addEnchantments(enchantments);
    }

    @Override
    public @NotNull C removeEnchantment(@NotNull String enchantment, int level) {
        return (C) Item.super.removeEnchantment(enchantment, level);
    }

    @Override
    public @NotNull C removeEnchantments(String @NotNull ... enchantments) {
        return (C) Item.super.removeEnchantments(enchantments);
    }

    @Override
    public @NotNull C removeEnchantments(Enchantment @NotNull ... enchantments) {
        return (C) Item.super.removeEnchantments(enchantments);
    }

    @Override
    public @NotNull C removeEnchantments(@NotNull Collection<Enchantment> enchantments) {
        return (C) Item.super.removeEnchantments(enchantments);
    }

    @Override
    public @NotNull C addItemFlags(ItemFlag @NotNull ... itemFlags) {
        return (C) Item.super.addItemFlags(itemFlags);
    }

    @Override
    public @NotNull C addItemFlags(@NotNull Collection<ItemFlag> itemFlags) {
        return (C) Item.super.addItemFlags(itemFlags);
    }

    @Override
    public @NotNull C removeItemFlags(ItemFlag @NotNull ... itemFlags) {
        return (C) Item.super.removeItemFlags(itemFlags);
    }

    @Override
    public @NotNull C removeItemFlags(@NotNull Collection<ItemFlag> itemFlags) {
        return (C) Item.super.removeItemFlags(itemFlags);
    }

    @Override
    public @NotNull C copy() {
        return (C) super.copy();
    }

}
