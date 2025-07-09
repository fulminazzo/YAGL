package it.fulminazzo.yagl.action;

import it.fulminazzo.yagl.content.GUIContent;
import it.fulminazzo.yagl.gui.GUI;
import it.fulminazzo.yagl.viewer.Viewer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class GUIItemCloseTest {

    @Test
    void testExecute() {
        Viewer viewer = mock(Viewer.class);
        new GUIItemClose().execute(viewer, mock(GUI.class), mock(GUIContent.class));

        verify(viewer).closeGUI();
    }

    @Test
    void testHashCode() {
        GUIItemClose instance = new GUIItemClose();
        assertEquals(GUIItemClose.class.hashCode(), instance.hashCode());
    }

    @Test
    void testEquals() {
        assertEquals(new GUIItemClose(), new GUIItemClose());
    }

    @Test
    void testNotEquals() {
        assertNotEquals(new GUIItemClose(), new GUIItemBack());
    }

    @Test
    void testToString() {
        assertEquals(GUIItemClose.class.getSimpleName(), new GUIItemClose().toString());
    }

}
