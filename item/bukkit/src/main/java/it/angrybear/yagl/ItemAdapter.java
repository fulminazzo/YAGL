package it.angrybear.yagl;

import it.angrybear.yagl.items.BukkitItem;
import it.angrybear.yagl.items.Item;
import it.angrybear.yagl.items.fields.ItemFlag;
import it.fulminazzo.fulmicollection.objects.Refl;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A collection of utilities for {@link Item}.
 */
public class ItemAdapter {
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
            try {
                int modelData = meta.getCustomModelData();
                if (modelData > 0) item.setCustomModelData(modelData);
            } catch (Exception ignored) {}
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
        String material = item.getMaterial();
        if (material == null) throw new NullPointerException("Material cannot be null!");
        ItemStack itemStack = new ItemStack(Material.valueOf(material.toUpperCase()), item.getAmount());
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(item.getDisplayName());
            meta.setLore(item.getLore());
            item.getEnchantments().forEach(e -> {
                NamespacedKey name = new NamespacedKey("minecraft", e.getEnchantment());
                org.bukkit.enchantments.Enchantment enchantment = org.bukkit.enchantments.Enchantment.getByKey(name);
                if (enchantment != null) meta.addEnchant(enchantment, e.getLevel(), true);
            });
            item.getItemFlags().forEach(f -> meta.addItemFlags(org.bukkit.inventory.ItemFlag.valueOf(f.name())));
            if (meta instanceof Damageable) ((Damageable) meta).setDamage(item.getDurability());
            meta.setUnbreakable(item.isUnbreakable());
            try {
                int modelData = item.getCustomModelData();
                if (modelData > 0) meta.setCustomModelData(modelData);
            } catch (Exception ignored) {}
            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }

    public static Recipe recipeToMinecraft(final @Nullable it.angrybear.yagl.items.recipes.Recipe recipe) {
        if (recipe == null) return null;
        Recipe result;
        if (recipe instanceof it.angrybear.yagl.items.recipes.ShapedRecipe)
            result = recipeToMinecraft((it.angrybear.yagl.items.recipes.ShapedRecipe) recipe);
        else if (recipe instanceof it.angrybear.yagl.items.recipes.ShapelessRecipe)
            result = recipeToMinecraft((it.angrybear.yagl.items.recipes.ShapelessRecipe) recipe);
        else if (recipe instanceof it.angrybear.yagl.items.recipes.FurnaceRecipe)
            result = recipeToMinecraft((it.angrybear.yagl.items.recipes.FurnaceRecipe) recipe);
        else throw new IllegalArgumentException("Unrecognized recipe type: " + recipe.getClass());
        return result;
    }

    private static Recipe recipeToMinecraft(final @Nullable it.angrybear.yagl.items.recipes.ShapedRecipe recipe) {
        if (recipe == null) return null;
        final NamespacedKey namespacedKey = new NamespacedKey(ID_KEY, recipe.getId());

        final ItemStack output = recipe.getOutput().copy(BukkitItem.class).create();

        ShapedRecipe r = new ShapedRecipe(namespacedKey, output);

        StringBuilder charShape = new StringBuilder();
        it.angrybear.yagl.items.recipes.ShapedRecipe.Shape shape = recipe.getShape();
        char c = 'A';
        for (int i = 0; i < shape.getRows(); i++) {
            for (int j = 0; j < shape.getColumns(); j++) {
                charShape.append(c++);
            }
            charShape.append(",");
        }
        r.shape(charShape.toString().split(","));

        List<Item> ingredients = recipe.getIngredients();
        Refl<?> tmp = new Refl<>(r);
        Map<Character, Object> finalIngredients = tmp.getFieldObject("ingredients");

        if (finalIngredients != null)
            for (char ch = 'A'; ch < c; ch++)
                finalIngredients.put(ch, getItemOrRecipeChoice(ingredients.get(ch - 'A')));

        return r;
    }

    private static Recipe recipeToMinecraft(final @Nullable it.angrybear.yagl.items.recipes.ShapelessRecipe recipe) {
        if (recipe == null) return null;
        final NamespacedKey namespacedKey = new NamespacedKey(ID_KEY, recipe.getId());

        final List<Object> ingredients = recipe.getIngredients().stream()
                .map(ItemAdapter::getItemOrRecipeChoice)
                .collect(Collectors.toList());
        final ItemStack output = recipe.getOutput().copy(BukkitItem.class).create();

        ShapelessRecipe r = new ShapelessRecipe(namespacedKey, output);
        Refl<?> tmp = new Refl<>(r);
        tmp.setFieldObject("ingredients", ingredients);

        return r;
    }

    private static Recipe recipeToMinecraft(final @Nullable it.angrybear.yagl.items.recipes.FurnaceRecipe recipe) {
        if (recipe == null) return null;
        final NamespacedKey namespacedKey = new NamespacedKey(ID_KEY, recipe.getId());

        final Object ingredient = getItemOrRecipeChoice(recipe.getIngredients().get(0));
        final ItemStack output = recipe.getOutput().copy(BukkitItem.class).create();
        final int cookingTime = recipe.getCookingTime();
        final float experience = recipe.getExperience();

        FurnaceRecipe r = new FurnaceRecipe(namespacedKey, output, Material.STONE, experience, cookingTime);
        Refl<?> tmp = new Refl<>(r);
        tmp.setFieldObject("ingredient", ingredient);

        return r;
    }

    private static Object getItemOrRecipeChoice(final @Nullable Item item) {
        if (item == null) return null;
        ItemStack itemStack = item.copy(BukkitItem.class).create();
        try {
            return new Refl<>("org.bukkit.inventory.RecipeChoice.ExactChoice", itemStack).getObject();
        } catch (Exception e) {
            return itemStack;
        }
    }
}
