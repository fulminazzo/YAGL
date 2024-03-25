package it.angrybear.yagl;

import it.angrybear.yagl.particles.Particle;
import it.angrybear.yagl.particles.*;
import it.angrybear.yagl.wrappers.Enchantment;
import it.angrybear.yagl.wrappers.PotionEffect;
import it.fulminazzo.fulmicollection.objects.Refl;
import org.bukkit.Color;
import org.bukkit.*;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WrappersAdapterTest {

    private static org.bukkit.potion.PotionEffect[] getPotionEffects() {
        List<PotionEffectType> potionEffects = new ArrayList<>();
        for (Field field : PotionEffectType.class.getDeclaredFields())
            if (field.getType().equals(PotionEffectType.class))
                try {
                    PotionEffectType type = (PotionEffectType) field.get(PotionEffectType.class);
                    potionEffects.add(new MockPotionEffect(type.getId(), field.getName()));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
        // Register potion effects
        Map<String, PotionEffectType> byName = new Refl<>(PotionEffectType.class)
                .getFieldObject("byName");
        if (byName != null) potionEffects.forEach(e -> byName.put(e.getName().toLowerCase(), e));
        return potionEffects.stream()
                .map(t -> new org.bukkit.potion.PotionEffect(t, 15, 2, true, true, false))
                .toArray(org.bukkit.potion.PotionEffect[]::new);
    }

    private static org.bukkit.enchantments.Enchantment[] getEnchantments() {
        List<org.bukkit.enchantments.Enchantment> enchantments = new ArrayList<>();
        for (Field field : org.bukkit.enchantments.Enchantment.class.getDeclaredFields())
            if (field.getType().equals(org.bukkit.enchantments.Enchantment.class))
                try {
                    org.bukkit.enchantments.Enchantment enchant = (org.bukkit.enchantments.Enchantment) field.get(org.bukkit.enchantments.Enchantment.class);
                    enchantments.add(new MockEnchantment(enchant.getKey()));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
        // Register enchantments
        Map<NamespacedKey, org.bukkit.enchantments.Enchantment> byKey = new Refl<>(org.bukkit.enchantments.Enchantment.class)
                .getFieldObject("byKey");
        if (byKey != null) enchantments.forEach(e -> byKey.put(e.getKey(), e));
        return enchantments.toArray(new org.bukkit.enchantments.Enchantment[0]);
    }
    
    private static Particle[] getTestParticles() {
        List<Particle> particles = new ArrayList<>();
        for (ParticleType<?> type : ParticleType.values()) particles.add(type.createParticle());
        particles.add(ParticleType.SCULK_CHARGE.createParticle(new PrimitiveParticleOption<>(10f)));
        particles.add(ParticleType.SHRIEK.createParticle(new PrimitiveParticleOption<>(11)));
        particles.add(ParticleType.REDSTONE.createParticle(new DustParticleOption(it.angrybear.yagl.Color.RED, 12f)));
        particles.add(ParticleType.DUST_COLOR_TRANSITION.createParticle(new DustTransitionParticleOption(
                it.angrybear.yagl.Color.RED, it.angrybear.yagl.Color.BLUE, 12f)));
        return particles.toArray(new Particle[0]);
    }

    @ParameterizedTest
    @MethodSource("getTestParticles")
    void testSpawnParticle(Particle particle) {
        Player player = mock(Player.class);

        WrappersAdapter.spawnParticle(player, particle, 0, 0, 0, 1, 0, 0, 0);

        ArgumentCaptor<org.bukkit.Particle> particleArg = ArgumentCaptor.forClass(org.bukkit.Particle.class);
        if (particle.getOption() == null) {
            verify(player).spawnParticle(particleArg.capture(),
                    any(double.class), any(double.class), any(double.class),
                    any(int.class),
                    any(double.class), any(double.class), any(double.class));

            assertEquals(particle.getType(), particleArg.getValue().name());
        } else {
            ArgumentCaptor<?> extra = ArgumentCaptor.forClass(Object.class);
            verify(player).spawnParticle(particleArg.capture(),
                    any(double.class), any(double.class), any(double.class),
                    any(int.class),
                    any(double.class), any(double.class), any(double.class),
                    extra.capture());

            assertEquals(particle.getType(), particleArg.getValue().name());
            assertNotNull(extra.getValue());
        }
    }

    @Test
    void testPlaySound() {
        it.angrybear.yagl.wrappers.Sound sound = new it.angrybear.yagl.wrappers.Sound(
                Sound.BLOCK_GLASS_STEP.name(),10, 2, SoundCategory.BLOCKS.name());
        Player player = mock(Player.class);

        when(player.getLocation()).thenReturn(new Location(null, 0, 1, 0));

        WrappersAdapter.playSound(player, sound);

        ArgumentCaptor<Sound> soundArg = ArgumentCaptor.forClass(Sound.class);
        ArgumentCaptor<SoundCategory> categoryArg = ArgumentCaptor.forClass(SoundCategory.class);
        ArgumentCaptor<Float> volumeArg = ArgumentCaptor.forClass(Float.class);
        ArgumentCaptor<Float> pitchArg = ArgumentCaptor.forClass(Float.class);
        verify(player).playSound(any(Location.class), soundArg.capture(),
                categoryArg.capture(), volumeArg.capture(), pitchArg.capture());

        assertEquals(sound.getSound(), soundArg.getValue().name());

        assertEquals(sound.getVolume(), volumeArg.getValue());
        assertEquals(sound.getPitch(), pitchArg.getValue());

        assertEquals(sound.getCategory(), categoryArg.getValue().name());
    }

    @Test
    void testCustomPlaySound() {
        it.angrybear.yagl.wrappers.Sound sound = new it.angrybear.yagl.wrappers.Sound(
                "custom_sound",10, 2, SoundCategory.BLOCKS.name());
        Player player = mock(Player.class);

        when(player.getLocation()).thenReturn(new Location(null, 0, 1, 0));

        WrappersAdapter.playCustomSound(player, sound);

        ArgumentCaptor<String> soundArg = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<SoundCategory> categoryArg = ArgumentCaptor.forClass(SoundCategory.class);
        ArgumentCaptor<Float> volumeArg = ArgumentCaptor.forClass(Float.class);
        ArgumentCaptor<Float> pitchArg = ArgumentCaptor.forClass(Float.class);
        verify(player).playSound(any(Location.class), soundArg.capture(),
                categoryArg.capture(), volumeArg.capture(), pitchArg.capture());

        assertEquals(sound.getSound(), soundArg.getValue());

        assertEquals(sound.getVolume(), volumeArg.getValue());
        assertEquals(sound.getPitch(), pitchArg.getValue());

        assertEquals(sound.getCategory(), categoryArg.getValue().name());
    }

    @ParameterizedTest
    @MethodSource("getPotionEffects")
    void testEnchantmentsConversion(org.bukkit.potion.PotionEffect expected) {
        PotionEffect enchantment = WrappersAdapter.potionEffectToWPotionEffect(expected);
        assertEquals(expected, WrappersAdapter.wPotionEffectToPotionEffect(enchantment));
    }

    @ParameterizedTest
    @MethodSource("getEnchantments")
    void testEnchantmentsConversion(org.bukkit.enchantments.Enchantment expected) {
        Enchantment enchantment = WrappersAdapter.enchantToWEnchant(expected);
        assertEquals(expected, WrappersAdapter.wEnchantToEnchant(enchantment).getKey());
    }

    private static class MockPotionEffect extends PotionEffectType {
        private final String name;

        protected MockPotionEffect(int id, String name) {
            super(id, NamespacedKey.minecraft(name));
            this.name = name;
        }

        @Override
        public double getDurationModifier() {
            return 0;
        }

        @NotNull
        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public boolean isInstant() {
            return false;
        }

        @NotNull
        @Override
        public Color getColor() {
            return null;
        }
    }

    private static class MockEnchantment extends org.bukkit.enchantments.Enchantment {

        public MockEnchantment(@NotNull NamespacedKey key) {
            super(key);
        }

        @NotNull
        @Override
        public String getName() {
            return getKey().getKey();
        }

        @Override
        public int getMaxLevel() {
            return 0;
        }

        @Override
        public int getStartLevel() {
            return 0;
        }

        @NotNull
        @Override
        public EnchantmentTarget getItemTarget() {
            return null;
        }

        @Override
        public boolean isTreasure() {
            return false;
        }

        @Override
        public boolean isCursed() {
            return false;
        }

        @Override
        public boolean conflictsWith(@NotNull org.bukkit.enchantments.Enchantment other) {
            return false;
        }

        @Override
        public boolean canEnchantItem(@NotNull ItemStack item) {
            return false;
        }
    }
}