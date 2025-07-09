package it.fulminazzo.yagl.item;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yagl.ItemAdapter;
import it.fulminazzo.yagl.item.recipe.Recipe;
import it.fulminazzo.yagl.util.EnumUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * An implementation of {@link RecipeItemImpl} that actually implements {@link #registerRecipes()} and {@link #unregisterRecipes()}.
 */
class BukkitRecipeItemImpl extends RecipeItemImpl implements BukkitRecipeItem {
    private final List<org.bukkit.inventory.Recipe> recipeCache = new ArrayList<>();
    private @Nullable Class<? extends ItemMeta> itemMetaClass;
    private @Nullable Consumer<? extends ItemMeta> metaFunction;

    /**
     * Instantiates a new Bukkit recipe item.
     */
    public BukkitRecipeItemImpl() {
        super();
    }

    @Override
    public void registerRecipes() {
        for (Recipe recipe : this.recipes) {
            org.bukkit.inventory.Recipe realRecipe = ItemAdapter.recipeToMinecraft(recipe.setOutput(this));
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
    public @NotNull ItemStack create() {
        return new Refl<>(this).invokeMethod("create", this.itemMetaClass, this.metaFunction);
    }

    @Override
    public @NotNull <M extends ItemMeta> BukkitRecipeItem setMetadata(@Nullable Class<M> itemMetaClass, @Nullable Consumer<M> metaFunction) {
        this.itemMetaClass = itemMetaClass;
        this.metaFunction = metaFunction;
        return this;
    }

    @Override
    public @NotNull BukkitRecipeItem setMaterial(@NotNull String material) {
        EnumUtils.valueOf(Material.class, material);
        return (BukkitRecipeItem) super.setMaterial(material);
    }

    @Override
    public @NotNull BukkitRecipeItem setAmount(final int amount) {
        return (BukkitRecipeItem) super.setAmount(amount);
    }

    @Override
    public @NotNull BukkitRecipeItem setDurability(final int durability) {
        return (BukkitRecipeItem) super.setDurability(durability);
    }

    @Override
    public @NotNull BukkitRecipeItem setDisplayName(final @NotNull String displayName) {
        return (BukkitRecipeItem) super.setDisplayName(displayName);
    }

    @Override
    public @NotNull BukkitRecipeItem setUnbreakable(final boolean unbreakable) {
        return (BukkitRecipeItem) super.setUnbreakable(unbreakable);
    }

    @Override
    public @NotNull BukkitRecipeItem setCustomModelData(int customModelData) {
        return (BukkitRecipeItem) super.setCustomModelData(customModelData);
    }

    @Override
    public @NotNull BukkitRecipeItem addRecipes(Recipe @NotNull ... recipes) {
        return (BukkitRecipeItem) super.addRecipes(recipes);
    }

    @Override
    public @NotNull BukkitRecipeItem clearRecipes(@NotNull Predicate<Recipe> predicate) {
        return (BukkitRecipeItem) super.clearRecipes(predicate);
    }

}
