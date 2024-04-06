package it.angrybear.yagl.guis;

import it.angrybear.yagl.actions.GUIItemAction;
import it.angrybear.yagl.contents.GUIContent;
import it.angrybear.yagl.contents.RequirementChecker;
import it.angrybear.yagl.guis.GUI;
import it.angrybear.yagl.guis.GUIImpl;
import it.angrybear.yagl.items.Item;
import it.angrybear.yagl.viewers.Viewer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class GUIImplTest {

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
        public @NotNull GUIContent setClickSound(String rawSound) {
            return null;
        }

        @Override
        public @Nullable String getClickSound() {
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
        public @NotNull GUIContent copyContent() {
            return null;
        }
    }
}