package it.fulminazzo.yagl.item;

import it.fulminazzo.yagl.item.field.ItemFlag;
import it.fulminazzo.yagl.item.recipe.Recipe;
import it.fulminazzo.yagl.wrapper.Enchantment;
import it.fulminazzo.yagl.wrapper.PotionEffect;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * An implementation of {@link RecipeItem} that actually implements {@link #registerRecipes()} and {@link #unregisterRecipes()}.
 */
public interface BukkitRecipeItem extends RecipeItem, BukkitItem {

    @Override
    default @NotNull BukkitRecipeItem setRecipes(final Recipe @NotNull ... recipes) {
        return (BukkitRecipeItem) RecipeItem.super.setRecipes(recipes);
    }

    @Override
    @NotNull BukkitRecipeItem addRecipes(final Recipe @NotNull ... recipes);

    @Override
    @NotNull BukkitRecipeItem clearRecipes(final Predicate<Recipe> predicate);

    @Override
    default @NotNull BukkitRecipeItem clearRecipes() {
        return (BukkitRecipeItem) RecipeItem.super.clearRecipes();
    }

    @Override
    @NotNull <M extends ItemMeta> BukkitRecipeItem setMetadata(final @Nullable Class<M> itemMetaClass, final @Nullable Consumer<M> metaFunction);

    @Override
    @NotNull BukkitRecipeItem setMaterial(final @NotNull String material);

    @Override
    @NotNull BukkitRecipeItem setAmount(final int amount);

    @Override
    @NotNull BukkitRecipeItem setDurability(final int durability);

    @Override
    @NotNull BukkitRecipeItem setDisplayName(final @NotNull String displayName);

    @Override
    @NotNull BukkitRecipeItem setCustomModelData(final int customModelData);

    @Override
    default @NotNull BukkitRecipeItem addLore(final String @NotNull ... lore) {
        return (BukkitRecipeItem) RecipeItem.super.addLore(lore);
    }

    @Override
    default @NotNull BukkitRecipeItem addLore(final @NotNull Collection<String> lore) {
        return (BukkitRecipeItem) RecipeItem.super.addLore(lore);
    }

    @Override
    default @NotNull BukkitRecipeItem removeLore(final String @NotNull ... lore) {
        return (BukkitRecipeItem) RecipeItem.super.removeLore(lore);
    }

    @Override
    default @NotNull BukkitRecipeItem removeLore(final @NotNull Collection<String> lore) {
        return (BukkitRecipeItem) RecipeItem.super.removeLore(lore);
    }

    @Override
    default @NotNull BukkitRecipeItem setLore(final String @NotNull ... lore) {
        return (BukkitRecipeItem) RecipeItem.super.setLore(lore);
    }

    @Override
    default @NotNull BukkitRecipeItem setLore(final @NotNull Collection<String> lore) {
        return (BukkitRecipeItem) RecipeItem.super.setLore(lore);
    }

    @Override
    default @NotNull BukkitRecipeItem addEnchantment(final @NotNull String enchantment, final int level) {
        return (BukkitRecipeItem) RecipeItem.super.addEnchantment(enchantment, level);
    }

    @Override
    default @NotNull BukkitRecipeItem addEnchantments(final String @NotNull ... enchantments) {
        return (BukkitRecipeItem) RecipeItem.super.addEnchantments(enchantments);
    }

    @Override
    default @NotNull BukkitRecipeItem addEnchantments(final Enchantment @NotNull ... enchantments) {
        return (BukkitRecipeItem) RecipeItem.super.addEnchantments(enchantments);
    }

    @Override
    default @NotNull BukkitRecipeItem addEnchantments(final @NotNull Collection<Enchantment> enchantments) {
        return (BukkitRecipeItem) RecipeItem.super.addEnchantments(enchantments);
    }

    @Override
    default @NotNull BukkitRecipeItem removeEnchantment(final @NotNull String enchantment, final int level) {
        return (BukkitRecipeItem) RecipeItem.super.removeEnchantment(enchantment, level);
    }

    @Override
    default @NotNull BukkitRecipeItem removeEnchantments(final String @NotNull ... enchantments) {
        return (BukkitRecipeItem) RecipeItem.super.removeEnchantments(enchantments);
    }

    @Override
    default @NotNull BukkitRecipeItem removeEnchantments(final Enchantment @NotNull ... enchantments) {
        return (BukkitRecipeItem) RecipeItem.super.removeEnchantments(enchantments);
    }

    @Override
    default @NotNull BukkitRecipeItem removeEnchantments(final @NotNull Collection<Enchantment> enchantments) {
        return (BukkitRecipeItem) RecipeItem.super.removeEnchantments(enchantments);
    }

    @Override
    default @NotNull BukkitRecipeItem addItemFlags(final ItemFlag @NotNull ... itemFlags) {
        return (BukkitRecipeItem) RecipeItem.super.addItemFlags(itemFlags);
    }

    @Override
    default @NotNull BukkitRecipeItem addItemFlags(final @NotNull Collection<ItemFlag> itemFlags) {
        return (BukkitRecipeItem) RecipeItem.super.addItemFlags(itemFlags);
    }

    @Override
    default @NotNull BukkitRecipeItem removeItemFlags(final ItemFlag @NotNull ... itemFlags) {
        return (BukkitRecipeItem) RecipeItem.super.removeItemFlags(itemFlags);
    }

    @Override
    default @NotNull BukkitRecipeItem removeItemFlags(final @NotNull Collection<ItemFlag> itemFlags) {
        return (BukkitRecipeItem) RecipeItem.super.removeItemFlags(itemFlags);
    }

    @Override
    default @NotNull BukkitRecipeItem addPotionEffects(final PotionEffect @NotNull ... potionEffects) {
        return (BukkitRecipeItem) RecipeItem.super.addPotionEffects(potionEffects);
    }

    @Override
    default @NotNull BukkitRecipeItem addPotionEffects(final @NotNull Collection<PotionEffect> potionEffects) {
        return (BukkitRecipeItem) RecipeItem.super.addPotionEffects(potionEffects);
    }

    @Override
    default @NotNull BukkitRecipeItem removePotionEffects(final PotionEffect @NotNull ... potionEffects) {
        return (BukkitRecipeItem) RecipeItem.super.removePotionEffects(potionEffects);
    }

    @Override
    default @NotNull BukkitRecipeItem removePotionEffects(final @NotNull Collection<PotionEffect> potionEffects) {
        return (BukkitRecipeItem) RecipeItem.super.removePotionEffects(potionEffects);
    }

    @Override
    default @NotNull BukkitRecipeItem setPotionEffects(final PotionEffect @NotNull ... potionEffects) {
        return (BukkitRecipeItem) RecipeItem.super.setPotionEffects(potionEffects);
    }

    @Override
    default @NotNull BukkitRecipeItem setPotionEffects(final @NotNull Collection<PotionEffect> potionEffects) {
        return (BukkitRecipeItem) RecipeItem.super.setPotionEffects(potionEffects);
    }

    @Override
    @NotNull BukkitRecipeItem setUnbreakable(final boolean unbreakable);

    @Override
    default @NotNull BukkitRecipeItem copy() {
        return RecipeItem.super.copy(BukkitRecipeItem.class);
    }
}
