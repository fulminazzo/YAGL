import it.angrybear.yagl.Color
import it.angrybear.yagl.ItemAdapter
import it.angrybear.yagl.WrappersAdapter
import it.angrybear.yagl.particles.*
import it.fulminazzo.fulmicollection.objects.Refl
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot

def getOption(sender, particleType, optionType, args) {
    if (optionType == DustParticleOption)
        new DustParticleOption(Color.fromARGB(args[0]), Float.valueOf(args[1]))
    if (optionType == DustTransitionParticleOption)
        new DustTransitionParticleOption(Color.fromARGB(args[0]), Color.fromARGB(args[1]), Float.valueOf(args[2]))
    else if (optionType == ItemParticleOption)
        ItemAdapter.itemStackToItem(sender.getInventory().getItem(EquipmentSlot.HAND))
    else if (optionType == BlockDataOption)
        new BlockDataOption(args[0])
    else if (particleType == ParticleType.SCULK_CHARGE)
        new PrimitiveParticleOption<>(Float.valueOf(args[0]))
    else if (particleType == ParticleType.SHRIEK)
        new PrimitiveParticleOption<>(Integer.valueOf(args[0]))
    else throw new IllegalArgumentException("Cannot get particle option of ${optionType}")
}

def run = { sender, label, args ->
    if (sender instanceof Player) {
        try {
            ParticleType type = ParticleType.valueOf(args[0])
            Class optionType = new Refl<>(type).getFieldObject('optionType')
            Particle particle
            if (args.length > 1 && optionType != null) {
                def option = getOption(sender, type, optionType, Arrays.copyOfRange(args, 1, args.length))
                particle = type.create(option)
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