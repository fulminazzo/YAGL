package it.fulminazzo.yagl.items;

import it.fulminazzo.yagl.items.fields.ItemFlag;
import it.fulminazzo.yagl.items.recipes.Recipe;
import it.fulminazzo.yagl.wrappers.Enchantment;
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
    default RecipeItem setRecipes(final Recipe @NotNull ... recipes) {
        return clearRecipes().addRecipes(recipes);
    }

    /**
     * Add recipes.
     *
     * @param recipes the recipes
     * @return this recipe item
     */
    RecipeItem addRecipes(final Recipe @NotNull ... recipes);

    /**
     * Removes all the recipes that match the given {@link Predicate}.
     *
     * @param predicate the predicate
     * @return this recipe item
     */
    RecipeItem clearRecipes(final Predicate<Recipe> predicate);

    /**
     * Removes all the recipes.
     *
     * @return this recipe item
     */
    default RecipeItem clearRecipes() {
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
    RecipeItem setMaterial(final @NotNull String material);

    @Override
    RecipeItem setAmount(final int amount);

    @Override
    RecipeItem setDurability(final int durability);

    @Override
    RecipeItem setDisplayName(final @NotNull String displayName);

    @Override
    RecipeItem setCustomModelData(final int customModelData);

    @Override
    default RecipeItem addLore(final String @NotNull ... lore) {
        return (RecipeItem) Item.super.addLore(lore);
    }

    @Override
    default RecipeItem addLore(final @NotNull Collection<String> lore) {
        return (RecipeItem) Item.super.addLore(lore);
    }

    @Override
    default RecipeItem removeLore(final String @NotNull ... lore) {
        return (RecipeItem) Item.super.removeLore(lore);
    }

    @Override
    default RecipeItem removeLore(final @NotNull Collection<String> lore) {
        return (RecipeItem) Item.super.removeLore(lore);
    }

    @Override
    default RecipeItem setLore(final String @NotNull ... lore) {
        return (RecipeItem) Item.super.setLore(lore);
    }

    @Override
    default RecipeItem setLore(final @NotNull Collection<String> lore) {
        return (RecipeItem) Item.super.setLore(lore);
    }

    @Override
    default RecipeItem addEnchantment(final @NotNull String enchantment, final int level) {
        return (RecipeItem) Item.super.addEnchantment(enchantment, level);
    }

    @Override
    default RecipeItem addEnchantments(final String @NotNull ... enchantments) {
        return (RecipeItem) Item.super.addEnchantments(enchantments);
    }

    @Override
    default RecipeItem addEnchantments(final Enchantment @NotNull ... enchantments) {
        return (RecipeItem) Item.super.addEnchantments(enchantments);
    }

    @Override
    default RecipeItem addEnchantments(final @NotNull Collection<Enchantment> enchantments) {
        return (RecipeItem) Item.super.addEnchantments(enchantments);
    }

    @Override
    default RecipeItem removeEnchantment(final @NotNull String enchantment, final int level) {
        return (RecipeItem) Item.super.removeEnchantment(enchantment, level);
    }

    @Override
    default RecipeItem removeEnchantments(final String @NotNull ... enchantments) {
        return (RecipeItem) Item.super.removeEnchantments(enchantments);
    }

    @Override
    default RecipeItem removeEnchantments(final Enchantment @NotNull ... enchantments) {
        return (RecipeItem) Item.super.removeEnchantments(enchantments);
    }

    @Override
    default RecipeItem removeEnchantments(final @NotNull Collection<Enchantment> enchantments) {
        return (RecipeItem) Item.super.removeEnchantments(enchantments);
    }

    @Override
    default RecipeItem addItemFlags(final ItemFlag @NotNull ... itemFlags) {
        return (RecipeItem) Item.super.addItemFlags(itemFlags);
    }

    @Override
    default RecipeItem addItemFlags(final @NotNull Collection<ItemFlag> itemFlags) {
        return (RecipeItem) Item.super.addItemFlags(itemFlags);
    }

    @Override
    default RecipeItem removeItemFlags(final ItemFlag @NotNull ... itemFlags) {
        return (RecipeItem) Item.super.removeItemFlags(itemFlags);
    }

    @Override
    default RecipeItem removeItemFlags(final @NotNull Collection<ItemFlag> itemFlags) {
        return (RecipeItem) Item.super.removeItemFlags(itemFlags);
    }

    @Override
    RecipeItem setUnbreakable(final boolean unbreakable);

    @Override
    default RecipeItem copy() {
        return Item.super.copy(RecipeItem.class);
    }
}
