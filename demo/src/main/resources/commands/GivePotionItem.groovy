import it.fulminazzo.yagl.item.BukkitItem
import it.fulminazzo.yagl.item.Item
import it.fulminazzo.yagl.item.field.ItemFlag
import it.fulminazzo.yagl.util.NMSUtils
import it.fulminazzo.yagl.wrapper.Enchantment
import it.fulminazzo.yagl.wrapper.PotionEffect
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

def run = { CommandSender sender, String label, String[] args ->
    if (sender instanceof Player) {
        def version = NMSUtils.serverVersion

        Item item = Item.newItem('potion')
                .setDisplayName('&6Super Potion')
                .setLore('&eUse this potion to', '&efeel like Superman!')
                .addEnchantments(new Enchantment('lure', 1))
                .addItemFlags(ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
                .addPotionEffects(
                        new PotionEffect(version < 20 ? 'increase_damage' : 'strength', 30, 3),
                        new PotionEffect('speed', 90, 2),
                        new PotionEffect(version < 20 ? 'jump' : 'jump_boost', 10, 2)
                )
                .setUnbreakable(true)
        sender.inventory.addItem(item.copy(BukkitItem).create())
    } else sender.sendMessage('Console cannot execute this command!')
}
