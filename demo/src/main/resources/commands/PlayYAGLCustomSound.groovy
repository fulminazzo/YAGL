import it.fulminazzo.yagl.WrappersAdapter
import it.fulminazzo.yagl.wrappers.Sound
import org.bukkit.entity.Player

def run = { sender, label, args ->
    if (sender instanceof Player) {
        try {
            Sound sound = new Sound(args[0], Float.valueOf(args[1]), Float.valueOf(args[2]), args[3])
            WrappersAdapter.playCustomSound(sender, sound)
        } catch (IndexOutOfBoundsException ignored) {
            sender.sendMessage('Usage: /playsound <sound> <volume> <pitch> <category>')
        } catch (NumberFormatException ignored) {
            // auto-generated code
        } catch (Exception e) {
            sender.sendMessage(e.message)
        }
    } else sender.sendMessage('Console cannot execute this command!')
}
