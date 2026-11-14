package it.fulminazzo.yagl;

import it.fulminazzo.yagl.serializer.SerializersLoader;
import it.fulminazzo.yagl.serializer.YaglSerializer;
import it.fulminazzo.yagl.wrapper.*;
import org.jetbrains.annotations.NotNull;

public final class WrapperSerializersLoader implements SerializersLoader {

    @Override
    public void load(final @NotNull YaglSerializer mainSerializer) {
        mainSerializer
                .registerSerializer(Enchantment.class, new EnchantmentSerializer())
                .registerSerializer(PotionEffect.class, new PotionEffectSerializer())
                .registerSerializer(Potion.class, new PotionSerializer())
                .registerSerializer(Sound.class, new SoundSerializer())
                .registerSerializer(Color.class, new ColorSerializer());
    }

}
