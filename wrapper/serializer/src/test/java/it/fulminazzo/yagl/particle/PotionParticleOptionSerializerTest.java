package it.fulminazzo.yagl.particle;

import it.fulminazzo.yagl.serializer.SerializerTestHelper;
import it.fulminazzo.yagl.wrapper.Potion;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PotionParticleOptionSerializerTest extends SerializerTestHelper<PotionParticleOption> {

    protected PotionParticleOptionSerializerTest() {
        super(PotionParticleOption.class, new SingleParticleOptionSerializer<>(
                PotionParticleOption.class,
                PotionParticleOption::new
        ));
    }

    @Test
    void testDeserialize() throws IOException {
        final PotionParticleOption actual = load("potion");
        assertNotNull(actual);
        assertEquals(new Potion("harming", 2), actual.getOption());
    }

    @Test
    void testSerialize() throws IOException {
        final PotionParticleOption particleOption = new PotionParticleOption(new Potion("healing"));
        String raw = readRawString(dump(particleOption, "potion"));
        assertEquals("healing", raw);
    }

}
