import it.fulminazzo.fulmicollection.objects.Refl
import it.fulminazzo.yagl.GUIManager
import it.fulminazzo.yagl.content.ItemGUIContent
import it.fulminazzo.yagl.gui.SearchGUI
import it.fulminazzo.yagl.item.BukkitItem
import it.fulminazzo.yagl.util.NMSUtils
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

import java.util.stream.Collectors

def run = { CommandSender sender, String label, String[] args ->
    if (sender instanceof Player)
        try {
            boolean texture = args.length > 0 && args[0] == "texture"

            def corner = (NMSUtils.serverVersion >= 13 ?
                    ItemGUIContent.newInstance(Material.BLACK_STAINED_GLASS_PANE.name()) :
                    ItemGUIContent.newInstance(Material.valueOf('STAINED_GLASS_PANE').name()).setDurability(15))
            if (texture) corner = (NMSUtils.serverVersion >= 13 ?
                    ItemGUIContent.newInstance(Material.LIGHT_GRAY_STAINED_GLASS_PANE.name()) :
                    ItemGUIContent.newInstance(Material.valueOf('STAINED_GLASS_PANE').name()).setDurability(8))
                    .setCustomModelData(1337)
            corner.setDisplayName(' ')

            def material = new Refl(Material)
            def materials = material.staticFields
            materials.removeIf(f -> f.name.contains('LEGACY'))
            materials.removeIf(f -> f.type != Material)
            def data = materials.stream()
                    .map(f -> (Material) material.getFieldObject(f))
                    .filter(m -> {
                        try {
                            return m.isItem()
                        } catch (MissingMethodException e) {
                            return m.isBlock()
                        }
                    })
                    .collect(Collectors.toList())
            SearchGUI gui = SearchGUI.newGUI(
                    m -> ItemGUIContent.newInstance(m.name()),
                    (m, s) -> m.name().contains(s.toUpperCase()),
                    data
            )

            def previousPage = (texture ?
                    (NMSUtils.serverVersion >= 13 ?
                            ItemGUIContent.newInstance(Material.RED_STAINED_GLASS_PANE.name()) :
                            ItemGUIContent.newInstance(Material.valueOf('STAINED_GLASS_PANE').name()).setDurability(14))
                            .setCustomModelData(1337) :
                    BukkitItem.newItem(Material.REDSTONE_BLOCK))
                    .setDisplayName('&7Go to page &e<previous_page>')

            def nextPage = (texture ?
                    (NMSUtils.serverVersion >= 13 ?
                            ItemGUIContent.newInstance(Material.GREEN_STAINED_GLASS_PANE.name()) :
                            ItemGUIContent.newInstance(Material.valueOf('STAINED_GLASS_PANE').name()).setDurability(13))
                            .setCustomModelData(1337) :
                    BukkitItem.newItem(Material.EMERALD_BLOCK))
                    .setDisplayName('&7Go to page &e<next_page>')

            gui.setContents(gui.south(), BukkitItem.newItem(Material.OBSIDIAN).setDisplayName('&7Page: &e<page>'))
                    .setPreviousPage(gui.south() - 2, previousPage)
                    .setNextPage(gui.south() + 2, nextPage)

            gui.setTitle(texture ? '\uE000&r&f\uE001' : 'Page #<page>')
                    .setBottomSide(corner.copy())
                    .setContents(0, corner.copy())
                    .setContents(1, corner.copy())
                    .setContents(2, corner.copy())
            gui.emptySlots().forEach(s -> gui.setMovable(s, true))
            gui.open(GUIManager.getViewer(sender))
        } catch (NumberFormatException ignored) {
            // auto-generated code
        } catch (Exception e) {
            sender.sendMessage(e.message)
            e.printStackTrace()
        }
    else sender.sendMessage('Console cannot execute this command!')
}
