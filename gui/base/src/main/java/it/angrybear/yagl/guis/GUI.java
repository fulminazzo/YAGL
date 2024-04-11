package it.angrybear.yagl.guis;

import it.angrybear.yagl.Metadatable;
import it.angrybear.yagl.actions.BiGUIAction;
import it.angrybear.yagl.actions.BiGUICommand;
import it.angrybear.yagl.actions.GUIAction;
import it.angrybear.yagl.actions.GUICommand;
import it.angrybear.yagl.contents.GUIContent;
import it.angrybear.yagl.contents.ItemGUIContent;
import it.angrybear.yagl.items.Item;
import it.angrybear.yagl.viewers.Viewer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * The general interface to represent a GUI.
 */
public interface GUI extends Metadatable {

    /**
     * Opens the current GUI for the given {@link Viewer}.
     *
     * @param viewer the viewer
     */
    void open(final @NotNull Viewer viewer);

    /**
     * Sets title.
     *
     * @param title the title
     * @return this gui
     */
    @NotNull GUI setTitle(final @Nullable String title);

    /**
     * Gets title.
     *
     * @return the title
     */
    @Nullable String getTitle();

    /**
     * Gets size.
     *
     * @return the size
     */
    int size();

    /**
     * Checks if the content at the given slot is movable.
     *
     * @param slot the slot
     * @return true if it is
     */
    boolean isMovable(int slot);

    /**
     * Sets all movable.
     *
     * @return this gui
     */
    default @NotNull GUI setAllMovable() {
        for (int i = 0; i < size(); i++) setMovable(i, true);
        return this;
    }

    /**
     * Sets all unmovable.
     *
     * @return this gui
     */
    default @NotNull GUI setAllUnmovable() {
        for (int i = 0; i < size(); i++) setMovable(i, false);
        return this;
    }

    /**
     * Sets the given slot movable.
     *
     * @param slot    the slot
     * @param movable true if it should be movable
     * @return this gui
     */
    @NotNull GUI setMovable(int slot, boolean movable);

    /**
     * Gets the most matching content at the given slot.
     * The contents are filtered using {@link GUIContent#hasViewRequirements(Viewer)}
     * and for those remaining, the one with higher {@link GUIContent#getPriority()} is returned.
     *
     * @param viewer the viewer
     * @param slot   the slot
     * @return the content
     */
    default @Nullable GUIContent getContent(final @NotNull Viewer viewer, int slot) {
        return getContents(slot).stream()
                .filter(c -> c.hasViewRequirements(viewer))
                .min(Comparator.comparing(c -> -c.getPriority()))
                .orElse(null);
    }

    /**
     * Gets a copy of the contents at the given slot.
     *
     * @param slot the slot
     * @return the contents
     */
    @NotNull List<GUIContent> getContents(int slot);

    /**
     * Gets a copy of all the contents.
     * To get the actual content, use the GUI in a for-enhanced loop or use {@link #iterator()}.
     *
     * @return the contents
     */
    @NotNull List<GUIContent> getContents();

    /**
     * Tries to add all the contents in the GUI.
     * If it fails (because of empty GUI) it throws an {@link IllegalArgumentException}.
     *
     * @param contents the contents
     * @return the gui
     */
    default @NotNull GUI addContent(final Item @NotNull ... contents) {
        return addContent(Arrays.stream(contents).map(ItemGUIContent::new).toArray(GUIContent[]::new));
    }

    /**
     * Tries to add all the contents in the GUI.
     * If it fails (because of empty GUI) it throws an {@link IllegalArgumentException}.
     *
     * @param contents the contents
     * @return this gui
     */
    @NotNull GUI addContent(final GUIContent @NotNull ... contents);

    /**
     * Sets the given contents at the specified index.
     * These will be then filtered using {@link #getContent(Viewer, int)}
     *
     * @param slot     the slot
     * @param contents the contents
     * @return the contents
     */
    default @NotNull GUI setContents(int slot, final Item @NotNull ... contents) {
        return setContents(slot, Arrays.stream(contents).map(ItemGUIContent::new).toArray(GUIContent[]::new));
    }

    /**
     * Sets the given contents at the specified index.
     * These will be then filtered using {@link #getContent(Viewer, int)}
     *
     * @param slot     the slot
     * @param contents the contents
     * @return this gui
     */
    @NotNull GUI setContents(int slot, final GUIContent @NotNull ... contents);

