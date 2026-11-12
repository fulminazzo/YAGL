package it.fulminazzo.yagl.gui;

import it.fulminazzo.yagl.action.BiGUIAction;
import it.fulminazzo.yagl.action.GUIAction;
import it.fulminazzo.yagl.content.CustomItemGUIContent;
import it.fulminazzo.yagl.content.GUIContent;
import it.fulminazzo.yagl.item.Item;
import it.fulminazzo.yagl.metadatable.Metadatable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Collection;

/**
 * Represents a "chest" GUI that can be resized.
 * This GUI can start with any value in bounds and multiple of 9 and can grow if necessary.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResizableGUI extends DefaultGUI {

    /**
     * Instantiates a new Gui.
     *
     * @param size the size
     */
    protected ResizableGUI(final @Range(from = Constants.MIN_DEFAULT_SIZE, to = Constants.MAX_SIZE) int size) {
        super(size);
    }

    /**
     * Resizes the current GUI to the new size.
     * Must be a multiple of 9 not higher than {@link Constants#MAX_SIZE}.
     *
     * @param size the size
     * @return this gui
     */
    public @NotNull ResizableGUI resize(final @Range(from = Constants.MIN_DEFAULT_SIZE, to = Constants.MAX_SIZE) int size) {
        checkValidSize(size);
        this.contents = createContents(size, this.contents);
        this.movableSlots.removeIf(s -> s >= size);
        return this;
    }

    @Override
    public @NotNull ResizableGUI setContents(final @Range(from = 0, to = Constants.MAX_SLOT) int slot, final @NotNull Collection<? extends GUIContent> contents) {
        return (ResizableGUI) super.setContents(slot, contents);
    }

    @Override
    public @NotNull ResizableGUI setContents(final @Range(from = 0, to = Constants.MAX_SLOT) int slot, final GUIContent @NotNull ... contents) {
        // First resize if possible and needed.
        if (slot >= size() && slot < Constants.MAX_SIZE) resize((slot / 9 + 1) * 9);
        return (ResizableGUI) super.setContents(slot, contents);
    }

    @Override
    public @NotNull ResizableGUI addContent(final GUIContent @NotNull ... contents) {
        if (getContents().size() + contents.length > Constants.MAX_SIZE)
            throw new IllegalArgumentException(
                    String.format("Could not add %s contents because GUI size would exceed maximum capacity of %s",
                            contents.length, Constants.MAX_SIZE)
            );
        int i = 0;
        for (GUIContent content : contents) {
            i = addSingle(content, i);
            if (i < size()) continue;
            if (size() < Constants.MAX_SIZE) {
                resize(size() + 9);
                this.contents.set(i, new Contents(content));
            }
        }
        return this;
    }

    /*
     * RETURN TYPE OVERRIDES
     */

    @Override
    public @NotNull ResizableGUI setTitle(final @Nullable String title) {
        return (ResizableGUI) super.setTitle(title);
    }

    @Override
    public @NotNull ResizableGUI setMovable(final @Range(from = 0, to = Constants.MAX_SLOT) int slot, final boolean movable) {
        return (ResizableGUI) super.setMovable(slot, movable);
    }

    @Override
    public @NotNull ResizableGUI unsetContent(final @Range(from = 0, to = Constants.MAX_SLOT) int slot) {
        return (ResizableGUI) super.unsetContent(slot);
    }

    @Override
    public @NotNull ResizableGUI clear() {
        return (ResizableGUI) super.clear();
    }

    @Override
    public @NotNull ResizableGUI setAllSides(final Item @NotNull ... contents) {
        return (ResizableGUI) super.setAllSides(contents);
    }

    @Override
    public @NotNull ResizableGUI setAllSides(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (ResizableGUI) super.setAllSides(contents);
    }

    @Override
    public @NotNull ResizableGUI setAllSides(final GUIContent @NotNull ... contents) {
        return (ResizableGUI) super.setAllSides(contents);
    }

    @Override
    public @NotNull ResizableGUI setAllSides(final @NotNull Collection<? extends GUIContent> contents) {
        return (ResizableGUI) super.setAllSides(contents);
    }

    @Override
    public @NotNull ResizableGUI unsetAllSides() {
        return (ResizableGUI) super.unsetAllSides();
    }

    @Override
    public @NotNull ResizableGUI setTopAndBottomSides(final Item @NotNull ... contents) {
        return (ResizableGUI) super.setTopAndBottomSides(contents);
    }

    @Override
    public @NotNull ResizableGUI setTopAndBottomSides(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (ResizableGUI) super.setTopAndBottomSides(contents);
    }

    @Override
    public @NotNull ResizableGUI setTopAndBottomSides(final GUIContent @NotNull ... contents) {
        return (ResizableGUI) super.setTopAndBottomSides(contents);
    }

    @Override
    public @NotNull ResizableGUI setTopAndBottomSides(final @NotNull Collection<? extends GUIContent> contents) {
        return (ResizableGUI) super.setTopAndBottomSides(contents);
    }

    @Override
    public @NotNull ResizableGUI unsetTopAndBottomSides() {
        return (ResizableGUI) super.unsetTopAndBottomSides();
    }

    @Override
    public @NotNull ResizableGUI setLeftAndRightSides(final Item @NotNull ... contents) {
        return (ResizableGUI) super.setLeftAndRightSides(contents);
    }

    @Override
    public @NotNull ResizableGUI setLeftAndRightSides(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (ResizableGUI) super.setLeftAndRightSides(contents);
    }

    @Override
    public @NotNull ResizableGUI setLeftAndRightSides(final GUIContent @NotNull ... contents) {
        return (ResizableGUI) super.setLeftAndRightSides(contents);
    }

    @Override
    public @NotNull ResizableGUI setLeftAndRightSides(final @NotNull Collection<? extends GUIContent> contents) {
        return (ResizableGUI) super.setLeftAndRightSides(contents);
    }

    @Override
    public @NotNull ResizableGUI unsetLeftAndRightSides() {
        return (ResizableGUI) super.unsetLeftAndRightSides();
    }

    @Override
    public @NotNull ResizableGUI setTopSide(final Item @NotNull ... contents) {
        return (ResizableGUI) super.setTopSide(contents);
    }

    @Override
    public @NotNull ResizableGUI setTopSide(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (ResizableGUI) super.setTopSide(contents);
    }

    @Override
    public @NotNull ResizableGUI setTopSide(final GUIContent @NotNull ... contents) {
        return (ResizableGUI) super.setTopSide(contents);
    }

    @Override
    public @NotNull ResizableGUI setTopSide(final @NotNull Collection<? extends GUIContent> contents) {
        return (ResizableGUI) super.setTopSide(contents);
    }

    @Override
    public @NotNull ResizableGUI unsetTopSide() {
        return (ResizableGUI) super.unsetTopSide();
    }

    @Override
    public @NotNull ResizableGUI setLeftSide(final Item @NotNull ... contents) {
        return (ResizableGUI) super.setLeftSide(contents);
    }

    @Override
    public @NotNull ResizableGUI setLeftSide(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (ResizableGUI) super.setLeftSide(contents);
    }

    @Override
    public @NotNull ResizableGUI setLeftSide(final GUIContent @NotNull ... contents) {
        return (ResizableGUI) super.setLeftSide(contents);
    }

    @Override
    public @NotNull ResizableGUI setLeftSide(final @NotNull Collection<? extends GUIContent> contents) {
        return (ResizableGUI) super.setLeftSide(contents);
    }

    @Override
    public @NotNull ResizableGUI unsetLeftSide() {
        return (ResizableGUI) super.unsetLeftSide();
    }

    @Override
    public @NotNull ResizableGUI setBottomSide(final Item @NotNull ... contents) {
        return (ResizableGUI) super.setBottomSide(contents);
    }

    @Override
    public @NotNull ResizableGUI setBottomSide(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (ResizableGUI) super.setBottomSide(contents);
    }

    @Override
    public @NotNull ResizableGUI setBottomSide(final GUIContent @NotNull ... contents) {
        return (ResizableGUI) super.setBottomSide(contents);
    }

    @Override
    public @NotNull ResizableGUI setBottomSide(final @NotNull Collection<? extends GUIContent> contents) {
        return (ResizableGUI) super.setBottomSide(contents);
    }

    @Override
    public @NotNull ResizableGUI unsetBottomSide() {
        return (ResizableGUI) super.unsetBottomSide();
    }

    @Override
    public @NotNull ResizableGUI setRightSide(final Item @NotNull ... contents) {
        return (ResizableGUI) super.setRightSide(contents);
    }

    @Override
    public @NotNull ResizableGUI setRightSide(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (ResizableGUI) super.setRightSide(contents);
    }

    @Override
    public @NotNull ResizableGUI setRightSide(final GUIContent @NotNull ... contents) {
        return (ResizableGUI) super.setRightSide(contents);
    }

    @Override
    public @NotNull ResizableGUI setRightSide(final @NotNull Collection<? extends GUIContent> contents) {
        return (ResizableGUI) super.setRightSide(contents);
    }

    @Override
    public @NotNull ResizableGUI unsetRightSide() {
        return (ResizableGUI) super.unsetRightSide();
    }

    @Override
    public @NotNull ResizableGUI setNorthWest(final Item @NotNull ... contents) {
        return (ResizableGUI) super.setNorthWest(contents);
    }

    @Override
    public @NotNull ResizableGUI setNorthWest(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (ResizableGUI) super.setNorthWest(contents);
    }

    @Override
    public @NotNull ResizableGUI setNorthWest(final GUIContent @NotNull ... contents) {
        return (ResizableGUI) super.setNorthWest(contents);
    }

    @Override
    public @NotNull ResizableGUI unsetNorthWest() {
        return (ResizableGUI) super.unsetNorthWest();
    }

    @Override
    public @NotNull ResizableGUI setNorth(final Item @NotNull ... contents) {
        return (ResizableGUI) super.setNorth(contents);
    }

    @Override
    public @NotNull ResizableGUI setNorth(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (ResizableGUI) super.setNorth(contents);
    }

    @Override
    public @NotNull ResizableGUI setNorth(final GUIContent @NotNull ... contents) {
        return (ResizableGUI) super.setNorth(contents);
    }

    @Override
    public @NotNull ResizableGUI unsetNorth() {
        return (ResizableGUI) super.unsetNorth();
    }

    @Override
    public @NotNull ResizableGUI setNorthEast(final Item @NotNull ... contents) {
        return (ResizableGUI) super.setNorthEast(contents);
    }

    @Override
    public @NotNull ResizableGUI setNorthEast(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (ResizableGUI) super.setNorthEast(contents);
    }

    @Override
    public @NotNull ResizableGUI setNorthEast(final GUIContent @NotNull ... contents) {
        return (ResizableGUI) super.setNorthEast(contents);
    }

    @Override
    public @NotNull ResizableGUI unsetNorthEast() {
        return (ResizableGUI) super.unsetNorthEast();
    }

    @Override
    public @NotNull ResizableGUI setMiddleWest(final Item @NotNull ... contents) {
        return (ResizableGUI) super.setMiddleWest(contents);
    }

    @Override
    public @NotNull ResizableGUI setMiddleWest(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (ResizableGUI) super.setMiddleWest(contents);
    }

    @Override
    public @NotNull ResizableGUI setMiddleWest(final GUIContent @NotNull ... contents) {
        return (ResizableGUI) super.setMiddleWest(contents);
    }

    @Override
    public @NotNull ResizableGUI unsetMiddleWest() {
        return (ResizableGUI) super.unsetMiddleWest();
    }

    @Override
    public @NotNull ResizableGUI setMiddle(final Item @NotNull ... contents) {
        return (ResizableGUI) super.setMiddle(contents);
    }

    @Override
    public @NotNull ResizableGUI setMiddle(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (ResizableGUI) super.setMiddle(contents);
    }

    @Override
    public @NotNull ResizableGUI setMiddle(final GUIContent @NotNull ... contents) {
        return (ResizableGUI) super.setMiddle(contents);
    }

    @Override
    public @NotNull ResizableGUI unsetMiddle() {
        return (ResizableGUI) super.unsetMiddle();
    }

    @Override
    public @NotNull ResizableGUI setMiddleEast(final Item @NotNull ... contents) {
        return (ResizableGUI) super.setMiddleEast(contents);
    }

    @Override
    public @NotNull ResizableGUI setMiddleEast(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (ResizableGUI) super.setMiddleEast(contents);
    }

    @Override
    public @NotNull ResizableGUI setMiddleEast(final GUIContent @NotNull ... contents) {
        return (ResizableGUI) super.setMiddleEast(contents);
    }

    @Override
    public @NotNull ResizableGUI unsetMiddleEast() {
        return (ResizableGUI) super.unsetMiddleEast();
    }

    @Override
    public @NotNull ResizableGUI setSouthWest(final Item @NotNull ... contents) {
        return (ResizableGUI) super.setSouthWest(contents);
    }

    @Override
    public @NotNull ResizableGUI setSouthWest(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (ResizableGUI) super.setSouthWest(contents);
    }

    @Override
    public @NotNull ResizableGUI setSouthWest(final GUIContent @NotNull ... contents) {
        return (ResizableGUI) super.setSouthWest(contents);
    }

    @Override
    public @NotNull ResizableGUI unsetSouthWest() {
        return (ResizableGUI) super.unsetSouthWest();
    }

    @Override
    public @NotNull ResizableGUI setSouth(final Item @NotNull ... contents) {
        return (ResizableGUI) super.setSouth(contents);
    }

    @Override
    public @NotNull ResizableGUI setSouth(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (ResizableGUI) super.setSouth(contents);
    }

    @Override
    public @NotNull ResizableGUI setSouth(final GUIContent @NotNull ... contents) {
        return (ResizableGUI) super.setSouth(contents);
    }

    @Override
    public @NotNull ResizableGUI unsetSouth() {
        return (ResizableGUI) super.unsetSouth();
    }

    @Override
    public @NotNull ResizableGUI setSouthEast(final Item @NotNull ... contents) {
        return (ResizableGUI) super.setSouthEast(contents);
    }

    @Override
    public @NotNull ResizableGUI setSouthEast(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (ResizableGUI) super.setSouthEast(contents);
    }

    @Override
    public @NotNull ResizableGUI setSouthEast(final GUIContent @NotNull ... contents) {
        return (ResizableGUI) super.setSouthEast(contents);
    }

    @Override
    public @NotNull ResizableGUI unsetSouthEast() {
        return (ResizableGUI) super.unsetSouthEast();
    }

    @Override
    public @NotNull ResizableGUI onClickOutside(final @NotNull GUIAction action) {
        return (ResizableGUI) super.onClickOutside(action);
    }

    @Override
    public @NotNull ResizableGUI onOpenGUI(final @NotNull GUIAction action) {
        return (ResizableGUI) super.onOpenGUI(action);
    }

    @Override
    public @NotNull ResizableGUI onCloseGUI(final @NotNull GUIAction action) {
        return (ResizableGUI) super.onCloseGUI(action);
    }

    @Override
    public @NotNull ResizableGUI onChangeGUI(final @NotNull BiGUIAction action) {
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
    public @NotNull ResizableGUI addContent(final Item @NotNull ... contents) {
        return (ResizableGUI) super.addContent(contents);
    }

    @Override
    public @NotNull ResizableGUI addContent(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (ResizableGUI) super.addContent(contents);
    }

    @Override
    public @NotNull ResizableGUI setContents(final @Range(from = 0, to = Constants.MAX_SLOT) int slot, final Item @NotNull ... contents) {
        return (ResizableGUI) super.setContents(slot, contents);
    }

    @Override
    public @NotNull ResizableGUI setContents(final @Range(from = 0, to = Constants.MAX_SLOT) int slot, final CustomItemGUIContent<?> @NotNull ... contents) {
        return (ResizableGUI) super.setContents(slot, contents);
    }

    @Override
    public @NotNull ResizableGUI fill(final @NotNull Item content) {
        return (ResizableGUI) super.fill(content);
    }

    @Override
    public @NotNull ResizableGUI fill(final @NotNull CustomItemGUIContent<?> content) {
        return (ResizableGUI) super.fill(content);
    }

    @Override
    public @NotNull ResizableGUI fill(final @NotNull GUIContent content) {
        return (ResizableGUI) super.fill(content);
    }

    @Override
    public @NotNull ResizableGUI onClickOutside(final @NotNull String command) {
        return (ResizableGUI) super.onClickOutside(command);
    }

    @Override
    public @NotNull ResizableGUI onOpenGUI(final @NotNull String command) {
        return (ResizableGUI) super.onOpenGUI(command);
    }

    @Override
    public @NotNull ResizableGUI onCloseGUI(final @NotNull String command) {
        return (ResizableGUI) super.onCloseGUI(command);
    }

    @Override
    public @NotNull ResizableGUI onChangeGUI(final @NotNull String command) {
        return (ResizableGUI) super.onChangeGUI(command);
    }

    @Override
    public @NotNull ResizableGUI onClickOutsideSend(final @NotNull String message) {
        return (ResizableGUI) super.onClickOutsideSend(message);
    }

    @Override
    public @NotNull ResizableGUI onOpenGUISend(final @NotNull String message) {
        return (ResizableGUI) super.onOpenGUISend(message);
    }

    @Override
    public @NotNull ResizableGUI onCloseGUISend(final @NotNull String message) {
        return (ResizableGUI) super.onCloseGUISend(message);
    }

    @Override
    public @NotNull ResizableGUI onChangeGUISend(final @NotNull String message) {
        return (ResizableGUI) super.onChangeGUISend(message);
    }

    @Override
    public @NotNull ResizableGUI setVariable(final @NotNull String name, final @NotNull String value) {
        return (ResizableGUI) super.setVariable(name, value);
    }

    @Override
    public @NotNull ResizableGUI unsetVariable(final @NotNull String name) {
        return (ResizableGUI) super.unsetVariable(name);
    }

    @Override
    public @NotNull ResizableGUI copyVariablesTo(final @NotNull Metadatable other, final boolean replace) {
        return (ResizableGUI) super.copyVariablesTo(other, replace);
    }

    @Override
    public @NotNull ResizableGUI copyVariablesFrom(final @NotNull Metadatable other, final boolean replace) {
        return (ResizableGUI) super.copyVariablesFrom(other, replace);
    }

    @Override
    public @NotNull ResizableGUI copyAll(final @NotNull GUI other, final boolean replace) {
        return (ResizableGUI) super.copyAll(other, replace);
    }

    @Override
    public @NotNull ResizableGUI copyFrom(final @NotNull GUI other, final boolean replace) {
        return (ResizableGUI) super.copyFrom(other, replace);
    }

    @Override
    public ResizableGUI copy() {
        return (ResizableGUI) super.copy();
    }

}
