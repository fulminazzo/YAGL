package it.fulminazzo.yagl;

import it.fulminazzo.yagl.items.BukkitItem;
import it.fulminazzo.yagl.items.Item;
import it.fulminazzo.yagl.items.fields.ItemFlag;
import it.fulminazzo.yagl.items.recipes.FurnaceRecipe;
import it.fulminazzo.yagl.items.recipes.Recipe;
import it.fulminazzo.yagl.items.recipes.ShapedRecipe;
import it.fulminazzo.yagl.items.recipes.ShapelessRecipe;
import it.fulminazzo.yagl.utils.EnumUtils;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.structures.tuples.Tuple;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A collection of utilities for {@link Item}.
 */
@SuppressWarnings("deprecation")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemAdapter {
    private static final String ID_KEY = "yagl";

    /**
     * Converts the given {@link ItemStack} to an {@link Item}.
     *
     * @param itemStack the item stack
     * @return the item
     */
    public static @Nullable Item itemStackToItem(final @Nullable ItemStack itemStack) {
        if (itemStack == null) return null;
        Item item = BukkitItem.newItem().setMaterial(itemStack.getType().name()).setAmount(itemStack.getAmount());
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            String displayName = meta.getDisplayName();
            if (displayName != null) item.setDisplayName(displayName);
            List<String> lore = meta.getLore();
            if (lore != null) item.setLore(lore);
            meta.getEnchants().forEach((e, l) -> item.addEnchantments(WrappersAdapter.enchantToWEnchant(e, l)));
            meta.getItemFlags().forEach(f -> item.addItemFlags(EnumUtils.valueOf(ItemFlag.class, f.name())));
            invokeNoSuchMethod(() -> {
                if (meta instanceof org.bukkit.inventory.meta.Damageable)
                    item.setDurability(((org.bukkit.inventory.meta.Damageable) meta).getDamage());
            }, item::getDurability);
            invokeNoSuchMethod(() -> item.setUnbreakable(meta.isUnbreakable()), () ->
                    item.setUnbreakable(meta.spigot().isUnbreakable()));
            invokeNoSuchMethod(() -> {
                if (meta.hasCustomModelData()) item.setCustomModelData(meta.getCustomModelData());
            }, null);
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
        if (material == null) throw new IllegalArgumentException("Material cannot be null!");
        ItemStack itemStack = new ItemStack(EnumUtils.valueOf(Material.class, item.getMaterial()), item.getAmount());

        final ItemMeta meta = getItemMeta(itemStack);
        if (meta != null) {
            meta.setDisplayName(item.getDisplayName());
            meta.setLore(item.getLore());
            item.getEnchantments().forEach(e -> {
                @NotNull Tuple<Enchantment, Integer> tuple = WrappersAdapter.wEnchantToEnchant(e);
                meta.addEnchant(tuple.getKey(), tuple.getValue(), true);
            });
            item.getItemFlags().forEach(f -> meta.addItemFlags(EnumUtils.valueOf(org.bukkit.inventory.ItemFlag.class, f.name())));
            invokeNoSuchMethod(() -> {
                if (meta instanceof org.bukkit.inventory.meta.Damageable) ((org.bukkit.inventory.meta.Damageable) meta).setDamage(item.getDurability());
            }, () -> item.setDurability(item.getDurability()));
            invokeNoSuchMethod(() -> meta.setUnbreakable(item.isUnbreakable()), () ->
                    meta.spigot().setUnbreakable(item.isUnbreakable()));
            invokeNoSuchMethod(() -> {
                int modelData = item.getCustomModelData();
                if (modelData > 0) meta.setCustomModelData(modelData);
            }, null);
            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }

    private static ItemMeta getItemMeta(final @NotNull ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return Bukkit.getItemFactory().getItemMeta(itemStack.getType());
        else return meta;
    }

    /**
     * Tries to execute the first given {@link Runnable}.
     * If it fails for a {@link NoSuchMethodError}, the second one is called.
     *
     * @param get the first function
     * @param orElse the second function
     */
    private static void invokeNoSuchMethod(final @NotNull Runnable get, final @Nullable Runnable orElse) {
        try {
            get.run();
        } catch (NoSuchMethodError | NoClassDefFoundError e) {
            if (orElse != null) orElse.run();
        }
    }

    /**
     * Converts the {@link Recipe} to a Minecraft {@link org.bukkit.inventory.Recipe}.
     *
     * @param recipe the recipe
     * @return the recipe
     */
    public static org.bukkit.inventory.Recipe recipeToMinecraft(final @Nullable Recipe recipe) {
        if (recipe == null) return null;
        org.bukkit.inventory.Recipe result;
        if (recipe instanceof ShapedRecipe)
            result = recipeToMinecraft((ShapedRecipe) recipe);
        else if (recipe instanceof ShapelessRecipe)
            result = recipeToMinecraft((ShapelessRecipe) recipe);
        else if (recipe instanceof FurnaceRecipe)
            result = recipeToMinecraft((FurnaceRecipe) recipe);
        else throw new IllegalArgumentException("Unrecognized recipe type: " + recipe.getClass());
        return result;
    }

    @SuppressWarnings("DataFlowIssue")
    private static org.bukkit.inventory.Recipe recipeToMinecraft(final @Nullable ShapedRecipe recipe) {
        if (recipe == null) return null;

        final ItemStack output = recipe.getOutput().copy(BukkitItem.class).create();

        Refl<org.bukkit.inventory.ShapedRecipe> r;
        try {
            final Object namespacedKey = WrappersAdapter.getNamespacedKey(ID_KEY, recipe.getId());
            r = new Refl<>(org.bukkit.inventory.ShapedRecipe.class, namespacedKey, output);
        } catch (RuntimeException e) {
            r = new Refl<>(org.bukkit.inventory.ShapedRecipe.class, output);
        }

        StringBuilder charShape = new StringBuilder();
        ShapedRecipe.Shape shape = recipe.getShape();
        char c = 'A';
        for (int i = 0; i < shape.getRows(); i++) {
            for (int j = 0; j < shape.getColumns(); j++) charShape.append(c++);
            charShape.append(",");
        }
        r.getObject().shape(charShape.toString().split(","));

        List<Item> ingredients = recipe.getIngredients();
        Map<Character, Object> finalIngredients = r.getFieldObject("ingredients");

        for (char ch = 'A'; ch < c; ch++)
            finalIngredients.put(ch, getItemOrRecipeChoice(ingredients.get(ch - 'A')));

        return r.getObject();
    }

    private static org.bukkit.inventory.Recipe recipeToMinecraft(final @Nullable ShapelessRecipe recipe) {
        if (recipe == null) return null;

        final List<Object> ingredients = recipe.getIngredients().stream()
                .map(ItemAdapter::getItemOrRecipeChoice)
                .collect(Collectors.toList());
        final ItemStack output = recipe.getOutput().copy(BukkitItem.class).create();

        Refl<org.bukkit.inventory.ShapelessRecipe> r;
        try {
            final Object namespacedKey = WrappersAdapter.getNamespacedKey(ID_KEY, recipe.getId());
            r = new Refl<>(org.bukkit.inventory.ShapelessRecipe.class, namespacedKey, output);
        } catch (RuntimeException e) {
            r = new Refl<>(org.bukkit.inventory.ShapelessRecipe.class, output);
        }

        r.setFieldObject("ingredients", ingredients);

        return r.getObject();
    }

    private static org.bukkit.inventory.Recipe recipeToMinecraft(final @Nullable FurnaceRecipe recipe) {
        if (recipe == null) return null;

        final Object ingredient = getItemOrRecipeChoice(recipe.getIngredients().get(0));
        final ItemStack output = recipe.getOutput().copy(BukkitItem.class).create();
        final int cookingTime = (int) (recipe.getCookingTime() * Constants.TICKS_IN_SECOND);
        final float experience = recipe.getExperience();

        Refl<org.bukkit.inventory.FurnaceRecipe> r;
        try {
            final Object namespacedKey = WrappersAdapter.getNamespacedKey(ID_KEY, recipe.getId());
            r = new Refl<>(org.bukkit.inventory.FurnaceRecipe.class, namespacedKey, output, Material.STONE, experience, cookingTime);
        } catch (RuntimeException e) {
            r = new Refl<>(org.bukkit.inventory.FurnaceRecipe.class, output, Material.STONE);
        }

        r.setFieldObject("ingredient", ingredient);

        return r.getObject();
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
