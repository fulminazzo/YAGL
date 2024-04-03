package it.angrybear.yagl.items;

import it.angrybear.yagl.items.fields.ItemField;
import it.angrybear.yagl.items.fields.ItemFlag;
import it.angrybear.yagl.utils.MessageUtils;
import it.angrybear.yagl.wrappers.Enchantment;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * An interface that represents an item.
 */
@SuppressWarnings({"unused", "UnusedReturnValue", "deprecation"})
public interface Item extends AbstractItem {

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
        return getEnchantments().stream().anyMatch(e -> e.getName().equalsIgnoreCase(enchantment));
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
        return getEnchantmentLevel(enchantment.getName());
    }

    /**
     * Gets enchantment level.
     *
     * @param enchantment the enchantment
     * @return the enchantment level
     */
    default int getEnchantmentLevel(final @NotNull String enchantment) {
        return getEnchantments().stream().filter(e -> e.getName().equalsIgnoreCase(enchantment)).map(Enchantment::getLevel).findFirst().orElse(-1);
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
     * Sets custom model data.
     *
     * @param customModelData the custom model data
     * @return the custom model data
     * @since Minecraft 1.14
     */
    Item setCustomModelData(final int customModelData);

    /**
     * Gets custom model data.
     *
     * @return the custom model data
     * @since Minecraft 1.14
     */
    int getCustomModelData();

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
        Class<? extends Item> clazz = this.getClass();
        try {
            Constructor<? extends Item> constructor = ReflectionUtils.getConstructor(clazz);
        } catch (Exception e) {
            clazz = ItemImpl.class;
        }
        return copy(clazz);
    }

    /**
     * Copies the current item into a new one using the provided class.
     * If an interface is provided (say {@link Item}),
     * it tries to convert it to {@link ItemImpl} by appending <i>Impl</i>.
     *
     * @param <I>   the type parameter
     * @param clazz the clazz
     * @return the item
     */
    default <I extends Item> I copy(@NotNull Class<I> clazz) {
        if (clazz.isInterface())
            try {
                clazz = ReflectionUtils.getClass(clazz.getCanonicalName() + "Impl");
            } catch (Exception ignored) {}

        Refl<I> item = new Refl<>(clazz, new Object[0]);
        for (final Field field : item.getNonStaticFields())
            try {
                ReflectionUtils.getField(this, field.getName());
                Object obj1 = ReflectionUtils.get(field, this);
                item.setFieldObject(field, obj1);
            } catch (IllegalArgumentException ignored) {

            }
        return item.getObject();
    }

    /**
     * Creates a new item to be used.
     *
     * @return the item
     */
    static @NotNull Item newItem() {
        return new ItemImpl();
    }

    /**
     * Creates a new item to be used with the given material.
     *
     * @param material the material
     * @return the item
     */
    static @NotNull Item newItem(final @NotNull String material) {
        return newItem().setMaterial(material);
    }

    /**
     * Creates a new recipe item to be used.
     *
     * @return the item
     */
    static @NotNull RecipeItem newRecipeItem() {
        return new RecipeItemImpl();
    }

    /**
     * Creates a new recipe item to be used with the given material.
     *
     * @param material the material
     * @return the item
     */
    static @NotNull RecipeItem newRecipeItem(final @NotNull String material) {
        return newRecipeItem().setMaterial(material);
    }
}
