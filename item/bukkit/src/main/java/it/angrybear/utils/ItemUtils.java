package it.angrybear.utils;

import it.angrybear.items.BukkitItem;
import it.angrybear.items.Item;
import it.angrybear.items.fields.Enchantment;
import it.angrybear.items.fields.ItemFlag;
import it.fulminazzo.fulmicollection.objects.Refl;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A collection of utilities for {@link Item}.
 */
public class ItemUtils {
    private static final String ID_KEY = "yagl";

    /**
     * Converts the given {@link ItemStack} to an {@link Item}.
     *
     * @param itemStack the item stack
     * @return the item
     */
    public static @Nullable Item itemStackToItem(final @Nullable ItemStack itemStack) {
        if (itemStack == null) return null;
        Item item = BukkitItem.newItem()
                .setMaterial(itemStack.getType().name())
                .setAmount(itemStack.getAmount());
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            String displayName = meta.getDisplayName();
            if (displayName != null) item.setDisplayName(displayName);
            List<String> lore = meta.getLore();
            if (lore != null) item.setLore(lore);
            meta.getEnchants().forEach((e, l) -> item.addEnchantments(new Enchantment(e.getKey().getKey(), l)));
            meta.getItemFlags().forEach(f -> item.addItemFlags(ItemFlag.valueOf(f.name())));
            if (meta instanceof Damageable) item.setDurability(((Damageable) meta).getDamage());
            item.setUnbreakable(meta.isUnbreakable());
        }
        return item;
    }

    /**
     * Converts the given {@link Item} to an {@link ItemStack}.
     *
     * @param item the item
     * @return the item stack
     */
    public static @Nullable ItemStack itemToItemStack(final @Nullable Item item) {
        if (item == null) return null;
        ItemStack itemStack = new ItemStack(Material.valueOf(item.getMaterial()), item.getAmount());
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(item.getDisplayName());
            meta.setLore(item.getLore());
            item.getEnchantments().forEach(e -> {
                NamespacedKey name = new NamespacedKey("minecraft", e.getEnchantment());
                org.bukkit.enchantments.Enchantment enchantment = org.bukkit.enchantments.Enchantment.getByKey(name);
                meta.addEnchant(enchantment, e.getLevel(), true);
            });
            item.getItemFlags().forEach(f -> meta.addItemFlags(org.bukkit.inventory.ItemFlag.valueOf(f.name())));
            if (meta instanceof Damageable) ((Damageable) meta).setDamage(item.getDurability());
            meta.setUnbreakable(item.isUnbreakable());
            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }

    public static Recipe recipeToMinecraft(final @Nullable it.angrybear.items.recipes.Recipe recipe) {
        if (recipe == null) return null;
        Recipe result;
        if (recipe instanceof it.angrybear.items.recipes.ShapedRecipe) {
        } else if (recipe instanceof it.angrybear.items.recipes.ShapelessRecipe)
            result = recipeToMinecraft((it.angrybear.items.recipes.ShapelessRecipe) recipe);
        else if (recipe instanceof it.angrybear.items.recipes.FurnaceRecipe)
            result = recipeToMinecraft((it.angrybear.items.recipes.FurnaceRecipe) recipe);
        else throw new IllegalArgumentException("Unrecognized recipe type: " + recipe.getClass());
        return result;
    }

    private static Recipe recipeToMinecraft(final @Nullable it.angrybear.items.recipes.ShapelessRecipe recipe) {
        if (recipe == null) return null;
        final NamespacedKey namespacedKey = new NamespacedKey(ID_KEY, recipe.getId());

        final List<Object> ingredients = recipe.getIngredients().stream()
                .map(i -> (BukkitItem) i)
                .map(ItemUtils::getItemOrRecipeChoice)
                .collect(Collectors.toList());
        final ItemStack output = ((BukkitItem) recipe.getOutput()).create();

        ShapelessRecipe r = new ShapelessRecipe(namespacedKey, output);
        Refl<?> tmp = new Refl<>(r);
        tmp.setFieldObject("ingredients", ingredients);

        return r;
    }

    private static Recipe recipeToMinecraft(final @Nullable it.angrybear.items.recipes.FurnaceRecipe recipe) {
        if (recipe == null) return null;
        final NamespacedKey namespacedKey = new NamespacedKey(ID_KEY, recipe.getId());

        final Object ingredient = getItemOrRecipeChoice((BukkitItem) recipe.getIngredients().get(0));
        final ItemStack output = ((BukkitItem) recipe.getOutput()).create();
        final int cookingTime = recipe.getCookingTime();
        final float experience = recipe.getExperience();

        FurnaceRecipe r = new FurnaceRecipe(namespacedKey, output, Material.STONE, experience, cookingTime);
        Refl<?> tmp = new Refl<>(r);
        tmp.setFieldObject("ingredient", ingredient);

        return r;
    }

    private static Object getItemOrRecipeChoice(final @NotNull BukkitItem item) {
        ItemStack itemStack = item.create();
        try {
            return new Refl<>("org.bukkit.inventory.RecipeChoice.ExactChoice", itemStack).getObject();
        } catch (Exception e) {
            return itemStack;
        }
    }
}
