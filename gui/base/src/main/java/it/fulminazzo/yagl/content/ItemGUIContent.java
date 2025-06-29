package it.fulminazzo.yagl.content;

import it.fulminazzo.yagl.action.command.GUIItemCommand;
import it.fulminazzo.yagl.content.requirement.PermissionRequirement;
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
 * A {@link GUIContent} that contains a single {@link Item}.
 * It also extends the interface to easily modify it without accessing it directly.
 */
public class ItemGUIContent extends CustomGUIContent<ItemGUIContent> implements GUIContent, Item {
    private final Item item;

    private ItemGUIContent() {
        this(Item.newItem());
    }

    private ItemGUIContent(final @NotNull String material) {
        this(Item.newItem(material));
    }

    private ItemGUIContent(final @NotNull Item item) {
        this.item = item;
    }

    @Override
    public @NotNull Item internalRender() {
        return this.item.copy();
    }

    @Override
    public ItemGUIContent setMaterial(@NotNull String material) {
        this.item.setMaterial(material);
        return this;
    }

    @Override
    public @Nullable String getMaterial() {
        return this.item.getMaterial();
    }

    @Override
    public ItemGUIContent setAmount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    @Override
    public int getAmount() {
        return this.item.getAmount();
    }

    @Override
    public ItemGUIContent setDurability(int durability) {
        this.item.setDurability(durability);
        return this;
    }

    @Override
    public int getDurability() {
        return this.item.getDurability();
    }

    @Override
    public ItemGUIContent setDisplayName(@NotNull String displayName) {
        this.item.setDisplayName(displayName);
        return this;
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
    public Set<Enchantment> getEnchantments() {
        return this.item.getEnchantments();
    }

    @Override
    public Set<ItemFlag> getItemFlags() {
        return this.item.getItemFlags();
    }

    @Override
    public ItemGUIContent setUnbreakable(boolean unbreakable) {
        this.item.setUnbreakable(unbreakable);
        return this;
    }

    @Override
    public boolean isUnbreakable() {
        return this.item.isUnbreakable();
    }

    @Override
    public ItemGUIContent setCustomModelData(int customModelData) {
        this.item.setCustomModelData(customModelData);
        return this;
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
    public ItemGUIContent addLore(String @NotNull ... lore) {
        return (ItemGUIContent) Item.super.addLore(lore);
    }

    @Override
    public ItemGUIContent addLore(@NotNull Collection<String> lore) {
        return (ItemGUIContent) Item.super.addLore(lore);
    }

    @Override
    public ItemGUIContent removeLore(String @NotNull ... lore) {
        return (ItemGUIContent) Item.super.removeLore(lore);
    }

    @Override
    public ItemGUIContent removeLore(@NotNull Collection<String> lore) {
        return (ItemGUIContent) Item.super.removeLore(lore);
    }

    @Override
    public ItemGUIContent setLore(String @NotNull ... lore) {
        return (ItemGUIContent) Item.super.setLore(lore);
    }

    @Override
    public ItemGUIContent setLore(@NotNull Collection<String> lore) {
        return (ItemGUIContent) Item.super.setLore(lore);
    }

    @Override
    public ItemGUIContent addEnchantment(@NotNull String enchantment, int level) {
        return (ItemGUIContent) Item.super.addEnchantment(enchantment, level);
    }

    @Override
    public ItemGUIContent addEnchantments(String @NotNull ... enchantments) {
        return (ItemGUIContent) Item.super.addEnchantments(enchantments);
    }

    @Override
    public ItemGUIContent addEnchantments(Enchantment @NotNull ... enchantments) {
        return (ItemGUIContent) Item.super.addEnchantments(enchantments);
    }

    @Override
    public ItemGUIContent addEnchantments(@NotNull Collection<Enchantment> enchantments) {
        return (ItemGUIContent) Item.super.addEnchantments(enchantments);
    }

    @Override
    public ItemGUIContent removeEnchantment(@NotNull String enchantment, int level) {
        return (ItemGUIContent) Item.super.removeEnchantment(enchantment, level);
    }

    @Override
    public ItemGUIContent removeEnchantments(String @NotNull ... enchantments) {
        return (ItemGUIContent) Item.super.removeEnchantments(enchantments);
    }

    @Override
    public ItemGUIContent removeEnchantments(Enchantment @NotNull ... enchantments) {
        return (ItemGUIContent) Item.super.removeEnchantments(enchantments);
    }

    @Override
    public ItemGUIContent removeEnchantments(@NotNull Collection<Enchantment> enchantments) {
        return (ItemGUIContent) Item.super.removeEnchantments(enchantments);
    }

    @Override
    public ItemGUIContent addItemFlags(ItemFlag @NotNull ... itemFlags) {
        return (ItemGUIContent) Item.super.addItemFlags(itemFlags);
    }

    @Override
    public ItemGUIContent addItemFlags(@NotNull Collection<ItemFlag> itemFlags) {
        return (ItemGUIContent) Item.super.addItemFlags(itemFlags);
    }

    @Override
    public ItemGUIContent removeItemFlags(ItemFlag @NotNull ... itemFlags) {
        return (ItemGUIContent) Item.super.removeItemFlags(itemFlags);
    }

    @Override
    public ItemGUIContent removeItemFlags(@NotNull Collection<ItemFlag> itemFlags) {
        return (ItemGUIContent) Item.super.removeItemFlags(itemFlags);
    }

    @Override
    public @NotNull ItemGUIContent copy() {
        return (ItemGUIContent) super.copy();
    }

    @Override
    public @NotNull ItemGUIContent internalCopy() {
        ItemGUIContent copy = ItemGUIContent.newInstance(this.item.copy()).copyFrom(this, true);
        copy.requirements = this.requirements instanceof PermissionRequirement ?
                new PermissionRequirement(this.requirements.serialize()) :
                this.requirements;
        copy.clickAction = this.clickAction instanceof GUIItemCommand ?
                new GUIItemCommand(this.clickAction.serialize()) :
                this.clickAction;
        return copy;
    }

    /**
     * Creates an instance of {@link ItemGUIContent}.
     *
     * @return the item gui content
     */
    public static ItemGUIContent newInstance() {
        return new ItemGUIContent();
    }

    /**
     * Creates an instance of {@link ItemGUIContent} with the given material.
     *
     * @param material the material
     * @return the item gui content
     */
    public static ItemGUIContent newInstance(final @NotNull String material) {
        return new ItemGUIContent(material);
    }

    /**
     * Creates an instance of {@link ItemGUIContent} with the given item.
     *
     * @param item the item
     * @return the item gui content
     */
    public static ItemGUIContent newInstance(final @NotNull Item item) {
        return new ItemGUIContent(item);
    }

}
