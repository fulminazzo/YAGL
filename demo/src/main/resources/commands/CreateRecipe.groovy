import it.fulminazzo.yagl.item.BukkitItem
import it.fulminazzo.yagl.item.Item
import it.fulminazzo.yagl.item.recipe.FurnaceRecipe
import it.fulminazzo.yagl.item.recipe.ShapedRecipe
import it.fulminazzo.yagl.item.recipe.ShapelessRecipe
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

static shaped(CommandSender sender, String[] args, String output, String name) {
    try {
        def rows = Integer.valueOf(args[0])
        def columns = Integer.valueOf(args[1])
        def recipe = new ShapedRecipe(name).setShape(rows, columns)
        if (args.length < 2) throw new IndexOutOfBoundsException()
        for (int i = 2; i < args.length; i += 1)
            recipe.setIngredient(i - 2, Item.newItem(args[i]))
        BukkitItem.newRecipeItem(output).addRecipes(recipe).registerRecipes()
        sender.sendMessage('Ok')
    } catch (NumberFormatException ignored) {
        // auto-generated code
    } catch (IndexOutOfBoundsException ignored) {
        sender.sendMessage('Usage: /createrecipe <name> <result> shaped <rows> <columns> <ingredients...>')
    }
}

static shapeless(CommandSender sender, String[] args, String output, String name) {
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
        // auto-generated code
    } catch (IndexOutOfBoundsException ignored) {
        sender.sendMessage('Usage: /createrecipe <name> <result> shapeless <ingredients...>')
    }
}

static furnace(CommandSender sender, String[] args, String output, String name) {
    try {
        BukkitItem.newRecipeItem(output)
                .addRecipes(new FurnaceRecipe(name)
                        .setIngredient(Item.newItem(args[0]))
                        .setCookingTime(Double.valueOf(args[1]))
                        .setExperience(Float.valueOf(args[2])))
                .registerRecipes()
        sender.sendMessage('Ok')
    } catch (NumberFormatException ignored) {
        // auto-generated code
    } catch (IndexOutOfBoundsException ignored) {
        sender.sendMessage('Usage: /createrecipe <name> <result> furnace <ingredient> <cooking-time> <experience>')
    }
}

def run = { CommandSender sender, String label, String[] args ->
    if (sender instanceof Player)
        try {
            "${args[2].toLowerCase()}"(sender, Arrays.copyOfRange(args, 3, args.length), args[1], args[0])
        } catch (NumberFormatException ignored) {
            // auto-generated code
        } catch (IndexOutOfBoundsException ignored) {
            sender.sendMessage('Usage: /createrecipe <name> <result> <shaped|shapeless|furnace> <data>')
        } catch (Exception e) {
            sender.sendMessage(e.message)
        }
    else sender.sendMessage('Console cannot execute this command!')
}
