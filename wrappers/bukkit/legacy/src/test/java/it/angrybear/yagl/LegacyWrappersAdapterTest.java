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

        Location location = new Location(null, 0, 0, 0);
        WrappersAdapter.spawnEffect(player, particle, location);

        ArgumentCaptor<Effect> effectArg = ArgumentCaptor.forClass(Effect.class);
        if (particle.getOption() == null) {
            verify(player).playEffect(any(Location.class), effectArg.capture(), any());

            assertEquals(particle.getType(), effectArg.getValue().name());
        } else {
            ArgumentCaptor<?> extra = ArgumentCaptor.forClass(Object.class);
            verify(player).playEffect(any(Location.class), effectArg.capture(), extra.capture());

            assertEquals(particle.getType(), effectArg.getValue().name());
            if (particle.getType().equals(LegacyParticleType.POTION_BREAK.name()))
                assertInstanceOf(org.bukkit.potion.Potion.class, extra.getValue());
            assertNotNull(extra.getValue());
        }
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
}
