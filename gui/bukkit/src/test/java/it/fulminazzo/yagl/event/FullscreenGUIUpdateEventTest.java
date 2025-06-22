package it.fulminazzo.yagl.event;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FullscreenGUIUpdateEventTest {

    @Test
    void testGetHandlersFunctions() {
        FullscreenGUIUpdateEvent updateEvent = new FullscreenGUIUpdateEvent(null);

        assertEquals(FullscreenGUIUpdateEvent.getHandlerList(), updateEvent.getHandlers());
    }

}