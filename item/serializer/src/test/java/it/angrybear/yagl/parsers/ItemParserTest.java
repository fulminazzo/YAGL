package it.angrybear.yagl.parsers;

import it.angrybear.yagl.items.Item;
import it.angrybear.yagl.items.fields.ItemFlag;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;
import it.fulminazzo.yamlparser.utils.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemParserTest {

    @Test
    void testSaveAndLoad() throws IOException {
        ItemYAGLParser.addAllParsers();

        File output = new File("build/resources/test/item.yml");
        if (output.exists()) FileUtils.deleteFile(output);
        FileUtils.createNewFile(output);
        Item item = Item.newItem().setMaterial("STONE").setAmount(2).setDurability(15)
                .setDisplayName("&7Cool stone").setLore("Click on this", "To be OP")
                .addEnchantment("enchant1", 10).addEnchantment("enchant2", 20)
                .addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS)
                .setUnbreakable(true)
                .setCustomModelData(7);
        FileConfiguration configuration = new FileConfiguration(output);
        configuration.set("item", item);
        configuration.save();

        configuration = new FileConfiguration(output);
        Item item2 = configuration.get("item", Item.class);
        assertEquals(item, item2);
    }
}