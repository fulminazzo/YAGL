import it.fulminazzo.yagl.GUIManager
import it.fulminazzo.yagl.contents.ItemGUIContent
import it.fulminazzo.yagl.guis.SearchGUI
import it.fulminazzo.yagl.items.BukkitItem
import it.fulminazzo.yagl.utils.NMSUtils
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

import java.util.stream.Collectors

def run = { CommandSender sender, String label, String[] args ->
    if (sender instanceof Player)
        try {
            def corner = (NMSUtils.serverVersion >= 13 ?
                    ItemGUIContent.newInstance(Material.BLACK_STAINED_GLASS_PANE) :
                    ItemGUIContent.newInstance(Material.valueOf('STAINED_GLASS_PANE')).setDurability(15))
                    .setDisplayName(' ')

            def data = Arrays.stream(Material.values())
                    .filter(m -> m.isBlock())
                    .collect(Collectors.toList())
            data.removeIf(m -> m.name().contains("LEGACY"))
            SearchGUI gui = SearchGUI.newGUI(
                    m -> ItemGUIContent.newInstance(m.name()),
                    (m, s) -> m.name().contains(s.toUpperCase()),
                    data
            )
            gui.setContents(gui.south(), BukkitItem.newItem(Material.OBSIDIAN).setDisplayName('&7Page: &e<page>'))
                    .setPreviousPage(gui.south() - 2, BukkitItem.newItem(Material.REDSTONE_BLOCK)
                            .setDisplayName('&7Go to page &e<previous_page>'))
                    .setNextPage(gui.south() + 2, BukkitItem.newItem(Material.EMERALD_BLOCK)
                            .setDisplayName('&7Go to page &e<next_page>'))

            gui.setTitle('Page #<page>')
                    .setBottomSide(corner.copy())
                    .setContents(0, corner.copy())
                    .setContents(1, corner.copy())
                    .setContents(2, corner.copy())
                    .onClickOutside((v, g) -> v.sendMessage('Please only click inside me!'))
                    .onOpenGUI((v, g) -> v.sendMessage(g.apply('Opening page <page>')))
                    .onCloseGUI((v, g) -> v.sendMessage('Goodbye!'))
                    .open(GUIManager.getViewer(sender))
        } catch (NumberFormatException ignored) {
            // auto-generated code
        } catch (Exception e) {
            sender.sendMessage(e.message)
            e.printStackTrace()
        }
    else sender.sendMessage('Console cannot execute this command!')
}
