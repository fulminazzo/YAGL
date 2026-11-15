package it.fulminazzo.yagl;

import it.fulminazzo.yagl.particle.*;
import it.fulminazzo.yagl.serializer.SerializationException;
import it.fulminazzo.yagl.serializer.YaglSerializer;
import it.fulminazzo.yagl.wrapper.Enchantment;
import it.fulminazzo.yagl.wrapper.Potion;
import it.fulminazzo.yagl.wrapper.PotionEffect;
import it.fulminazzo.yagl.wrapper.Sound;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class WrapperSerializersLoaderTest {

    private static Class<?>[] testTypes() {
        return new Class<?>[]{
                Enchantment.class,
                PotionEffect.class,
                Potion.class,
                Sound.class,
                Color.class,
                PrimitiveParticleOption.class,
                ColorParticleOption.class,
                ItemParticleOption.class,
                PotionParticleOption.class,
                DustParticleOption.class,
                DustTransitionParticleOption.class
        };
    }

    @ParameterizedTest
    @MethodSource("testTypes")
    void testThatSerializerIsRegisteredFor(final @NotNull Class<?> type) throws SerializationException {
        assertNotNull(YaglSerializer.getInstance().getSerializer(type));
    }

}