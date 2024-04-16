import it.angrybear.yagl.GUIManager
import it.angrybear.yagl.guis.GUI
import it.angrybear.yagl.guis.GUIType
import it.angrybear.yagl.items.Item
import it.angrybear.yagl.items.fields.ItemFlag
import it.angrybear.yagl.utils.EnumUtils
import org.bukkit.entity.Player

def run = { sender, label, args ->
    if (sender instanceof Player)
        try {
            def columns = 9
            def border = Item.newItem("black_stained_glass_pane").setDisplayName(" ")
            GUI gui
            try {
                gui = GUI.newGUI(EnumUtils.valueOf(GUIType, args[0]))
            } catch (IllegalArgumentException ignored) {
                gui = GUI.newResizableGUI(Integer.valueOf(args[0]))
            } catch (IndexOutOfBoundsException ignored) {
                sender.sendMessage("Usage: /opengui <type|size>")
                return
            }
            for (int i = 0; i < columns; i += 1) {
                gui.setContents(i, border)
                gui.setContents(gui.size() - i - 1, border)
            }
            for (int i = 0; i < gui.size(); i += columns) {
                gui.setContents(i, border)
                gui.setContents(gui.size() - i - 1, border)
            }
            def middle = (int) (gui.size() / 2)
            gui.setContents(middle, Item.newItem("gold_block").setDisplayName("This is a demo GUI!")
                            .addEnchantment("unbreaking", 1)
                            .addItemFlags(ItemFlag.HIDE_ENCHANTS))
                    .setContents(middle - 1, Item.newItem("diamond_sword")
                            .setDisplayName("Pick me!")
                            .addEnchantment("sharpness", 2))
                    .setContents(middle + 1, Item.newItem("diamond_pickaxe")
                            .setDisplayName("Can't pick me...")
                            .addEnchantment("efficiency", 10))
                    .setMovable(middle - 1, true)
                    .setTitle("Demo GUI")
                    .open(GUIManager.getViewer(sender))
        } catch (NumberFormatException ignored) {

        } catch (Exception e) {
            e.printStackTrace()
            sender.sendMessage(e.getMessage())
        }
    else sender.sendMessage('Console cannot execute this command!')
}