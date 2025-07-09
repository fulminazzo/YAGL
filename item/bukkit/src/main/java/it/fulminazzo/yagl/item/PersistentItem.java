package it.fulminazzo.yagl.item;

import it.fulminazzo.yagl.action.ClickItemAction;
import it.fulminazzo.yagl.action.InteractItemAction;
import it.fulminazzo.yagl.item.field.ItemFlag;
import it.fulminazzo.yagl.listener.PersistentListener;
import it.fulminazzo.yagl.wrapper.Enchantment;
import lombok.AccessLevel;
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
    public @NotNull <M extends ItemMeta> ItemStack create(@Nullable Class<M> itemMetaClass, Consumer<M> metaFunction) {
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
    public @NotNull PersistentItem setMobility(Mobility mobility) {
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
    public PersistentItem setMaterial(@NotNull Material material) {
        return (PersistentItem) super.setMaterial(material);
    }

    @Override
    public PersistentItem addLore(String @NotNull ... lore) {
        return (PersistentItem) super.addLore(lore);
    }

    @Override
    public PersistentItem addLore(@NotNull Collection<String> lore) {
        return (PersistentItem) super.addLore(lore);
    }

    @Override
    public PersistentItem removeLore(String @NotNull ... lore) {
        return (PersistentItem) super.removeLore(lore);
    }

    @Override
    public PersistentItem removeLore(@NotNull Collection<String> lore) {
        return (PersistentItem) super.removeLore(lore);
    }

    @Override
    public PersistentItem setLore(String @NotNull ... lore) {
        return (PersistentItem) super.setLore(lore);
    }

    @Override
    public PersistentItem addEnchantment(@NotNull String enchantment, int level) {
        return (PersistentItem) super.addEnchantment(enchantment, level);
    }

    @Override
    public PersistentItem addEnchantments(String @NotNull ... enchantments) {
        return (PersistentItem) super.addEnchantments(enchantments);
    }

    @Override
    public PersistentItem addEnchantments(Enchantment @NotNull ... enchantments) {
        return (PersistentItem) super.addEnchantments(enchantments);
    }

    @Override
    public PersistentItem addEnchantments(@NotNull Collection<Enchantment> enchantments) {
        return (PersistentItem) super.addEnchantments(enchantments);
    }

    @Override
    public PersistentItem removeEnchantment(@NotNull String enchantment, int level) {
        return (PersistentItem) super.removeEnchantment(enchantment, level);
    }

    @Override
    public PersistentItem removeEnchantments(String @NotNull ... enchantments) {
        return (PersistentItem) super.removeEnchantments(enchantments);
    }

    @Override
    public PersistentItem removeEnchantments(Enchantment @NotNull ... enchantments) {
        return (PersistentItem) super.removeEnchantments(enchantments);
    }

    @Override
    public PersistentItem removeEnchantments(@NotNull Collection<Enchantment> enchantments) {
        return (PersistentItem) super.removeEnchantments(enchantments);
    }

    @Override
    public PersistentItem addItemFlags(ItemFlag @NotNull ... itemFlags) {
        return (PersistentItem) super.addItemFlags(itemFlags);
    }

    @Override
    public PersistentItem addItemFlags(@NotNull Collection<ItemFlag> itemFlags) {
        return (PersistentItem) super.addItemFlags(itemFlags);
    }

    @Override
    public PersistentItem removeItemFlags(ItemFlag @NotNull ... itemFlags) {
        return (PersistentItem) super.removeItemFlags(itemFlags);
    }

    @Override
    public PersistentItem removeItemFlags(@NotNull Collection<ItemFlag> itemFlags) {
        return (PersistentItem) super.removeItemFlags(itemFlags);
    }

    @Override
    public PersistentItem setMaterial(@NotNull String material) {
        return (PersistentItem) super.setMaterial(material);
    }

    @Override
    public PersistentItem setAmount(int amount) {
        return (PersistentItem) super.setAmount(amount);
    }

    @Override
    public PersistentItem setDurability(int durability) {
        return (PersistentItem) super.setDurability(durability);
    }

    @Override
    public PersistentItem setDisplayName(@NotNull String displayName) {
        return (PersistentItem) super.setDisplayName(displayName);
    }

    @Override
    public PersistentItem setLore(@NotNull Collection<String> lore) {
        return (PersistentItem) super.setLore(lore);
    }

    @Override
    public PersistentItem setUnbreakable(boolean unbreakable) {
        return (PersistentItem) super.setUnbreakable(unbreakable);
    }

    @Override
    public PersistentItem setCustomModelData(int customModelData) {
        return (PersistentItem) super.setCustomModelData(customModelData);
    }

    @Override
    public PersistentItem copy() {
        return super.copy(PersistentItem.class);
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
