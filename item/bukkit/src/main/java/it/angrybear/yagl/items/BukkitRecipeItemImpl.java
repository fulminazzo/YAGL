package it.angrybear.yagl.items;

import it.angrybear.yagl.ItemAdapter;
import it.angrybear.yagl.items.recipes.Recipe;
import it.angrybear.yagl.utils.EnumUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

/**
 * An implementation of {@link RecipeItemImpl} that actually implements {@link #registerRecipes()} and {@link #unregisterRecipes()}.
 */
class BukkitRecipeItemImpl extends RecipeItemImpl implements BukkitRecipeItem {
    private final List<org.bukkit.inventory.Recipe> recipeCache = new ArrayList<>();

    /**
     * Instantiates a new Bukkit recipe item.
     */
    public BukkitRecipeItemImpl() {
        super();
    }

    @Override
    public void registerRecipes() {
        for (Recipe recipe : this.recipes) {
            recipe.setOutput(this);
            org.bukkit.inventory.Recipe realRecipe = ItemAdapter.recipeToMinecraft(recipe);
            this.recipeCache.add(realRecipe);
            Bukkit.addRecipe(realRecipe);
        }
    }

    @Override
    public void unregisterRecipes() {
        Iterator<org.bukkit.inventory.Recipe> recipes = Bukkit.recipeIterator();
        while (recipes.hasNext()) {
            org.bukkit.inventory.Recipe r = recipes.next();
            if (this.recipeCache.contains(r)) recipes.remove();
        }
        this.recipeCache.clear();
    }

    @Override
    public BukkitRecipeItem setMaterial(@NotNull String material) {
        EnumUtils.valueOf(Material.class, material);
        return (BukkitRecipeItem) super.setMaterial(material);
    }

    @Override
    public BukkitRecipeItem setAmount(final int amount) {
        return (BukkitRecipeItem) super.setAmount(amount);
    }

    @Override
    public BukkitRecipeItem setDurability(final int durability) {
        return (BukkitRecipeItem) super.setDurability(durability);
    }

    @Override
    public BukkitRecipeItem setDisplayName(final @NotNull String displayName) {
        return (BukkitRecipeItem) super.setDisplayName(displayName);
    }

    @Override
    public BukkitRecipeItem setLore(final @NotNull Collection<String> lore) {
        return (BukkitRecipeItem) super.setLore(lore);
    }

    @Override
    public BukkitRecipeItem setUnbreakable(final boolean unbreakable) {
        return (BukkitRecipeItem) super.setUnbreakable(unbreakable);
    }

    @Override
    public BukkitRecipeItem setCustomModelData(int customModelData) {
        return (BukkitRecipeItem) super.setCustomModelData(customModelData);
    }

    @Override
    public BukkitRecipeItem copy() {
        return super.copy(BukkitRecipeItemImpl.class);
    }

    @Override
    public BukkitRecipeItem setRecipes(Recipe @NotNull ... recipes) {
        return (BukkitRecipeItem) super.setRecipes(recipes);
    }

    @Override
    public BukkitRecipeItem clearRecipes() {
        return (BukkitRecipeItem) super.clearRecipes();
    }

    @Override
    public BukkitRecipeItem addRecipes(Recipe @NotNull ... recipes) {
        return (BukkitRecipeItem) super.addRecipes(recipes);
    }

    @Override
    public BukkitRecipeItem clearRecipes(Predicate<Recipe> predicate) {
        return (BukkitRecipeItem) super.clearRecipes(predicate);
    }
}
