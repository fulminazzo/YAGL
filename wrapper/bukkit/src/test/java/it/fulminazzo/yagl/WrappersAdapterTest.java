package it.fulminazzo.yagl;

import it.fulminazzo.fulmicollection.objects.Printable;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.structures.tuples.Tuple;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import it.fulminazzo.jbukkit.BukkitUtils;
import it.fulminazzo.jbukkit.annotations.After1_;
import it.fulminazzo.jbukkit.annotations.Before1_;
import it.fulminazzo.yagl.item.AbstractItem;
import it.fulminazzo.yagl.particle.Particle;
import it.fulminazzo.yagl.particle.*;
import it.fulminazzo.yagl.wrapper.Enchantment;
import it.fulminazzo.yagl.wrapper.PotionEffect;
import it.fulminazzo.yagl.wrapper.Sound;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.mockito.Mockito.*;

@SuppressWarnings("deprecation")
@After1_(13)
class WrappersAdapterTest extends BukkitUtils {

    @BeforeAll
    static void setAllUp() {
        setupServer();
        setupEnchantments();
    }

    @BeforeEach
    @Override
    protected void setUp() {
        super.setUp();
        when(Bukkit.getServer().createBlockData(any(Material.class), any(String.class)))
                .thenReturn(mock(org.bukkit.block.data.BlockData.class));
    }

    private static Object[] getParticles() {
        check();
        return org.bukkit.Particle.values();
    }

    @ParameterizedTest
    @MethodSource("getParticles")
    void testAllBukkitParticlesAreConverted(Object particle) {
        String name = new Refl<>(particle).invokeMethod("name");
        assumeFalse(name.contains("LEGACY"));
        for (ParticleType<?> type : ParticleType.values()) {
            try {
                Tuple<org.bukkit.Particle, ?> converted = WrappersAdapter.wParticleToParticle(type.create());
                if (converted.getKey().equals(particle)) return;
            } catch (IllegalArgumentException ignored) {
                // Older version trying to use newer particles
            }
        }
        fail("Could not convert particle: " + particle);
    }

    private static Particle[] getTestLegacyParticles() {
        List<Tuple<LegacyParticleType<?>, Object>> particles = new ArrayList<>();
        for (LegacyParticleType<?> type : LegacyParticleType.values()) particles.add(new Tuple<>(type, null));
        particles.add(new Tuple<>(LegacyParticleType.COMPOSTER_FILL_ATTEMPT, new PrimitiveParticleOption<>(true)));
        particles.add(new Tuple<>(LegacyParticleType.BONE_MEAL_USE, new PrimitiveParticleOption<>(1)));
        particles.add(new Tuple<>(LegacyParticleType.INSTANT_POTION_BREAK, new ColorParticleOption(Color.AQUA)));
        // Remove effects not belonging to current Minecraft version
        particles.removeIf(p -> {
            try {
                Effect.valueOf(p.getKey().name());
                return false;
            } catch (IllegalArgumentException e) {
                return true;
            }
        });
        return particles.stream()
                .map(t -> {
                    LegacyParticleType<?> particle = t.getKey();
                    Object option = t.getValue();
                    if (option == null) return particle.create();
                    else return new Refl<>(particle).invokeMethod("create",
                            new Class<?>[]{option.getClass().getSuperclass()},
                            option);
                })
                .toArray(Particle[]::new);
    }

    @ParameterizedTest
    @MethodSource("getTestLegacyParticles")
    void testPlayerSpawnEffect(Particle particle) {
        Player player = mock(Player.class);

        testSpawnEffect(Player.class, player, player, particle);
    }

    @ParameterizedTest
    @MethodSource("getTestLegacyParticles")
    void testWorldSpawnEffect(Particle particle) {
        Player player = mock(Player.class);
        World world = mock(World.class);
        when(world.getPlayers()).thenReturn(Collections.singletonList(player));

        testSpawnEffect(World.class, world, player, particle);
    }

    private <T> void testSpawnEffect(Class<T> targetClass, T target, Object executor, Particle particle) {
        TestUtils.testMultipleMethods(WrappersAdapter.class, m -> m.getName().equals("spawnEffect") && m.getParameterTypes()[0].equals(targetClass),
                a -> {
                    ArgumentCaptor<?> arg = a[1];
                    Object value = arg.getValue();
                    assertInstanceOf(Effect.class, value);
                    assertEquals(particle.getType(), ((Effect) value).name());
                    if (particle.getOption() != null) assertNotNull(a[a.length - 1].getValue());
                }, new Object[]{target, particle}, executor, "playEffect", Location.class, Effect.class, Object.class);
    }

