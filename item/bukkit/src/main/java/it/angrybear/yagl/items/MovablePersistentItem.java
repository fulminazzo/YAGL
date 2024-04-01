package it.angrybear.yagl.items;

import it.angrybear.yagl.actions.ClickItemAction;
import it.angrybear.yagl.actions.InteractItemAction;
import it.angrybear.yagl.items.fields.ItemFlag;
import it.angrybear.yagl.persistent.DeathAction;
import it.angrybear.yagl.wrappers.Enchantment;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * An implementation of {@link MovablePersistentItem} that allows to be moved inside the player's inventory.
 */
public class MovablePersistentItem extends PersistentItem {

    /**
     * Instantiates a new Movable persistent item.
     */
    public MovablePersistentItem() {
        super();
    }

    /**
     * Instantiates a new Movable persistent item.
     *
     * @param material the material
     */
    public MovablePersistentItem(@NotNull Material material) {
        super(material);
    }

    /**
     * Instantiates a new Movable persistent item.
     *
     * @param material the material
     */
    public MovablePersistentItem(@Nullable String material) {
        super(material);
    }

    /**
     * Instantiates a new Movable persistent item.
     *
     * @param material the material
     * @param amount   the amount
     */
    public MovablePersistentItem(@NotNull Material material, int amount) {
        super(material, amount);
    }

    /**
     * Instantiates a new Movable persistent item.
     *
     * @param material the material
     * @param amount   the amount
     */
    public MovablePersistentItem(@Nullable String material, int amount) {
        super(material, amount);
    }

    @Override
    public @NotNull MovablePersistentItem setDeathAction(@NotNull DeathAction deathAction) {
        return (MovablePersistentItem) super.setDeathAction(deathAction);
    }

    @Override
    public @NotNull MovablePersistentItem onInteract(@Nullable InteractItemAction action) {
        return (MovablePersistentItem) super.onInteract(action);
    }

    @Override
    public @NotNull MovablePersistentItem onClick(@Nullable ClickItemAction action) {
        return (MovablePersistentItem) super.onClick(action);
    }

    @Override
    public MovablePersistentItem setMaterial(@NotNull Material material) {
        return (MovablePersistentItem) super.setMaterial(material);
    }

    @Override
    public MovablePersistentItem addLore(String @NotNull ... lore) {
        return (MovablePersistentItem) super.addLore(lore);
    }

    @Override
    public MovablePersistentItem addLore(@NotNull Collection<String> lore) {
        return (MovablePersistentItem) super.addLore(lore);
    }

    @Override
    public MovablePersistentItem removeLore(String @NotNull ... lore) {
        return (MovablePersistentItem) super.removeLore(lore);
    }

    @Override
    public MovablePersistentItem removeLore(@NotNull Collection<String> lore) {
        return (MovablePersistentItem) super.removeLore(lore);
    }

    @Override
    public MovablePersistentItem setLore(String @NotNull ... lore) {
        return (MovablePersistentItem) super.setLore(lore);
    }

    @Override
    public MovablePersistentItem addEnchantment(@NotNull String enchantment, int level) {
        return (MovablePersistentItem) super.addEnchantment(enchantment, level);
    }

    @Override
    public MovablePersistentItem addEnchantments(String @NotNull ... enchantments) {
        return (MovablePersistentItem) super.addEnchantments(enchantments);
    }

    @Override
    public MovablePersistentItem addEnchantments(Enchantment @NotNull ... enchantments) {
        return (MovablePersistentItem) super.addEnchantments(enchantments);
    }

    @Override
    public MovablePersistentItem addEnchantments(@NotNull Collection<Enchantment> enchantments) {
        return (MovablePersistentItem) super.addEnchantments(enchantments);
    }

    @Override
    public MovablePersistentItem removeEnchantment(@NotNull String enchantment, int level) {
        return (MovablePersistentItem) super.removeEnchantment(enchantment, level);
    }

    @Override
    public MovablePersistentItem removeEnchantments(String @NotNull ... enchantments) {
        return (MovablePersistentItem) super.removeEnchantments(enchantments);
    }

    @Override
    public MovablePersistentItem removeEnchantments(Enchantment @NotNull ... enchantments) {
        return (MovablePersistentItem) super.removeEnchantments(enchantments);
    }

    @Override
    public MovablePersistentItem removeEnchantments(@NotNull Collection<Enchantment> enchantments) {
        return (MovablePersistentItem) super.removeEnchantments(enchantments);
    }

    @Override
    public MovablePersistentItem addItemFlags(ItemFlag @NotNull ... itemFlags) {
        return (MovablePersistentItem) super.addItemFlags(itemFlags);
    }

    @Override
    public MovablePersistentItem addItemFlags(@NotNull Collection<ItemFlag> itemFlags) {
        return (MovablePersistentItem) super.addItemFlags(itemFlags);
    }

    @Override
    public MovablePersistentItem removeItemFlags(ItemFlag @NotNull ... itemFlags) {
        return (MovablePersistentItem) super.removeItemFlags(itemFlags);
    }

    @Override
    public MovablePersistentItem removeItemFlags(@NotNull Collection<ItemFlag> itemFlags) {
        return (MovablePersistentItem) super.removeItemFlags(itemFlags);
    }

    @Override
    public MovablePersistentItem setMaterial(@NotNull String material) {
        return (MovablePersistentItem) super.setMaterial(material);
    }

    @Override
    public MovablePersistentItem setAmount(int amount) {
        return (MovablePersistentItem) super.setAmount(amount);
    }

    @Override
    public MovablePersistentItem setDurability(int durability) {
        return (MovablePersistentItem) super.setDurability(durability);
    }

    @Override
    public MovablePersistentItem setDisplayName(@NotNull String displayName) {
        return (MovablePersistentItem) super.setDisplayName(displayName);
    }

    @Override
    public MovablePersistentItem setLore(@NotNull Collection<String> lore) {
        return (MovablePersistentItem) super.setLore(lore);
    }

    @Override
    public MovablePersistentItem setUnbreakable(boolean unbreakable) {
        return (MovablePersistentItem) super.setUnbreakable(unbreakable);
    }

    @Override
    public MovablePersistentItem setCustomModelData(int customModelData) {
        return (MovablePersistentItem) super.setCustomModelData(customModelData);
    }

    @Override
    public MovablePersistentItem copy() {
        return super.copy(MovablePersistentItem.class);
    }
}
