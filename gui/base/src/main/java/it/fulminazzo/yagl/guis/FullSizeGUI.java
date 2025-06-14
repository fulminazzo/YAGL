package it.fulminazzo.yagl.guis;

import it.fulminazzo.fulmicollection.objects.FieldEquable;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import it.fulminazzo.yagl.actions.BiGUIAction;
import it.fulminazzo.yagl.actions.GUIAction;
import it.fulminazzo.yagl.contents.GUIContent;
import it.fulminazzo.yagl.viewers.Viewer;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents a {@link GUI} that supports a full size display.
 * When shown to the player, their inventory will be used as well to
 * show contents.
 */
@Getter
public class FullSizeGUI extends FieldEquable implements GUI {
    static final int SECOND_INVENTORY_SIZE = 36;

    private final @NotNull GUI upperGUI;
    private final @NotNull GUI lowerGUI;

    /**
     * Instantiates a new Full size gui.
     *
     * @param size the size
     */
    FullSizeGUI(final int size) {
        this.upperGUI = GUI.newGUI(size);
        this.lowerGUI = GUI.newGUI(SECOND_INVENTORY_SIZE);
    }

    /**
     * Instantiates a new Full size gui.
     *
     * @param type the type
     */
    FullSizeGUI(final @NotNull GUIType type) {
        this.upperGUI = GUI.newGUI(type);
        this.lowerGUI = GUI.newGUI(SECOND_INVENTORY_SIZE);
    }

    /**
     * Gets the relative slot from the given slot.
     *
     * @param slot the slot
     * @return the relative slot
     */
    int getCorrespondingSlot(final int slot) {
        GUIUtils.checkSlot(slot, size());
        if (slot >= upperGUI.size()) return slot - upperGUI.size();
        else return slot;
    }

    /**
     * Gets the corresponding gui from the given slot.
     *
     * @param slot the slot
     * @return the corresponding gui
     */
    @NotNull GUI getCorrespondingGUI(final int slot) {
        GUIUtils.checkSlot(slot, size());
        if (slot >= upperGUI.size()) return lowerGUI;
        else return upperGUI;
    }

    @Override
    public void open(@NotNull Viewer viewer) {
        final Class<?> guiUtils;
        try {
            guiUtils = ReflectionUtils.getClass("it.fulminazzo.yagl.GUIAdapter");
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Could not find GUIAdapter class. This function requires the 'gui:bukkit' module to be added");
        }
        new Refl<>(guiUtils).invokeMethod("openGUI", this, viewer);
    }

    @Override
    public @NotNull GUI setTitle(@Nullable String title) {
        this.upperGUI.setTitle(title);
        return this;
    }

    @Override
    public @Nullable String getTitle() {
        return this.upperGUI.getTitle();
    }

    @Override
    public int size() {
        return this.upperGUI.size() + this.lowerGUI.size();
    }

    @Override
    public boolean isMovable(int slot) {
        return getCorrespondingGUI(slot).isMovable(getCorrespondingSlot(slot));
    }

    @Override
    public @NotNull GUI setMovable(int slot, boolean movable) {
        getCorrespondingGUI(slot).setMovable(getCorrespondingSlot(slot), movable);
        return this;
    }

    @Override
    public @NotNull List<GUIContent> getContents(int slot) {
        return getCorrespondingGUI(slot).getContents(getCorrespondingSlot(slot));
    }

    @Override
    public @NotNull List<GUIContent> getContents() {
        return Stream.concat(
                this.upperGUI.getContents().stream(),
                this.lowerGUI.getContents().stream()
        ).collect(Collectors.toList());
    }

    @Override
    public @NotNull GUI addContent(GUIContent @NotNull ... contents) {
        int i;
        for (i = 0; i < contents.length; i++)
            try {
                this.upperGUI.addContent(contents[i]);
            } catch (IllegalArgumentException e) {
                IllegalArgumentException ex = GUIUtils.cannotAddContentAtIndexException(0);
                // If another IllegalArgumentException, rethrow
                if (ex.getMessage().equals(e.getMessage())) break;
                else throw e;
            }
        if (i != contents.length)
            this.lowerGUI.addContent(Arrays.copyOfRange(contents, i, contents.length));
        return this;
    }

    @Override
    public @NotNull GUI setContents(int slot, GUIContent @NotNull ... contents) {
        getCorrespondingGUI(slot).setContents(getCorrespondingSlot(slot), contents);
        return this;
    }

    @Override
    public @NotNull GUI unsetContent(int slot) {
        getCorrespondingGUI(slot).unsetContent(getCorrespondingSlot(slot));
        return this;
    }

    @Override
    public int rows() {
        return this.upperGUI.rows() + this.lowerGUI.rows();
    }

    @Override
    public int columns() {
        return this.lowerGUI.columns();
    }

    @Override
    public @NotNull GUI clear() {
        this.upperGUI.clear();
        this.lowerGUI.clear();
        return this;
    }

    @Override
    public @NotNull GUI onClickOutside(@NotNull GUIAction action) {
        this.upperGUI.onClickOutside(action);
        return this;
    }

    @Override
    public @NotNull Optional<GUIAction> clickOutsideAction() {
        return this.upperGUI.clickOutsideAction();
    }

    @Override
    public @NotNull GUI onOpenGUI(@NotNull GUIAction action) {
        this.upperGUI.onOpenGUI(action);
        return this;
    }

    @Override
    public @NotNull Optional<GUIAction> openGUIAction() {
        return this.upperGUI.openGUIAction();
    }

    @Override
    public @NotNull GUI onCloseGUI(@NotNull GUIAction action) {
        this.upperGUI.onCloseGUI(action);
        return this;
    }

    @Override
    public @NotNull Optional<GUIAction> closeGUIAction() {
        return this.upperGUI.closeGUIAction();
    }

    @Override
    public @NotNull GUI onChangeGUI(@NotNull BiGUIAction action) {
        this.upperGUI.onChangeGUI(action);
        return this;
    }

    @Override
    public @NotNull Optional<BiGUIAction> changeGUIAction() {
        return this.upperGUI.changeGUIAction();
    }

    @Override
    public @NotNull Map<String, String> variables() {
        return this.upperGUI.variables();
    }

}
