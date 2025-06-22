package it.fulminazzo.yagl.action;

import it.fulminazzo.yagl.content.GUIContent;
import it.fulminazzo.yagl.gui.GUI;
import it.fulminazzo.yagl.viewer.Viewer;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class GUIItemCloseTest {

    @Test
    void testExecute() {
        Viewer viewer = mock(Viewer.class);
        new GUIItemClose().execute(viewer, mock(GUI.class), mock(GUIContent.class));

        verify(viewer).closeGUI();
    }

}
