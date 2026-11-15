package it.fulminazzo.yagl.particle;

import it.fulminazzo.yagl.Color;
import it.fulminazzo.yagl.serializer.SerializerTestHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DustParticleOptionSerializerTest extends SerializerTestHelper<DustParticleOption> {

    protected DustParticleOptionSerializerTest() {
        super(DustParticleOption.class, new PropertyParticleOptionSerializer<>(m ->
                new DustParticleOption(m.get("color"), m.get("size"))
                )
                .addProperty("color", new Property<>(Color.class, DustParticleOption::getColor))
                .addProperty("size", new Property<>(Float.class, DustParticleOption::getSize))
        );
    }

    @Test
    void testDeserialize() throws IOException {
        final DustParticleOption actual = load("dust");
        assertNotNull(actual);
        assertEquals(Color.RED, actual.getColor());
        assertEquals(1.414f, actual.getSize());
    }

    @Test
    void testSerialize() throws IOException {
        final DustParticleOption particleOption = new DustParticleOption(Color.BLUE, 3.14f);
        Map<?, ?> raw = readRawMap(dump(particleOption, "dust"));
        assertNotNull(raw);
        assertEquals(Color.BLUE.name(), raw.get("color"));
        assertEquals(3.14, raw.get("size"));
    }

}
