import it.angrybear.yagl.WrappersAdapter
import it.angrybear.yagl.particles.LegacyParticleType
import it.angrybear.yagl.particles.Particle
import it.angrybear.yagl.particles.PrimitiveParticleOption
import it.fulminazzo.fulmicollection.objects.Refl
import org.bukkit.entity.Player

def getOption(sender, particleType, optionType, args) {
    if (optionType);
    else if (particleType == LegacyParticleType.SMOKE)
        new PrimitiveParticleOption<>(args[0])
    else if (particleType == LegacyParticleType.VILLAGER_PLANT_GROW)
        new PrimitiveParticleOption<>(Integer.valueOf(args[0]))
    else if (particleType == LegacyParticleType.ITEM_BREAK)
        new PrimitiveParticleOption<>(args[0])
    else if (particleType == LegacyParticleType.COMPOSTER_FILL_ATTEMPT)
        new PrimitiveParticleOption<>(Boolean.valueOf(args[0]))
    else if (particleType == LegacyParticleType.BONE_MEAL_USE)
        new PrimitiveParticleOption<>(Integer.valueOf(args[0]))
    else if (particleType == LegacyParticleType.ELECTRIC_SPARK)
        new PrimitiveParticleOption<>(args[0])
    else throw new IllegalArgumentException("Cannot get particle option of ${optionType}")
}

def run = { sender, label, args ->
    if (sender instanceof Player) {
        try {
            LegacyParticleType type = LegacyParticleType.valueOf(args[0])
            Class optionType = new Refl<>(type).getFieldObject('optionType')
            Particle particle
            if (args.length > 1 && optionType != null) {
                def option = getOption(sender, type, optionType, Arrays.copyOfRange(args, 1, args.length))
                particle = type.create(option)
            } else particle = type.create()
            WrappersAdapter.spawnParticle(sender, particle, sender.getEyeLocation(), 1)
        } catch (IndexOutOfBoundsException ignored) {
            sender.sendMessage('Usage: /playeffect <particle> <data...>')
        } catch (NumberFormatException ignored) {

        } catch (Exception e) {
            sender.sendMessage(e.getMessage())
        }
    } else sender.sendMessage('Console cannot execute this command!')
}