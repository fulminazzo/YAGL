package it.fulminazzo.yagl.gui;

import it.fulminazzo.yagl.TestUtils;
import it.fulminazzo.yagl.content.GUIContent;
import it.fulminazzo.yagl.content.ItemGUIContent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResizableGUITest {
    private GUI gui;

    @BeforeEach
    void setUp() {
        this.gui = new ResizableGUI(9);
    }

    @Test
    void testCopy() {
        ResizableGUI gui = new ResizableGUI(18)
                .setTitle("Resizable")
                .setVariable("hello", "world")
                .setContents(0, ItemGUIContent.newInstance("stone"))
                .setContents(9, ItemGUIContent.newInstance("diamond"))
                .setContents(17, ItemGUIContent.newInstance("emerald"))
                .setMovable(0, true)
                .setMovable(9, true)
                .onOpenGUI("openCmd")
                .onCloseGUI("closeCmd")
                .onClickOutside("clickOutsideCmd")
                .onChangeGUI("changeCmd");

        GUI copy = gui.copy();

        assertInstanceOf(ResizableGUI.class, copy);
        assertEquals(gui.size(), copy.size());
        assertEquals(gui.getTitle(), copy.getTitle());
        assertEquals(gui.variables(), copy.variables());
        assertEquals(gui.getContents(0), copy.getContents(0));
        assertEquals(gui.getContents(9), copy.getContents(9));
        assertEquals(gui.getContents(17), copy.getContents(17));
        assertEquals(gui.isMovable(0), copy.isMovable(0));
        assertEquals(gui.isMovable(9), copy.isMovable(9));
        assertEquals(gui.isMovable(1), copy.isMovable(1));
        assertEquals(gui.openGUIAction().isPresent(), copy.openGUIAction().isPresent());
        assertEquals(gui.closeGUIAction().isPresent(), copy.closeGUIAction().isPresent());
        assertEquals(gui.clickOutsideAction().isPresent(), copy.clickOutsideAction().isPresent());
        assertEquals(gui.changeGUIAction().isPresent(), copy.changeGUIAction().isPresent());
        assertNotSame(gui, copy);
        assertEquals(gui, copy);
    }

    @Test
    void testValidAdd() {
        List<GUIContent> contents = new LinkedList<>();
        for (int i = 0; i < Constants.MAX_SIZE; i++)
            contents.add(new GUIImplTest.MockContent());
        assertDoesNotThrow(() -> this.gui.addContent(contents.toArray(new GUIContent[0])));
    }

    @Test
    void testInvalidAdd() {
        List<GUIContent> contents = new LinkedList<>();
        for (int i = 0; i < Constants.MAX_SIZE + 1; i++)
            contents.add(new GUIImplTest.MockContent());
        assertThrowsExactly(IllegalArgumentException.class, () ->
                this.gui.addContent(contents.toArray(new GUIContent[0])));
    }

    @ParameterizedTest
    @ValueSource(ints = {-10, 180, 7})
    void testInvalidResize(int size) {
        assertThrowsExactly(IllegalArgumentException.class, () -> ((ResizableGUI) this.gui).resize(size));
    }

    @Test
    void testReturnTypes() {
        TestUtils.testReturnType(new ResizableGUI(45), GUI.class, m -> m.getName().equals("copy"));
    }

    @Test
    void testValidSet() {
        assertDoesNotThrow(() -> this.gui.setContents(9, new GUIImplTest.MockContent()));
    }

    @Test
    void testInvalidSet() {
        assertThrowsExactly(IllegalArgumentException.class, () -> this.gui.setContents(Constants.MAX_SIZE, new GUIImplTest.MockContent()));
    }

}