    private static Particle[] getTestParticles() {
        check();
        List<Tuple<ParticleType<?>, Object>> particles = new ArrayList<>();
        for (ParticleType<?> type : ParticleType.values()) particles.add(new Tuple<>(type, null));
        particles.add(new Tuple<>(ParticleType.SCULK_CHARGE, new PrimitiveParticleOption<>(10f)));
        particles.add(new Tuple<>(ParticleType.SHRIEK, new PrimitiveParticleOption<>(11)));
        particles.add(new Tuple<>(ParticleType.SHRIEK, null));
        particles.add(new Tuple<>(ParticleType.REDSTONE, new DustParticleOption(Color.RED, 12f)));
        particles.add(new Tuple<>(ParticleType.DUST_COLOR_TRANSITION, new DustTransitionParticleOption(
                Color.RED, Color.BLUE, 12f)));
        try {
            particles.add(new Tuple<>(ParticleType.VIBRATION, new PrimitiveParticleOption<>(
                    new org.bukkit.Vibration(mock(Location.class), mock(org.bukkit.Vibration.Destination.class), 10))));
        } catch (NoClassDefFoundError ignored) {}
        particles.add(new Tuple<>(ParticleType.ITEM_CRACK, new ItemParticleOption<>(mock(AbstractItem.class))));
        particles.add(new Tuple<>(ParticleType.BLOCK_CRACK, new BlockDataOption("oak_log", "axis=y")));
        particles.add(new Tuple<>(ParticleType.BLOCK_DUST, new BlockDataOption("oak_log", "axis=y")));
        particles.add(new Tuple<>(ParticleType.FALLING_DUST, new BlockDataOption("oak_log", "axis=y")));
        // Remove particles not belonging to current Minecraft version
        particles.removeIf(p -> {
            try {
                org.bukkit.Particle.valueOf(p.getKey().name());
                return false;
            } catch (IllegalArgumentException e) {
                return true;
            }
        });
        return particles.stream()
                .map(t -> {
                    ParticleType<?> particle = t.getKey();
                    Object option = t.getValue();
                    if (option == null) return particle.create();
                    else return new Refl<>(particle).invokeMethod("create",
                            new Class<?>[]{option.getClass().getSuperclass()},
                            option);
                })
                .toArray(Particle[]::new);
    }

    @ParameterizedTest
    @MethodSource("getTestParticles")
    void testPlayerSpawnParticle(Particle particle) {
        testSpawnParticle(Player.class, particle);
    }

    @ParameterizedTest
    @MethodSource("getTestParticles")
    void testWorldSpawnParticle(Particle particle) {
        testSpawnParticle(World.class, particle);
    }

    private <T> void testSpawnParticle(Class<T> targetClass, Particle particle) {
        T target = mock(targetClass);

        final @NotNull Consumer<ArgumentCaptor<?>[]> captorsValidator;
        final Class<?> @NotNull [] invokedMethodParamTypes;

        if (particle.getOption() == null) {
            captorsValidator = a -> {
                Object value = a[0].getValue();
                assertInstanceOf(org.bukkit.Particle.class, value);
                assertEquals(particle.getType(), ((org.bukkit.Particle) value).name());
            };
            invokedMethodParamTypes = new Class[]{org.bukkit.Particle.class, Location.class, int.class,
                    double.class, double.class, double.class, double.class};
        } else {
            captorsValidator = a -> {
                Object value = a[0].getValue();
                assertInstanceOf(org.bukkit.Particle.class, value);
                assertEquals(particle.getType(), ((org.bukkit.Particle) value).name());
                assertNotNull(a[a.length - 1].getValue());
            };
            invokedMethodParamTypes = new Class[]{org.bukkit.Particle.class, Location.class, int.class,
                    double.class, double.class, double.class, double.class, Object.class};
        }

        TestUtils.testMultipleMethods(WrappersAdapter.class,
                m -> m.getName().equals("spawnParticle") && m.getParameterTypes()[0].isAssignableFrom(target.getClass()),
                captorsValidator, new Object[]{target, particle}, target, "spawnParticle", invokedMethodParamTypes);
    }

