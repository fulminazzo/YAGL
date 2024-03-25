package it.angrybear.yagl.items;

import it.angrybear.yagl.items.fields.ItemFlag;
import it.angrybear.yagl.items.recipes.Recipe;
import it.angrybear.yagl.wrappers.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * An implementation of {@link Item} that supports a {@link Recipe}.
 */
public interface RecipeItem extends Item {

    default RecipeItem setRecipes(final Recipe @NotNull ... recipes) {
        return clearRecipes().addRecipes(recipes);
    }

    RecipeItem addRecipes(final Recipe @NotNull ... recipes);

    RecipeItem clearRecipes(final Predicate<Recipe> predicate);

    default RecipeItem clearRecipes() {
        return clearRecipes(t -> true);
    }

    void registerRecipes();

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
