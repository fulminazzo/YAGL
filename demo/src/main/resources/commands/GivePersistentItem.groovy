import it.fulminazzo.yagl.item.BukkitItem
import it.fulminazzo.yagl.item.DeathAction
import it.fulminazzo.yagl.item.Mobility
import it.fulminazzo.yagl.item.PersistentItem
import it.fulminazzo.yagl.item.field.ItemFlag
import it.fulminazzo.yagl.wrapper.Enchantment
import it.fulminazzo.yagl.wrapper.WrapperParser
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

def run = { CommandSender sender, String label, String[] args ->
    if (sender instanceof Player)
        try {
            DeathAction action = DeathAction.valueOf(args[0].toUpperCase())
            Mobility mobility = Mobility.valueOf(args[1].toUpperCase())
            PersistentItem item = PersistentItem.newItem(args[2]).setDeathAction(action).setMobility(mobility)
            try {
                item.setAmount(Integer.valueOf(args[3]))
                        .setDurability(Integer.valueOf(args[4]))
                        .setDisplayName(args[5])
                        .setLore(args[6].split(';'))
                        .addEnchantments(Arrays.stream(args[7].split(';'))
                                .map(a -> WrapperParser.parseWrapperFromString(a, Enchantment))
                                .toArray(Enchantment[]::new))
                        .addItemFlags(Arrays.stream(args[8].split(';'))
                                .map(a -> ItemFlag.valueOf(a.toUpperCase()))
                                .toArray(ItemFlag[]::new))
                        .setUnbreakable(Boolean.valueOf(args[9]))
                        .setCustomModelData(Integer.valueOf(args[10]))
            } catch (IndexOutOfBoundsException ignored) {
                //
            }
            sender.inventory.addItem(item.copy(BukkitItem).create())
        } catch (NumberFormatException ignored) {
            // auto-generated code
        } catch (IndexOutOfBoundsException ignored) {
            // Groovy does not like separations
            sender.sendMessage('Usage: /givepersistentitem <death-action> <mobility> <material> <amount> <durability> <name> <lore> <enchantments> <item-flags> <unbreakable> <custom-model-data>')
            sender.sendMessage('At least death-action, mobility and material are required!')
        }
    else sender.sendMessage('Console cannot execute this command!')
}
