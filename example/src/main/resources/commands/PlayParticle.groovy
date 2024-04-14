import it.angrybear.yagl.WrappersAdapter
import it.angrybear.yagl.particles.Particle
import it.angrybear.yagl.particles.ParticleType
import org.bukkit.entity.Player

def run = { sender, label, args ->
    if (sender instanceof Player) {
        try {
            ParticleType type = ParticleType.valueOf(args[0])
            Particle particle
            if (args.length > 1) {

            } else particle = type.create()
            WrappersAdapter.spawnParticle(sender, particle, sender.getEyeLocation(), 1)
        } catch (IndexOutOfBoundsException ignored) {
            sender.sendMessage('Usage: /playparticle <particle> <data...>')
        } catch (NumberFormatException ignored) {

        } catch (Exception e) {
            sender.sendMessage(e.getMessage())
        }
    } else sender.sendMessage('Console cannot execute this command!')
}