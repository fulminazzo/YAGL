package it.angrybear.items;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface Item {

    Item setMaterial(final @NotNull String material);

    @NotNull String getMaterial();

    Item setAmount(final int amount);

    int getAmount();

    Item setDurability(final short durability);

    short getDurability();

    Item setDisplayName(final @NotNull String displayName);

    @NotNull String getDisplayName();

    default Item setLore(final String @NotNull ... lore) {
        return setLore(Arrays.asList(lore));
    }

    Item setLore(final @NotNull Collection<String> lore);

    @NotNull List<String> getLore();

    default boolean hasEnchantment(final @NotNull String enchantment) {
        return getEnchantments().stream().anyMatch(e -> e.getEnchantment().equalsIgnoreCase(enchantment));
    }

    default boolean hasEnchantment(final @NotNull Enchantment enchantment) {
        return getEnchantments().stream().anyMatch(e -> e.equals(enchantment));
    }

    default int getEnchantmentLevel(final @NotNull Enchantment enchantment) {
        return getEnchantmentLevel(enchantment.getEnchantment());
    }

    default int getEnchantmentLevel(final @NotNull String enchantment) {
        return getEnchantments().stream().filter(e -> e.getEnchantment().equalsIgnoreCase(enchantment)).map(Enchantment::getLevel).findFirst().orElse(-1);
    }

    default Item addEnchantment(final @NotNull String enchantment, final int level) {
        return addEnchantments(new Enchantment(enchantment, level));
    }

    default Item addEnchantments(final String @NotNull ... enchantments) {
        return addEnchantments(Arrays.stream(enchantments).distinct().map(Enchantment::new).collect(Collectors.toList()));
    }

    default Item addEnchantments(final Enchantment @NotNull ... enchantments) {
        return addEnchantments(Arrays.asList(enchantments));
    }

    default Item addEnchantments(final @NotNull Collection<Enchantment> enchantments) {
        final Set<Enchantment> enchants = getEnchantments();
        enchants.addAll(enchantments);
        return this;
    }

    default Item removeEnchantment(final @NotNull String enchantment, final int level) {
        return removeEnchantments(new Enchantment(enchantment, level));
    }

    default Item removeEnchantments(final String @NotNull ... enchantments) {
        final Set<Enchantment> enchants = getEnchantments();
        for (final String e : enchantments) enchants.removeIf(e2 -> e2.isSimilar(new Enchantment(e)));
        return this;
    }

    default Item removeEnchantments(final Enchantment @NotNull ... enchantments) {
        return removeEnchantments(Arrays.asList(enchantments));
    }

    default Item removeEnchantments(final @NotNull Collection<Enchantment> enchantments) {
        final Set<Enchantment> enchants = getEnchantments();
        for (final Enchantment e : enchantments) enchants.removeIf(e2 -> e2.equals(e));
        return this;
    }

    Set<Enchantment> getEnchantments();

    default boolean hasItemFlag(final @NotNull ItemFlag flag) {
        return getItemFlags().stream().anyMatch(f -> f.equals(flag));
    }

    default Item addItemFlags(final ItemFlag @NotNull ... itemFlags) {
        return addItemFlags(Arrays.asList(itemFlags));
    }

    default Item addItemFlags(final @NotNull Collection<ItemFlag> itemFlags) {
        Set<ItemFlag> flags = getItemFlags();
        flags.addAll(itemFlags);
        return this;
    }

    default Item removeItemFlags(final ItemFlag @NotNull ... itemFlags) {
        return removeItemFlags(Arrays.asList(itemFlags));
    }

    default Item removeItemFlags(final @NotNull Collection<ItemFlag> itemFlags) {
        Set<ItemFlag> flags = getItemFlags();
        for (final ItemFlag i : itemFlags) flags.remove(i);
        return this;
    }

    Set<ItemFlag> getItemFlags();

    //TODO:
    boolean isSimilar();
}
