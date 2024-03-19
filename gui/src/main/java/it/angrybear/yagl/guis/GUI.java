package it.angrybear.yagl.guis;

import it.angrybear.yagl.actions.BiGUIAction;
import it.angrybear.yagl.actions.GUIAction;
import it.angrybear.yagl.contents.GUIContent;
import it.angrybear.yagl.viewers.Viewer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * The general interface to represent a GUI.
 */
public interface GUI extends Iterable<GUIContent> {

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
     * Sets previous GUI.
     *
     * @param previous the previous gui
     * @return this gui
     */
    @NotNull GUI setPrevious(final @Nullable GUI previous);

    /**
     * Gets the previous GUI.
     *
     * @return the previous
     */
    @Nullable GUI getPrevious();

    /**
     * Sets next GUI.
     *
     * @param next the next
     * @return this gui
     */
    @NotNull GUI setNext(final @Nullable GUI next);

    /**
     * Gets the next GUI.
     *
     * @return the next
     */
    @Nullable GUI getNext();

    /**
     * Sets back GUI.
     *
     * @param back the back
     * @return this gui
     */
    @NotNull GUI setBack(final @Nullable GUI back);

    /**
     * Gets the GUI opened before opening this GUI (not previous).
     *
     * @return the back
     */
    @Nullable GUI getBack();

    /**
     * Gets size.
     *
     * @return the size
     */
    int getSize();

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
        for (int i = 0; i < getSize(); i++) setMovable(i, true);
        return this;
    }

    /**
     * Sets all unmovable.
     *
     * @return this gui
     */
    default @NotNull GUI setAllUnmovable() {
        for (int i = 0; i < getSize(); i++) setMovable(i, false);
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
     * @return the content
     */
    @Nullable GUIContent getContent(final @NotNull Viewer viewer, int slot);

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
     * @return this gui
     */
    @NotNull GUI addContent(final GUIContent @NotNull ... contents);

    /**
     * Sets the given contents at the specified index.
     * These will be then filtered using {@link #getContent(Viewer, int)}
     *
     * @param slot    the slot
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
}
