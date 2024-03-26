package it.angrybear.yagl.parsers;

import it.angrybear.yagl.items.Item;
import it.angrybear.yagl.items.fields.ItemFlag;
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
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
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

            final Item item = Item.newItem();

            final String material = itemSection.getString("material");
            if (material == null) throw new NullPointerException("'material' cannot be null");
            item.setMaterial(material);

            itemSection.getOptional("amount", Integer.class).ifPresent(item::setAmount);
            itemSection.getOptional("durability", Integer.class).ifPresent(item::setDurability);
            itemSection.getOptional("display-name", String.class).ifPresent(item::setDisplayName);
            itemSection.getOptional("lore", List.class).ifPresent(item::setLore);
            itemSection.getOptional("unbreakable", Boolean.class).ifPresent(item::setUnbreakable);
            itemSection.getOptional("custom-model-data", Integer.class).ifPresent(item::setCustomModelData);

            List<String> flags = itemSection.getStringList("item-flags");
            if (flags == null) flags = new LinkedList<>();
            for (final String flag : flags)
                try {
                    item.addItemFlags(ItemFlag.valueOf(flag.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    Logger.getGlobal().warning(String.format("Could not find Item Flag '%s'", flag));
                }

            final List<Object> enchantments = itemSection.getList("enchantments", Object.class);
            if (enchantments != null)
                for (int j = 0; j < enchantments.size(); j++) {
                    ConfigurationSection section = new ConfigurationSection(itemSection, String.valueOf(j));
                    section.set("tmp", enchantments.get(j));
                    item.addEnchantments(section.get("tmp", Enchantment.class));
                }

            return item;
        };
    }

    @Override
    protected @NotNull TriConsumer<@NotNull IConfiguration, @NotNull String, @Nullable Item> getDumper() {
        return (c, s, i) -> {
            c.set(s, null);
            if (i == null) return;
            final ConfigurationSection itemSection = c.createSection(s);
            itemSection.set("material", i.getMaterial());
            itemSection.set("amount", i.getAmount());
            itemSection.set("durability", i.getDurability());
            itemSection.set("display-name", MessageUtils.decolor(i.getDisplayName()));
            itemSection.set("lore", i.getLore().stream().map(MessageUtils::decolor).collect(Collectors.toList()));

            final List<Enchantment> enchantments = new LinkedList<>(i.getEnchantments());
            List<Object> enchantsToSave = new ArrayList<>();
            for (int j = 0; j < enchantments.size(); j++) {
                ConfigurationSection section = new ConfigurationSection(itemSection, String.valueOf(j));
                section.set("tmp", enchantments.get(j));
                enchantsToSave.add(section.getObject("tmp"));
            }
            itemSection.set("enchantments", enchantsToSave);

            itemSection.set("item-flags", i.getItemFlags().stream().map(Enum::name).collect(Collectors.toList()));
            itemSection.set("unbreakable", i.isUnbreakable());
            itemSection.set("custom-model-data", i.getCustomModelData());
        };
    }
}
