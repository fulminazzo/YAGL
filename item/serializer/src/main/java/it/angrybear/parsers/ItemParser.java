package it.angrybear.parsers;

import it.angrybear.items.Item;
import it.angrybear.items.fields.Enchantment;
import it.angrybear.items.fields.ItemFlag;
import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.yamlparser.configuration.ConfigurationSection;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.YAMLParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ItemParser extends YAMLParser<Item> {

    public ItemParser() {
        super(Item.class);
    }

    @Override
    protected BiFunctionException<@NotNull IConfiguration, @NotNull String, @Nullable Item> getLoader() {
        return (c, s) -> {
            final ConfigurationSection itemSection = c.getConfigurationSection(s);
            if (itemSection == null) return null;

            final Item item = Item.newItem();

            final String material = itemSection.getString("material");
            if (material == null) throw new NullPointerException("material cannot be null");
            item.setMaterial(material);

            itemSection.getOptional("amount", Integer.class).ifPresent(item::setAmount);
            itemSection.getOptional("durability", Integer.class).ifPresent(item::setDurability);
            itemSection.getOptional("displayName", String.class).ifPresent(item::setDisplayName);
            itemSection.getOptional("lore", List.class).ifPresent(item::setLore);
            itemSection.getOptional("unbreakable", Boolean.class).ifPresent(item::setUnbreakable);
            itemSection.getOptional("customModelData", Integer.class).ifPresent(item::setCustomModelData);

            List<String> flags = itemSection.getStringList("itemFlags");
            if (flags == null) flags = new LinkedList<>();
            for (final String flag : flags)
                try {
                    item.addItemFlags(ItemFlag.valueOf(flag.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    Logger.getGlobal().warning(String.format("Could not find Item Flag '%s'", flag));
                }

            final ConfigurationSection enchantmentsSection = itemSection.getConfigurationSection("enchantments");
            if (enchantmentsSection != null)
                for (final String enchant : enchantmentsSection.getKeys())
                    item.addEnchantments(enchantmentsSection.get(enchant, Enchantment.class));

            return item;
        };
    }

    @Override
    protected TriConsumer<@NotNull IConfiguration, @NotNull String, @Nullable Item> getDumper() {
        return (c, s, i) -> {
            c.set(s, null);
            if (i == null) return;
            final ConfigurationSection itemSection = c.createSection(s);
            itemSection.set("material", i.getMaterial());
            itemSection.set("amount", i.getAmount());
            itemSection.set("durability", i.getDurability());
            itemSection.set("displayName", i.getDisplayName());
            itemSection.set("lore", i.getLore());

            final List<Enchantment> enchantments = new LinkedList<>(i.getEnchantments());
            final ConfigurationSection enchantmentsSection = itemSection.createSection("enchantments");
            for (int j = 0; j < enchantments.size(); j++)
                enchantmentsSection.set(String.valueOf(j), enchantments.get(j));

            itemSection.set("itemFlags", i.getItemFlags().stream().map(Enum::name).collect(Collectors.toList()));
            itemSection.set("unbreakable", i.isUnbreakable());
            itemSection.set("customModelData", i.getCustomModelData());
        };
    }
}
