package it.fulminazzo.yagl.action;

import it.fulminazzo.yagl.content.GUIContent;
import it.fulminazzo.yagl.gui.GUI;
import it.fulminazzo.yagl.viewer.Viewer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GUIItemBackTest {

    @Test
    void testExecute() {
        GUI previousGUI = mock(GUI.class);
        Viewer viewer = mock(Viewer.class);
        when(viewer.getPreviousGUI()).thenReturn(previousGUI);

        new GUIItemBack().execute(viewer, mock(GUI.class), mock(GUIContent.class));

        verify(previousGUI).open(viewer);
    }

    @Test
    void testExecuteNoPreviousGUI() {
        Viewer viewer = mock(Viewer.class);
        assertDoesNotThrow(() -> new GUIItemBack().execute(viewer, mock(GUI.class), mock(GUIContent.class)));
    }

    @Test
    void testHashCode() {
        GUIItemBack instance = new GUIItemBack();
        assertEquals(GUIItemBack.class.hashCode(), instance.hashCode());
    }

    @Test
    void testEquals() {
        assertEquals(new GUIItemBack(), new GUIItemBack());
    }

    @Test
    void testNotEquals() {
        assertNotEquals(new GUIItemBack(), new GUIItemClose());
    }

    @Test
    void testToString() {
        assertEquals(GUIItemBack.class.getSimpleName(), new GUIItemBack().toString());
    }

}
