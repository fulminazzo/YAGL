package it.fulminazzo.yagl.gui;

import it.fulminazzo.fulmicollection.objects.FieldEquable;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yagl.metadatable.Metadatable;
import it.fulminazzo.yagl.action.BiGUIAction;
import it.fulminazzo.yagl.action.GUIAction;
import it.fulminazzo.yagl.content.GUIContent;
import it.fulminazzo.yagl.content.ItemGUIContent;
import it.fulminazzo.yagl.item.Item;
import it.fulminazzo.yagl.viewer.Viewer;
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
public class FullscreenGUI extends FieldEquable implements GUI {
    /**
     * The Second inventory size.
     */
    public static final int SECOND_INVENTORY_SIZE = 36;

    private final @NotNull GUI upperGUI;
    private final @NotNull ResizableGUI lowerGUI;

    private FullscreenGUI() {
        this.upperGUI = new DefaultGUI();
        this.lowerGUI = new FullscreenResizableGUI().resize(SECOND_INVENTORY_SIZE);
    }

    /**
     * Instantiates a new Full size gui.
     *
     * @param size the size
     */
    FullscreenGUI(final int size) {
        this.upperGUI = GUI.newGUI(size);
        this.lowerGUI = new FullscreenResizableGUI().resize(SECOND_INVENTORY_SIZE);
    }

