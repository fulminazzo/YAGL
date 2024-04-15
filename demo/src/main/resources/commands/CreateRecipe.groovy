import it.angrybear.yagl.items.BukkitItem
import it.angrybear.yagl.items.Item
import it.angrybear.yagl.items.recipes.ShapelessRecipe
import org.bukkit.entity.Player

def shapeless(sender, label, args, output, name) {
    try {
        if (args.length == 0) throw new IndexOutOfBoundsException()
        BukkitItem.newRecipeItem(output)
                .addRecipes(new ShapelessRecipe(name)
                        .addIngredients(Arrays.stream(args)
                                .map(Item::newItem)
                                .toArray(Item[]::new)))
                .registerRecipes()
        sender.sendMessage('Ok')
    } catch (NumberFormatException ignored) {

    } catch (IndexOutOfBoundsException ignored) {
        sender.sendMessage('Usage: /createrecipe <name> <result> shapeless <ingredients...>')
    }
}

def run = { sender, label, args ->
    if (sender instanceof Player)
        try {
            "${args[2].toLowerCase()}"(sender, label, Arrays.copyOfRange(args, 3, args.length), args[1], args[0])
        } catch (NumberFormatException ignored) {

        } catch (IndexOutOfBoundsException ignored) {
            sender.sendMessage('Usage: /createrecipe <name> <result> <shaped|shapeless|furnace> <data>')
        } catch (Exception e) {
            sender.sendMessage(e.getMessage())
        }
    else sender.sendMessage('Console cannot execute this command!')
}