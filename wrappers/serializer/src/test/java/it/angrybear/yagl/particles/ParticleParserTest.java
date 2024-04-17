package it.angrybear.yagl.particles;

import it.angrybear.yagl.Color;
import it.angrybear.yagl.ParserTestHelper;
import it.angrybear.yagl.parsers.WrappersYAGLParser;
import it.angrybear.yagl.wrappers.Potion;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import it.fulminazzo.yamlparser.configuration.ConfigurationSection;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;
import it.fulminazzo.yamlparser.utils.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ParticleParserTest extends ParserTestHelper<Particle> {

    private static AParticleType<?>[] getTestOptions() {
        return Stream.concat(Arrays.stream(ParticleType.values()), Arrays.stream(LegacyParticleType.values()))
                .toArray(AParticleType[]::new);
    }

    @SuppressWarnings("unchecked")
    @ParameterizedTest
    @MethodSource("getTestOptions")
    void testTypes(AParticleType<?> type) throws IOException {
        Particle expected = type.create();
        if (type.equals(ParticleType.BLOCK_CRACK) || type.equals(ParticleType.BLOCK_DUST) || type.equals(ParticleType.FALLING_DUST))
            expected = ((ParticleType<BlockDataOption>) type).create(new BlockDataOption("oak_log[axis=y]"));
        if (type.equals(ParticleType.SHRIEK))
            expected = ParticleType.SHRIEK.create(new PrimitiveParticleOption<>(3));
        if (type.equals(ParticleType.SCULK_CHARGE))
            expected = ParticleType.SCULK_CHARGE.create(new PrimitiveParticleOption<>(2.0f));
        if (type.equals(ParticleType.REDSTONE))
            expected = ParticleType.REDSTONE.create(new DustParticleOption(Color.RED, 4.0f));
        if (type.equals(ParticleType.DUST_COLOR_TRANSITION))
            expected = ParticleType.DUST_COLOR_TRANSITION.create(
                    new DustTransitionParticleOption(Color.WHITE, Color.BLACK, 5.0f));
        if (type.equals(LegacyParticleType.SMOKE))
            expected = LegacyParticleType.SMOKE.create("NORTH");
        if (type.equals(LegacyParticleType.POTION_BREAK))
            expected = LegacyParticleType.POTION_BREAK.create(new Potion("strength", 2));
        if (type.equals(LegacyParticleType.VILLAGER_PLANT_GROW))
            expected = LegacyParticleType.VILLAGER_PLANT_GROW.create(10);
        if (type.equals(LegacyParticleType.ITEM_BREAK))
            expected = LegacyParticleType.ITEM_BREAK.create("DIAMOND_SWORD");
        if (type.equals(LegacyParticleType.TILE_BREAK))
            expected = LegacyParticleType.TILE_BREAK.create(new MaterialDataOption("stone", 1));
        if (type.equals(LegacyParticleType.TILE_DUST))
            expected = LegacyParticleType.TILE_DUST.create(new MaterialDataOption("stone", 1));
        if (type.equals(LegacyParticleType.COMPOSTER_FILL_ATTEMPT))
            expected = LegacyParticleType.COMPOSTER_FILL_ATTEMPT.create(true);
        if (type.equals(LegacyParticleType.BONE_MEAL_USE))
            expected = LegacyParticleType.BONE_MEAL_USE.create(7);
        if (type.equals(LegacyParticleType.ELECTRIC_SPARK))
            expected = LegacyParticleType.ELECTRIC_SPARK.create("X");
        if (type.equals(LegacyParticleType.INSTANT_POTION_BREAK))
            expected = LegacyParticleType.INSTANT_POTION_BREAK.create(Color.PURPLE);

        if (FileConfiguration.getParser(Particle.class) == null)
            WrappersYAGLParser.addAllParsers();

        File file = new File("build/resources/test/particles.yml");
        if (!file.exists()) FileUtils.createNewFile(file);

        FileConfiguration configuration = new FileConfiguration(file);
        configuration.set(expected.getType(), expected);
        configuration.save();

        configuration = new FileConfiguration(file);
        Particle actual = configuration.get(expected.getType(), Particle.class);

        Field[] fields = expected.getClass().getDeclaredFields();
        for (Field field : fields) {
            Object obj1 = ReflectionUtils.getOrThrow(field, expected);
            Object obj2 = ReflectionUtils.getOrThrow(field, actual);
            if (obj1 == null) assertNull(obj2);
            else {
                assertEquals(obj1.getClass(), obj2.getClass());
                assertEquals(obj1, obj2);
            }
        }
    }

    @Test
    void testNullType() {
        WrappersYAGLParser.addAllParsers();
        ConfigurationSection particleSection = new ConfigurationSection(null, "main");
        particleSection.set("particle.type", null);

        assertThrowsExactly(IllegalArgumentException.class, () ->
                particleSection.get("particle", Particle.class));
    }

    @Override
    protected Class<?> getParser() {
        return ParticleParser.class;
    }
}