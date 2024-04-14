import it.angrybear.yagl.WrappersAdapter
import it.angrybear.yagl.wrappers.Enchantment
import it.fulminazzo.fulmicollection.structures.Tuple
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta

def run = { sender, label, args ->
    if (sender instanceof Player)
        try {
            Enchantment enchantment = new Enchantment(args[0], Integer.valueOf(args[1]))
            ItemStack book = new ItemStack(Material.ENCHANTED_BOOK)
            EnchantmentStorageMeta meta = book.getItemMeta()
            Tuple<org.bukkit.enchantments.Enchantment, Integer> tuple = WrappersAdapter.wEnchantToEnchant(enchantment)
            meta.addStoredEnchant(tuple.getKey(), tuple.getValue(), true)
            book.setItemMeta(meta)
            sender.getInventory().addItem(book)
        } catch (NumberFormatException ignored) {
            sender.sendMessage("Invalid number '${args[1]}'")
        } catch (IndexOutOfBoundsException ignored) {
            sender.sendMessage('Usage: /getenchantment <enchantment> <level>')
        }
    else sender.sendMessage('Console cannot execute this command!')
}