    /**
     * Removes the content from the given index.
     *
     * @param slot the slot
     * @return this gui
     */
    @NotNull GUI unsetContent(int slot);

    /**
     * Forces the {@link Viewer} to execute the given command when clicking outside the GUI (will not include player's inventory slots).
     *
     * @param command the command
     * @return this gui
     */
    default @NotNull GUI onClickOutside(final @NotNull String command) {
        return onClickOutside(new GUICommand(command));
    }

    /**
     * Executes the given action when clicking outside the GUI (will not include player's inventory slots).
     *
     * @param action the action
     * @return this gui
     */
    @NotNull GUI onClickOutside(final @NotNull GUIAction action);

    /**
     * Click outside action.
     *
     * @return the action
     */
    @NotNull Optional<GUIAction> clickOutsideAction();

    /**
     * Forces the {@link Viewer} to execute the given command when opening this GUI.
     *
     * @param command the command
     * @return this gui
     */
    default @NotNull GUI onOpenGUI(final @NotNull String command) {
        return onOpenGUI(new GUICommand(command));
    }

    /**
     * Executes the given action when opening this GUI.
     *
     * @param action the action
     * @return this gui
     */
    @NotNull GUI onOpenGUI(final @NotNull GUIAction action);

    /**
     * Open gui action.
     *
     * @return the action
     */
    @NotNull Optional<GUIAction> openGUIAction();

    /**
     * Forces the {@link Viewer} to execute the given command when closing this GUI.
     * This will NOT be called when an action is passed to {@link #onChangeGUI(BiGUIAction)}
     * and another GUI is open.
     *
     * @param command the command
     * @return this gui
     */
    default @NotNull GUI onCloseGUI(final @NotNull String command) {
        return onCloseGUI(new GUICommand(command));
    }

    /**
     * Executes the given action when closing this GUI.
     * This will NOT be called when an action is passed to {@link #onChangeGUI(BiGUIAction)}
     * and another GUI is open.
     *
     * @param action the action
     * @return this gui
     */
    @NotNull GUI onCloseGUI(final @NotNull GUIAction action);

    /**
     * Close gui action.
     *
     * @return the action
     */
    @NotNull Optional<GUIAction> closeGUIAction();

    /**
     * Forces the {@link Viewer} to execute the given command when opening another GUI while having this one already open.
     * This will NOT call the action passed {@link #onCloseGUI(GUIAction)}.
     *
     * @param command the command
     * @return this gui
     */
    default @NotNull GUI onChangeGUI(final @NotNull String command) {
        return onChangeGUI(new BiGUICommand(command));
    }

    /**
     * Executes the given action when opening another GUI while having this one already open.
     * This will NOT call the action passed {@link #onCloseGUI(GUIAction)}.
     *
     * @param action the action
     * @return this gui
     */
    @NotNull GUI onChangeGUI(final @NotNull BiGUIAction action);

    /**
     * Change gui action.
     *
     * @return the action
     */
    @NotNull Optional<BiGUIAction> changeGUIAction();

    @Override
    @NotNull
    default GUI setVariable(final @NotNull String name, final @NotNull String value) {
        return (GUI) Metadatable.super.setVariable(name, value);
    }

    @Override
    @NotNull
    default GUI unsetVariable(final @NotNull String name) {
        return (GUI) Metadatable.super.unsetVariable(name);
    }

    @Override
    @NotNull
    default GUI copyAll(final @NotNull Metadatable other, final boolean replace) {
        return (GUI) Metadatable.super.copyAll(other, replace);
    }

    @Override
    @NotNull
    default GUI copyFrom(final @NotNull Metadatable other, final boolean replace) {
        return (GUI) Metadatable.super.copyFrom(other, replace);
    }

    /**
     * Creates a new {@link ResizableGUI}.
     *
     * @param size the size
     * @return the gui
     */
    static GUI newGUI(final int size) {
        return new ResizableGUI(size);
    }

    /**
     * Creates a new {@link TypeGUI}.
     *
     * @param type the type
     * @return the gui
     */
    static GUI newGUI(final GUIType type) {
        return new TypeGUI(type);
    }
}
