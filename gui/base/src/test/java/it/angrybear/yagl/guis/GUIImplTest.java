package it.angrybear.yagl.guis;

import it.angrybear.yagl.actions.GUIItemAction;
import it.angrybear.yagl.contents.GUIContent;
import it.angrybear.yagl.contents.ItemGUIContent;
import it.angrybear.yagl.contents.requirements.RequirementChecker;
import it.angrybear.yagl.items.Item;
import it.angrybear.yagl.viewers.Viewer;
import it.angrybear.yagl.wrappers.Sound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class GUIImplTest {

    @ParameterizedTest
    @ValueSource(ints = {-1, 180})
    void testInvalidSize(int size) {
        assertThrowsExactly(IllegalArgumentException.class, () -> GUI.newGUI(size));
    }

    @Test
    void testCorrectAdd() {
        GUI gui = new GUIImpl(2);
        assertDoesNotThrow(() -> gui.addContent(new MockContent(), new MockContent()));
    }

    @Test
    void testInvalidAdd() {
        GUI gui = new GUIImpl(1);
        assertThrowsExactly(IllegalArgumentException.class, () ->
                gui.addContent(new MockContent(), new MockContent()));
    }

    @Test
    void testTypeGUI() {
        GUIType type = GUIType.BEACON;
        GUI gui = GUI.newGUI(type);
        assertInstanceOf(TypeGUI.class, gui);
        TypeGUI typeGUI = (TypeGUI) gui;
        assertEquals(type, typeGUI.getInventoryType());
        assertEquals(type.getSize(), typeGUI.size());
    }

    @Test
    void testGetContentPriority() {
        GUIContent c1 = ItemGUIContent.newInstance("stone").setPriority(5);
        GUIContent c2 = ItemGUIContent.newInstance("diamond").setPriority(20);
        GUI gui = GUI.newGUI(9).setContents(0, c1, c2);
        GUIContent actual = gui.getContent(mock(Viewer.class), 0);
        assertEquals(c2, actual);
    }

    @Test
    void testOpenWithNoBukkitModule() {
        assertThrowsExactly(IllegalStateException.class, () -> new GUIImpl(9).open(mock(Viewer.class)));
    }

    @Nested
    public class ContentsTest {

        @Test
        void testEquals() {
            GUIImpl.Contents c1 = new GUIImpl.Contents(
                    ItemGUIContent.newInstance("stone"),
                    ItemGUIContent.newInstance("grass")
            );
            GUIImpl.Contents c2 = new GUIImpl.Contents(
                    ItemGUIContent.newInstance("stone"),
                    ItemGUIContent.newInstance("grass")
            );
            assertEquals(c1, c2);
        }

        @Test
        void testEqualsDifferentLengths() {
            GUIImpl.Contents c1 = new GUIImpl.Contents(
                    ItemGUIContent.newInstance("stone"),
                    ItemGUIContent.newInstance("grass")
            );
            GUIImpl.Contents c2 = new GUIImpl.Contents(
                    ItemGUIContent.newInstance("stone"),
                    ItemGUIContent.newInstance("grass"),
                    ItemGUIContent.newInstance("diamond")
            );
            assertNotEquals(c1, c2);
        }

        @Test
        void testEqualsDifferentContents() {
            GUIImpl.Contents c1 = new GUIImpl.Contents(
                    ItemGUIContent.newInstance("stone"),
                    ItemGUIContent.newInstance("grass")
            );
            GUIImpl.Contents c2 = new GUIImpl.Contents(
                    ItemGUIContent.newInstance("stone"),
                    ItemGUIContent.newInstance("diamond")
            );
            assertNotEquals(c1, c2);
        }

        @Test
        void testEqualsDifferentObjects() {
            GUIImpl.Contents c1 = new GUIImpl.Contents(
                    ItemGUIContent.newInstance("stone"),
                    ItemGUIContent.newInstance("grass")
            );
            assertNotEquals(c1, ItemGUIContent.newInstance("grass"));
        }

    }
    
    public static class MockContent implements GUIContent {

        @Override
        public @NotNull Item render() {
            return null;
        }

        @Override
        public @NotNull GUIContent setPriority(int priority) {
            return null;
        }

        @Override
        public int getPriority() {
            return 0;
        }

        @Override
        public @NotNull GUIContent setClickSound(Sound sound) {
            return null;
        }

        @Override
        public @Nullable Sound getClickSound() {
            return null;
        }

        @Override
        public @NotNull GUIContent setViewRequirements(@NotNull RequirementChecker requirements) {
            return null;
        }

        @Override
        public boolean hasViewRequirements(@NotNull Viewer viewer) {
            return false;
        }

        @Override
        public @NotNull GUIContent onClickItem(@NotNull GUIItemAction action) {
            return null;
        }

        @Override
        public @NotNull Optional<GUIItemAction> clickItemAction() {
            return Optional.empty();
        }

        @Override
        public @NotNull GUIContent copy() {
            return null;
        }

        @Override
        public @NotNull Map<String, String> variables() {
            return Collections.emptyMap();
        }
    }
}