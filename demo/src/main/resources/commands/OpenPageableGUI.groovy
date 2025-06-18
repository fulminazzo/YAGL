import it.fulminazzo.yagl.GUIManager
import it.fulminazzo.yagl.contents.ItemGUIContent
import it.fulminazzo.yagl.guis.GUIType
import it.fulminazzo.yagl.guis.PageableGUI
import it.fulminazzo.yagl.items.BukkitItem
import it.fulminazzo.yagl.utils.EnumUtils
import it.fulminazzo.yagl.utils.NMSUtils
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

def run = { CommandSender sender, String label, String[] args ->
    if (sender instanceof Player)
        try {
            PageableGUI gui
            try {
                gui = PageableGUI.newGUI(EnumUtils.valueOf(GUIType, args[0])).setPages(Integer.valueOf(args[1]))
            } catch (IllegalArgumentException ignored) {
                gui = PageableGUI.newGUI(Integer.valueOf(args[0])).setPages(Integer.valueOf(args[1]))
            } catch (IndexOutOfBoundsException ignored) {
                sender.sendMessage('Usage: /openpageablegui <type|size> <pages>')
                return
            }
            if (gui.size() > 1) {
                gui.setContents(gui.south(), BukkitItem.newItem(Material.OBSIDIAN).setDisplayName('&7Page: &e<page>'))
                        .setPreviousPage(gui.south() - 2, BukkitItem.newItem(Material.REDSTONE_BLOCK)
                                .setDisplayName('&7Go to page &e<previous_page>'))
                        .setNextPage(gui.south() + 2, BukkitItem.newItem(Material.EMERALD_BLOCK)
                                .setDisplayName('&7Go to page &e<next_page>'))
            }

            def corner = (NMSUtils.serverVersion >= 13 ?
                    ItemGUIContent.newInstance(Material.BLACK_STAINED_GLASS_PANE) :
                    ItemGUIContent.newInstance(Material.valueOf('STAINED_GLASS_PANE')).setDurability(15))
                    .setDisplayName(' ')

            gui.setTitle('Page #<page>')
                    .setBottomSide(corner)
                    .onClickOutside((v, g) -> v.sendMessage('Please only click inside me!'))
                    .onOpenGUI((v, g) -> v.sendMessage(g.apply('Opening page <page>')))
                    .onCloseGUI((v, g) -> v.sendMessage('Goodbye!'))
                    .open(GUIManager.getViewer(sender))
        } catch (NumberFormatException ignored) {
            // auto-generated code
        } catch (Exception e) {
            sender.sendMessage(e.message)
        }
    else sender.sendMessage('Console cannot execute this command!')
}
