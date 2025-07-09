package it.fulminazzo.yagl.item;

import it.fulminazzo.yagl.item.field.ItemFlag;
import it.fulminazzo.yagl.item.recipe.Recipe;
import it.fulminazzo.yagl.wrapper.Enchantment;
import it.fulminazzo.yagl.wrapper.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * An implementation of {@link Item} that supports a {@link Recipe}.
 */
public interface RecipeItem extends Item, Iterable<Recipe> {

    /**
     * Sets recipes.
     *
     * @param recipes the recipes
     * @return this recipe item
     */
    default @NotNull RecipeItem setRecipes(final Recipe @NotNull ... recipes) {
        return clearRecipes().addRecipes(recipes);
    }

    /**
     * Add recipes.
     *
     * @param recipes the recipes
     * @return this recipe item
     */
    @NotNull RecipeItem addRecipes(final Recipe @NotNull ... recipes);

    /**
     * Removes all the recipes that match the given {@link Predicate}.
     *
     * @param predicate the predicate
     * @return this recipe item
     */
    @NotNull RecipeItem clearRecipes(final Predicate<Recipe> predicate);

    /**
     * Removes all the recipes.
     *
     * @return this recipe item
     */
    default @NotNull RecipeItem clearRecipes() {
        return clearRecipes(t -> true);
    }

    /**
     * Register recipes.
     */
    void registerRecipes();

    /**
     * Unregister recipes.
     */
    void unregisterRecipes();

    @Override
    @NotNull RecipeItem setMaterial(final @NotNull String material);

    @Override
    @NotNull RecipeItem setAmount(final int amount);

    @Override
    @NotNull RecipeItem setDurability(final int durability);

    @Override
    @NotNull RecipeItem setDisplayName(final @NotNull String displayName);

    @Override
    @NotNull RecipeItem setCustomModelData(final int customModelData);

    @Override
    default @NotNull RecipeItem addLore(final String @NotNull ... lore) {
        return (RecipeItem) Item.super.addLore(lore);
    }

    @Override
    default @NotNull RecipeItem addLore(final @NotNull Collection<String> lore) {
        return (RecipeItem) Item.super.addLore(lore);
    }

    @Override
    default @NotNull RecipeItem removeLore(final String @NotNull ... lore) {
        return (RecipeItem) Item.super.removeLore(lore);
    }

    @Override
    default @NotNull RecipeItem removeLore(final @NotNull Collection<String> lore) {
        return (RecipeItem) Item.super.removeLore(lore);
    }

    @Override
    default @NotNull RecipeItem setLore(final String @NotNull ... lore) {
        return (RecipeItem) Item.super.setLore(lore);
    }

    @Override
    default @NotNull RecipeItem setLore(final @NotNull Collection<String> lore) {
        return (RecipeItem) Item.super.setLore(lore);
    }

    @Override
    default @NotNull RecipeItem addEnchantment(final @NotNull String enchantment, final int level) {
        return (RecipeItem) Item.super.addEnchantment(enchantment, level);
    }

    @Override
    default @NotNull RecipeItem addEnchantments(final String @NotNull ... enchantments) {
        return (RecipeItem) Item.super.addEnchantments(enchantments);
    }

    @Override
    default @NotNull RecipeItem addEnchantments(final Enchantment @NotNull ... enchantments) {
        return (RecipeItem) Item.super.addEnchantments(enchantments);
    }

    @Override
    default @NotNull RecipeItem addEnchantments(final @NotNull Collection<Enchantment> enchantments) {
        return (RecipeItem) Item.super.addEnchantments(enchantments);
    }

    @Override
    default @NotNull RecipeItem removeEnchantment(final @NotNull String enchantment, final int level) {
        return (RecipeItem) Item.super.removeEnchantment(enchantment, level);
    }

    @Override
    default @NotNull RecipeItem removeEnchantments(final String @NotNull ... enchantments) {
        return (RecipeItem) Item.super.removeEnchantments(enchantments);
    }

    @Override
    default @NotNull RecipeItem removeEnchantments(final Enchantment @NotNull ... enchantments) {
        return (RecipeItem) Item.super.removeEnchantments(enchantments);
    }

    @Override
    default @NotNull RecipeItem removeEnchantments(final @NotNull Collection<Enchantment> enchantments) {
        return (RecipeItem) Item.super.removeEnchantments(enchantments);
    }

    @Override
    default @NotNull RecipeItem addItemFlags(final ItemFlag @NotNull ... itemFlags) {
        return (RecipeItem) Item.super.addItemFlags(itemFlags);
    }

    @Override
    default @NotNull RecipeItem addItemFlags(final @NotNull Collection<ItemFlag> itemFlags) {
        return (RecipeItem) Item.super.addItemFlags(itemFlags);
    }

    @Override
    default @NotNull RecipeItem removeItemFlags(final ItemFlag @NotNull ... itemFlags) {
        return (RecipeItem) Item.super.removeItemFlags(itemFlags);
    }

    @Override
    default @NotNull RecipeItem removeItemFlags(final @NotNull Collection<ItemFlag> itemFlags) {
        return (RecipeItem) Item.super.removeItemFlags(itemFlags);
    }

    @Override
    default @NotNull RecipeItem addPotionEffects(final PotionEffect @NotNull ... potionEffects) {
        return (RecipeItem) Item.super.addPotionEffects(potionEffects);
    }

    @Override
    default @NotNull RecipeItem addPotionEffects(final @NotNull Collection<PotionEffect> potionEffects) {
        return (RecipeItem) Item.super.addPotionEffects(potionEffects);
    }

    @Override
    default @NotNull RecipeItem removePotionEffects(final PotionEffect @NotNull ... potionEffects) {
        return (RecipeItem) Item.super.removePotionEffects(potionEffects);
    }

    @Override
    default @NotNull RecipeItem removePotionEffects(final @NotNull Collection<PotionEffect> potionEffects) {
        return (RecipeItem) Item.super.removePotionEffects(potionEffects);
    }

    @Override
    default @NotNull RecipeItem setPotionEffects(final PotionEffect @NotNull ... potionEffects) {
        return (RecipeItem) Item.super.setPotionEffects(potionEffects);
    }

    @Override
    default @NotNull RecipeItem setPotionEffects(final @NotNull Collection<PotionEffect> potionEffects) {
        return (RecipeItem) Item.super.setPotionEffects(potionEffects);
    }

    @Override
    @NotNull RecipeItem setUnbreakable(final boolean unbreakable);

    @Override
    default @NotNull RecipeItem copy() {
        return Item.super.copy(RecipeItem.class);
    }

}
