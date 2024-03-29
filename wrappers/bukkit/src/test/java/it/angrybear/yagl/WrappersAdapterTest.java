package it.angrybear.yagl;

import it.angrybear.yagl.items.Item;
import it.angrybear.yagl.particles.Particle;
import it.angrybear.yagl.particles.*;
import it.angrybear.yagl.wrappers.Enchantment;
import it.angrybear.yagl.wrappers.PotionEffect;
import it.fulminazzo.fulmicollection.objects.Refl;
import org.bukkit.Color;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFactory;
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
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
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
        for (ParticleType<?> type : ParticleType.values()) particles.add(type.create());
        particles.add(ParticleType.SCULK_CHARGE.create(new PrimitiveParticleOption<>(10f)));
        particles.add(ParticleType.SHRIEK.create(new PrimitiveParticleOption<>(11)));
        particles.add(ParticleType.REDSTONE.create(new DustParticleOption(it.angrybear.yagl.Color.RED, 12f)));
        particles.add(ParticleType.DUST_COLOR_TRANSITION.create(new DustTransitionParticleOption(
                it.angrybear.yagl.Color.RED, it.angrybear.yagl.Color.BLUE, 12f)));
        particles.add(ParticleType.VIBRATION.create(new PrimitiveParticleOption<>(
                new Vibration(mock(Location.class), mock(Vibration.Destination.class), 10))));
        return particles.toArray(new Particle[0]);
    }

    private static Particle[] getTestLegacyParticles() {
        List<Particle> particles = new ArrayList<>();
        for (LegacyParticleType<?> type : LegacyParticleType.values()) particles.add(type.create());
        for (LegacyParticleType<?> type : LegacyParticleType.legacyValues())
            particles.removeIf(t -> t.getType().equalsIgnoreCase(type.name()));
        particles.add(LegacyParticleType.COMPOSTER_FILL_ATTEMPT.create(new PrimitiveParticleOption<>(true)));
        particles.add(LegacyParticleType.BONE_MEAL_USE.create(new PrimitiveParticleOption<>(1)));
        particles.add(LegacyParticleType.INSTANT_POTION_BREAK.create(new ColorParticleOption(it.angrybear.yagl.Color.AQUA)));
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

    @ParameterizedTest
    @MethodSource("getTestParticles")
    void testSpawnParticle(Particle particle) {
        // Initialize Bukkit variables
        Server server = mock(Server.class);
        when(server.createBlockData(any(Material.class), any(String.class))).thenReturn(mock(BlockData.class));
        new Refl<>(Bukkit.class).setFieldObject("server", server);

        Player player = mock(Player.class);

        Location location = new Location(null, 0, 0, 0);
        WrappersAdapter.spawnParticle(player, particle, location, 1);

        ArgumentCaptor<org.bukkit.Particle> particleArg = ArgumentCaptor.forClass(org.bukkit.Particle.class);
        if (particle.getOption() == null) {
            verify(player).spawnParticle(particleArg.capture(),
                    any(Location.class), any(int.class),
                    any(double.class), any(double.class), any(double.class));

            assertEquals(particle.getType(), particleArg.getValue().name());
        } else {
            ArgumentCaptor<?> extra = ArgumentCaptor.forClass(Object.class);
            verify(player).spawnParticle(particleArg.capture(),
                    any(Location.class), any(int.class),
                    any(double.class), any(double.class), any(double.class),
                    extra.capture());

            assertEquals(particle.getType(), particleArg.getValue().name());
            assertNotNull(extra.getValue());
        }
    }

    @Test
    void testSpawnItemCrack() {
        // Initialize Bukkit variables
        ItemFactory factory = mock(ItemFactory.class);
        Server server = mock(Server.class);
        when(server.getItemFactory()).thenReturn(factory);

        new Refl<>(Bukkit.class).setFieldObject("server", server);

        Particle particle = ParticleType.ITEM_CRACK.create(mock(Item.class));
        Player player = mock(Player.class);

        Location location = new Location(null, 0, 0, 0);
        WrappersAdapter.spawnParticle(player, particle, location, 1);

        ArgumentCaptor<org.bukkit.Particle> particleArg = ArgumentCaptor.forClass(org.bukkit.Particle.class);
        ArgumentCaptor<?> extra = ArgumentCaptor.forClass(Object.class);
        verify(player).spawnParticle(particleArg.capture(),
                any(Location.class), any(int.class),
                any(double.class), any(double.class), any(double.class),
                extra.capture());

        assertEquals(particle.getType(), particleArg.getValue().name());

        ItemStack expected = new ItemStack(Material.STONE, 7);
        assertInstanceOf(ItemStack.class, extra.getValue());
        ItemStack actual = (ItemStack) extra.getValue();
        assertEquals(expected.getType(), actual.getType());
        assertEquals(expected.getAmount(), actual.getAmount());
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

        assertEquals(sound.getName(), soundArg.getValue().name());

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

        assertEquals(sound.getName(), soundArg.getValue());

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

    @Test
    void testInvalidClassProvided() {
        Refl<?> refl = new Refl<>(WrappersAdapter.class);
        assertThrowsExactly(IllegalArgumentException.class, () -> refl.invokeMethod("wParticleToGeneral",
                ParticleType.DUST_COLOR_TRANSITION.create(it.angrybear.yagl.Color.RED, it.angrybear.yagl.Color.RED, 3f),
                org.bukkit.Particle.class, (Function<?, Class<?>>) s -> org.bukkit.Particle.REDSTONE.getDataType()));
    }

    @Test
    void testNoConstructorDataTypeProvided() {
        Refl<?> refl = new Refl<>(WrappersAdapter.class);
        assertThrowsExactly(IllegalArgumentException.class, () -> refl.invokeMethod("wParticleToGeneral",
                ParticleType.REDSTONE.create(it.angrybear.yagl.Color.RED, 3f), org.bukkit.Particle.class,
                (Function<?, Class<?>>) s -> Item.class));
    }

    private static class MockPotionEffect extends PotionEffectType {
        private final String name;

        protected MockPotionEffect(int id, String name) {
            super(id, NamespacedKey.minecraft(name.toLowerCase()));
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