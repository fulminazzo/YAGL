package it.fulminazzo.yagl.items;

import it.fulminazzo.yagl.items.fields.ItemFlag;
import it.fulminazzo.yagl.items.recipes.Recipe;
import it.fulminazzo.yagl.wrappers.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * An implementation of {@link RecipeItem} that actually implements {@link #registerRecipes()} and {@link #unregisterRecipes()}.
 */
public interface BukkitRecipeItem extends RecipeItem, BukkitItem {

    @Override
    default BukkitRecipeItem setRecipes(final Recipe @NotNull ... recipes) {
        return (BukkitRecipeItem) RecipeItem.super.setRecipes(recipes);
    }

    @Override
    BukkitRecipeItem addRecipes(final Recipe @NotNull ... recipes);

    @Override
    BukkitRecipeItem clearRecipes(final Predicate<Recipe> predicate);

    @Override
    default BukkitRecipeItem clearRecipes() {
        return (BukkitRecipeItem) RecipeItem.super.clearRecipes();
    }

    @Override
    BukkitRecipeItem setMaterial(final @NotNull String material);

    @Override
    BukkitRecipeItem setAmount(final int amount);

    @Override
    BukkitRecipeItem setDurability(final int durability);

    @Override
    BukkitRecipeItem setDisplayName(final @NotNull String displayName);

    @Override
    BukkitRecipeItem setCustomModelData(final int customModelData);

    @Override
    default BukkitRecipeItem addLore(final String @NotNull ... lore) {
        return (BukkitRecipeItem) RecipeItem.super.addLore(lore);
    }

    @Override
    default BukkitRecipeItem addLore(final @NotNull Collection<String> lore) {
        return (BukkitRecipeItem) RecipeItem.super.addLore(lore);
    }

    @Override
    default BukkitRecipeItem removeLore(final String @NotNull ... lore) {
        return (BukkitRecipeItem) RecipeItem.super.removeLore(lore);
    }

    @Override
    default BukkitRecipeItem removeLore(final @NotNull Collection<String> lore) {
        return (BukkitRecipeItem) RecipeItem.super.removeLore(lore);
    }

    @Override
    default BukkitRecipeItem setLore(final String @NotNull ... lore) {
        return (BukkitRecipeItem) RecipeItem.super.setLore(lore);
    }

    @Override
    default BukkitRecipeItem setLore(final @NotNull Collection<String> lore) {
        return (BukkitRecipeItem) RecipeItem.super.setLore(lore);
    }

    @Override
    default BukkitRecipeItem addEnchantment(final @NotNull String enchantment, final int level) {
        return (BukkitRecipeItem) RecipeItem.super.addEnchantment(enchantment, level);
    }

    @Override
    default BukkitRecipeItem addEnchantments(final String @NotNull ... enchantments) {
        return (BukkitRecipeItem) RecipeItem.super.addEnchantments(enchantments);
    }

    @Override
    default BukkitRecipeItem addEnchantments(final Enchantment @NotNull ... enchantments) {
        return (BukkitRecipeItem) RecipeItem.super.addEnchantments(enchantments);
    }

    @Override
    default BukkitRecipeItem addEnchantments(final @NotNull Collection<Enchantment> enchantments) {
        return (BukkitRecipeItem) RecipeItem.super.addEnchantments(enchantments);
    }

    @Override
    default BukkitRecipeItem removeEnchantment(final @NotNull String enchantment, final int level) {
        return (BukkitRecipeItem) RecipeItem.super.removeEnchantment(enchantment, level);
    }

    @Override
    default BukkitRecipeItem removeEnchantments(final String @NotNull ... enchantments) {
        return (BukkitRecipeItem) RecipeItem.super.removeEnchantments(enchantments);
    }

    @Override
    default BukkitRecipeItem removeEnchantments(final Enchantment @NotNull ... enchantments) {
        return (BukkitRecipeItem) RecipeItem.super.removeEnchantments(enchantments);
    }

    @Override
    default BukkitRecipeItem removeEnchantments(final @NotNull Collection<Enchantment> enchantments) {
        return (BukkitRecipeItem) RecipeItem.super.removeEnchantments(enchantments);
    }

    @Override
    default BukkitRecipeItem addItemFlags(final ItemFlag @NotNull ... itemFlags) {
        return (BukkitRecipeItem) RecipeItem.super.addItemFlags(itemFlags);
    }

    @Override
    default BukkitRecipeItem addItemFlags(final @NotNull Collection<ItemFlag> itemFlags) {
        return (BukkitRecipeItem) RecipeItem.super.addItemFlags(itemFlags);
    }

    @Override
    default BukkitRecipeItem removeItemFlags(final ItemFlag @NotNull ... itemFlags) {
        return (BukkitRecipeItem) RecipeItem.super.removeItemFlags(itemFlags);
    }

    @Override
    default BukkitRecipeItem removeItemFlags(final @NotNull Collection<ItemFlag> itemFlags) {
        return (BukkitRecipeItem) RecipeItem.super.removeItemFlags(itemFlags);
    }

    @Override
    BukkitRecipeItem setUnbreakable(final boolean unbreakable);

    @Override
    default BukkitRecipeItem copy() {
        return RecipeItem.super.copy(BukkitRecipeItem.class);
    }
}