    @Test
    void testSpawnItemCrack() throws NoSuchMethodException {
        Particle particle = ParticleType.ITEM_CRACK.create(mock(AbstractItem.class));
        Player player = mock(Player.class);

        ArgumentCaptor<?> @NotNull [] captors = TestUtils.testSingleMethod(WrappersAdapter.class,
                WrappersAdapter.class.getMethod("spawnParticle", Player.class, Particle.class, Location.class, int.class),
                new Object[]{player, particle}, player, "spawnParticle",
                org.bukkit.Particle.class, Location.class, int.class, double.class, double.class, double.class, double.class, Object.class);

        Object value = captors[0].getValue();
        assertInstanceOf(org.bukkit.Particle.class, value);
        assertEquals(BukkitUtils.getNumericalVersion() >= 20.6 ? "ITEM" : particle.getType(), ((org.bukkit.Particle) value).name());

        ArgumentCaptor<?> extra = captors[captors.length - 1];
        ItemStack expected = new ItemStack(Material.STONE, 7);
        assertInstanceOf(ItemStack.class, extra.getValue());
        ItemStack actual = (ItemStack) extra.getValue();
        assertEquals(expected.getType(), actual.getType());
        assertEquals(expected.getAmount(), actual.getAmount());
    }

    @Test
    void testInvalidTargetSpawnParticle() {
        assertThrowsExactly(IllegalArgumentException.class, () ->
                new Refl<>(WrappersAdapter.class).invokeMethod("spawnParticleCommon",
                        10, ParticleType.ASH.create(),
                        mock(Location.class), 0, 0.0, 1.0, 0.0, 0.0));
    }

    @Test
    @After1_(17)
    void testInvalidClassProvided() {
        check();
        Refl<?> refl = new Refl<>(WrappersAdapter.class);
        IllegalArgumentException e = assertThrowsExactly(IllegalArgumentException.class, () ->
                refl.invokeMethod("wParticleToGeneral", ParticleType.DUST_COLOR_TRANSITION.create(Color.RED, Color.RED, 3f),
                org.bukkit.Particle.class, (Function<?, Class<?>>) s -> org.bukkit.Particle.DustOptions.class));
        assertTrue(e.getMessage().contains("Invalid option"),
                String.format("Message \"%s\" did not contain expected message",
                        e.getMessage()));
    }

    @Test
    void testNoConstructorDataTypeProvided() {
        Refl<?> refl = new Refl<>(WrappersAdapter.class);
        assertThrowsExactly(IllegalArgumentException.class, () -> refl.invokeMethod("wParticleToGeneral",
                ParticleType.REDSTONE.create(Color.RED, 3f), org.bukkit.Particle.class,
                (Function<?, Class<?>>) s -> AbstractItem.class));
    }

    @Test
    void testInvalidParticleGeneralDataType() {
        Tuple<?, ?> conversion = new Refl<>(WrappersAdapter.class).invokeMethod("wParticleToGeneral",
                ParticleType.FLAME.create(), org.bukkit.Particle.class, (Function<?, Class<?>>) s -> null);
        assertNotNull(conversion);
        assertNotNull(conversion.getKey());
        assertNull(conversion.getValue());
    }

    @Test
    void testInvalidDataTypeButNotOption() {
        Tuple<?, ?> conversion = new Refl<>(WrappersAdapter.class).invokeMethod("wParticleToGeneral",
                ParticleType.REDSTONE.create(Color.RED, 3f), org.bukkit.Particle.class, (Function<?, Class<?>>) s -> null);
        assertNotNull(conversion);
        assertNotNull(conversion.getKey());
        assertNull(conversion.getValue());
    }

    @Test
    @Before1_(21)
    void testPlaySound() {
        check();
        Sound sound = new Sound("BLOCK_GLASS_STEP",10, 2, SoundCategory.BLOCKS.name());
        Player player = mock(Player.class);

        when(player.getLocation()).thenReturn(new Location(null, 0, 1, 0));

        WrappersAdapter.playSound(player, sound);

        ArgumentCaptor<org.bukkit.Sound> soundArg = ArgumentCaptor.forClass(org.bukkit.Sound.class);
        ArgumentCaptor<SoundCategory> categoryArg = ArgumentCaptor.forClass(SoundCategory.class);
        ArgumentCaptor<Float> volumeArg = ArgumentCaptor.forClass(Float.class);
        ArgumentCaptor<Float> pitchArg = ArgumentCaptor.forClass(Float.class);
        verify(player).playSound(any(Location.class), soundArg.capture(),
                categoryArg.capture(), volumeArg.capture(), pitchArg.capture());

        assertEquals(sound.getName(), new Refl<>(soundArg).invokeMethodRefl("getValue").invokeMethod("name"));

        assertEquals(sound.getVolume(), volumeArg.getValue());
        assertEquals(sound.getPitch(), pitchArg.getValue());

        assertEquals(sound.getCategory(), categoryArg.getValue().name());
    }

