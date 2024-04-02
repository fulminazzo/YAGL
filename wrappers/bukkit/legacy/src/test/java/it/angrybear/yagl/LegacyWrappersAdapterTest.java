package it.angrybear.yagl;

import it.angrybear.yagl.particles.*;
import it.angrybear.yagl.wrappers.Potion;
import it.angrybear.yagl.wrappers.PotionEffect;
import it.fulminazzo.fulmicollection.objects.Refl;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LegacyWrappersAdapterTest {

    private static it.angrybear.yagl.Color[] getColors() {
        return it.angrybear.yagl.Color.values();
    }

    private static Particle[] getTestLegacyParticles() {
        List<Particle> particles = new ArrayList<>();
        for (LegacyParticleType<?> type : LegacyParticleType.legacyValues()) particles.add(type.create());
        particles.add(LegacyParticleType.VILLAGER_PLANT_GROW.create(new PrimitiveParticleOption<>(10)));
        particles.add(LegacyParticleType.SMOKE.create(new PrimitiveParticleOption<>(BlockFace.NORTH.name())));
        particles.add(LegacyParticleType.ITEM_BREAK.create(new PrimitiveParticleOption<>(Material.STONE.name())));
        particles.add(LegacyParticleType.TILE_BREAK.create(new MaterialDataOption(Material.STONE.name())));
        particles.add(LegacyParticleType.TILE_DUST.create(new MaterialDataOption(Material.STONE.name(), 10)));
        particles.add(LegacyParticleType.POTION_BREAK.create(new PotionParticleOption(new Potion(PotionType.JUMP.name()))));
        return particles.toArray(new Particle[0]);
    }

    @ParameterizedTest
    @MethodSource("getTestLegacyParticles")
    void testSpawnEffect(Particle particle) {
        Player player = mock(Player.class);

        TestUtils.testMultipleMethods(WrappersAdapter.class, m -> m.getName().equals("spawnEffect") && m.getParameterTypes()[0].equals(Player.class),
                a -> {
                    ArgumentCaptor<?> arg = a[1];
                    Object value = arg.getValue();
                    assertInstanceOf(Effect.class, value);
                    assertEquals(particle.getType(), ((Effect) value).name());
                    if (particle.getOption() != null) assertNotNull(a[a.length - 1].getValue());
                }, new Object[]{player, particle}, player, "playEffect", Location.class, Effect.class, Object.class);
    }

    @Test
    void testWPotionEffectToPotionEffect() {
        String name = "INCREASE_DAMAGE";
        PotionEffectType type = mock(PotionEffectType.class);
        when(type.getName()).thenReturn(name);
        Map<String, PotionEffectType> map = new Refl<>(PotionEffectType.class).getFieldObject("byName");
        map.put(name.toLowerCase(), type);
        PotionEffect potionEffect = new PotionEffect(type.getName(), 10, 1, false, true);
        assertEquals(new org.bukkit.potion.PotionEffect(type, 10 * 20, 0, false, false),
                WrappersAdapter.wPotionEffectToPotionEffect(potionEffect));
    }

    @Test
    void testPotionEffectToWPotionEffect() {
        String name = "INCREASE_DAMAGE";
        PotionEffectType type = mock(PotionEffectType.class);
        when(type.getName()).thenReturn(name);
        Map<String, PotionEffectType> map = new Refl<>(PotionEffectType.class).getFieldObject("byName");
        map.put(name.toLowerCase(), type);
        org.bukkit.potion.PotionEffect potionEffect = new org.bukkit.potion.PotionEffect(type, 10 * 20, 0, false, false);
        assertEquals(new PotionEffect(type.getName(), 10, 1, false, true),
                WrappersAdapter.potionEffectToWPotionEffect(potionEffect));
    }

    @Test
    void testPotionConversion() {
        Potion expected = new Potion(PotionType.JUMP.name(), 1, true, false);
        org.bukkit.potion.Potion potion = WrappersAdapter.wPotionToPotion(expected);
        assertEquals(expected, WrappersAdapter.potionToWPotion(potion));
    }

    @ParameterizedTest
    @MethodSource("getColors")
    void testColorConversion(it.angrybear.yagl.Color expected) {
        org.bukkit.Color color = WrappersAdapter.wColorToColor(expected);
        assertEquals(expected, WrappersAdapter.colorToWColor(color));
    }
}
