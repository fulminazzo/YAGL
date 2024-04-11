package it.angrybear.yagl.contents;

import it.angrybear.yagl.items.Item;
import it.angrybear.yagl.items.fields.ItemField;
import it.angrybear.yagl.items.fields.ItemFlag;
import it.angrybear.yagl.wrappers.Enchantment;
import it.fulminazzo.fulmicollection.objects.Refl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * A {@link GUIContent} that contains a single {@link Item}.
 * It also extends the interface to easily modify it without accessing it directly.
 */
public class ItemGUIContent extends GUIContentImpl implements GUIContent, Item {
    private final Item item;

    /**
     * Instantiates a new Item gui content.
     */
    public ItemGUIContent() {
        this.item = Item.newItem();
    }

    /**
     * Instantiates a new Item gui content.
     *
     * @param item the item
     */
    public ItemGUIContent(final @NotNull Item item) {
        this.item = item.copy(Item.class);
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
    public ItemGUIContent copy() {
        ItemGUIContent guiContent = new ItemGUIContent();
        new Refl<>(guiContent).setFieldObject("item", this.item.copy());
        return guiContent;
    }

    @Override
    public @NotNull GUIContent copyContent() {
        return new ItemGUIContent(this.item);
    }
}
