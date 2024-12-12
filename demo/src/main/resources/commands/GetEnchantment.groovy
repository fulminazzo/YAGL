import it.fulminazzo.yagl.WrappersAdapter
import it.fulminazzo.yagl.wrappers.Enchantment
import it.fulminazzo.fulmicollection.structures.tuples.Tuple
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta

def run = { CommandSender sender, String label, String[] args ->
    if (sender instanceof Player)
        try {
            Enchantment enchantment = new Enchantment(args[0], Integer.valueOf(args[1]))
            ItemStack book = new ItemStack(Material.ENCHANTED_BOOK)
            EnchantmentStorageMeta meta = book.itemMeta as EnchantmentStorageMeta
            Tuple<org.bukkit.enchantments.Enchantment, Integer> tuple = WrappersAdapter.wEnchantToEnchant(enchantment)
            meta.addStoredEnchant(tuple.key, tuple.value, true)
            book.setItemMeta(meta)
            sender.inventory.addItem(book)
        } catch (NumberFormatException ignored) {
            // auto-generated code
        } catch (IndexOutOfBoundsException ignored) {
            sender.sendMessage('Usage: /getenchantment <enchantment> <level>')
        }
    else sender.sendMessage('Console cannot execute this command!')
}
