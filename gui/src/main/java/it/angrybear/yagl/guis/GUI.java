package it.angrybear.yagl.guis;

import it.angrybear.yagl.contents.GUIContent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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
     * Gets the content at the given slot.
     *
     * @param slot the slot
     * @return the content
     */
    @Nullable GUIContent getContent(int slot);

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
     * Sets the given slot movable.
     *
     * @param slot    the slot
     * @param movable true if it should be movable
     * @return this gui
     */
    @NotNull GUI setMovable(int slot, boolean movable);

    /**
     * Tries to add all the contents in the GUI.
     * If it fails (because of empty GUI) it throws an {@link IllegalArgumentException}.
     *
     * @param contents the contents
     * @return this gui
     */
    @NotNull GUI addContent(final GUIContent @NotNull ... contents);

    /**
     * Sets the content at the given index.
     *
     * @param slot    the slot
     * @param content the content
     * @return this gui
     */
    @NotNull GUI setContent(int slot, final @NotNull GUIContent content);

    /**
     * Removes the content from the given index.
     *
     * @param slot the slot
     * @return this gui
     */
    @NotNull GUI unsetContent(int slot);

    /**
     * Gets a copy of all the contents.
     * To get the actual content, use the GUI in a for-enhanced loop or use {@link #iterator()}.
     *
     * @return the contents
     */
    @NotNull List<GUIContent> getContents();
}
