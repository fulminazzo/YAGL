package it.angrybear.yagl.parsers;

import it.angrybear.yagl.actions.BiGUIAction;
import it.angrybear.yagl.actions.GUIAction;
import it.angrybear.yagl.contents.GUIContent;
import it.angrybear.yagl.guis.GUI;
import it.angrybear.yagl.viewers.Viewer;
import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.YAMLParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MockGUIParser extends YAMLParser<MockGUIParser.MockGUI> {

    public MockGUIParser() {
        super(MockGUI.class);
    }

    @Override
    protected BiFunctionException<IConfiguration, String, MockGUI> getLoader() {
        return (c, s) -> new MockGUI();
    }

    @Override
    protected TriConsumer<IConfiguration, String, MockGUI> getDumper() {
        return (c, s, g) -> c.set(s, g.getTitle());
    }

    public static class MockGUI implements GUI {

        @Override
        public void open(@NotNull Viewer viewer) {

        }

        @Override
        public @NotNull GUI setTitle(@Nullable String title) {
            return null;
        }

        @Override
        public @Nullable String getTitle() {
            return "JustAMOCK";
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isMovable(int slot) {
            return false;
        }

        @Override
        public @NotNull GUI setMovable(int slot, boolean movable) {
            return null;
        }

        @Override
        public @NotNull List<GUIContent> getContents(int slot) {
            return Collections.emptyList();
        }

        @Override
        public @NotNull List<GUIContent> getContents() {
            return Collections.emptyList();
        }

        @Override
        public @NotNull GUI addContent(GUIContent @NotNull ... contents) {
            return null;
        }

        @Override
        public @NotNull GUI setContents(int slot, GUIContent @NotNull ... contents) {
            return null;
        }

        @Override
        public @NotNull GUI unsetContent(int slot) {
            return null;
        }

        @Override
        public @NotNull GUI onClickOutside(@NotNull GUIAction action) {
            return null;
        }

        @Override
        public @NotNull Optional<GUIAction> clickOutsideAction() {
            return Optional.empty();
        }

        @Override
        public @NotNull GUI onOpenGUI(@NotNull GUIAction action) {
            return null;
        }

        @Override
        public @NotNull Optional<GUIAction> openGUIAction() {
            return Optional.empty();
        }

        @Override
        public @NotNull GUI onCloseGUI(@NotNull GUIAction action) {
            return null;
        }

        @Override
        public @NotNull Optional<GUIAction> closeGUIAction() {
            return Optional.empty();
        }

        @Override
        public @NotNull GUI onChangeGUI(@NotNull BiGUIAction action) {
            return null;
        }

        @Override
        public @NotNull Optional<BiGUIAction> changeGUIAction() {
            return Optional.empty();
        }

        @Override
        public @NotNull Map<String, String> variables() {
            return Collections.emptyMap();
        }
    }
}
