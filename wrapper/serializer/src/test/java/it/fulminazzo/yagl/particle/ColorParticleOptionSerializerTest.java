package it.fulminazzo.yagl.particle;

import it.fulminazzo.yagl.Color;
import it.fulminazzo.yagl.serializer.SerializerTestHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ColorParticleOptionSerializerTest extends SerializerTestHelper<ColorParticleOption> {

    protected ColorParticleOptionSerializerTest() {
        super(ColorParticleOption.class, new SingleParticleOptionSerializer<>(
                ColorParticleOption.class,
                ColorParticleOption::new
        ));
    }

    @Test
    void testDeserialize() throws IOException {
        final ColorParticleOption actual = load("color");
        assertNotNull(actual);
        assertEquals(Color.RED, actual.getOption());
    }

    @Test
    void testSerialize() throws IOException {
        final ColorParticleOption particleOption = new ColorParticleOption(Color.BLUE);
        String raw = readRawString(dump(particleOption, "color"));
        assertEquals(Color.BLUE.name(), raw);
    }

}
