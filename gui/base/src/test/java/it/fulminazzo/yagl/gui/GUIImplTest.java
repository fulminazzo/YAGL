package it.fulminazzo.yagl.gui;

import it.fulminazzo.yagl.action.GUIItemAction;
import it.fulminazzo.yagl.content.GUIContent;
import it.fulminazzo.yagl.content.ItemGUIContent;
import it.fulminazzo.yagl.content.requirement.RequirementChecker;
import it.fulminazzo.yagl.item.Item;
import it.fulminazzo.yagl.reflect.Reflect;
import it.fulminazzo.yagl.viewer.Viewer;
import it.fulminazzo.yagl.wrapper.Sound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
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
    @ValueSource(ints = {-1, 180, 17})
    void testInvalidSize(int size) {
        assertThrowsExactly(IllegalArgumentException.class, () -> GUI.newGUI(size));
    }

    @Test
    void testNotEmpty() {
        assertFalse(GUI.newGUI(9).setContents(0, Item.newItem()).isEmpty());
    }

    @Test
    void testEmpty() {
        assertTrue(GUI.newGUI(9).isEmpty());
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

    @Test
    void testDefaultGUICopy() {
        GUI gui = GUI.newGUI(27)
                .setTitle("Test Title")
                .setVariable("key1", "value1")
                .setVariable("key2", "value2")
                .setContents(0, Item.newItem("stone"))
                .setContents(4, Item.newItem("diamond"))
                .setContents(8, Item.newItem("emerald"))
                .setMovable(0, true)
                .setMovable(4, true)
                .onOpenGUI("openCommand")
                .onCloseGUI("closeCommand")
                .onClickOutside("clickOutsideCommand")
                .onChangeGUI("changeCommand");

        GUI copy = gui.copy();

        assertInstanceOf(DefaultGUI.class, copy);
        assertNotSame(gui, copy);
        assertEquals(gui, copy);
        assertEquals(gui.size(), copy.size());
        assertEquals(gui.getTitle(), copy.getTitle());
        assertEquals(gui.variables(), copy.variables());
        assertEquals(gui.getContents(0), copy.getContents(0));
        assertEquals(gui.getContents(4), copy.getContents(4));
        assertEquals(gui.getContents(8), copy.getContents(8));
        assertEquals(gui.isMovable(0), copy.isMovable(0));
        assertEquals(gui.isMovable(4), copy.isMovable(4));
        assertEquals(gui.isMovable(1), copy.isMovable(1));
        assertEquals(gui.openGUIAction().isPresent(), copy.openGUIAction().isPresent());
        assertEquals(gui.closeGUIAction().isPresent(), copy.closeGUIAction().isPresent());
        assertEquals(gui.clickOutsideAction().isPresent(), copy.clickOutsideAction().isPresent());
        assertEquals(gui.changeGUIAction().isPresent(), copy.changeGUIAction().isPresent());
    }

    @Test
    void testTypeGUICopy() {
        GUI gui = GUI.newGUI(GUIType.CHEST)
                .setTitle("Chest GUI")
                .setVariable("type", "chest")
                .setContents(0, Item.newItem("gold_ingot"))
                .setContents(13, Item.newItem("iron_ingot"))
                .setMovable(0, true)
                .onOpenGUI("onOpen")
                .onCloseGUI("onClose")
                .onClickOutside("onClickOutside")
                .onChangeGUI("onChange");

        GUI copy = gui.copy();

        assertInstanceOf(TypeGUI.class, copy);
        assertEquals(((TypeGUI) gui).getInventoryType(), ((TypeGUI) copy).getInventoryType());
        assertEquals(gui.size(), copy.size());
        assertEquals(gui.getTitle(), copy.getTitle());
        assertEquals(gui.variables(), copy.variables());
        assertEquals(gui.getContents(0), copy.getContents(0));
        assertEquals(gui.getContents(13), copy.getContents(13));
        assertEquals(gui.isMovable(0), copy.isMovable(0));
        assertEquals(gui.isMovable(13), copy.isMovable(13));
        assertEquals(gui.openGUIAction().isPresent(), copy.openGUIAction().isPresent());
        assertEquals(gui.closeGUIAction().isPresent(), copy.closeGUIAction().isPresent());
        assertEquals(gui.clickOutsideAction().isPresent(), copy.clickOutsideAction().isPresent());
        assertEquals(gui.changeGUIAction().isPresent(), copy.changeGUIAction().isPresent());
        assertNotSame(gui, copy);
        assertEquals(gui, copy);
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
        assertThrowsExactly(IllegalStateException.class, () -> new MockGUI(9).open(mock(Viewer.class)));
    }

    @Nested
    public class CopyTest {
        private GUI expected;

        @BeforeEach
        void setUp() {
            this.expected = GUI.newGUI(9)
                    .setTitle("Hello")
                    .setVariable("hello", "world")
                    .setContents(0, Item.newItem("stone"))
                    .addContent(Item.newItem("grass"));
        }

        @Test
        void testCopyOfDifferentSize() {
            assertThrowsExactly(IllegalArgumentException.class, () ->
                    this.expected.copyFrom(GUI.newGUI(18), true));
        }

        @Test
        void testCopyNoReplace() {
            GUI actual = GUI.newGUI(this.expected.size()).copyFrom(this.expected, false);
            assertEquals(this.expected, actual);
        }

        @Test
        void testCopyNoReplaceTitleAndContents() {
            GUI actual = GUI.newGUI(this.expected.size())
                    .setTitle("Hi")
                    .setContents(0, Item.newItem("diamond"))
                    .copyFrom(this.expected, false);
            assertNotEquals(this.expected.getTitle(), actual.getTitle());
            assertNotEquals(this.expected.getContents(0), actual.getContents(0));
            actual.setTitle(this.expected.getTitle())
                    .setContents(0, this.expected.getContents(0));
            assertEquals(this.expected, actual);
        }

        @Test
        void testCopyReplaceTitleAndContents() {
            GUI actual = GUI.newGUI(this.expected.size())
                    .setTitle("Hi")
                    .setContents(0, Item.newItem("diamond"))
                    .copyFrom(this.expected, true);
            assertEquals(this.expected, actual);
        }

        @Test
        void testCopyActionSourceNoActionDestinationNoReplace() {
            this.expected.onOpenGUI("command")
                    .onCloseGUI("command")
                    .onChangeGUI("command")
                    .onClickOutside("command");
            GUI actual = GUI.newGUI(this.expected.size())
                    .copyFrom(this.expected, false);
            assertEquals(this.expected, actual);
        }

        @Test
        void testCopyActionSourceActionDestinationNoReplace() {
            this.expected.onOpenGUI("command")
                    .onCloseGUI("command")
                    .onChangeGUI("command")
                    .onClickOutside("command");
            GUI actual = GUI.newGUI(this.expected.size())
                    .onOpenGUI("command1")
                    .onCloseGUI("command1")
                    .onChangeGUI("command1")
                    .onClickOutside("command1")
                    .copyFrom(this.expected, false);
            assertNotEquals(this.expected.openGUIAction().get(), actual.openGUIAction().get());
            assertNotEquals(this.expected.closeGUIAction().get(), actual.closeGUIAction().get());
            assertNotEquals(this.expected.changeGUIAction().get(), actual.changeGUIAction().get());
            assertNotEquals(this.expected.clickOutsideAction().get(), actual.clickOutsideAction().get());
        }

        @Test
        void testCopyActionSourceActionDestinationReplace() {
            this.expected.onOpenGUI("command")
                    .onCloseGUI("command")
                    .onChangeGUI("command")
                    .onClickOutside("command");
            GUI actual = GUI.newGUI(this.expected.size())
                    .onOpenGUI("command1")
                    .onCloseGUI("command1")
                    .onChangeGUI("command1")
                    .onClickOutside("command1")
                    .copyFrom(this.expected, true);
            assertEquals(this.expected, actual);
        }

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
            assertEquals(c1.hashCode(), c2.hashCode());
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
            assertNotEquals(c1.hashCode(), c2.hashCode());
        }

        @Test
        void testEqualsDifferentContents() {
            GUIImpl.Contents c1 = new GUIImpl.Contents(
                    ItemGUIContent.newInstance("stone"),
                    ItemGUIContent.newInstance("grass")
            );
            GUIImpl.Contents c2 = new GUIImpl.Contents(
                    null,
                    ItemGUIContent.newInstance("diamond")
            );
            assertNotEquals(c1, c2);
            assertNotEquals(c1.hashCode(), c2.hashCode());
        }

        @Test
        void testEqualsDifferentObjects() {
            GUIImpl.Contents c1 = new GUIImpl.Contents(
                    ItemGUIContent.newInstance("stone"),
                    ItemGUIContent.newInstance("grass")
            );
            assertNotEquals(c1, ItemGUIContent.newInstance("grass"));
        }

        @Test
        void testCopy() {
            GUIImpl.Contents c1 = new GUIImpl.Contents(
                    ItemGUIContent.newInstance("stone"),
                    ItemGUIContent.newInstance("grass"),
                    null
            );
            GUIImpl.Contents c2 = c1.copy();
            assertEquals(c1.getContents(), c2.getContents());
            Object cs1 = Reflect.on(c1).get("contents").get();
            Object cs2 = Reflect.on(c2).get("contents").get();
            assertNotEquals(cs1.hashCode(), cs2.hashCode());
        }

    }

    public static class MockGUI extends GUIImpl {

        public MockGUI(int size) {
            super(size);
        }

        @Override
        protected @NotNull GUI initializeNewInstance() {
            return new MockGUI(size());
        }

        @Override
        public int rows() {
            return 0;
        }

        @Override
        public int columns() {
            return 0;
        }

    }

    public static class MockContent implements GUIContent {

        @Override
        public @NotNull Item render() {
            return null;
        }

        @Override
        public @NotNull GUIContent setPriority(final int priority) {
            return null;
        }

        @Override
        public int getPriority() {
            return 0;
        }

        @Override
        public @NotNull GUIContent setClickSound(final Sound sound) {
            return null;
        }

        @Override
        public @Nullable Sound getClickSound() {
            return null;
        }

        @Override
        public @NotNull GUIContent setViewRequirements(final @NotNull RequirementChecker requirements) {
            return null;
        }

        @Override
        public boolean hasViewRequirements(final @NotNull Viewer viewer) {
            return false;
        }

        @Override
        public @NotNull GUIContent onClickItem(final @NotNull GUIItemAction action) {
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
