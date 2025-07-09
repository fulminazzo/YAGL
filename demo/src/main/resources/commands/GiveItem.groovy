import it.fulminazzo.yagl.item.BukkitItem
import it.fulminazzo.yagl.item.Item
import it.fulminazzo.yagl.item.field.ItemFlag
import it.fulminazzo.yagl.wrapper.Enchantment
import it.fulminazzo.yagl.wrapper.WrapperParser
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

def run = { CommandSender sender, String label, String[] args ->
    if (sender instanceof Player)
        try {
            Item item = Item.newItem(args[0])
            try {
                item.setAmount(Integer.valueOf(args[1]))
                        .setDurability(Integer.valueOf(args[2]))
                        .setDisplayName(args[3])
                        .setLore(args[4].split(';'))
                        .addEnchantments(Arrays.stream(args[5].split(';'))
                                .map(a -> WrapperParser.deserializeWrapper(a, Enchantment))
                                .toArray(Enchantment[]::new))
                        .addItemFlags(Arrays.stream(args[6].split(';'))
                                .map(a -> ItemFlag.valueOf(a.toUpperCase()))
                                .toArray(ItemFlag[]::new))
                        .setUnbreakable(Boolean.valueOf(args[7]))
                        .setCustomModelData(Integer.valueOf(args[8]))
            } catch (IndexOutOfBoundsException ignored) {
                //
            }
            sender.inventory.addItem(item.copy(BukkitItem).create())
        } catch (NumberFormatException ignored) {
            // auto-generated code
        } catch (IndexOutOfBoundsException ignored) {
            // Groovy does not like separations
            sender.sendMessage('Usage: /giveitem <material> <amount> <durability> <name> <lore> <enchantments> <item-flags> <unbreakable> <custom-model-data>')
            sender.sendMessage('At least material is required!')
        }
    else sender.sendMessage('Console cannot execute this command!')
}
