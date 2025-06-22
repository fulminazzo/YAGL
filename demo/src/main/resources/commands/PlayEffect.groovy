/**
 * Spawns the specified particle at the player's eyes location.
 * If additional arguments are specified, and the particle supports it,
 * they are converted to the corresponding ParticleOption.
 * See {@link #getOption(CommandSender, LegacyParticleType, Class, String[])} to find out how.
 */
import it.fulminazzo.yagl.Color
import it.fulminazzo.yagl.WrappersAdapter
import it.fulminazzo.yagl.particle.ColorParticleOption
import it.fulminazzo.yagl.particle.LegacyParticleType
import it.fulminazzo.yagl.particle.MaterialDataOption
import it.fulminazzo.yagl.particle.Particle
import it.fulminazzo.yagl.particle.PotionParticleOption
import it.fulminazzo.yagl.particle.PrimitiveParticleOption
import it.fulminazzo.yagl.wrapper.Potion
import it.fulminazzo.fulmicollection.objects.Refl
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

static getOption(CommandSender sender, LegacyParticleType particleType, Class optionType, String[] args) {
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

def run = { CommandSender sender, String label, String[] args ->
    if (sender instanceof Player) {
        try {
            LegacyParticleType type = LegacyParticleType.valueOf(args[0])
            Class optionType = new Refl<>(type).getFieldObject('optionType')
            Particle particle
            if (args.length > 1 && optionType != null) {
                def option = getOption(sender, type, optionType, Arrays.copyOfRange(args, 1, args.length))
                particle = type.create(option)
            } else particle = type.create()
            WrappersAdapter.spawnEffect(sender, particle, sender.eyeLocation)
        } catch (IndexOutOfBoundsException ignored) {
            sender.sendMessage('Usage: /playeffect <particle> <data...>')
        } catch (NumberFormatException ignored) {
            // auto-generated code
        } catch (Exception e) {
            sender.sendMessage(e.message)
        }
    } else sender.sendMessage('Console cannot execute this command!')
}
