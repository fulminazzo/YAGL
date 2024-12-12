/**
 * Spawns the specified particle at the player's eyes location.
 * If additional arguments are specified, and the particle supports it,
 * they are converted to the corresponding ParticleOption.
 * See {@link #getOption(Object, Object, Object, Object)} to find out how.
 */
import it.fulminazzo.yagl.Color
import it.fulminazzo.yagl.ItemAdapter
import it.fulminazzo.yagl.WrappersAdapter
import it.fulminazzo.yagl.particles.BlockDataOption
import it.fulminazzo.yagl.particles.DustParticleOption
import it.fulminazzo.yagl.particles.DustTransitionParticleOption
import it.fulminazzo.yagl.particles.ItemParticleOption
import it.fulminazzo.yagl.particles.Particle
import it.fulminazzo.yagl.particles.ParticleType
import it.fulminazzo.yagl.particles.PrimitiveParticleOption
import it.fulminazzo.fulmicollection.objects.Refl
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot

static getOption(CommandSender sender, ParticleType particleType, Class optionType, String[] args) {
    if (optionType == DustParticleOption)
        new DustParticleOption(Color.fromARGB(args[0]), Float.valueOf(args[1]))
    else if (optionType == DustTransitionParticleOption)
        new DustTransitionParticleOption(Color.fromARGB(args[0]), Color.fromARGB(args[1]), Float.valueOf(args[2]))
    else if (optionType == ItemParticleOption)
        ItemAdapter.itemStackToItem(sender.inventory.getItem(EquipmentSlot.HAND))
    else if (optionType == BlockDataOption)
        new BlockDataOption(args[0])
    else if (particleType == ParticleType.VIBRATION) {
        Location start = sender.location
        Location end = start.clone().add(0, 10, 0)
        // Qualified reference is necessary to permit retro-compatibility
        def dest = new org.bukkit.Vibration.Destination.BlockDestination(end)
        new PrimitiveParticleOption<>(new org.bukkit.Vibration(start, dest, Integer.valueOf(args[0])))
    } else if (particleType == ParticleType.SCULK_CHARGE)
        new PrimitiveParticleOption<>(Float.valueOf(args[0]))
    else if (particleType == ParticleType.SHRIEK)
        new PrimitiveParticleOption<>(Integer.valueOf(args[0]))
    else throw new IllegalArgumentException("Cannot get particle option of ${optionType}")
}

def run = { CommandSender sender, String label, String[] args ->
    if (sender instanceof Player) {
        try {
            ParticleType type = ParticleType.valueOf(args[0])
            Class optionType = new Refl<>(type).getFieldObject('optionType')
            Particle particle
            if (args.length > 1 && optionType != null) {
                def option = getOption(sender, type, optionType, Arrays.copyOfRange(args, 1, args.length))
                particle = type.create(option)
            } else particle = type.create()
            WrappersAdapter.spawnParticle(sender, particle, sender.eyeLocation, 1)
        } catch (IndexOutOfBoundsException ignored) {
            sender.sendMessage('Usage: /playparticle <particle> <data...>')
        } catch (NumberFormatException ignored) {
            // auto-generated code
        } catch (Exception e) {
            sender.sendMessage(e.message)
        }
    } else sender.sendMessage('Console cannot execute this command!')
}
