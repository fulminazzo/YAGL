package it.fulminazzo.yagl.guis;

import it.fulminazzo.fulmicollection.objects.FieldEquable;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import it.fulminazzo.yagl.Metadatable;
import it.fulminazzo.yagl.actions.BiGUIAction;
import it.fulminazzo.yagl.actions.GUIAction;
import it.fulminazzo.yagl.contents.GUIContent;
import it.fulminazzo.yagl.contents.ItemGUIContent;
import it.fulminazzo.yagl.items.Item;
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
    /**
     * The Second inventory size.
     */
    static final int SECOND_INVENTORY_SIZE = 36;

    private final @NotNull GUI upperGUI;
    private final @NotNull GUI lowerGUI;

    private FullSizeGUI() {
        this.upperGUI = new DefaultGUI();
        this.lowerGUI = GUI.newGUI(SECOND_INVENTORY_SIZE);
    }

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
        final Class<?> guiUtils;
        try {
            guiUtils = ReflectionUtils.getClass("it.fulminazzo.yagl.GUIAdapter");
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Could not find GUIAdapter class. This function requires the 'gui:bukkit' module to be added");
        }
        new Refl<>(guiUtils).invokeMethod("openGUI", this, viewer);
    }

    @Override
    public @NotNull FullSizeGUI setTitle(@Nullable String title) {
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
    public @NotNull FullSizeGUI setMovable(int slot, boolean movable) {
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
    public @NotNull FullSizeGUI addContent(GUIContent @NotNull ... contents) {
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
    public @NotNull FullSizeGUI setContents(int slot, GUIContent @NotNull ... contents) {
        getCorrespondingGUI(slot).setContents(getCorrespondingSlot(slot), contents);
        return this;
    }

    @Override
    public @NotNull FullSizeGUI unsetContent(int slot) {
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
    public @NotNull FullSizeGUI clear() {
        this.upperGUI.clear();
        this.lowerGUI.clear();
        return this;
    }

    @Override
    public @NotNull FullSizeGUI onClickOutside(@NotNull GUIAction action) {
        this.upperGUI.onClickOutside(action);
        return this;
    }

    @Override
    public @NotNull Optional<GUIAction> clickOutsideAction() {
        return this.upperGUI.clickOutsideAction();
    }

    @Override
    public @NotNull FullSizeGUI onOpenGUI(@NotNull GUIAction action) {
        this.upperGUI.onOpenGUI(action);
        return this;
    }

    @Override
    public @NotNull Optional<GUIAction> openGUIAction() {
        return this.upperGUI.openGUIAction();
    }

    @Override
    public @NotNull FullSizeGUI onCloseGUI(@NotNull GUIAction action) {
        this.upperGUI.onCloseGUI(action);
        return this;
    }

    @Override
    public @NotNull Optional<GUIAction> closeGUIAction() {
        return this.upperGUI.closeGUIAction();
    }

    @Override
    public @NotNull FullSizeGUI onChangeGUI(@NotNull BiGUIAction action) {
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
    public FullSizeGUI copy() {
        return new Refl<>(new FullSizeGUI())
                .setFieldObject("upperGUI", this.upperGUI.copy())
                .setFieldObject("lowerGUI", this.lowerGUI.copy())
                .getObject();
    }

    @Override
    public @NotNull FullSizeGUI setAllMovable() {
        return (FullSizeGUI) GUI.super.setAllMovable();
    }

    @Override
    public @NotNull FullSizeGUI setAllUnmovable() {
        return (FullSizeGUI) GUI.super.setAllUnmovable();
    }

    @Override
    public @NotNull FullSizeGUI addContent(Item @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.addContent(contents);
    }

    @Override
    public @NotNull FullSizeGUI addContent(ItemGUIContent @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.addContent(contents);
    }

    @Override
    public @NotNull FullSizeGUI setContents(int slot, Item @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setContents(slot, contents);
    }

    @Override
    public @NotNull FullSizeGUI setContents(int slot, ItemGUIContent @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setContents(slot, contents);
    }

    @Override
    public @NotNull FullSizeGUI setContents(int slot, @NotNull Collection<GUIContent> contents) {
        return (FullSizeGUI) GUI.super.setContents(slot, contents);
    }

    @Override
    public @NotNull FullSizeGUI setAllSides(Item @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setAllSides(contents);
    }

    @Override
    public @NotNull FullSizeGUI setAllSides(ItemGUIContent @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setAllSides(contents);
    }

    @Override
    public @NotNull FullSizeGUI setAllSides(GUIContent @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setAllSides(contents);
    }

    @Override
    public @NotNull FullSizeGUI setAllSides(@NotNull Collection<GUIContent> contents) {
        return (FullSizeGUI) GUI.super.setAllSides(contents);
    }

    @Override
    public @NotNull FullSizeGUI unsetAllSides() {
        return (FullSizeGUI) GUI.super.unsetAllSides();
    }

    @Override
    public @NotNull FullSizeGUI setTopAndBottomSides(Item @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setTopAndBottomSides(contents);
    }

    @Override
    public @NotNull FullSizeGUI setTopAndBottomSides(ItemGUIContent @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setTopAndBottomSides(contents);
    }

    @Override
    public @NotNull FullSizeGUI setTopAndBottomSides(GUIContent @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setTopAndBottomSides(contents);
    }

    @Override
    public @NotNull FullSizeGUI setTopAndBottomSides(@NotNull Collection<GUIContent> contents) {
        return (FullSizeGUI) GUI.super.setTopAndBottomSides(contents);
    }

    @Override
    public @NotNull FullSizeGUI unsetTopAndBottomSides() {
        return (FullSizeGUI) GUI.super.unsetTopAndBottomSides();
    }

    @Override
    public @NotNull FullSizeGUI setLeftAndRightSides(Item @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setLeftAndRightSides(contents);
    }

    @Override
    public @NotNull FullSizeGUI setLeftAndRightSides(ItemGUIContent @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setLeftAndRightSides(contents);
    }

    @Override
    public @NotNull FullSizeGUI setLeftAndRightSides(GUIContent @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setLeftAndRightSides(contents);
    }

    @Override
    public @NotNull FullSizeGUI setLeftAndRightSides(@NotNull Collection<GUIContent> contents) {
        return (FullSizeGUI) GUI.super.setLeftAndRightSides(contents);
    }

    @Override
    public @NotNull FullSizeGUI unsetLeftAndRightSides() {
        return (FullSizeGUI) GUI.super.unsetLeftAndRightSides();
    }

    @Override
    public @NotNull FullSizeGUI setTopSide(Item @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setTopSide(contents);
    }

    @Override
    public @NotNull FullSizeGUI setTopSide(ItemGUIContent @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setTopSide(contents);
    }

    @Override
    public @NotNull FullSizeGUI setTopSide(GUIContent @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setTopSide(contents);
    }

    @Override
    public @NotNull FullSizeGUI setTopSide(@NotNull Collection<GUIContent> contents) {
        return (FullSizeGUI) GUI.super.setTopSide(contents);
    }

    @Override
    public @NotNull FullSizeGUI unsetTopSide() {
        return (FullSizeGUI) GUI.super.unsetTopSide();
    }

    @Override
    public @NotNull FullSizeGUI setLeftSide(Item @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setLeftSide(contents);
    }

    @Override
    public @NotNull FullSizeGUI setLeftSide(ItemGUIContent @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setLeftSide(contents);
    }

    @Override
    public @NotNull FullSizeGUI setLeftSide(GUIContent @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setLeftSide(contents);
    }

    @Override
    public @NotNull FullSizeGUI setLeftSide(@NotNull Collection<GUIContent> contents) {
        return (FullSizeGUI) GUI.super.setLeftSide(contents);
    }

    @Override
    public @NotNull FullSizeGUI unsetLeftSide() {
        return (FullSizeGUI) GUI.super.unsetLeftSide();
    }

    @Override
    public @NotNull FullSizeGUI setBottomSide(Item @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setBottomSide(contents);
    }

    @Override
    public @NotNull FullSizeGUI setBottomSide(ItemGUIContent @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setBottomSide(contents);
    }

    @Override
    public @NotNull FullSizeGUI setBottomSide(GUIContent @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setBottomSide(contents);
    }

    @Override
    public @NotNull FullSizeGUI setBottomSide(@NotNull Collection<GUIContent> contents) {
        return (FullSizeGUI) GUI.super.setBottomSide(contents);
    }

    @Override
    public @NotNull FullSizeGUI unsetBottomSide() {
        return (FullSizeGUI) GUI.super.unsetBottomSide();
    }

    @Override
    public @NotNull FullSizeGUI setRightSide(Item @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setRightSide(contents);
    }

    @Override
    public @NotNull FullSizeGUI setRightSide(ItemGUIContent @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setRightSide(contents);
    }

    @Override
    public @NotNull FullSizeGUI setRightSide(GUIContent @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setRightSide(contents);
    }

    @Override
    public @NotNull FullSizeGUI setRightSide(@NotNull Collection<GUIContent> contents) {
        return (FullSizeGUI) GUI.super.setRightSide(contents);
    }

    @Override
    public @NotNull FullSizeGUI unsetRightSide() {
        return (FullSizeGUI) GUI.super.unsetRightSide();
    }

    @Override
    public @NotNull FullSizeGUI setNorthWest(Item @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setNorthWest(contents);
    }

    @Override
    public @NotNull FullSizeGUI setNorthWest(ItemGUIContent @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setNorthWest(contents);
    }

    @Override
    public @NotNull FullSizeGUI setNorthWest(GUIContent @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setNorthWest(contents);
    }

    @Override
    public @NotNull FullSizeGUI unsetNorthWest() {
        return (FullSizeGUI) GUI.super.unsetNorthWest();
    }

    @Override
    public @NotNull FullSizeGUI setNorth(Item @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setNorth(contents);
    }

    @Override
    public @NotNull FullSizeGUI setNorth(ItemGUIContent @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setNorth(contents);
    }

    @Override
    public @NotNull FullSizeGUI setNorth(GUIContent @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setNorth(contents);
    }

    @Override
    public @NotNull FullSizeGUI unsetNorth() {
        return (FullSizeGUI) GUI.super.unsetNorth();
    }

    @Override
    public @NotNull FullSizeGUI setNorthEast(Item @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setNorthEast(contents);
    }

    @Override
    public @NotNull FullSizeGUI setNorthEast(ItemGUIContent @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setNorthEast(contents);
    }

    @Override
    public @NotNull FullSizeGUI setNorthEast(GUIContent @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setNorthEast(contents);
    }

    @Override
    public @NotNull FullSizeGUI unsetNorthEast() {
        return (FullSizeGUI) GUI.super.unsetNorthEast();
    }

    @Override
    public @NotNull FullSizeGUI setMiddleWest(Item @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setMiddleWest(contents);
    }

    @Override
    public @NotNull FullSizeGUI setMiddleWest(ItemGUIContent @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setMiddleWest(contents);
    }

    @Override
    public @NotNull FullSizeGUI setMiddleWest(GUIContent @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setMiddleWest(contents);
    }

    @Override
    public @NotNull FullSizeGUI unsetMiddleWest() {
        return (FullSizeGUI) GUI.super.unsetMiddleWest();
    }

    @Override
    public @NotNull FullSizeGUI setMiddle(Item @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setMiddle(contents);
    }

    @Override
    public @NotNull FullSizeGUI setMiddle(ItemGUIContent @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setMiddle(contents);
    }

    @Override
    public @NotNull FullSizeGUI setMiddle(GUIContent @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setMiddle(contents);
    }

    @Override
    public @NotNull FullSizeGUI unsetMiddle() {
        return (FullSizeGUI) GUI.super.unsetMiddle();
    }

    @Override
    public @NotNull FullSizeGUI setMiddleEast(Item @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setMiddleEast(contents);
    }

    @Override
    public @NotNull FullSizeGUI setMiddleEast(ItemGUIContent @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setMiddleEast(contents);
    }

    @Override
    public @NotNull FullSizeGUI setMiddleEast(GUIContent @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setMiddleEast(contents);
    }

    @Override
    public @NotNull FullSizeGUI unsetMiddleEast() {
        return (FullSizeGUI) GUI.super.unsetMiddleEast();
    }

    @Override
    public @NotNull FullSizeGUI setSouthWest(Item @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setSouthWest(contents);
    }

    @Override
    public @NotNull FullSizeGUI setSouthWest(ItemGUIContent @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setSouthWest(contents);
    }

    @Override
    public @NotNull FullSizeGUI setSouthWest(GUIContent @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setSouthWest(contents);
    }

    @Override
    public @NotNull FullSizeGUI unsetSouthWest() {
        return (FullSizeGUI) GUI.super.unsetSouthWest();
    }

    @Override
    public @NotNull FullSizeGUI setSouth(Item @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setSouth(contents);
    }

    @Override
    public @NotNull FullSizeGUI setSouth(ItemGUIContent @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setSouth(contents);
    }

    @Override
    public @NotNull FullSizeGUI setSouth(GUIContent @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setSouth(contents);
    }

    @Override
    public @NotNull FullSizeGUI unsetSouth() {
        return (FullSizeGUI) GUI.super.unsetSouth();
    }

    @Override
    public @NotNull FullSizeGUI setSouthEast(Item @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setSouthEast(contents);
    }

    @Override
    public @NotNull FullSizeGUI setSouthEast(ItemGUIContent @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setSouthEast(contents);
    }

    @Override
    public @NotNull FullSizeGUI setSouthEast(GUIContent @NotNull ... contents) {
        return (FullSizeGUI) GUI.super.setSouthEast(contents);
    }

    @Override
    public @NotNull FullSizeGUI unsetSouthEast() {
        return (FullSizeGUI) GUI.super.unsetSouthEast();
    }

    @Override
    public @NotNull FullSizeGUI fill(@NotNull Item content) {
        return (FullSizeGUI) GUI.super.fill(content);
    }

    @Override
    public @NotNull FullSizeGUI fill(@NotNull ItemGUIContent content) {
        return (FullSizeGUI) GUI.super.fill(content);
    }

    @Override
    public @NotNull FullSizeGUI fill(@NotNull GUIContent content) {
        return (FullSizeGUI) GUI.super.fill(content);
    }

    @Override
    public @NotNull FullSizeGUI onClickOutsideSend(@NotNull String message) {
        return (FullSizeGUI) GUI.super.onClickOutsideSend(message);
    }

    @Override
    public @NotNull FullSizeGUI onClickOutside(@NotNull String command) {
        return (FullSizeGUI) GUI.super.onClickOutside(command);
    }

    @Override
    public @NotNull FullSizeGUI onOpenGUISend(@NotNull String message) {
        return (FullSizeGUI) GUI.super.onOpenGUISend(message);
    }

    @Override
    public @NotNull FullSizeGUI onOpenGUI(@NotNull String command) {
        return (FullSizeGUI) GUI.super.onOpenGUI(command);
    }

    @Override
    public @NotNull FullSizeGUI onCloseGUISend(@NotNull String message) {
        return (FullSizeGUI) GUI.super.onCloseGUISend(message);
    }

    @Override
    public @NotNull FullSizeGUI onCloseGUI(@NotNull String command) {
        return (FullSizeGUI) GUI.super.onCloseGUI(command);
    }

    @Override
    public @NotNull FullSizeGUI onChangeGUISend(@NotNull String message) {
        return (FullSizeGUI) GUI.super.onChangeGUISend(message);
    }

    @Override
    public @NotNull FullSizeGUI onChangeGUI(@NotNull String command) {
        return (FullSizeGUI) GUI.super.onChangeGUI(command);
    }

    @Override
    public @NotNull FullSizeGUI setVariable(@NotNull String name, @NotNull String value) {
        return (FullSizeGUI) GUI.super.setVariable(name, value);
    }

    @Override
    public @NotNull FullSizeGUI unsetVariable(@NotNull String name) {
        return (FullSizeGUI) GUI.super.unsetVariable(name);
    }

    @Override
    public @NotNull FullSizeGUI copyAll(@NotNull GUI other, boolean replace) {
        return (FullSizeGUI) GUI.super.copyAll(other, replace);
    }

    @Override
    public @NotNull FullSizeGUI copyFrom(@NotNull GUI other, boolean replace) {
        return (FullSizeGUI) GUI.super.copyFrom(other, replace);
    }

    @Override
    public @NotNull FullSizeGUI copyAll(@NotNull Metadatable other, boolean replace) {
        return (FullSizeGUI) GUI.super.copyAll(other, replace);
    }

    @Override
    public @NotNull FullSizeGUI copyFrom(@NotNull Metadatable other, boolean replace) {
        return (FullSizeGUI) GUI.super.copyFrom(other, replace);
    }

}