    /**
     * Instantiates a new Full size gui.
     *
     * @param type the type
     */
    FullscreenGUI(final @NotNull GUIType type) {
        this.upperGUI = GUI.newGUI(type);
        this.lowerGUI = new FullscreenResizableGUI().resize(SECOND_INVENTORY_SIZE);
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

    /**
     * Gets all the {@link GUIContent}s per slot.
     *
     * @return the full contents
     */
    public @NotNull Map<Integer, List<GUIContent>> getFullContents() {
        Map<Integer, List<GUIContent>> contents = new LinkedHashMap<>();
        for (int i = 0; i < size(); i++) contents.put(i, getContents(i));
        return contents;
    }

    @Override
    public void open(@NotNull Viewer viewer) {
        GUIUtils.executeGUIAdapterFunction("openGUI", this, viewer);
    }

    /**
     * Updates the viewer open {@link FullscreenGUI} by updating the {@link #lowerGUI} contents.
     *
     * @param viewer the viewer
     */
    public void update(@NotNull Viewer viewer) {
        GUIUtils.executeGUIAdapterFunction("updatePlayerGUI", this, viewer);
    }

    @Override
    public @NotNull FullscreenGUI setTitle(@Nullable String title) {
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
    public @NotNull FullscreenGUI setMovable(int slot, boolean movable) {
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
    public @NotNull FullscreenGUI addContent(GUIContent @NotNull ... contents) {
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
    public @NotNull FullscreenGUI setContents(int slot, GUIContent @NotNull ... contents) {
        getCorrespondingGUI(slot).setContents(getCorrespondingSlot(slot), contents);
        return this;
    }

    @Override
    public @NotNull FullscreenGUI unsetContent(int slot) {
        getCorrespondingGUI(slot).unsetContent(getCorrespondingSlot(slot));
        return this;
    }

    @Override
    public @NotNull Set<Integer> leftSlots() {
        return Stream.concat(
                this.upperGUI.leftSlots().stream(),
                this.lowerGUI.leftSlots().stream()
                        .map(s -> s + this.upperGUI.size())
        )
                .sorted(Comparator.comparing(s -> s))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public @NotNull Set<Integer> rightSlots() {
        return Stream.concat(
                this.upperGUI.rightSlots().stream(),
                this.lowerGUI.rightSlots().stream()
                        .map(s -> s + this.upperGUI.size())
        )
                .sorted(Comparator.comparing(s -> s))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public int northWest() {
        return this.upperGUI.northWest();
    }

    @Override
    public int north() {
        return this.upperGUI.north();
    }

    @Override
    public int northEast() {
        return this.upperGUI.northEast();
    }

    @Override
    public int middleLine() {
        if (this.upperGUI.columns() == this.lowerGUI.columns()) return GUI.super.middleLine();
        else return this.upperGUI.size() + this.lowerGUI.middleLine();
    }

    @Override
    public int southLine() {
        return this.upperGUI.size() + this.lowerGUI.southLine();
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
    public @NotNull FullscreenGUI clear() {
        this.upperGUI.clear();
        this.lowerGUI.clear();
        return this;
    }

    @Override
    public @NotNull FullscreenGUI onClickOutside(@NotNull GUIAction action) {
        this.upperGUI.onClickOutside(action);
        return this;
    }

    @Override
    public @NotNull Optional<GUIAction> clickOutsideAction() {
        return this.upperGUI.clickOutsideAction();
    }

    @Override
    public @NotNull FullscreenGUI onOpenGUI(@NotNull GUIAction action) {
        this.upperGUI.onOpenGUI(action);
        return this;
    }

    @Override
    public @NotNull Optional<GUIAction> openGUIAction() {
        return this.upperGUI.openGUIAction();
    }

    @Override
    public @NotNull FullscreenGUI onCloseGUI(@NotNull GUIAction action) {
        this.upperGUI.onCloseGUI(action);
        return this;
    }

    @Override
    public @NotNull Optional<GUIAction> closeGUIAction() {
        return this.upperGUI.closeGUIAction();
    }

    @Override
    public @NotNull FullscreenGUI onChangeGUI(@NotNull BiGUIAction action) {
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

    @Override
    public FullscreenGUI copy() {
        return new Refl<>(new FullscreenGUI())
                .setFieldObject("upperGUI", this.upperGUI.copy())
                .setFieldObject("lowerGUI", this.lowerGUI.copy())
                .getObject();
    }

    @Override
    public @NotNull FullscreenGUI setAllMovable() {
        return (FullscreenGUI) GUI.super.setAllMovable();
    }

    @Override
    public @NotNull FullscreenGUI setAllUnmovable() {
        return (FullscreenGUI) GUI.super.setAllUnmovable();
    }

    @Override
    public @NotNull FullscreenGUI addContent(Item @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.addContent(contents);
    }

    @Override
    public @NotNull FullscreenGUI addContent(ItemGUIContent @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.addContent(contents);
    }

    @Override
    public @NotNull FullscreenGUI setContents(int slot, Item @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setContents(slot, contents);
    }

    @Override
    public @NotNull FullscreenGUI setContents(int slot, ItemGUIContent @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setContents(slot, contents);
    }

    @Override
    public @NotNull FullscreenGUI setContents(int slot, @NotNull Collection<GUIContent> contents) {
        return (FullscreenGUI) GUI.super.setContents(slot, contents);
    }

    @Override
    public @NotNull FullscreenGUI setAllSides(Item @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setAllSides(contents);
    }

    @Override
    public @NotNull FullscreenGUI setAllSides(ItemGUIContent @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setAllSides(contents);
    }

    @Override
    public @NotNull FullscreenGUI setAllSides(GUIContent @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setAllSides(contents);
    }

    @Override
    public @NotNull FullscreenGUI setAllSides(@NotNull Collection<GUIContent> contents) {
        return (FullscreenGUI) GUI.super.setAllSides(contents);
    }

    @Override
    public @NotNull FullscreenGUI unsetAllSides() {
        return (FullscreenGUI) GUI.super.unsetAllSides();
    }

    @Override
    public @NotNull FullscreenGUI setTopAndBottomSides(Item @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setTopAndBottomSides(contents);
    }

    @Override
    public @NotNull FullscreenGUI setTopAndBottomSides(ItemGUIContent @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setTopAndBottomSides(contents);
    }

    @Override
    public @NotNull FullscreenGUI setTopAndBottomSides(GUIContent @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setTopAndBottomSides(contents);
    }

    @Override
    public @NotNull FullscreenGUI setTopAndBottomSides(@NotNull Collection<GUIContent> contents) {
        return (FullscreenGUI) GUI.super.setTopAndBottomSides(contents);
    }

    @Override
    public @NotNull FullscreenGUI unsetTopAndBottomSides() {
        return (FullscreenGUI) GUI.super.unsetTopAndBottomSides();
    }

    @Override
    public @NotNull FullscreenGUI setLeftAndRightSides(Item @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setLeftAndRightSides(contents);
    }

    @Override
    public @NotNull FullscreenGUI setLeftAndRightSides(ItemGUIContent @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setLeftAndRightSides(contents);
    }

    @Override
    public @NotNull FullscreenGUI setLeftAndRightSides(GUIContent @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setLeftAndRightSides(contents);
    }

    @Override
    public @NotNull FullscreenGUI setLeftAndRightSides(@NotNull Collection<GUIContent> contents) {
        return (FullscreenGUI) GUI.super.setLeftAndRightSides(contents);
    }

    @Override
    public @NotNull FullscreenGUI unsetLeftAndRightSides() {
        return (FullscreenGUI) GUI.super.unsetLeftAndRightSides();
    }

    @Override
    public @NotNull FullscreenGUI setTopSide(Item @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setTopSide(contents);
    }

    @Override
    public @NotNull FullscreenGUI setTopSide(ItemGUIContent @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setTopSide(contents);
    }

    @Override
    public @NotNull FullscreenGUI setTopSide(GUIContent @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setTopSide(contents);
    }

    @Override
    public @NotNull FullscreenGUI setTopSide(@NotNull Collection<GUIContent> contents) {
        return (FullscreenGUI) GUI.super.setTopSide(contents);
    }

    @Override
    public @NotNull FullscreenGUI unsetTopSide() {
        return (FullscreenGUI) GUI.super.unsetTopSide();
    }

    @Override
    public @NotNull FullscreenGUI setLeftSide(Item @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setLeftSide(contents);
    }

    @Override
    public @NotNull FullscreenGUI setLeftSide(ItemGUIContent @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setLeftSide(contents);
    }

    @Override
    public @NotNull FullscreenGUI setLeftSide(GUIContent @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setLeftSide(contents);
    }

    @Override
    public @NotNull FullscreenGUI setLeftSide(@NotNull Collection<GUIContent> contents) {
        return (FullscreenGUI) GUI.super.setLeftSide(contents);
    }

    @Override
    public @NotNull FullscreenGUI unsetLeftSide() {
        return (FullscreenGUI) GUI.super.unsetLeftSide();
    }

    @Override
    public @NotNull FullscreenGUI setBottomSide(Item @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setBottomSide(contents);
    }

    @Override
    public @NotNull FullscreenGUI setBottomSide(ItemGUIContent @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setBottomSide(contents);
    }

    @Override
    public @NotNull FullscreenGUI setBottomSide(GUIContent @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setBottomSide(contents);
    }

    @Override
    public @NotNull FullscreenGUI setBottomSide(@NotNull Collection<GUIContent> contents) {
        return (FullscreenGUI) GUI.super.setBottomSide(contents);
    }

    @Override
    public @NotNull FullscreenGUI unsetBottomSide() {
        return (FullscreenGUI) GUI.super.unsetBottomSide();
    }

    @Override
    public @NotNull FullscreenGUI setRightSide(Item @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setRightSide(contents);
    }

    @Override
    public @NotNull FullscreenGUI setRightSide(ItemGUIContent @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setRightSide(contents);
    }

    @Override
    public @NotNull FullscreenGUI setRightSide(GUIContent @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setRightSide(contents);
    }

    @Override
    public @NotNull FullscreenGUI setRightSide(@NotNull Collection<GUIContent> contents) {
        return (FullscreenGUI) GUI.super.setRightSide(contents);
    }

    @Override
    public @NotNull FullscreenGUI unsetRightSide() {
        return (FullscreenGUI) GUI.super.unsetRightSide();
    }

    @Override
    public @NotNull FullscreenGUI setNorthWest(Item @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setNorthWest(contents);
    }

    @Override
    public @NotNull FullscreenGUI setNorthWest(ItemGUIContent @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setNorthWest(contents);
    }

    @Override
    public @NotNull FullscreenGUI setNorthWest(GUIContent @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setNorthWest(contents);
    }

    @Override
    public @NotNull FullscreenGUI unsetNorthWest() {
        return (FullscreenGUI) GUI.super.unsetNorthWest();
    }

    @Override
    public @NotNull FullscreenGUI setNorth(Item @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setNorth(contents);
    }

    @Override
    public @NotNull FullscreenGUI setNorth(ItemGUIContent @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setNorth(contents);
    }

    @Override
    public @NotNull FullscreenGUI setNorth(GUIContent @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setNorth(contents);
    }

    @Override
    public @NotNull FullscreenGUI unsetNorth() {
        return (FullscreenGUI) GUI.super.unsetNorth();
    }

    @Override
    public @NotNull FullscreenGUI setNorthEast(Item @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setNorthEast(contents);
    }

    @Override
    public @NotNull FullscreenGUI setNorthEast(ItemGUIContent @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setNorthEast(contents);
    }

    @Override
    public @NotNull FullscreenGUI setNorthEast(GUIContent @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setNorthEast(contents);
    }

    @Override
    public @NotNull FullscreenGUI unsetNorthEast() {
        return (FullscreenGUI) GUI.super.unsetNorthEast();
    }

    @Override
    public @NotNull FullscreenGUI setMiddleWest(Item @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setMiddleWest(contents);
    }

    @Override
    public @NotNull FullscreenGUI setMiddleWest(ItemGUIContent @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setMiddleWest(contents);
    }

    @Override
    public @NotNull FullscreenGUI setMiddleWest(GUIContent @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setMiddleWest(contents);
    }

    @Override
    public @NotNull FullscreenGUI unsetMiddleWest() {
        return (FullscreenGUI) GUI.super.unsetMiddleWest();
    }

    @Override
    public @NotNull FullscreenGUI setMiddle(Item @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setMiddle(contents);
    }

    @Override
    public @NotNull FullscreenGUI setMiddle(ItemGUIContent @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setMiddle(contents);
    }

    @Override
    public @NotNull FullscreenGUI setMiddle(GUIContent @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setMiddle(contents);
    }

    @Override
    public @NotNull FullscreenGUI unsetMiddle() {
        return (FullscreenGUI) GUI.super.unsetMiddle();
    }

    @Override
    public @NotNull FullscreenGUI setMiddleEast(Item @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setMiddleEast(contents);
    }

    @Override
    public @NotNull FullscreenGUI setMiddleEast(ItemGUIContent @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setMiddleEast(contents);
    }

    @Override
    public @NotNull FullscreenGUI setMiddleEast(GUIContent @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setMiddleEast(contents);
    }

    @Override
    public @NotNull FullscreenGUI unsetMiddleEast() {
        return (FullscreenGUI) GUI.super.unsetMiddleEast();
    }

    @Override
    public @NotNull FullscreenGUI setSouthWest(Item @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setSouthWest(contents);
    }

    @Override
    public @NotNull FullscreenGUI setSouthWest(ItemGUIContent @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setSouthWest(contents);
    }

    @Override
    public @NotNull FullscreenGUI setSouthWest(GUIContent @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setSouthWest(contents);
    }

    @Override
    public @NotNull FullscreenGUI unsetSouthWest() {
        return (FullscreenGUI) GUI.super.unsetSouthWest();
    }

    @Override
    public @NotNull FullscreenGUI setSouth(Item @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setSouth(contents);
    }

    @Override
    public @NotNull FullscreenGUI setSouth(ItemGUIContent @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setSouth(contents);
    }

    @Override
    public @NotNull FullscreenGUI setSouth(GUIContent @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setSouth(contents);
    }

    @Override
    public @NotNull FullscreenGUI unsetSouth() {
        return (FullscreenGUI) GUI.super.unsetSouth();
    }

    @Override
    public @NotNull FullscreenGUI setSouthEast(Item @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setSouthEast(contents);
    }

    @Override
    public @NotNull FullscreenGUI setSouthEast(ItemGUIContent @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setSouthEast(contents);
    }

    @Override
    public @NotNull FullscreenGUI setSouthEast(GUIContent @NotNull ... contents) {
        return (FullscreenGUI) GUI.super.setSouthEast(contents);
    }

    @Override
    public @NotNull FullscreenGUI unsetSouthEast() {
        return (FullscreenGUI) GUI.super.unsetSouthEast();
    }

    @Override
    public @NotNull FullscreenGUI fill(@NotNull Item content) {
        return (FullscreenGUI) GUI.super.fill(content);
    }

    @Override
    public @NotNull FullscreenGUI fill(@NotNull ItemGUIContent content) {
        return (FullscreenGUI) GUI.super.fill(content);
    }

    @Override
    public @NotNull FullscreenGUI fill(@NotNull GUIContent content) {
        return (FullscreenGUI) GUI.super.fill(content);
    }

    @Override
    public @NotNull FullscreenGUI onClickOutsideSend(@NotNull String message) {
        return (FullscreenGUI) GUI.super.onClickOutsideSend(message);
    }

    @Override
    public @NotNull FullscreenGUI onClickOutside(@NotNull String command) {
        return (FullscreenGUI) GUI.super.onClickOutside(command);
    }

    @Override
    public @NotNull FullscreenGUI onOpenGUISend(@NotNull String message) {
        return (FullscreenGUI) GUI.super.onOpenGUISend(message);
    }

    @Override
    public @NotNull FullscreenGUI onOpenGUI(@NotNull String command) {
        return (FullscreenGUI) GUI.super.onOpenGUI(command);
    }

    @Override
    public @NotNull FullscreenGUI onCloseGUISend(@NotNull String message) {
        return (FullscreenGUI) GUI.super.onCloseGUISend(message);
    }

    @Override
    public @NotNull FullscreenGUI onCloseGUI(@NotNull String command) {
        return (FullscreenGUI) GUI.super.onCloseGUI(command);
    }

    @Override
    public @NotNull FullscreenGUI onChangeGUISend(@NotNull String message) {
        return (FullscreenGUI) GUI.super.onChangeGUISend(message);
    }

    @Override
    public @NotNull FullscreenGUI onChangeGUI(@NotNull String command) {
        return (FullscreenGUI) GUI.super.onChangeGUI(command);
    }

    @Override
    public @NotNull FullscreenGUI setVariable(@NotNull String name, @NotNull String value) {
        return (FullscreenGUI) GUI.super.setVariable(name, value);
    }

    @Override
    public @NotNull FullscreenGUI unsetVariable(@NotNull String name) {
        return (FullscreenGUI) GUI.super.unsetVariable(name);
    }

    @Override
    public @NotNull FullscreenGUI copyAll(@NotNull GUI other, boolean replace) {
        return (FullscreenGUI) GUI.super.copyAll(other, replace);
    }

    @Override
    public @NotNull FullscreenGUI copyFrom(@NotNull GUI other, boolean replace) {
        return (FullscreenGUI) GUI.super.copyFrom(other, replace);
    }

    @Override
    public @NotNull FullscreenGUI copyAll(@NotNull Metadatable other, boolean replace) {
        return (FullscreenGUI) GUI.super.copyAll(other, replace);
    }

    @Override
    public @NotNull FullscreenGUI copyFrom(@NotNull Metadatable other, boolean replace) {
        return (FullscreenGUI) GUI.super.copyFrom(other, replace);
    }

    /**
     * An implementation of {@link ResizableGUI} that supports a maximum size of {@link #SECOND_INVENTORY_SIZE}.
     */
    static class FullscreenResizableGUI extends ResizableGUI {

        @Override
        void checkSize(int size) {
            if (size < COLUMNS || size > SECOND_INVENTORY_SIZE)
                throw new IllegalArgumentException(String.format("GUIs size must be bound between %s and %s!",
                        COLUMNS, SECOND_INVENTORY_SIZE
                ));
        }

    }

}
