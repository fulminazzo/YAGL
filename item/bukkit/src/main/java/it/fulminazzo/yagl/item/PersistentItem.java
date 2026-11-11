package it.fulminazzo.yagl.item;

import it.fulminazzo.yagl.action.ClickItemAction;
import it.fulminazzo.yagl.action.InteractItemAction;
import it.fulminazzo.yagl.item.field.ItemFlag;
import it.fulminazzo.yagl.listener.PersistentListener;
import it.fulminazzo.yagl.wrapper.Enchantment;
import it.fulminazzo.yagl.wrapper.PotionEffect;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * An implementation of {@link BukkitItemImpl} created to be a constant item in a player's inventory.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class PersistentItem extends BukkitItemImpl {
    private static final String WARNING_MESSAGE = "Creating a PersistentItem without registering a PersistentListener will cause the former to fail. Please register one listener.";
    private static final List<PersistentItem> PERSISTENT_ITEMS = new ArrayList<>();
    private DeathAction deathAction;
    @Getter(AccessLevel.NONE)
    private @Nullable ClickItemAction clickAction;
    @Getter(AccessLevel.NONE)
    private @Nullable InteractItemAction interactAction;
    private Mobility mobility;

    private PersistentItem() {
        this((String) null);
    }

    private PersistentItem(final @NotNull Material material) {
        this(material.name(), 1);
    }

    private PersistentItem(final @Nullable String material) {
        this(material, 1);
    }

    private PersistentItem(final @NotNull Material material, final int amount) {
        this(material.name(), amount);
    }

    private PersistentItem(final @Nullable String material, final int amount) {
        super(material, amount);
        this.deathAction = DeathAction.MAINTAIN;
        this.mobility = Mobility.STATIC;
        PERSISTENT_ITEMS.add(this);
    }

    @Override
    public @NotNull PersistentItem copy() {
        return (PersistentItem) super.copy();
    }

    @Override
    protected @NotNull <T extends Item> T copyDataTo(final @NotNull T copy) {
        if (copy instanceof PersistentItem)
            ((PersistentItem) copy)
                    .setDeathAction(deathAction)
                    .setMobility(mobility)
                    .onClick(clickAction)
                    .onInteract(interactAction);
        return super.copyDataTo(copy);
    }

    @Override
    public @NotNull <M extends ItemMeta> ItemStack create(final @Nullable Class<M> itemMetaClass, final Consumer<M> metaFunction) {
        if (!PersistentListener.isInitialized()) Logger.getGlobal().warning(WARNING_MESSAGE);
        return super.create(itemMetaClass, metaFunction);
    }

    /**
     * Sets death action.
     *
     * @param deathAction the death action
     * @return this persistent item
     */
    public @NotNull PersistentItem setDeathAction(final @NotNull DeathAction deathAction) {
        this.deathAction = deathAction;
        return this;
    }

    /**
     * Sets mobility.
     *
     * @param mobility the mobility
     * @return this persistent item
     */
    public @NotNull PersistentItem setMobility(final Mobility mobility) {
        this.mobility = mobility;
        return this;
    }

    /**
     * Executes {@link #interactAction}.
     *
     * @param player         the player
     * @param itemStack      the item stack
     * @param interactAction the interact action
     */
    public void interact(final @NotNull Player player, final @NotNull ItemStack itemStack, final @NotNull Action interactAction) {
        if (this.interactAction != null) this.interactAction.execute(player, itemStack, interactAction);
    }

    /**
     * Set the action executed on interacting.
     * A player interacts with an item when they right-click with it in the game.
     *
     * @param action the action
     * @return this persistent item
     */
    public @NotNull PersistentItem onInteract(final @Nullable InteractItemAction action) {
        this.interactAction = action;
        return this;
    }

    /**
     * Executes {@link #clickAction}.
     *
     * @param player    the player
     * @param itemStack the item stack
     * @param clickType the click type
     */
    public void click(final @NotNull Player player, final @NotNull ItemStack itemStack, final @NotNull ClickType clickType) {
        if (this.clickAction != null) this.clickAction.execute(player, itemStack, clickType);
    }

    /**
     * Set the action executed on clicking.
     * A player clicks with an item when they click on it in their inventory.
     *
     * @param action the action
     * @return this persistent item
     */
    public @NotNull PersistentItem onClick(final @Nullable ClickItemAction action) {
        this.clickAction = action;
        return this;
    }

    @Override
    public PersistentItem setMaterial(final @NotNull Material material) {
        return (PersistentItem) super.setMaterial(material);
    }

    @Override
    public @NotNull PersistentItem addLore(final String @NotNull ... lore) {
        return (PersistentItem) super.addLore(lore);
    }

    @Override
    public @NotNull PersistentItem addLore(final @NotNull Collection<String> lore) {
        return (PersistentItem) super.addLore(lore);
    }

    @Override
    public @NotNull PersistentItem removeLore(final String @NotNull ... lore) {
        return (PersistentItem) super.removeLore(lore);
    }

    @Override
    public @NotNull PersistentItem removeLore(final @NotNull Collection<String> lore) {
        return (PersistentItem) super.removeLore(lore);
    }

    @Override
    public @NotNull PersistentItem setLore(final String @NotNull ... lore) {
        return (PersistentItem) super.setLore(lore);
    }

    @Override
    public @NotNull PersistentItem addEnchantment(final @NotNull String enchantment, final int level) {
        return (PersistentItem) super.addEnchantment(enchantment, level);
    }

    @Override
    public @NotNull PersistentItem addEnchantments(final String @NotNull ... enchantments) {
        return (PersistentItem) super.addEnchantments(enchantments);
    }

    @Override
    public @NotNull PersistentItem addEnchantments(final Enchantment @NotNull ... enchantments) {
        return (PersistentItem) super.addEnchantments(enchantments);
    }

    @Override
    public @NotNull PersistentItem addEnchantments(final @NotNull Collection<Enchantment> enchantments) {
        return (PersistentItem) super.addEnchantments(enchantments);
    }

    @Override
    public @NotNull PersistentItem removeEnchantment(final @NotNull String enchantment, final int level) {
        return (PersistentItem) super.removeEnchantment(enchantment, level);
    }

    @Override
    public @NotNull PersistentItem removeEnchantments(final String @NotNull ... enchantments) {
        return (PersistentItem) super.removeEnchantments(enchantments);
    }

    @Override
    public @NotNull PersistentItem removeEnchantments(final Enchantment @NotNull ... enchantments) {
        return (PersistentItem) super.removeEnchantments(enchantments);
    }

    @Override
    public @NotNull PersistentItem removeEnchantments(final @NotNull Collection<Enchantment> enchantments) {
        return (PersistentItem) super.removeEnchantments(enchantments);
    }

    @Override
    public @NotNull PersistentItem addItemFlags(final ItemFlag @NotNull ... itemFlags) {
        return (PersistentItem) super.addItemFlags(itemFlags);
    }

    @Override
    public @NotNull PersistentItem addItemFlags(final @NotNull Collection<ItemFlag> itemFlags) {
        return (PersistentItem) super.addItemFlags(itemFlags);
    }

    @Override
    public @NotNull PersistentItem removeItemFlags(final ItemFlag @NotNull ... itemFlags) {
        return (PersistentItem) super.removeItemFlags(itemFlags);
    }

    @Override
    public @NotNull PersistentItem removeItemFlags(final @NotNull Collection<ItemFlag> itemFlags) {
        return (PersistentItem) super.removeItemFlags(itemFlags);
    }

    @Override
    public @NotNull PersistentItem addPotionEffects(final PotionEffect @NotNull ... potionEffects) {
        return (PersistentItem) super.addPotionEffects(potionEffects);
    }

    @Override
    public @NotNull PersistentItem addPotionEffects(final @NotNull Collection<PotionEffect> potionEffects) {
        return (PersistentItem) super.addPotionEffects(potionEffects);
    }

    @Override
    public @NotNull PersistentItem removePotionEffects(final PotionEffect @NotNull ... potionEffects) {
        return (PersistentItem) super.removePotionEffects(potionEffects);
    }

    @Override
    public @NotNull PersistentItem removePotionEffects(final @NotNull Collection<PotionEffect> potionEffects) {
        return (PersistentItem) super.removePotionEffects(potionEffects);
    }

    @Override
    public @NotNull PersistentItem setPotionEffects(final PotionEffect @NotNull ... potionEffects) {
        return (PersistentItem) super.setPotionEffects(potionEffects);
    }

    @Override
    public @NotNull PersistentItem setPotionEffects(final @NotNull Collection<PotionEffect> potionEffects) {
        return (PersistentItem) super.setPotionEffects(potionEffects);
    }

    @Override
    public @NotNull PersistentItem setMaterial(final @NotNull String material) {
        return (PersistentItem) super.setMaterial(material);
    }

    @Override
    public @NotNull PersistentItem setAmount(final int amount) {
        return (PersistentItem) super.setAmount(amount);
    }

    @Override
    public @NotNull PersistentItem setDurability(final int durability) {
        return (PersistentItem) super.setDurability(durability);
    }

    @Override
    public @NotNull PersistentItem setDisplayName(final @NotNull String displayName) {
        return (PersistentItem) super.setDisplayName(displayName);
    }

    @Override
    public @NotNull PersistentItem setLore(final @NotNull Collection<String> lore) {
        return (PersistentItem) super.setLore(lore);
    }

    @Override
    public @NotNull PersistentItem setUnbreakable(final boolean unbreakable) {
        return (PersistentItem) super.setUnbreakable(unbreakable);
    }

    @Override
    public @NotNull PersistentItem setCustomModelData(final int customModelData) {
        return (PersistentItem) super.setCustomModelData(customModelData);
    }

    @Override
    public @NotNull String toString() {
        String base = super.toString();
        return base.substring(0, base.length() - 1)
                + String.format(", deathAction=%s, mobility=%s}", deathAction, mobility);
    }

    /**
     * Tries to get the corresponding {@link PersistentItem} from the given {@link ItemStack}.
     *
     * @param itemStack the item stack
     * @return this persistent item
     */
    public static @Nullable PersistentItem getPersistentItem(final @Nullable ItemStack itemStack) {
        if (itemStack == null) return null;
        for (final PersistentItem item : PERSISTENT_ITEMS)
            if (item.isSimilar(itemStack)) return item;
        return null;
    }

    /**
     * Instantiates a new Persistent item.
     *
     * @return the persistent item
     */
    public static @NotNull PersistentItem newItem() {
        return new PersistentItem();
    }

    /**
     * Instantiates a new Persistent item.
     *
     * @param material the material
     * @return the persistent item
     */
    public static @NotNull PersistentItem newItem(final @NotNull Material material) {
        return new PersistentItem(material);
    }

    /**
     * Instantiates a new Persistent item.
     *
     * @param material the material
     * @return the persistent item
     */
    public static @NotNull PersistentItem newItem(final @Nullable String material) {
        return new PersistentItem(material);
    }

    /**
     * Instantiates a new Persistent item.
     *
     * @param material the material
     * @param amount   the amount
     * @return the persistent item
     */
    public static @NotNull PersistentItem newItem(final @NotNull Material material, final int amount) {
        return new PersistentItem(material, amount);
    }

    /**
     * Instantiates a new Persistent item.
     *
     * @param material the material
     * @param amount   the amount
     * @return the persistent item
     */
    public static @NotNull PersistentItem newItem(final @Nullable String material, final int amount) {
        return new PersistentItem(material, amount);
    }

    /**
     * Clear persistent items.
     */
    public static void clearPersistentItems() {
        PERSISTENT_ITEMS.clear();
    }

}
