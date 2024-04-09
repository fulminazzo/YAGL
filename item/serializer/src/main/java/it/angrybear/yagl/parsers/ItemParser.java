package it.angrybear.yagl.parsers;

import it.angrybear.yagl.items.Item;
import it.angrybear.yagl.items.RecipeItem;
import it.angrybear.yagl.items.fields.ItemFlag;
import it.angrybear.yagl.items.recipes.Recipe;
import it.angrybear.yagl.utils.MessageUtils;
import it.angrybear.yagl.wrappers.Enchantment;
import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.yamlparser.configuration.ConfigurationSection;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.YAMLParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A parser to serialize {@link Item}.
 */
public class ItemParser extends YAMLParser<Item> {

    /**
     * Instantiates a new Item parser.
     */
    public ItemParser() {
        super(Item.class);
    }

    @Override
    protected @NotNull BiFunctionException<@NotNull IConfiguration, @NotNull String, @Nullable Item> getLoader() {
        return (c, s) -> {
            final ConfigurationSection itemSection = c.getConfigurationSection(s);
            if (itemSection == null) return null;
            else {
                Item item = Item.newItem();

                final String material = itemSection.getString("material");
                if (material == null) throw new IllegalArgumentException("'material' cannot be null");
                item.setMaterial(material);

                itemSection.getOptional("amount", Integer.class).ifPresent(item::setAmount);
                itemSection.getOptional("durability", Integer.class).ifPresent(item::setDurability);
                itemSection.getOptional("display-name", String.class).ifPresent(item::setDisplayName);
                itemSection.getOptional("lore", List.class).ifPresent(item::setLore);
                itemSection.getOptional("unbreakable", Boolean.class).ifPresent(item::setUnbreakable);
                itemSection.getOptional("custom-model-data", Integer.class).ifPresent(item::setCustomModelData);
                itemSection.getListOptional("enchantments", Enchantment.class).ifPresent(item::addEnchantments);
                itemSection.getListOptional("item-flags", ItemFlag.class).ifPresent(item::addItemFlags);

                if (itemSection.contains("recipes")) {
                    List<Recipe> recipes = itemSection.getList("recipes", Recipe.class);
                    if (recipes == null) recipes = new ArrayList<>();
                    item = item.copy(RecipeItem.class).setRecipes(recipes.toArray(new Recipe[0]));
                }

                return item;
            }
        };
    }

    @Override
    protected @NotNull TriConsumer<@NotNull IConfiguration, @NotNull String, @Nullable Item> getDumper() {
        return (c, s, i) -> {
            c.set(s, null);
            if (i != null) {
                final ConfigurationSection itemSection = c.createSection(s);
                String material = i.getMaterial();
                if (material != null) itemSection.set("material", material.toLowerCase());

                itemSection.set("amount", i.getAmount());
                itemSection.set("durability", i.getDurability());
                itemSection.set("display-name", MessageUtils.decolor(i.getDisplayName()));
                itemSection.set("lore", i.getLore().stream().map(MessageUtils::decolor).collect(Collectors.toList()));
                itemSection.setList("enchantments", i.getEnchantments());
                itemSection.set("item-flags", i.getItemFlags().stream().map(Enum::name).map(String::toLowerCase).collect(Collectors.toList()));
                itemSection.set("unbreakable", i.isUnbreakable());
                itemSection.set("custom-model-data", i.getCustomModelData());

                if (i instanceof RecipeItem) {
                    List<Recipe> recipes = new ArrayList<>();
                    ((RecipeItem) i).forEach(recipes::add);
                    itemSection.setList("recipes", recipes);
                }
            }
        };
    }
}
