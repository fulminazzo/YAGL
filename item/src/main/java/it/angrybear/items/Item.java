package it.angrybear.items;

import it.angrybear.utils.MessageUtils;
import it.fulminazzo.fulmicollection.objects.Refl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * An interface that represents an item.
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface Item {

    /**
     * Sets material.
     *
     * @param material the material
     * @return this item
     */
    Item setMaterial(final @NotNull String material);

    /**
     * Gets material.
     *
     * @return the material
     */
    @Nullable String getMaterial();

    /**
     * Sets amount.
     *
     * @param amount the amount
     * @return this item
     */
    Item setAmount(final int amount);

    /**
     * Gets amount.
     *
     * @return the amount
     */
    int getAmount();

    /**
     * Sets durability.
     *
     * @param durability the durability
     * @return this item
     */
    Item setDurability(final int durability);

    /**
     * Gets durability.
     *
     * @return the durability
     */
    int getDurability();

    /**
     * Sets display name.
     *
     * @param displayName the display name
     * @return this item
     */
    Item setDisplayName(final @NotNull String displayName);

    /**
     * Gets display name.
     *
     * @return the display name
     */
    @NotNull String getDisplayName();

    /**
     * Adds lore.
     *
     * @param lore the lore
     * @return this item
     */
    default Item addLore(final String @NotNull ... lore) {
        return addLore(Arrays.asList(lore));
    }

    /**
     * Adds lore.
     *
     * @param lore the lore
     * @return this item
     */
    default Item addLore(final @NotNull Collection<String> lore) {
        @NotNull List<String> prevLore = getLore();
        for (final String l : lore) prevLore.add(MessageUtils.color(l));
        return this;
    }

    /**
     * Removes lore.
     *
     * @param lore the lore
     * @return this item
     */
    default Item removeLore(final String @NotNull ... lore) {
        return removeLore(Arrays.asList(lore));
    }

    /**
     * Removes lore.
     *
     * @param lore the lore
     * @return this item
     */
    default Item removeLore(final @NotNull Collection<String> lore) {
        @NotNull List<String> prevLore = getLore();
        for (final String l : lore) prevLore.removeIf(l2 -> l2.equals(MessageUtils.color(l)));
        return this;
    }

    /**
     * Sets lore.
     *
     * @param lore the lore
     * @return this item
     */
    default Item setLore(final String @NotNull ... lore) {
        return setLore(Arrays.asList(lore));
    }

    /**
     * Sets lore.
     *
     * @param lore the lore
     * @return this item
     */
    default Item setLore(final @NotNull Collection<String> lore) {
        getLore().clear();
        return addLore(lore);
    }

    /**
     * Gets lore.
     *
     * @return the lore
     */
    @NotNull List<String> getLore();

    /**
     * Checks if the current item has the given enchantment.
     *
     * @param enchantment the enchantment
     * @return true if it does
     */
    default boolean hasEnchantment(final @NotNull String enchantment) {
        return getEnchantments().stream().anyMatch(e -> e.getEnchantment().equalsIgnoreCase(enchantment));
    }

    /**
     * Checks if the current item has the given enchantment.
     *
     * @param enchantment the enchantment
     * @return true if it does
     */
    default boolean hasEnchantment(final @NotNull Enchantment enchantment) {
        return getEnchantments().stream().anyMatch(e -> e.equals(enchantment));
    }

    /**
     * Gets enchantment level.
     *
     * @param enchantment the enchantment
     * @return the enchantment level
     */
    default int getEnchantmentLevel(final @NotNull Enchantment enchantment) {
        return getEnchantmentLevel(enchantment.getEnchantment());
    }

    /**
     * Gets enchantment level.
     *
     * @param enchantment the enchantment
     * @return the enchantment level
     */
    default int getEnchantmentLevel(final @NotNull String enchantment) {
        return getEnchantments().stream().filter(e -> e.getEnchantment().equalsIgnoreCase(enchantment)).map(Enchantment::getLevel).findFirst().orElse(-1);
    }

    /**
     * Add enchantment.
     *
     * @param enchantment the enchantment
     * @param level       the level
     * @return this item
     */
    default Item addEnchantment(final @NotNull String enchantment, final int level) {
        return addEnchantments(new Enchantment(enchantment, level));
    }

    /**
     * Add enchantments.
     *
     * @param enchantments the enchantments
     * @return this item
     */
    default Item addEnchantments(final String @NotNull ... enchantments) {
        return addEnchantments(Arrays.stream(enchantments).distinct().map(Enchantment::new).collect(Collectors.toList()));
    }

    /**
     * Add enchantments.
     *
     * @param enchantments the enchantments
     * @return this item
     */
    default Item addEnchantments(final Enchantment @NotNull ... enchantments) {
        return addEnchantments(Arrays.asList(enchantments));
    }

    /**
     * Add enchantments.
     *
     * @param enchantments the enchantments
     * @return this item
     */
    default Item addEnchantments(final @NotNull Collection<Enchantment> enchantments) {
        final Set<Enchantment> enchants = getEnchantments();
        enchants.addAll(enchantments);
        return this;
    }

    /**
     * Remove enchantment.
     *
     * @param enchantment the enchantment
     * @param level       the level
     * @return this item
     */
    default Item removeEnchantment(final @NotNull String enchantment, final int level) {
        return removeEnchantments(new Enchantment(enchantment, level));
    }

    /**
     * Remove enchantments.
     *
     * @param enchantments the enchantments
     * @return this item
     */
    default Item removeEnchantments(final String @NotNull ... enchantments) {
        final Set<Enchantment> enchants = getEnchantments();
        for (final String e : enchantments) enchants.removeIf(e2 -> e2.isSimilar(new Enchantment(e)));
        return this;
    }

    /**
     * Remove enchantments.
     *
     * @param enchantments the enchantments
     * @return this item
     */
    default Item removeEnchantments(final Enchantment @NotNull ... enchantments) {
        return removeEnchantments(Arrays.asList(enchantments));
    }

    /**
     * Remove enchantments.
     *
     * @param enchantments the enchantments
     * @return this item
     */
    default Item removeEnchantments(final @NotNull Collection<Enchantment> enchantments) {
        final Set<Enchantment> enchants = getEnchantments();
        for (final Enchantment e : enchantments) enchants.removeIf(e2 -> e2.equals(e));
        return this;
    }

    /**
     * Gets enchantments.
     *
     * @return the enchantments
     */
    Set<Enchantment> getEnchantments();

    /**
     * Checks if the current item has the given enchantment.
     *
     * @param flag the flag
     * @return true if it does
     */
    default boolean hasItemFlag(final @NotNull ItemFlag flag) {
        return getItemFlags().stream().anyMatch(f -> f.equals(flag));
    }

    /**
     * Add item flags.
     *
     * @param itemFlags the item flags
     * @return this item
     */
    default Item addItemFlags(final ItemFlag @NotNull ... itemFlags) {
        return addItemFlags(Arrays.asList(itemFlags));
    }

    /**
     * Add item flags.
     *
     * @param itemFlags the item flags
     * @return this item
     */
    default Item addItemFlags(final @NotNull Collection<ItemFlag> itemFlags) {
        Set<ItemFlag> flags = getItemFlags();
        flags.addAll(itemFlags);
        return this;
    }

    /**
     * Remove item flags.
     *
     * @param itemFlags the item flags
     * @return this item
     */
    default Item removeItemFlags(final ItemFlag @NotNull ... itemFlags) {
        return removeItemFlags(Arrays.asList(itemFlags));
    }

    /**
     * Remove item flags.
     *
     * @param itemFlags the item flags
     * @return this item
     */
    default Item removeItemFlags(final @NotNull Collection<ItemFlag> itemFlags) {
        Set<ItemFlag> flags = getItemFlags();
        for (final ItemFlag i : itemFlags) flags.remove(i);
        return this;
    }

    /**
     * Gets item flags.
     *
     * @return the item flags
     */
    Set<ItemFlag> getItemFlags();

    /**
     * Sets unbreakable.
     *
     * @param unbreakable the unbreakable
     * @return this item
     */
    Item setUnbreakable(final boolean unbreakable);

    /**
     * Checks if the current item is unbreakable.
     *
     * @return true if it does
     */
    boolean isUnbreakable();

    /**
     * Compares this item with the given one.
     * Uses the given {@link ItemField}s to skip checks on those.
     *
     * @param item   the item
     * @param ignore the ignored fields
     * @return true if they match
     */
    boolean isSimilar(final @Nullable Item item, final ItemField @NotNull ... ignore);

    /**
     * Copies the current item into a new one.
     *
     * @return the item
     */
    default Item copy() {
        return copy(ItemImpl.class);
    }

    /**
     * Copies the current item into a new one using the provided class.
     *
     * @param <I>   the type parameter
     * @param clazz the clazz
     * @return the item
     */
    default <I extends Item> I copy(final @NotNull Class<I> clazz) {
        Refl<I> item = new Refl<>(clazz, new Object[0]);
        for (final Field field : item.getNonStaticFields())
            try {
                field.setAccessible(true);
                Object obj1 = field.get(this);
                item.setFieldObject(field, obj1);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        return item.getObject();
    }
}