    @Test
    @Before1_(21)
    void testPlaySoundNoCategory() {
        check();
        Sound sound = new Sound("BLOCK_GLASS_STEP",10, 2);
        Player player = mock(Player.class);

        when(player.getLocation()).thenReturn(new Location(null, 0, 1, 0));

        WrappersAdapter.playSound(player, sound);

        ArgumentCaptor<org.bukkit.Sound> soundArg = ArgumentCaptor.forClass(org.bukkit.Sound.class);
        ArgumentCaptor<Float> volumeArg = ArgumentCaptor.forClass(Float.class);
        ArgumentCaptor<Float> pitchArg = ArgumentCaptor.forClass(Float.class);
        verify(player).playSound(any(Location.class), soundArg.capture(), volumeArg.capture(), pitchArg.capture());

        assertEquals(sound.getName(), new Refl<>(soundArg).invokeMethodRefl("getValue").invokeMethod("name"));

        assertEquals(sound.getVolume(), volumeArg.getValue());
        assertEquals(sound.getPitch(), pitchArg.getValue());
    }

    @Test
    @Before1_(21)
    void testPlaySoundInvalidCategory() {
        check();
        Player player = mock(Player.class);
        assertThrowsExactly(IllegalArgumentException.class, () ->
                WrappersAdapter.playSound(player, new Sound("BLOCK_ANVIL_FALL",
                        1f, 1f, "hostiles")));
    }

