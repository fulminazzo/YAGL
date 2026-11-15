package it.fulminazzo.yagl.particle;

import it.fulminazzo.yagl.Color;
import it.fulminazzo.yagl.serializer.SerializerTestHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DustTransitionParticleOptionSerializerTest extends SerializerTestHelper<DustTransitionParticleOption> {

    protected DustTransitionParticleOptionSerializerTest() {
        super(DustTransitionParticleOption.class, new PropertyParticleOptionSerializer<>(
                        m -> new DustTransitionParticleOption(m.get("from"), m.get("to"), m.get("size"))
                )
                        .addProperty("from", new Property<>(Color.class, DustTransitionParticleOption::getFrom))
                        .addProperty("to", new Property<>(Color.class, DustTransitionParticleOption::getTo))
                        .addProperty("size", new Property<>(Float.class, DustTransitionParticleOption::getSize))
        );
    }

    @Test
    void testDeserialize() throws IOException {
        final DustTransitionParticleOption actual = load("dust_transition");
        assertNotNull(actual);
        assertEquals(Color.RED, actual.getFrom());
        assertEquals(Color.BLUE, actual.getTo());
        assertEquals(1.414f, actual.getSize());
    }

    @Test
    void testSerialize() throws IOException {
        final DustTransitionParticleOption particleOption = new DustTransitionParticleOption(Color.GREEN, Color.YELLOW, 3.14f);
        Map<?, ?> raw = readRawMap(dump(particleOption, "dust_transition"));
        assertNotNull(raw);
        assertEquals(Color.GREEN.name(), raw.get("from"));
        assertEquals(Color.YELLOW.name(), raw.get("to"));
        assertEquals(3.14, raw.get("size"));
    }

}
