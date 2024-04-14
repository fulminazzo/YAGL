import it.angrybear.yagl.Color
import it.angrybear.yagl.WrappersAdapter
import it.angrybear.yagl.particles.ColorParticleOption
import it.angrybear.yagl.particles.LegacyParticleType
import it.angrybear.yagl.particles.MaterialDataOption
import it.angrybear.yagl.particles.Particle
import it.angrybear.yagl.particles.PotionParticleOption
import it.angrybear.yagl.particles.PrimitiveParticleOption
import it.angrybear.yagl.wrappers.Potion
import it.fulminazzo.fulmicollection.objects.Refl
import org.bukkit.entity.Player

def getOption(sender, particleType, optionType, args) {
    if (optionType == PotionParticleOption)
        new PotionParticleOption(new Potion(args[0], Integer.valueOf(args[1]),
                Boolean.valueOf(args[2]), Boolean.valueOf(args[3])))
    else if (optionType == MaterialDataOption)
        new MaterialDataOption(args[0])
    else if (optionType == ColorParticleOption)
        new ColorParticleOption(Color.fromARGB(args[0]))
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
            WrappersAdapter.spawnEffect(sender, particle, sender.getEyeLocation())
        } catch (IndexOutOfBoundsException ignored) {
            sender.sendMessage('Usage: /playeffect <particle> <data...>')
        } catch (NumberFormatException ignored) {

        } catch (Exception e) {
            sender.sendMessage(e.getMessage())
        }
    } else sender.sendMessage('Console cannot execute this command!')
}