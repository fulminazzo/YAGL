package it.angrybear.yagl.guis;

import it.angrybear.yagl.contents.GUIContent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class ResizableGUITest {
    private GUI gui;

    @BeforeEach
    void setUp() {
        this.gui = new ResizableGUI(9);
    }

    @Test
    void testValidAdd() {
        List<GUIContent> contents = new LinkedList<>();
        for (int i = 0; i < GUIImpl.MAX_SIZE; i++)
            contents.add(new GUIImplTest.MockContent());
        assertDoesNotThrow(() -> this.gui.addContent(contents.toArray(new GUIContent[0])));
    }

    @Test
    void testInvalidAdd() {
        List<GUIContent> contents = new LinkedList<>();
        for (int i = 0; i < GUIImpl.MAX_SIZE + 1; i++)
            contents.add(new GUIImplTest.MockContent());
        assertThrowsExactly(IllegalArgumentException.class, () ->
                this.gui.addContent(contents.toArray(new GUIContent[0])));
    }

    @Test
    void testValidSet() {
        Assertions.assertDoesNotThrow(() -> this.gui.setContents(9, new GUIImplTest.MockContent()));
    }

    @Test
    void testInvalidSet() {
        assertThrowsExactly(IndexOutOfBoundsException.class, () ->
                this.gui.setContents(GUIImpl.MAX_SIZE, new GUIImplTest.MockContent()));
    }
}