package it.fulminazzo.yagl.item;

import it.fulminazzo.yagl.ItemSerializersLoader;
import it.fulminazzo.yagl.item.field.ItemFlag;
import it.fulminazzo.yagl.item.recipe.ShapelessRecipe;
import it.fulminazzo.yagl.serializer.SerializerTestHelper;
import it.fulminazzo.yagl.wrapper.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ItemSerializerTest extends SerializerTestHelper<Item> {
    private static final @NotNull Item EXPECTED_ITEM = Item.newItem("diamond")
            .setAmount(32)
            .setDurability(200)
            .setDisplayName("Mana")
            .addLore("Use this item", "to cast your", "magic spells!")
            .addEnchantment("unbreaking", 1)
            .addItemFlags(ItemFlag.HIDE_ENCHANTS)
            .setUnbreakable(true)
            .setCustomModelData(15)
            .setComponent(new EnchantmentItemComponent(
                    new Enchantment("unbreaking", 5)
            ));
    private static final @NotNull RecipeItem EXPECTED_RECIPE_ITEM;

    static {
        Item ingredient = EXPECTED_ITEM.copy()
                .setAmount(1)
                .removeComponent(EnchantmentItemComponent.class)
                .setDurability(0);
        ShapelessRecipe recipe = new ShapelessRecipe("mana_recipe")
                .setOutput(Item.newItem("DIAMOND_BLOCK"));
        for (int i = 0; i < 9; i++) recipe.addIngredients(ingredient);
        EXPECTED_RECIPE_ITEM = EXPECTED_ITEM.copy(RecipeItem.class).addRecipes(recipe);
    }

    protected ItemSerializerTest() {
        super(Item.class, new ItemSerializer());
        ItemSerializersLoader.getItemComponentSerializer().registerSerializer(
                "ENCHANTMENT",
                EnchantmentItemComponent.class,
                new EnchantmentItemComponentSerializer()
        );
    }

    @Test
    void testThatSerializeOnlyWritesNonEmptyFields() throws IOException {
        Item item = Item.newItem("diamond").setDisplayName("Ruby").setCustomModelData(15);
        @Nullable Map<?, ?> map = readRawMap(dump(item, "stripped_item"));
        assertNotNull(map);
        assertEquals(3, map.size(), map.toString());
        assertEquals("diamond", map.get(ItemSerializer.MATERIAL_PATH));
        assertEquals(15, map.get(ItemSerializer.CUSTOM_MODEL_DATA_PATH));
        assertEquals("Ruby", map.get(ItemSerializer.DISPLAY_NAME_PATH));
    }

    @Test
    void testDeserialize() throws IOException {
        Item item = load("item");
        assertEquals(EXPECTED_ITEM, item);
    }

    @Test
    void testSerialize() throws IOException {
        @Nullable Map<?, ?> map = readRawMap(dump(EXPECTED_ITEM, "item"));
        assertNotNull(map);
        assertEquals(EXPECTED_ITEM.getMaterial(), map.get(ItemSerializer.MATERIAL_PATH));
        assertEquals(EXPECTED_ITEM.getAmount(), map.get(ItemSerializer.AMOUNT_PATH));
        assertEquals(EXPECTED_ITEM.getDurability(), map.get(ItemSerializer.DURABILITY_PATH));
        assertEquals(EXPECTED_ITEM.getDisplayName(), map.get(ItemSerializer.DISPLAY_NAME_PATH));
        assertEquals(EXPECTED_ITEM.getLore(), map.get(ItemSerializer.LORE_PATH));
        Object enchantments = map.get(ItemSerializer.ENCHANTMENTS_PATH);
        assertInstanceOf(List.class, enchantments);
        int expectedEnchantmentCount = EXPECTED_ITEM.getEnchantments().size();
        assertEquals(
                expectedEnchantmentCount,
                ((List<?>) enchantments).size(),
                "Invalid enchantment count, expected " + expectedEnchantmentCount + " but got: " + enchantments
        );
        assertEquals(new ArrayList<>(EXPECTED_ITEM.getItemFlags()), map.get(ItemSerializer.ITEM_FLAGS_PATH));
        assertEquals(EXPECTED_ITEM.isUnbreakable(), map.get(ItemSerializer.UNBREAKABLE_PATH));
        assertEquals(EXPECTED_ITEM.getCustomModelData(), map.get(ItemSerializer.CUSTOM_MODEL_DATA_PATH));
        Object components = map.get(ItemSerializer.COMPONENTS_PATH);
        assertInstanceOf(List.class, components);
        assertEquals(
                ((List<?>) components).size(),
                EXPECTED_ITEM.getComponents().size(),
                "Invalid component count, expected " + EXPECTED_ITEM.getComponents().size() + " but got: " + components
        );
    }

    @Test
    void testDeserializeRecipe() throws IOException {
        RecipeItem recipeItem = load("recipe_item").to(RecipeItem.class);
        assertEquals(EXPECTED_RECIPE_ITEM, recipeItem);
    }

    @Test
    void testSerializeRecipe() throws IOException {
        @Nullable Map<?, ?> map = readRawMap(dump(EXPECTED_RECIPE_ITEM, "recipe_item"));
        assertNotNull(map);
        assertEquals(EXPECTED_RECIPE_ITEM.getMaterial(), map.get(ItemSerializer.MATERIAL_PATH));
        assertEquals(EXPECTED_RECIPE_ITEM.getAmount(), map.get(ItemSerializer.AMOUNT_PATH));
        assertEquals(EXPECTED_RECIPE_ITEM.getDurability(), map.get(ItemSerializer.DURABILITY_PATH));
        assertEquals(EXPECTED_RECIPE_ITEM.getDisplayName(), map.get(ItemSerializer.DISPLAY_NAME_PATH));
        assertEquals(EXPECTED_RECIPE_ITEM.getLore(), map.get(ItemSerializer.LORE_PATH));
        Object enchantments = map.get(ItemSerializer.ENCHANTMENTS_PATH);
        assertInstanceOf(List.class, enchantments);
        int expectedEnchantmentCount = EXPECTED_RECIPE_ITEM.getEnchantments().size();
        assertEquals(
                expectedEnchantmentCount,
                ((List<?>) enchantments).size(),
                "Invalid enchantment count, expected " + expectedEnchantmentCount + " but got: " + enchantments
        );
        assertEquals(new ArrayList<>(EXPECTED_RECIPE_ITEM.getItemFlags()), map.get(ItemSerializer.ITEM_FLAGS_PATH));
        assertEquals(EXPECTED_RECIPE_ITEM.isUnbreakable(), map.get(ItemSerializer.UNBREAKABLE_PATH));
        assertEquals(EXPECTED_RECIPE_ITEM.getCustomModelData(), map.get(ItemSerializer.CUSTOM_MODEL_DATA_PATH));
        Object components = map.get(ItemSerializer.COMPONENTS_PATH);
        assertInstanceOf(List.class, components);
        int expectedComponentsCount = EXPECTED_RECIPE_ITEM.getComponents().size();
        assertEquals(
                ((List<?>) components).size(),
                expectedComponentsCount,
                "Invalid component count, expected " + expectedComponentsCount + " but got: " + components
        );
        Object recipes = map.get(ItemSerializer.RECIPES_PATH);
        assertInstanceOf(List.class, recipes);
        int expectedRecipesCount = EXPECTED_RECIPE_ITEM.getRecipes().size();
        assertEquals(
                ((List<?>) recipes).size(),
                expectedRecipesCount,
                "Invalid recipe count, expected " + expectedRecipesCount + " but got: " + recipes
        );
    }

}