    @Test
    @Before1_(21)
    void testCustomPlaySound() {
        check();
        Sound sound = new Sound(
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

    @Test
    @Before1_(21)
    void testCustomPlaySoundNoCategory() {
        check();
        Sound sound = new Sound(
                "custom_sound",10, 2);
        Player player = mock(Player.class);

        when(player.getLocation()).thenReturn(new Location(null, 0, 1, 0));

        WrappersAdapter.playCustomSound(player, sound);

        ArgumentCaptor<String> soundArg = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Float> volumeArg = ArgumentCaptor.forClass(Float.class);
        ArgumentCaptor<Float> pitchArg = ArgumentCaptor.forClass(Float.class);
        verify(player).playSound(any(Location.class), soundArg.capture(), volumeArg.capture(), pitchArg.capture());

        assertEquals(sound.getName(), soundArg.getValue());

        assertEquals(sound.getVolume(), volumeArg.getValue());
        assertEquals(sound.getPitch(), pitchArg.getValue());
    }

    private static org.bukkit.potion.PotionEffect[] getPotionEffects() {
        Refl<?> potionEffectsClass = new Refl<>(org.bukkit.potion.PotionEffectType.class);
        return potionEffectsClass.getFields(f -> Modifier.isStatic(f.getModifiers()) &&
                        f.getType().isAssignableFrom(org.bukkit.potion.PotionEffectType.class)).stream()
                .map(potionEffectsClass::getFieldObject)
                .map(f -> (org.bukkit.potion.PotionEffectType) f)
                .filter(Objects::nonNull)
                .map(f -> new org.bukkit.potion.PotionEffect(f, 0, 0, true, true))
                .toArray(org.bukkit.potion.PotionEffect[]::new);
    }

    @ParameterizedTest
    @MethodSource("getPotionEffects")
    void testPotionsConversion(org.bukkit.potion.PotionEffect expected) {
        PotionEffect potionEffect = WrappersAdapter.potionEffectToWPotionEffect(expected);
        org.bukkit.potion.PotionEffect actual = WrappersAdapter.wPotionEffectToPotionEffect(potionEffect);
        assertEquals(Printable.printObject(expected, ""), Printable.printObject(actual, ""));
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("getPotionEffects")
    void testPotionsConversionNewerVersions(org.bukkit.potion.PotionEffect expected) {
        String type = expected.getType().getName().replace("_", " ");
        if (type.equals("UNLUCK")) type = "BAD LUCK";
        if (!type.contains(" ")) type += " ";
        try (MockedStatic<PotionEffectType> ignored = mockStatic(PotionEffectType.class)) {
            when(PotionEffectType.getByName(any())).thenAnswer(a -> {
                String name = a.getArgument(0);
                if (name.contains(" "))
                    throw new IllegalArgumentException("Could not find potion type with name: " + name);
                else return PotionEffectType.ABSORPTION;
            });

            PotionEffect potionEffect = new PotionEffect(type);
            Object converted = WrappersAdapter.wPotionEffectToPotionEffect(potionEffect);
            assertNotNull(converted, "Converted should have not been null");
        }
    }

    private static org.bukkit.enchantments.Enchantment[] getEnchantments() {
        Refl<?> enchantmentClass = new Refl<>(org.bukkit.enchantments.Enchantment.class);
        return enchantmentClass.getFields(f -> Modifier.isStatic(f.getModifiers()) &&
                f.getType().isAssignableFrom(org.bukkit.enchantments.Enchantment.class)).stream()
                .map(enchantmentClass::getFieldObject)
                .map(f -> (org.bukkit.enchantments.Enchantment) f)
                .toArray(org.bukkit.enchantments.Enchantment[]::new);
    }

    @ParameterizedTest
    @MethodSource("getEnchantments")
    void testEnchantmentsConversion(org.bukkit.enchantments.Enchantment expected) {
        Enchantment enchantment = WrappersAdapter.enchantToWEnchant(expected);
        assertEquals(expected, WrappersAdapter.wEnchantToEnchant(enchantment).getKey());
    }

    @Test
    void testEnchantmentConversionByName() {
        org.bukkit.enchantments.Enchantment expected = org.bukkit.enchantments.Enchantment.SILK_TOUCH;
        Enchantment enchantment = new Enchantment(expected.getName());
        assertEquals(expected, WrappersAdapter.wEnchantToEnchant(enchantment).getKey());
    }

    @Test
    void testInvalidEnchantmentProvided() {
        assertThrowsExactly(IllegalArgumentException.class, () -> WrappersAdapter.wEnchantToEnchant(new Enchantment("mock")));
    }

    private static Color[] getColors() {
        return Color.values();
    }

    @ParameterizedTest
    @MethodSource("getColors")
    void testColorConversion(Color expected) {
        org.bukkit.Color color = WrappersAdapter.wColorToColor(expected);
        assertEquals(expected, WrappersAdapter.colorToWColor(color));
    }

    @Test
    void testNullDataForMaterialData() {
        Material material = Material.LEGACY_STONE;
        Tuple<String, Integer> option = new Tuple<>(material.name(), null);
        MaterialData expected = material.getNewData((byte) 0);
        assertEquals(expected, WrappersAdapter.convertOption(MaterialData.class, option));
    }

    @Test
    void testInvalidOptionForMaterialData() {
        assertThrowsExactly(IllegalArgumentException.class, () -> WrappersAdapter.convertOption(MaterialData.class, "string"));
    }

    private static Material[] nonLegacyMaterials() {
        return Arrays.stream(Material.values()).filter(m -> !m.name().contains("LEGACY")).toArray(Material[]::new);
    }

    @ParameterizedTest
    @MethodSource("nonLegacyMaterials")
    void testAllBlockData(Material material) {
        Executable executable = () -> WrappersAdapter.convertOption(org.bukkit.block.data.BlockData.class, material.name());
        if (material.isBlock()) assertDoesNotThrow(executable);
        else assertThrowsExactly(IllegalArgumentException.class, executable);
    }

    @Test
    void testInvalidDataType() {
        assertThrowsExactly(IllegalArgumentException.class, () -> WrappersAdapter.convertOption(MockDataType.class, "String"));
    }

    @Test
    void testItemModuleNotProvided() {
        try (MockedStatic<ReflectionUtils> clazz = mockStatic(ReflectionUtils.class)) {
            clazz.when(() -> ReflectionUtils.getClass(any(String.class))).thenThrow(IllegalArgumentException.class);

            assertThrowsExactly(IllegalStateException.class, () -> WrappersAdapter.itemToItemStack(null),
                    "Should throw exception signaling missing item module");
        }
    }

    @Test
    void testGetNamespacedKeyClassNotExisting() {
        try (MockedStatic<WrappersAdapter> ignored = mockStatic(WrappersAdapter.class, CALLS_REAL_METHODS)) {
            when(new Refl<>(WrappersAdapter.class).invokeMethod("getNamespacedKeyClass"))
                    .thenThrow(ClassNotFoundException.class);

            assertThrowsExactly(IllegalStateException.class, () -> WrappersAdapter.getNamespacedKey("mock"));
        }
    }

    @SuppressWarnings("unused")
    private static class MockDataType {

        public MockDataType(String s1, String s2, String s3, String s4) {

        }

    }

}
