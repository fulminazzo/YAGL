package it.angrybear.yagl.parsers;

import it.angrybear.yagl.items.fields.Enchantment;
import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.yamlparser.configuration.ConfigurationSection;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.YAMLParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnchantmentParser extends YAMLParser<Enchantment> {

    public EnchantmentParser() {
        super(Enchantment.class);
    }

    @Override
    protected BiFunctionException<@NotNull IConfiguration, @NotNull String, @Nullable Enchantment> getLoader() {
        return (c, s) -> {
            final ConfigurationSection enchantSection = c.getConfigurationSection(s);
            if (enchantSection == null) return null;
            final String enchantment = enchantSection.getString("enchantment");
            if (enchantment == null) throw new NullPointerException("enchantment cannot be null");
            final int level = enchantSection.getInteger("level", 0);
            return new Enchantment(enchantment, level);
        };
    }

    @Override
    protected TriConsumer<@NotNull IConfiguration, @NotNull String, @Nullable Enchantment> getDumper() {
        return (c, s, e) -> {
            c.set(s, null);
            if (e == null) return;
            final ConfigurationSection enchantSection = c.createSection(s);
            enchantSection.set("enchantment", e.getEnchantment());
            enchantSection.set("level", e.getLevel());
        };
    }
}
