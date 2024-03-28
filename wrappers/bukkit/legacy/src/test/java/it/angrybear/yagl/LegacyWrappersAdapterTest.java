package it.angrybear.yagl;

import it.angrybear.yagl.particles.LegacyParticleType;
import it.angrybear.yagl.particles.Particle;
import it.angrybear.yagl.particles.PrimitiveParticleOption;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class LegacyWrappersAdapterTest {

    private static Particle[] getTestLegacyParticles() {
        List<Particle> particles = new ArrayList<>();
        for (LegacyParticleType<?> type : LegacyParticleType.legacyValues()) particles.add(type.createParticle());
        particles.add(LegacyParticleType.VILLAGER_PLANT_GROW.createParticle(new PrimitiveParticleOption<>(10)));
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
            assertNotNull(extra.getValue());
        }
    }
}
