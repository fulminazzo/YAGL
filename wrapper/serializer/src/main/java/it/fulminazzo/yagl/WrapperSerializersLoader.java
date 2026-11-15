package it.fulminazzo.yagl;

import it.fulminazzo.yagl.item.AbstractItem;
import it.fulminazzo.yagl.particle.*;
import it.fulminazzo.yagl.serializer.SerializersLoader;
import it.fulminazzo.yagl.serializer.YaglSerializer;
import it.fulminazzo.yagl.wrapper.*;
import org.jetbrains.annotations.NotNull;

public final class WrapperSerializersLoader implements SerializersLoader {

    @SuppressWarnings("deprecation")
    @Override
    public void load(final @NotNull YaglSerializer mainSerializer) {
        mainSerializer
                .registerSerializer(Enchantment.class, new EnchantmentSerializer())
                .registerSerializer(PotionEffect.class, new PotionEffectSerializer())
                .registerSerializer(Potion.class, new PotionSerializer())
                .registerSerializer(Sound.class, new SoundSerializer())
                .registerSerializer(Color.class, new ColorSerializer())
                .registerSerializer(PrimitiveParticleOption.class, new SingleParticleOptionSerializer<>(
                        PrimitiveParticleOption.class,
                        PrimitiveParticleOption::new
                ))
                .registerSerializer(ColorParticleOption.class, new SingleParticleOptionSerializer<>(
                        ColorParticleOption.class,
                        ColorParticleOption::new
                ))
                .registerSerializer(ItemParticleOption.class, new SingleParticleOptionSerializer<>(
                        ItemParticleOption.class,
                        i -> new ItemParticleOption((AbstractItem) i)
                ))
                .registerSerializer(PotionParticleOption.class, new SingleParticleOptionSerializer<>(
                        PotionParticleOption.class,
                        PotionParticleOption::new
                ))
                .registerSerializer(DustParticleOption.class, new PropertyParticleOptionSerializer<>(
                                m -> new DustParticleOption(m.get("color"), m.get("size"))
                        )
                                .addProperty("color", new Property<>(Color.class, DustParticleOption::getColor))
                                .addProperty("size", new Property<>(Float.class, DustParticleOption::getSize))
                )
                .registerSerializer(DustTransitionParticleOption.class, new PropertyParticleOptionSerializer<>(
                                m -> new DustTransitionParticleOption(m.get("from"), m.get("to"), m.get("size"))
                        )
                                .addProperty("from", new Property<>(Color.class, DustTransitionParticleOption::getFrom))
                                .addProperty("to", new Property<>(Color.class, DustTransitionParticleOption::getTo))
                                .addProperty("size", new Property<>(Float.class, DustTransitionParticleOption::getSize))
                )
                .registerSerializer(BlockDataOption.class, new BlockDataOptionSerializer())
                .registerSerializer(MaterialDataOption.class, new MaterialDataOptionSerializer())
        ;
    }

}
