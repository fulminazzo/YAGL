import it.angrybear.yagl.WrappersAdapter
import it.angrybear.yagl.wrappers.Enchantment
import it.fulminazzo.fulmicollection.structures.tuples.Tuple
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta

def run = { sender, label, args ->
    if (sender instanceof Player)
        try {
            Enchantment enchantment = new Enchantment(args[0], Integer.valueOf(args[1]))
            ItemStack book = new ItemStack(Material.ENCHANTED_BOOK)
            EnchantmentStorageMeta meta = book.itemMeta
            Tuple<org.bukkit.enchantments.Enchantment, Integer> tuple = WrappersAdapter.wEnchantToEnchant(enchantment)
            meta.addStoredEnchant(tuple.key, tuple.value, true)
            book.setItemMeta(meta)
            sender.inventory.addItem(book)
        } catch (NumberFormatException ignored) {

        } catch (IndexOutOfBoundsException ignored) {
            sender.sendMessage('Usage: /getenchantment <enchantment> <level>')
        }
    else sender.sendMessage('Console cannot execute this command!')
}
