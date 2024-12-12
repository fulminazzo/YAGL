package it.fulminazzo.yagl.actions;

import it.fulminazzo.yagl.contents.GUIContent;
import it.fulminazzo.yagl.guis.GUI;
import it.fulminazzo.yagl.viewers.Viewer;
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