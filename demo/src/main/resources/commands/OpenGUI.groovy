import it.fulminazzo.yagl.GUIManager
import it.fulminazzo.yagl.action.GUIItemBack
import it.fulminazzo.yagl.content.ItemGUIContent
import it.fulminazzo.yagl.gui.GUI
import it.fulminazzo.yagl.gui.GUIType
import it.fulminazzo.yagl.item.Item
import it.fulminazzo.yagl.item.field.ItemFlag
import it.fulminazzo.yagl.util.EnumUtils
import it.fulminazzo.yagl.util.NMSUtils
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

            GUI secretGUI = GUI.newGUI(9)
                    .setTitle('&cSecret GUI')
                    .fill(corner)
                    .setContents(4, Item.newItem('obsidian').setDisplayName('&4&lYOU SHOULD NOT BE HERE'))
                    .setContents(0, ItemGUIContent.newInstance('barrier')
                            .setDisplayName('&cGo back')
                            .onClickItem(new GUIItemBack())
                    )

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
            gui.setContents(middle, ItemGUIContent.newInstance('gold_block').setDisplayName('This is a <name> GUI!')
                    .addEnchantment('lure', 1)
                    .addItemFlags(ItemFlag.HIDE_ENCHANTS)
                    .onClickItem(e -> secretGUI.open(e.viewer))
            )
            if (middle - 1 >= 0)
                gui.setContents(middle - 1, Item.newItem('diamond_sword')
                        .setDisplayName('Pick me!')
                        .addEnchantment('fire_aspect', 2))
                        .setMovable(middle - 1, true)
            if (middle + 1 < gui.size())
                gui.setContents(middle + 1, ItemGUIContent.newInstance('diamond_pickaxe')
                        .setDisplayName('Can\'t pick me...')
                        .addEnchantment('lure', 10)
                        .onClickItem(e -> e.viewer.sendMessage('You cannot pick this item!')))
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
