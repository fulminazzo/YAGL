import it.fulminazzo.yagl.items.BukkitItem
import it.fulminazzo.yagl.items.Item
import it.fulminazzo.yagl.items.fields.ItemFlag
import it.fulminazzo.yagl.wrappers.Enchantment
import it.fulminazzo.yagl.wrappers.WrapperParser
import org.bukkit.entity.Player

def run = { sender, label, args ->
    if (sender instanceof Player)
        try {
            Item item = Item.newItem(args[0])
            try {
                item.setAmount(Integer.valueOf(args[1]))
                        .setDurability(Integer.valueOf(args[2]))
                        .setDisplayName(args[3])
                        .setLore(args[4].split(';'))
                        .addEnchantments(Arrays.stream(args[5].split(';'))
                                .map(a -> WrapperParser.parseWrapperFromString(a, Enchantment))
                                .toArray(Enchantment[]::new))
                        .addItemFlags(Arrays.stream(args[6].split(';'))
                                .map(a -> ItemFlag.valueOf(a.toUpperCase()))
                                .toArray(ItemFlag[]::new))
                        .setUnbreakable(Boolean.valueOf(args[7]))
                        .setCustomModelData(Integer.valueOf(args[8]))
            } catch (IndexOutOfBoundsException ignored) {

            }
            sender.inventory.addItem(item.copy(BukkitItem).create())
        } catch (NumberFormatException ignored) {
            // auto-generated code
        } catch (IndexOutOfBoundsException ignored) {
            sender.sendMessage('Usage: /giveitem <material> <amount> <durability> <name> <lore> <enchantments> <item-flags> <unbreakable> <custom-model-data>')
            sender.sendMessage('At least material is required!')
        }
    else sender.sendMessage('Console cannot execute this command!')
}
