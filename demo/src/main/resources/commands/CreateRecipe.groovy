import it.angrybear.yagl.items.BukkitItem
import it.angrybear.yagl.items.Item
import it.angrybear.yagl.items.recipes.FurnaceRecipe
import it.angrybear.yagl.items.recipes.ShapedRecipe
import it.angrybear.yagl.items.recipes.ShapelessRecipe
import org.bukkit.entity.Player

static def shaped(sender, label, args, output, name) {
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

    } catch (IndexOutOfBoundsException ignored) {
        sender.sendMessage('Usage: /createrecipe <name> <result> shaped <rows> <columns> <ingredients...>')
    }
}

static def shapeless(sender, label, args, output, name) {
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

static def furnace(sender, label, args, output, name) {
    try {
        BukkitItem.newRecipeItem(output)
                .addRecipes(new FurnaceRecipe(name)
                        .setIngredient(Item.newItem(args[0]))
                        .setCookingTime(Double.valueOf(args[1]))
                        .setExperience(Float.valueOf(args[2])))
                .registerRecipes()
        sender.sendMessage('Ok')
    } catch (NumberFormatException ignored) {

    } catch (IndexOutOfBoundsException ignored) {
        sender.sendMessage('Usage: /createrecipe <name> <result> furnace <ingredient> <cooking-time> <experience>')
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
            sender.sendMessage(e.message)
        }
    else sender.sendMessage('Console cannot execute this command!')
}
