package it.angrybear.yagl.guis;


import it.angrybear.yagl.Metadatable;
import it.angrybear.yagl.actions.BiGUIAction;
import it.angrybear.yagl.actions.GUIAction;
import it.angrybear.yagl.contents.GUIContent;
import it.angrybear.yagl.contents.ItemGUIContent;
import it.angrybear.yagl.items.Item;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Represents a "chest" GUI that can be resized.
 * This GUI can start with any value in bounds and multiple of 9 and can grow if necessary.
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ResizableGUI extends DefaultGUI {

    /**
     * Instantiates a new Gui.
     *
     * @param size the size
     */
    ResizableGUI(int size) {
        super(size);
    }

    @Override
    public @NotNull ResizableGUI setContents(int slot, GUIContent @NotNull ... contents) {
        if (slot >= size() && slot < MAX_SIZE) resize((slot / 9 + 1) * 9);
        return (ResizableGUI) super.setContents(slot, contents);
    }

    @Override
    public @NotNull ResizableGUI setContents(int slot, @NotNull Collection<GUIContent> contents) {
        return (ResizableGUI) super.setContents(slot, contents);
    }

    @Override
    public @NotNull ResizableGUI addContent(GUIContent @NotNull ... contents) {
        int j = 0;
        for (int i = 0; i < contents.length; i++) {
            GUIContent content = contents[i];
            j = addSingle(content, j);
            if (j < size()) continue;
            if (size() < MAX_SIZE) {
                resize(size() + 9);
                this.contents.set(j, new Contents(content));
            } else throw new IllegalArgumentException(String.format("Could not set content at index %s because contents are already full", i));
        }
        return this;
    }

    /**
     * Resizes the current GUI to the new size.
     * Must be a multiple of 9 not higher than {@link #MAX_SIZE}.
     *
     * @param size the size
     */
    public void resize(int size) {
        checkSize(size);
        this.contents = createContents(size, this.contents);
    }

    @Override
    public @NotNull ResizableGUI setTitle(@Nullable String title) {
        return (ResizableGUI) super.setTitle(title);
    }

    @Override
    public @NotNull ResizableGUI setMovable(int slot, boolean movable) {
        return (ResizableGUI) super.setMovable(slot, movable);
    }

    @Override
    public @NotNull ResizableGUI unsetContent(int slot) {
        return (ResizableGUI) super.unsetContent(slot);
    }

    @Override
    public @NotNull ResizableGUI onClickOutside(@NotNull GUIAction action) {
        return (ResizableGUI) super.onClickOutside(action);
    }

    @Override
    public @NotNull ResizableGUI onOpenGUI(@NotNull GUIAction action) {
        return (ResizableGUI) super.onOpenGUI(action);
    }

    @Override
    public @NotNull ResizableGUI onCloseGUI(@NotNull GUIAction action) {
        return (ResizableGUI) super.onCloseGUI(action);
    }

    @Override
    public @NotNull ResizableGUI onChangeGUI(@NotNull BiGUIAction action) {
        return (ResizableGUI) super.onChangeGUI(action);
    }

    @Override
    public @NotNull ResizableGUI setAllMovable() {
        return (ResizableGUI) super.setAllMovable();
    }

    @Override
    public @NotNull ResizableGUI setAllUnmovable() {
        return (ResizableGUI) super.setAllUnmovable();
    }

    @Override
    public @NotNull ResizableGUI addContent(Item @NotNull ... contents) {
        return (ResizableGUI) super.addContent(contents);
    }

    @Override
    public @NotNull ResizableGUI addContent(ItemGUIContent @NotNull ... contents) {
        return (ResizableGUI) super.addContent(contents);
    }

    @Override
    public @NotNull ResizableGUI setContents(int slot, Item @NotNull ... contents) {
        return (ResizableGUI) super.setContents(slot, contents);
    }

    @Override
    public @NotNull ResizableGUI setContents(int slot, ItemGUIContent @NotNull ... contents) {
        return (ResizableGUI) super.setContents(slot, contents);
    }

    @Override
    public @NotNull ResizableGUI onClickOutside(@NotNull String command) {
        return (ResizableGUI) super.onClickOutside(command);
    }

    @Override
    public @NotNull ResizableGUI onOpenGUI(@NotNull String command) {
        return (ResizableGUI) super.onOpenGUI(command);
    }

    @Override
    public @NotNull ResizableGUI copyAll(@NotNull GUI other, boolean replace) {
        return (ResizableGUI) super.copyAll(other, replace);
    }

    @Override
    public @NotNull ResizableGUI copyFrom(@NotNull GUI other, boolean replace) {
        return (ResizableGUI) super.copyFrom(other, replace);
    }

    @Override
    public ResizableGUI copy() {
        return (ResizableGUI) super.copy();
    }

    @Override
    public @NotNull ResizableGUI onCloseGUI(@NotNull String command) {
        return (ResizableGUI) super.onCloseGUI(command);
    }

    @Override
    public @NotNull ResizableGUI onChangeGUI(@NotNull String command) {
        return (ResizableGUI) super.onChangeGUI(command);
    }

    @Override
    public @NotNull ResizableGUI setVariable(@NotNull String name, @NotNull String value) {
        return (ResizableGUI) super.setVariable(name, value);
    }

    @Override
    public @NotNull ResizableGUI unsetVariable(@NotNull String name) {
        return (ResizableGUI) super.unsetVariable(name);
    }

    @Override
    public @NotNull ResizableGUI copyAll(@NotNull Metadatable other, boolean replace) {
        return (ResizableGUI) super.copyAll(other, replace);
    }

    @Override
    public @NotNull ResizableGUI copyFrom(@NotNull Metadatable other, boolean replace) {
        return (ResizableGUI) super.copyFrom(other, replace);
    }
}
