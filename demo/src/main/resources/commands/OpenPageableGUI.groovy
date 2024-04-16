import it.angrybear.yagl.GUIManager
import it.angrybear.yagl.guis.GUIType
import it.angrybear.yagl.guis.PageableGUI
import it.angrybear.yagl.items.BukkitItem
import it.angrybear.yagl.items.Item
import it.angrybear.yagl.utils.EnumUtils
import org.bukkit.Material
import org.bukkit.entity.Player

def run = { sender, label, args ->
    if (sender instanceof Player)
        try {
            PageableGUI gui
            try {
                gui = PageableGUI.newGUI(EnumUtils.valueOf(GUIType, args[0])).setPages(Integer.valueOf(args[1]))
            } catch (IllegalArgumentException ignored) {
                gui = PageableGUI.newGUI(Integer.valueOf(args[0])).setPages(Integer.valueOf(args[1]))
            } catch (IndexOutOfBoundsException ignored) {
                sender.sendMessage("Usage: /openpageablegui <type|size> <pages>")
                return
            }
            def size = gui.size()
            def middle = (int) (size / 2)
            if (size > 1) gui.setContents(middle, BukkitItem.newItem(Material.OBSIDIAN).setDisplayName("&7Page: &e<page>"))
                    .setPreviousPage(0, BukkitItem.newItem(Material.REDSTONE_BLOCK)
                        .setDisplayName("&7Go to page &e<previous_page>"))
                    .setNextPage(size - 1, BukkitItem.newItem(Material.EMERALD_BLOCK)
                            .setDisplayName("&7Go to page &e<next_page>"))

            gui.setTitle("Page #<page>")
                    .onClickOutside((v, g) -> v.sendMessage('Please only click inside me!'))
                    .onOpenGUI((v, g) -> v.sendMessage(g.apply('Opening page <page>')))
                    .onCloseGUI((v, g) -> v.sendMessage('Goodbye!'))
                    .open(GUIManager.getViewer(sender))
        } catch (NumberFormatException ignored) {

        } catch (Exception e) {
            sender.sendMessage(e.getMessage())
        }
    else sender.sendMessage('Console cannot execute this command!')
}