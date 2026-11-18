package it.fulminazzo.yagl.item;

import it.fulminazzo.yagl.item.recipe.Recipe;
import it.fulminazzo.yagl.serializer.DataNode;
import it.fulminazzo.yagl.serializer.ObjectSerializer;
import it.fulminazzo.yagl.serializer.SerializationException;
import it.fulminazzo.yagl.serializer.Serializer;
import it.fulminazzo.yagl.wrapper.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link Serializer} for {@link Item} implementations.
 * <br>
 * The serializer will dynamically determine if the serialized object is an {@link Item} or a {@link RecipeItem}
 * and return the appropriate type.
 */
public final class ItemSerializer extends ObjectSerializer<Item> {
    static final @NotNull String MATERIAL_PATH = "material";
    static final @NotNull String AMOUNT_PATH = "amount";
    static final @NotNull String DURABILITY_PATH = "durability";
    static final @NotNull String DISPLAY_NAME_PATH = "displayName";
    static final @NotNull String LORE_PATH = "lore";
    static final @NotNull String ENCHANTMENTS_PATH = "enchantments";
    static final @NotNull String ITEM_FLAGS_PATH = "itemFlags";
    static final @NotNull String UNBREAKABLE_PATH = "unbreakable";
    static final @NotNull String CUSTOM_MODEL_DATA_PATH = "customModelData";
    static final @NotNull String COMPONENTS_PATH = "components";

    static final @NotNull String RECIPES_PATH = "recipes";

    @Override
    protected @Nullable Item initialize(final @NotNull DataNode node) throws SerializationException {
        String material = node.getOrNull(MATERIAL_PATH, String.class);
        if (material == null) return null;
        return node.getCollection(RECIPES_PATH, Recipe.class)
                .map(r -> (Item) Item.newRecipeItem(material).addRecipes(r.toArray(new Recipe[0])))
                .orElse(Item.newItem(material));
    }

    @Override
    protected void loadData(final @NotNull Item value, final @NotNull DataNode node) throws SerializationException {
        node.get(AMOUNT_PATH, Integer.class).ifPresent(value::setAmount);
        node.get(DURABILITY_PATH, Integer.class).ifPresent(value::setDurability);
        node.get(DISPLAY_NAME_PATH, String.class).ifPresent(value::setDisplayName);
        node.getCollection(LORE_PATH, String.class).ifPresent(value::setLore);
        node.getCollection(ENCHANTMENTS_PATH, Enchantment.class).ifPresent(value::addEnchantments);
        node.getCollection(ITEM_FLAGS_PATH, String.class).ifPresent(value::addItemFlags);
        node.get(UNBREAKABLE_PATH, Boolean.class).ifPresent(value::setUnbreakable);
        node.get(CUSTOM_MODEL_DATA_PATH, Integer.class).ifPresent(value::setCustomModelData);
        node.getCollection(COMPONENTS_PATH, ItemComponent.class)
                .ifPresent(c -> c.forEach(value::setComponent));
    }

    @Override
    protected void dumpData(final @NotNull Item value, final @NotNull DataNode output) throws SerializationException {
        output.set(MATERIAL_PATH, value.getMaterial());
        output.set(AMOUNT_PATH, value.getAmount());
        output.set(DURABILITY_PATH, value.getDurability());
        output.set(DISPLAY_NAME_PATH, value.getDisplayName());
        output.set(LORE_PATH, value.getLore());
        output.set(ENCHANTMENTS_PATH, value.getEnchantments());
        output.set(ITEM_FLAGS_PATH, value.getItemFlags());
        output.set(UNBREAKABLE_PATH, value.isUnbreakable());
        output.set(CUSTOM_MODEL_DATA_PATH, value.getCustomModelData());
        output.set(COMPONENTS_PATH, value.getComponents());
        if (value instanceof RecipeItem)
            output.set(RECIPES_PATH, value.to(RecipeItem.class).getRecipes());
    }

}
