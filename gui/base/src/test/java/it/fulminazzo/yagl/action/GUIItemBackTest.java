package it.fulminazzo.yagl.action;

import it.fulminazzo.yagl.content.GUIContent;
import it.fulminazzo.yagl.gui.GUI;
import it.fulminazzo.yagl.viewer.Viewer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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

}