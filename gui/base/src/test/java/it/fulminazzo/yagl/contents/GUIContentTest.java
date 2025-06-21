package it.fulminazzo.yagl.contents;

import it.fulminazzo.yagl.guis.GUI;
import it.fulminazzo.yagl.items.Item;
import it.fulminazzo.yagl.viewers.Viewer;
import it.fulminazzo.yagl.wrappers.Sound;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GUIContentTest {
    private UUID uuid;
    private GUIContent guiContent;

    @BeforeEach
    void setUp() {
        this.uuid = UUID.randomUUID();
        this.guiContent = new MockGUIContent();
    }

    @Test
    void testViewRequirementsNull() {
        Viewer viewer = getViewer("Steve");
        assertTrue(this.guiContent.hasViewRequirements(viewer));
    }

    @Test
    void testViewRequirementsPredicate() {
        Viewer viewer = getViewer("Steve");
        this.guiContent.setViewRequirements(v -> v.getName().equals("Alex"));
        assertFalse(this.guiContent.hasViewRequirements(viewer));
    }

    @Test
    void testViewRequirements() {
        Viewer viewer = getViewer("Steve");
        when(viewer.hasPermission(any())).thenReturn(true);
        this.guiContent.setViewRequirements("permission");
        assertTrue(this.guiContent.hasViewRequirements(viewer));
    }

    private static Sound[] sounds() {
        return new Sound[]{null, new Sound("test", 1, 3)};
    }

    @ParameterizedTest
    @MethodSource("sounds")
    void testClickSound(Sound expected) {
        this.guiContent.setClickSound(expected);
        assertEquals(expected, this.guiContent.getClickSound());

        Viewer viewer = getViewer("Steve");
        this.guiContent.clickItemAction().ifPresent(a -> a.execute(viewer, mock(GUI.class), this.guiContent));

        ArgumentCaptor<Sound> capture = ArgumentCaptor.forClass(Sound.class);
        verify(viewer, times(expected == null ? 0 : 1)).playSound(capture.capture());
        if (expected == null) return;

        Sound actual = capture.getValue();
        assertEquals(expected, actual);
    }

    private Viewer getViewer(final String name) {
        Viewer viewer = mock(Viewer.class);
        when(viewer.getUniqueId()).thenReturn(this.uuid);
        when(viewer.getName()).thenReturn(name);
        return viewer;
    }

    private static class MockGUIContent extends GUIContentImpl {

        @Override
        protected @NotNull Item internalRender() {
            return Item.newItem();
        }

        @Override
        protected @NotNull GUIContentImpl internalCopy() {
            return new MockGUIContent();
        }

    }
}