package it.angrybear.yagl.contents;

import it.angrybear.yagl.items.Item;
import it.angrybear.yagl.viewers.Viewer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GUIContentTest {
    private UUID uuid;
    private GUIContent guiContent;

    @BeforeEach
    void setUp() {
        this.uuid = UUID.randomUUID();
        this.guiContent = new MockGUIContent();
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
        public @NotNull GUIContent copy() {
            return new MockGUIContent();
        }

    }
}