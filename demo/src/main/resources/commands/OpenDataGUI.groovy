import it.fulminazzo.yagl.GUIManager
import it.fulminazzo.yagl.contents.GUIContent
import it.fulminazzo.yagl.contents.ItemGUIContent
import it.fulminazzo.yagl.guis.DataGUI
import it.fulminazzo.yagl.guis.GUIType
import it.fulminazzo.yagl.guis.PageableGUI
import it.fulminazzo.yagl.items.BukkitItem
import it.fulminazzo.yagl.utils.EnumUtils
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

import java.util.function.Function

def run = { CommandSender sender, String label, String[] args ->
    if (sender instanceof Player)
        try {
            def data = [
                    'James', 'Mary', 'Robert', 'Patricia', 'John', 'Jennifer', 'Michael',
                    'Linda', 'David', 'Elizabeth', 'William', 'Barbara', 'Richard', 'Susan',
                    'Joseph', 'Jessica', 'Thomas', 'Sarah', 'Christopher', 'Karen', 'Charles',
                    'Lisa', 'Daniel', 'Nancy', 'Matthew', 'Betty', 'Anthony', 'Sandra', 'Mark',
                    'Margaret', 'Donald', 'Ashley', 'Steven', 'Kimberly', 'Andrew', 'Emily', 'Paul',
                    'Donna', 'Joshua', 'Michelle', 'Kenneth', 'Carol', 'Kevin', 'Amanda', 'Brian',
                    'Melissa', 'George', 'Deborah', 'Timothy', 'Stephanie', 'Ronald', 'Dorothy',
                    'Jason', 'Rebecca', 'Edward', 'Sharon', 'Jeffrey', 'Laura', 'Ryan', 'Cynthia',
                    'Jacob', 'Amy', 'Gary', 'Kathleen', 'Nicholas', 'Angela', 'Eric', 'Shirley',
                    'Jonathan', 'Brenda', 'Stephen', 'Emma', 'Larry', 'Anna', 'Justin', 'Pamela',
                    'Scott', 'Nicole', 'Brandon', 'Samantha', 'Benjamin', 'Katherine', 'Samuel',
                    'Christine', 'Gregory', 'Helen', 'Alexander', 'Debra', 'Patrick', 'Rachel',
                    'Frank', 'Carolyn', 'Raymond', 'Janet', 'Jack', 'Maria', 'Dennis', 'Catherine',
                    'Jerry', 'Heather', 'Tyler', 'Diane', 'Aaron', 'Olivia', 'Jose', 'Julie', 'Adam',
                    'Joyce', 'Nathan', 'Victoria', 'Henry', 'Ruth', 'Zachary', 'Virginia', 'Douglas',
                    'Lauren', 'Peter', 'Kelly', 'Kyle', 'Christina', 'Noah', 'Joan', 'Ethan', 'Evelyn',
                    'Jeremy', 'Judith', 'Walter', 'Andrea', 'Christian', 'Hannah', 'Keith', 'Megan',
                    'Roger', 'Cheryl', 'Terry', 'Jacqueline', 'Austin', 'Martha', 'Sean', 'Madison',
                    'Gerald', 'Teresa', 'Carl', 'Gloria', 'Harold', 'Sara', 'Dylan', 'Janice', 'Arthur',
                    'Ann', 'Lawrence', 'Kathryn', 'Jordan', 'Abigail', 'Jesse', 'Sophia', 'Bryan', 'Frances',
                    'Billy', 'Jean', 'Bruce', 'Alice', 'Gabriel', 'Judy', 'Joe', 'Isabella', 'Logan', 'Julia',
                    'Alan', 'Grace', 'Juan', 'Amber', 'Albert', 'Denise', 'Willie', 'Danielle', 'Elijah',
                    'Marilyn', 'Wayne', 'Beverly', 'Randy', 'Charlotte', 'Vincent', 'Natalie', 'Mason',
                    'Theresa', 'Roy', 'Diana', 'Ralph', 'Brittany', 'Bobby', 'Doris', 'Russell', 'Kayla',
                    'Bradley', 'Alexis', 'Philip', 'Lori', 'Eugene', 'Marie',
            ]
            Function<String, GUIContent> converter = s -> ItemGUIContent.newInstance(Material.PLAYER_HEAD.name())
                    .setDisplayName("&e${s}'s head")
                    .setLore("&7This head belongs to &e${s}&7.",
                            '&7Make sure to give it back to them',
                            '&7once you are done playing with it!')
            PageableGUI gui
            try {
                gui = DataGUI.newGUI(EnumUtils.valueOf(GUIType, args[0]), converter, data)
            } catch (IllegalArgumentException ignored) {
                gui = DataGUI.newGUI(Integer.valueOf(args[0]), converter, data)
            } catch (IndexOutOfBoundsException ignored) {
                sender.sendMessage('Usage: /opendatagui <type|size>')
                return
            }
            def size = gui.size()
            def middle = (int) Math.min(size / 2, 9 / 2)
            if (size > 1) {
                size -= 1
                gui.setContents(size - middle, BukkitItem.newItem(Material.OBSIDIAN).setDisplayName('&7Page: &e<page>'))
                        .setPreviousPage(size - middle * 2, BukkitItem.newItem(Material.REDSTONE_BLOCK)
                                .setDisplayName('&7Go to page &e<previous_page>'))
                        .setNextPage(size, BukkitItem.newItem(Material.EMERALD_BLOCK)
                                .setDisplayName('&7Go to page &e<next_page>'))
            }

            gui.setTitle('Page #<page>')
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
