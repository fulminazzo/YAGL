package it.angrybear.yagl.guis;

import it.angrybear.yagl.actions.GUIItemAction;
import it.angrybear.yagl.contents.GUIContent;
import it.angrybear.yagl.contents.requirements.RequirementChecker;
import it.angrybear.yagl.items.Item;
import it.angrybear.yagl.viewers.Viewer;
import it.angrybear.yagl.wrappers.Sound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GUIImplTest {

    @ParameterizedTest
    @ValueSource(ints = {-1, 180})
    void testInvalidSize(int size) {
        assertThrowsExactly(IllegalArgumentException.class, () -> GUI.newGUI(size));
    }

    @Test
    void testCorrectAdd() {
        GUI gui = new MockGUI(2);
        assertDoesNotThrow(() -> gui.addContent(new MockContent(), new MockContent()));
    }

    @Test
    void testInvalidAdd() {
        GUI gui = new MockGUI(1);
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

    public static class MockGUI extends GUIImpl {

        /**
         * Instantiates a new Gui.
         *
         * @param size the size
         */
        public MockGUI(int size) {
            super(size);
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