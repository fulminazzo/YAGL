import it.angrybear.yagl.WrappersAdapter
import it.angrybear.yagl.wrappers.PotionEffect
import org.bukkit.entity.Player

def run = { sender, label, args ->
    if (sender instanceof Player) {
        try {
            PotionEffect effect = new PotionEffect(args[0], Double.valueOf(args[1]),
                    Integer.valueOf(args[2]), Boolean.valueOf(args[3]), Boolean.valueOf(args[4]))
            def potionEffect = WrappersAdapter.wPotionEffectToPotionEffect(effect)
            sender.addPotionEffect(potionEffect)
        } catch (IndexOutOfBoundsException ignored) {
            sender.sendMessage('Usage: /applypotioneffect <effect> <duration> <level> <show-particles> <show-icon>')
        } catch (NumberFormatException ignored) {

        } catch (Exception e) {
            sender.sendMessage(e.message)
        }
    } else sender.sendMessage('Console cannot execute this command!')
}