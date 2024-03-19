package it.angrybear.yagl.guis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResizableGUITest {
    private GUI gui;

    @BeforeEach
    void setUp() {
        this.gui = new ResizableGUI(9);
    }

    @Test
    void testValidSet() {
        assertDoesNotThrow(() -> this.gui.setContent(9, new GUIImplTest.MockContent()));
    }

    @Test
    void testInvalidSet() {
        assertThrowsExactly(IndexOutOfBoundsException.class, () ->
                this.gui.setContent(GUIImpl.MAX_SIZE, new GUIImplTest.MockContent()));
    }
}