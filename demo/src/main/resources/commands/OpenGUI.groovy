import it.fulminazzo.yagl.GUIManager
import it.fulminazzo.yagl.contents.ItemGUIContent
import it.fulminazzo.yagl.guis.GUI
import it.fulminazzo.yagl.guis.GUIType
import it.fulminazzo.yagl.items.Item
import it.fulminazzo.yagl.items.fields.ItemFlag
import it.fulminazzo.yagl.utils.EnumUtils
import it.fulminazzo.yagl.utils.NMSUtils
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

def run = { CommandSender sender, String label, String[] args ->
    if (sender instanceof Player)
        try {
            def columns = 9
            def corner = (NMSUtils.serverVersion >= 13 ?
                    ItemGUIContent.newInstance(Material.BLACK_STAINED_GLASS_PANE.name()) :
                    ItemGUIContent.newInstance(Material.valueOf('STAINED_GLASS_PANE').name()).setDurability(15))
                    .setDisplayName(' ')
            GUI gui
            try {
                gui = GUI.newGUI(EnumUtils.valueOf(GUIType, args[0]))
            } catch (IllegalArgumentException ignored) {
                gui = GUI.newResizableGUI(Integer.valueOf(args[0]))
            } catch (IndexOutOfBoundsException ignored) {
                sender.sendMessage('Usage: /opengui <type|size>')
                return
            }
            for (int i = 0; i < Math.min(gui.size(), columns); i += 1) {
                gui.setContents(i, corner)
                gui.setContents(gui.size() - i - 1, corner)
            }
            for (int i = 0; i < gui.size(); i += columns) {
                gui.setContents(i, corner)
                gui.setContents(gui.size() - i - 1, corner)
            }
            def middle = (int) (gui.size() / 2)
            gui.setContents(middle, Item.newItem('gold_block').setDisplayName('This is a <name> GUI!')
                            .addEnchantment('unbreaking', 1)
                            .addItemFlags(ItemFlag.HIDE_ENCHANTS))
            if (middle - 1 >= 0)
                gui.setContents(middle - 1, Item.newItem('diamond_sword')
                        .setDisplayName('Pick me!')
                        .addEnchantment('sharpness', 2))
                        .setMovable(middle - 1, true)
            if (middle + 1 < gui.size())
                gui.setContents(middle + 1, ItemGUIContent.newInstance('diamond_pickaxe')
                        .setDisplayName('Can\'t pick me...')
                        .addEnchantment('efficiency', 10)
                        .onClickItem((v, g, c) -> v.sendMessage('You cannot pick this item!')))
            gui.setTitle('<name> GUI')
                    .onClickOutside((v, g) -> v.sendMessage('Please only click inside me!'))
                    .onOpenGUI((v, g) -> v.sendMessage('Opening the GUI... voila'))
                    .onCloseGUI((v, g) -> v.sendMessage('Goodbye!'))
                    .setVariable('name', 'Demo')
                    .open(GUIManager.getViewer(sender))
        } catch (NumberFormatException ignored) {
            // auto-generated code
        } catch (Exception e) {
            sender.sendMessage(e.message)
        }
    else sender.sendMessage('Console cannot execute this command!')
}
