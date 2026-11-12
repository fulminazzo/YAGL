package it.fulminazzo.yagl.gui;

import it.fulminazzo.yagl.action.BiGUIAction;
import it.fulminazzo.yagl.action.GUIAction;
import it.fulminazzo.yagl.content.CustomItemGUIContent;
import it.fulminazzo.yagl.content.GUIContent;
import it.fulminazzo.yagl.exception.NotImplementedException;
import it.fulminazzo.yagl.item.Item;
import it.fulminazzo.yagl.metadatable.Metadatable;
import it.fulminazzo.yagl.viewer.Viewer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An implementation of {@link PageableGUI} that allows data to be automatically displayed in a multi-paged format.
 *
 * @param <T> the type of the data
 */
@SuppressWarnings("unchecked")
@EqualsAndHashCode(callSuper = true)
public class DataGUI<T> extends PageableGUI {
    static final String ERROR_MESSAGE = "Pages are dynamically calculated when opening this GUI. They cannot be singly edited";

    @Getter
    private final @NotNull List<T> data;
    @EqualsAndHashCode.Exclude
    protected final @NotNull Function<T, GUIContent> dataConverter;

    /**
     * Instantiates a new Data gui.
     */
    DataGUI() {
        this.data = new LinkedList<>();
        this.dataConverter = t -> {
            throw new NotImplementedException();
        };
    }

    /**
     * Instantiates a new Data gui.
     *
     * @param templateGUI the template gui
     */
    DataGUI(final @NotNull GUI templateGUI) {
        super(templateGUI);
        this.data = new LinkedList<>();
        this.dataConverter = t -> {
            throw new NotImplementedException();
        };
    }

    /**
     * Instantiates a new Data gui.
     *
     * @param templateGUI   the templateGUI
     * @param dataConverter the data converter
     */
    DataGUI(final @NotNull GUI templateGUI, final @NotNull Function<T, GUIContent> dataConverter) {
        super(templateGUI);
        this.data = new LinkedList<>();
        this.dataConverter = dataConverter;
    }

    /**
     * Adds the given data.
     *
     * @param data the data
     * @return this gui
     */
    public @NotNull DataGUI<T> addData(final T @NotNull ... data) {
        return addData(Arrays.asList(data));
    }

    /**
     * Adds the given data.
     *
     * @param data the data
     * @return this gui
     */
    public @NotNull DataGUI<T> addData(final @NotNull Collection<T> data) {
        this.data.addAll(data);
        return this;
    }

    /**
     * Clears the current data and sets the given data.
     *
     * @param data the data
     * @return the data
     */
    public @NotNull DataGUI<T> setData(final T @NotNull ... data) {
        return setData(Arrays.asList(data));
    }

    /**
     * Clears the current data and sets the given data.
     *
     * @param data the data
     * @return the data
     */
    public @NotNull DataGUI<T> setData(final @NotNull Collection<T> data) {
        return clearData().addData(data);
    }

    /**
     * Removes the data equal to any of the given data.
     *
     * @param data the data
     * @return this gui
     */
    public @NotNull DataGUI<T> removeData(final T @NotNull ... data) {
        return removeData(Arrays.asList(data));
    }

    /**
     * Removes the data equal to any of the given data.
     *
     * @param data the data
     * @return this gui
     */
    public @NotNull DataGUI<T> removeData(final @NotNull Collection<T> data) {
        return removeData(t -> data.stream().anyMatch(c -> Objects.equals(c, t)));
    }

    /**
     * Removes the data that match the given {@link Predicate} function.
     *
     * @param function the function
     * @return this gui
     */
    public @NotNull DataGUI<T> removeData(final @NotNull Predicate<T> function) {
        this.data.removeIf(function);
        return this;
    }

    /**
     * Removes all the data.
     *
     * @return this gui
     */
    public @NotNull DataGUI<T> clearData() {
        this.data.clear();
        return this;
    }

    private @NotNull GUI fillContents(final @NotNull GUI gui, final @Range(from = 0, to = Integer.MAX_VALUE) int page) {
        List<T> dataList = getDataList();
        int emptySlots = getPageEmptySlots();
        int min = emptySlots * page;
        int dataSize = dataList.size();
        if (dataSize == 0) return gui;
        if (min >= dataSize)
            throw new IllegalArgumentException(String.format("No such page '%s'", page));
        if (page > 0) min++;
        int size = Math.min(gui.emptySlots().size() + min, dataSize);
        for (int i = min; i < size; i++) {
            T data = dataList.get(i);
            GUIContent content = this.dataConverter.apply(data);
            gui.addContent(content);
        }
        return gui;
    }

    /**
     * Gets the data list.
     *
     * @return data list
     */
    protected @NotNull List<T> getDataList() {
        return this.data;
    }

    /**
     * Gets the first page empty slots.
     *
     * @return the slots
     */
    protected @Range(from = 0, to = Constants.MAX_SLOT) int getFirstPageEmptySlots() {
        int slots = getPageEmptySlots();
        if (this.previousPage.isPresent()) slots++;
        return slots;
    }

    /**
     * Gets the last page empty slots.
     *
     * @return the slots
     */
    protected @Range(from = 0, to = Constants.MAX_SLOT) int getLastPageEmptySlots() {
        int slots = getPageEmptySlots();
        if (this.nextPage.isPresent()) slots++;
        return slots;
    }

    /**
     * Gets a general page empty slots.
     *
     * @return the slots
     */
    protected @Range(from = 0, to = Constants.MAX_SLOT) int getPageEmptySlots() {
        return emptySlots().size();
    }

    @Override
    public void open(final @NotNull Viewer viewer, final @Range(from = 0, to = Integer.MAX_VALUE) int page) {
        prepareOpenGUI(this.templateGUI, page).open(viewer);
    }

    @Override
    protected @NotNull GUI prepareOpenGUI(final @NotNull GUI gui, final @Range(from = 0, to = Integer.MAX_VALUE) int page) {
        return fillContents(super.prepareOpenGUI(gui, page), page);
    }

    /**
     * Gets the number of pages based on the amount of data provided.
     *
     * @return the pages
     */
    @Override
    public @Range(from = 0, to = Integer.MAX_VALUE) int pages() {
        int dataSize = getDataList().size();
        if (dataSize == 0) return 1;
        final int emptySlots = getPageEmptySlots();
        final int firstPageSlots = getFirstPageEmptySlots();
        final int lastPageSlots = getLastPageEmptySlots();
        int pages = 1;
        // First page
        dataSize -= firstPageSlots;
        if (dataSize < 1) return pages;
        if (emptySlots < 1)
            throw new IllegalStateException(String.format("Not enough empty slots available to set %s items of data", dataSize));
        // Last page
        pages++;
        dataSize -= lastPageSlots;
        if (dataSize < 1) return pages;
        // Other pages
        while (dataSize > 0) {
            pages++;
            dataSize -= emptySlots;
        }
        return pages;
    }

    /**
     * Gets the {@link GUI} page from the given index.
     * The index starts from <b>0</b>.
     *
     * @param page the page
     * @return the corresponding {@link GUI} page
     * @deprecated In {@link DataGUI}s pages are not pre-defined, but rather calculated upon opening.
     */
    @Override
    @Deprecated
    public @NotNull GUI getPage(final @Range(from = 0, to = Integer.MAX_VALUE) int page) {
        throw new IllegalStateException(ERROR_MESSAGE);
    }

    /**
     * Sets pages.
     *
     * @param pages the pages
     * @return this gui
     * @deprecated In {@link DataGUI}s pages are not pre-defined, but rather calculated upon opening.
     */
    @Override
    @Deprecated
    public @NotNull DataGUI<T> setPages(final @Range(from = Constants.MIN_TYPE_SIZE, to = Integer.MAX_VALUE) int pages) {
        throw new IllegalStateException(ERROR_MESSAGE);
    }

    /*
     * RETURN TYPE OVERRIDES
     */

    @Override
    public @NotNull DataGUI<T> setPreviousPage(final @Range(from = 0, to = Constants.MAX_SLOT) int slot, final @NotNull Item previousPage) {
        return (DataGUI<T>) super.setPreviousPage(slot, previousPage);
    }

    @Override
    public @NotNull DataGUI<T> setPreviousPage(final @Range(from = 0, to = Constants.MAX_SLOT) int slot, final @NotNull GUIContent previousPage) {
        return (DataGUI<T>) super.setPreviousPage(slot, previousPage);
    }

    @Override
    public @NotNull DataGUI<T> unsetPreviousPage() {
        return (DataGUI<T>) super.unsetPreviousPage();
    }

    @Override
    public @NotNull DataGUI<T> setNextPage(final @Range(from = 0, to = Constants.MAX_SLOT) int slot, final @NotNull Item nextPage) {
        return (DataGUI<T>) super.setNextPage(slot, nextPage);
    }

    @Override
    public @NotNull DataGUI<T> setNextPage(final @Range(from = 0, to = Constants.MAX_SLOT) int slot, final @NotNull GUIContent nextPage) {
        return (DataGUI<T>) super.setNextPage(slot, nextPage);
    }

    @Override
    public @NotNull DataGUI<T> unsetNextPage() {
        return (DataGUI<T>) super.unsetNextPage();
    }

    @Override
    public @NotNull DataGUI<T> setTitle(final @Nullable String title) {
        return (DataGUI<T>) super.setTitle(title);
    }

    @Override
    public @NotNull DataGUI<T> setMovable(final @Range(from = 0, to = Constants.MAX_SLOT) int slot, final boolean movable) {
        return (DataGUI<T>) super.setMovable(slot, movable);
    }

    @Override
    public @NotNull DataGUI<T> addContent(final GUIContent @NotNull ... contents) {
        return (DataGUI<T>) super.addContent(contents);
    }

    @Override
    public @NotNull DataGUI<T> setContents(final @Range(from = 0, to = Constants.MAX_SLOT) int slot, final GUIContent @NotNull ... contents) {
        return (DataGUI<T>) super.setContents(slot, contents);
    }

    @Override
    public @NotNull DataGUI<T> setContents(final @Range(from = 0, to = Constants.MAX_SLOT) int slot, final @NotNull Collection<? extends GUIContent> contents) {
        return (DataGUI<T>) super.setContents(slot, contents);
    }

    @Override
    public @NotNull DataGUI<T> unsetContent(final @Range(from = 0, to = Constants.MAX_SLOT) int slot) {
        return (DataGUI<T>) super.unsetContent(slot);
    }

    @Override
    public @NotNull DataGUI<T> onClickOutside(final @NotNull GUIAction action) {
        return (DataGUI<T>) super.onClickOutside(action);
    }

    @Override
    public @NotNull DataGUI<T> onOpenGUI(final @NotNull GUIAction action) {
        return (DataGUI<T>) super.onOpenGUI(action);
    }

    @Override
    public @NotNull DataGUI<T> onCloseGUI(final @NotNull GUIAction action) {
        return (DataGUI<T>) super.onCloseGUI(action);
    }

    @Override
    public @NotNull DataGUI<T> onChangeGUI(final @NotNull BiGUIAction action) {
        return (DataGUI<T>) super.onChangeGUI(action);
    }

    @Override
    public @NotNull DataGUI<T> setAllMovable() {
        return (DataGUI<T>) super.setAllMovable();
    }

    @Override
    public @NotNull DataGUI<T> setAllUnmovable() {
        return (DataGUI<T>) super.setAllUnmovable();
    }

    @Override
    public @NotNull DataGUI<T> addContent(final Item @NotNull ... contents) {
        return (DataGUI<T>) super.addContent(contents);
    }

    @Override
    public @NotNull DataGUI<T> addContent(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (DataGUI<T>) super.addContent(contents);
    }

    @Override
    public @NotNull DataGUI<T> setContents(final @Range(from = 0, to = Constants.MAX_SLOT) int slot, final Item @NotNull ... contents) {
        return (DataGUI<T>) super.setContents(slot, contents);
    }

    @Override
    public @NotNull DataGUI<T> setContents(final @Range(from = 0, to = Constants.MAX_SLOT) int slot, final CustomItemGUIContent<?> @NotNull ... contents) {
        return (DataGUI<T>) super.setContents(slot, contents);
    }

    @Override
    public @NotNull DataGUI<T> setAllSides(final Item @NotNull ... contents) {
        return (DataGUI<T>) super.setAllSides(contents);
    }

    @Override
    public @NotNull DataGUI<T> setAllSides(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (DataGUI<T>) super.setAllSides(contents);
    }

    @Override
    public @NotNull DataGUI<T> setAllSides(final GUIContent @NotNull ... contents) {
        return (DataGUI<T>) super.setAllSides(contents);
    }

    @Override
    public @NotNull DataGUI<T> setAllSides(final @NotNull Collection<? extends GUIContent> contents) {
        return (DataGUI<T>) super.setAllSides(contents);
    }

    @Override
    public @NotNull DataGUI<T> unsetAllSides() {
        return (DataGUI<T>) super.unsetAllSides();
    }

    @Override
    public @NotNull DataGUI<T> setTopAndBottomSides(final Item @NotNull ... contents) {
        return (DataGUI<T>) super.setTopAndBottomSides(contents);
    }

    @Override
    public @NotNull DataGUI<T> setTopAndBottomSides(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (DataGUI<T>) super.setTopAndBottomSides(contents);
    }

    @Override
    public @NotNull DataGUI<T> setTopAndBottomSides(final GUIContent @NotNull ... contents) {
        return (DataGUI<T>) super.setTopAndBottomSides(contents);
    }

    @Override
    public @NotNull DataGUI<T> setTopAndBottomSides(final @NotNull Collection<? extends GUIContent> contents) {
        return (DataGUI<T>) super.setTopAndBottomSides(contents);
    }

    @Override
    public @NotNull DataGUI<T> unsetTopAndBottomSides() {
        return (DataGUI<T>) super.unsetTopAndBottomSides();
    }

    @Override
    public @NotNull DataGUI<T> setLeftAndRightSides(final Item @NotNull ... contents) {
        return (DataGUI<T>) super.setLeftAndRightSides(contents);
    }

    @Override
    public @NotNull DataGUI<T> setLeftAndRightSides(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (DataGUI<T>) super.setLeftAndRightSides(contents);
    }

    @Override
    public @NotNull DataGUI<T> setLeftAndRightSides(final GUIContent @NotNull ... contents) {
        return (DataGUI<T>) super.setLeftAndRightSides(contents);
    }

    @Override
    public @NotNull DataGUI<T> setLeftAndRightSides(final @NotNull Collection<? extends GUIContent> contents) {
        return (DataGUI<T>) super.setLeftAndRightSides(contents);
    }

    @Override
    public @NotNull DataGUI<T> unsetLeftAndRightSides() {
        return (DataGUI<T>) super.unsetLeftAndRightSides();
    }

    @Override
    public @NotNull DataGUI<T> setTopSide(final Item @NotNull ... contents) {
        return (DataGUI<T>) super.setTopSide(contents);
    }

    @Override
    public @NotNull DataGUI<T> setTopSide(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (DataGUI<T>) super.setTopSide(contents);
    }

    @Override
    public @NotNull DataGUI<T> setTopSide(final GUIContent @NotNull ... contents) {
        return (DataGUI<T>) super.setTopSide(contents);
    }

    @Override
    public @NotNull DataGUI<T> setTopSide(final @NotNull Collection<? extends GUIContent> contents) {
        return (DataGUI<T>) super.setTopSide(contents);
    }

    @Override
    public @NotNull DataGUI<T> unsetTopSide() {
        return (DataGUI<T>) super.unsetTopSide();
    }

    @Override
    public @NotNull DataGUI<T> setLeftSide(final Item @NotNull ... contents) {
        return (DataGUI<T>) super.setLeftSide(contents);
    }

    @Override
    public @NotNull DataGUI<T> setLeftSide(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (DataGUI<T>) super.setLeftSide(contents);
    }

    @Override
    public @NotNull DataGUI<T> setLeftSide(final GUIContent @NotNull ... contents) {
        return (DataGUI<T>) super.setLeftSide(contents);
    }

    @Override
    public @NotNull DataGUI<T> setLeftSide(final @NotNull Collection<? extends GUIContent> contents) {
        return (DataGUI<T>) super.setLeftSide(contents);
    }

    @Override
    public @NotNull DataGUI<T> unsetLeftSide() {
        return (DataGUI<T>) super.unsetLeftSide();
    }

    @Override
    public @NotNull DataGUI<T> setBottomSide(final Item @NotNull ... contents) {
        return (DataGUI<T>) super.setBottomSide(contents);
    }

    @Override
    public @NotNull DataGUI<T> setBottomSide(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (DataGUI<T>) super.setBottomSide(contents);
    }

    @Override
    public @NotNull DataGUI<T> setBottomSide(final GUIContent @NotNull ... contents) {
        return (DataGUI<T>) super.setBottomSide(contents);
    }

    @Override
    public @NotNull DataGUI<T> setBottomSide(final @NotNull Collection<? extends GUIContent> contents) {
        return (DataGUI<T>) super.setBottomSide(contents);
    }

    @Override
    public @NotNull DataGUI<T> unsetBottomSide() {
        return (DataGUI<T>) super.unsetBottomSide();
    }

    @Override
    public @NotNull DataGUI<T> setRightSide(final Item @NotNull ... contents) {
        return (DataGUI<T>) super.setRightSide(contents);
    }

    @Override
    public @NotNull DataGUI<T> setRightSide(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (DataGUI<T>) super.setRightSide(contents);
    }

    @Override
    public @NotNull DataGUI<T> setRightSide(final GUIContent @NotNull ... contents) {
        return (DataGUI<T>) super.setRightSide(contents);
    }

    @Override
    public @NotNull DataGUI<T> setRightSide(final @NotNull Collection<? extends GUIContent> contents) {
        return (DataGUI<T>) super.setRightSide(contents);
    }

    @Override
    public @NotNull DataGUI<T> unsetRightSide() {
        return (DataGUI<T>) super.unsetRightSide();
    }

    @Override
    public @NotNull DataGUI<T> setNorthWest(final Item @NotNull ... contents) {
        return (DataGUI<T>) super.setNorthWest(contents);
    }

    @Override
    public @NotNull DataGUI<T> setNorthWest(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (DataGUI<T>) super.setNorthWest(contents);
    }

    @Override
    public @NotNull DataGUI<T> setNorthWest(final GUIContent @NotNull ... contents) {
        return (DataGUI<T>) super.setNorthWest(contents);
    }

    @Override
    public @NotNull DataGUI<T> unsetNorthWest() {
        return (DataGUI<T>) super.unsetNorthWest();
    }

    @Override
    public @NotNull DataGUI<T> setNorth(final Item @NotNull ... contents) {
        return (DataGUI<T>) super.setNorth(contents);
    }

    @Override
    public @NotNull DataGUI<T> setNorth(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (DataGUI<T>) super.setNorth(contents);
    }

    @Override
    public @NotNull DataGUI<T> setNorth(final GUIContent @NotNull ... contents) {
        return (DataGUI<T>) super.setNorth(contents);
    }

    @Override
    public @NotNull DataGUI<T> unsetNorth() {
        return (DataGUI<T>) super.unsetNorth();
    }

    @Override
    public @NotNull DataGUI<T> setNorthEast(final Item @NotNull ... contents) {
        return (DataGUI<T>) super.setNorthEast(contents);
    }

    @Override
    public @NotNull DataGUI<T> setNorthEast(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (DataGUI<T>) super.setNorthEast(contents);
    }

    @Override
    public @NotNull DataGUI<T> setNorthEast(final GUIContent @NotNull ... contents) {
        return (DataGUI<T>) super.setNorthEast(contents);
    }

    @Override
    public @NotNull DataGUI<T> unsetNorthEast() {
        return (DataGUI<T>) super.unsetNorthEast();
    }

    @Override
    public @NotNull DataGUI<T> setMiddleWest(final Item @NotNull ... contents) {
        return (DataGUI<T>) super.setMiddleWest(contents);
    }

    @Override
    public @NotNull DataGUI<T> setMiddleWest(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (DataGUI<T>) super.setMiddleWest(contents);
    }

    @Override
    public @NotNull DataGUI<T> setMiddleWest(final GUIContent @NotNull ... contents) {
        return (DataGUI<T>) super.setMiddleWest(contents);
    }

    @Override
    public @NotNull DataGUI<T> unsetMiddleWest() {
        return (DataGUI<T>) super.unsetMiddleWest();
    }

    @Override
    public @NotNull DataGUI<T> setMiddle(final Item @NotNull ... contents) {
        return (DataGUI<T>) super.setMiddle(contents);
    }

    @Override
    public @NotNull DataGUI<T> setMiddle(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (DataGUI<T>) super.setMiddle(contents);
    }

    @Override
    public @NotNull DataGUI<T> setMiddle(final GUIContent @NotNull ... contents) {
        return (DataGUI<T>) super.setMiddle(contents);
    }

    @Override
    public @NotNull DataGUI<T> unsetMiddle() {
        return (DataGUI<T>) super.unsetMiddle();
    }

    @Override
    public @NotNull DataGUI<T> setMiddleEast(final Item @NotNull ... contents) {
        return (DataGUI<T>) super.setMiddleEast(contents);
    }

    @Override
    public @NotNull DataGUI<T> setMiddleEast(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (DataGUI<T>) super.setMiddleEast(contents);
    }

    @Override
    public @NotNull DataGUI<T> setMiddleEast(final GUIContent @NotNull ... contents) {
        return (DataGUI<T>) super.setMiddleEast(contents);
    }

    @Override
    public @NotNull DataGUI<T> unsetMiddleEast() {
        return (DataGUI<T>) super.unsetMiddleEast();
    }

    @Override
    public @NotNull DataGUI<T> setSouthWest(final Item @NotNull ... contents) {
        return (DataGUI<T>) super.setSouthWest(contents);
    }

    @Override
    public @NotNull DataGUI<T> setSouthWest(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (DataGUI<T>) super.setSouthWest(contents);
    }

    @Override
    public @NotNull DataGUI<T> setSouthWest(final GUIContent @NotNull ... contents) {
        return (DataGUI<T>) super.setSouthWest(contents);
    }

    @Override
    public @NotNull DataGUI<T> unsetSouthWest() {
        return (DataGUI<T>) super.unsetSouthWest();
    }

    @Override
    public @NotNull DataGUI<T> setSouth(final Item @NotNull ... contents) {
        return (DataGUI<T>) super.setSouth(contents);
    }

    @Override
    public @NotNull DataGUI<T> setSouth(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (DataGUI<T>) super.setSouth(contents);
    }

    @Override
    public @NotNull DataGUI<T> setSouth(final GUIContent @NotNull ... contents) {
        return (DataGUI<T>) super.setSouth(contents);
    }

    @Override
    public @NotNull DataGUI<T> unsetSouth() {
        return (DataGUI<T>) super.unsetSouth();
    }

    @Override
    public @NotNull DataGUI<T> setSouthEast(final Item @NotNull ... contents) {
        return (DataGUI<T>) super.setSouthEast(contents);
    }

    @Override
    public @NotNull DataGUI<T> setSouthEast(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (DataGUI<T>) super.setSouthEast(contents);
    }

    @Override
    public @NotNull DataGUI<T> setSouthEast(final GUIContent @NotNull ... contents) {
        return (DataGUI<T>) super.setSouthEast(contents);
    }

    @Override
    public @NotNull DataGUI<T> unsetSouthEast() {
        return (DataGUI<T>) super.unsetSouthEast();
    }

    @Override
    public @NotNull DataGUI<T> fill(final @NotNull Item content) {
        return (DataGUI<T>) super.fill(content);
    }

    @Override
    public @NotNull DataGUI<T> fill(final @NotNull CustomItemGUIContent<?> content) {
        return (DataGUI<T>) super.fill(content);
    }

    @Override
    public @NotNull DataGUI<T> fill(final @NotNull GUIContent content) {
        return (DataGUI<T>) super.fill(content);
    }

    @Override
    public @NotNull DataGUI<T> clear() {
        return (DataGUI<T>) super.clear();
    }

    @Override
    public @NotNull DataGUI<T> onClickOutside(final @NotNull String command) {
        return (DataGUI<T>) super.onClickOutside(command);
    }

    @Override
    public @NotNull DataGUI<T> onOpenGUI(final @NotNull String command) {
        return (DataGUI<T>) super.onOpenGUI(command);
    }

    @Override
    public @NotNull DataGUI<T> onCloseGUI(final @NotNull String command) {
        return (DataGUI<T>) super.onCloseGUI(command);
    }

    @Override
    public @NotNull DataGUI<T> onChangeGUI(final @NotNull String command) {
        return (DataGUI<T>) super.onChangeGUI(command);
    }

    @Override
    public @NotNull DataGUI<T> onClickOutsideSend(final @NotNull String message) {
        return (DataGUI<T>) super.onClickOutsideSend(message);
    }

    @Override
    public @NotNull DataGUI<T> onOpenGUISend(final @NotNull String message) {
        return (DataGUI<T>) super.onOpenGUISend(message);
    }

    @Override
    public @NotNull DataGUI<T> onCloseGUISend(final @NotNull String message) {
        return (DataGUI<T>) super.onCloseGUISend(message);
    }

    @Override
    public @NotNull DataGUI<T> onChangeGUISend(final @NotNull String message) {
        return (DataGUI<T>) super.onChangeGUISend(message);
    }

    @Override
    public @NotNull DataGUI<T> setVariable(final @NotNull String name, final @NotNull String value) {
        return (DataGUI<T>) super.setVariable(name, value);
    }

    @Override
    public @NotNull DataGUI<T> unsetVariable(final @NotNull String name) {
        return (DataGUI<T>) super.unsetVariable(name);
    }

    @Override
    public @NotNull DataGUI<T> copyAll(final @NotNull GUI other, final boolean replace) {
        return (DataGUI<T>) super.copyAll(other, replace);
    }

    @Override
    public @NotNull DataGUI<T> copyFrom(final @NotNull GUI other, final boolean replace) {
        return (DataGUI<T>) super.copyFrom(other, replace);
    }

    @Override
    public DataGUI<T> copy() {
        return (DataGUI<T>) super.copy();
    }

    @Override
    public @NotNull DataGUI<T> copyVariablesTo(final @NotNull Metadatable other, final boolean replace) {
        return (DataGUI<T>) super.copyVariablesTo(other, replace);
    }

    @Override
    public @NotNull DataGUI<T> copyVariablesFrom(final @NotNull Metadatable other, final boolean replace) {
        return (DataGUI<T>) super.copyVariablesFrom(other, replace);
    }

    /*
     * INITIALIZERS
     */

    /**
     * Creates a new {@link DataGUI} with the given size and converter.
     *
     * @param <T>           the type of the data
     * @param size          the size
     * @param dataConverter the data converter
     * @return the data gui
     */
    public static <T> @NotNull DataGUI<T> newGUI(final @Range(from = Constants.MIN_DEFAULT_SIZE, to = Constants.MAX_SIZE) int size,
                                                 final @NotNull Function<T, GUIContent> dataConverter) {
        return new DataGUI<>(GUI.newGUI(size), dataConverter);
    }

    /**
     * Creates a new {@link DataGUI} with the given size, converter and data.
     *
     * @param <T>           the type of the data
     * @param size          the size
     * @param dataConverter the data converter
     * @param data          the data
     * @return the data gui
     */
    @SafeVarargs
    public static <T> @NotNull DataGUI<T> newGUI(final @Range(from = Constants.MIN_DEFAULT_SIZE, to = Constants.MAX_SIZE) int size,
                                                 final @NotNull Function<T, GUIContent> dataConverter,
                                                 final T @NotNull ... data) {
        return new DataGUI<>(GUI.newGUI(size), dataConverter).setData(data);
    }

    /**
     * Creates a new {@link DataGUI} with the given size, converter and data.
     *
     * @param <T>           the type of the data
     * @param size          the size
     * @param dataConverter the data converter
     * @param data          the data
     * @return the data gui
     */
    public static <T> @NotNull DataGUI<T> newGUI(final @Range(from = Constants.MIN_DEFAULT_SIZE, to = Constants.MAX_SIZE) int size,
                                                 final @NotNull Function<T, GUIContent> dataConverter,
                                                 final @NotNull Collection<T> data) {
        return new DataGUI<>(GUI.newGUI(size), dataConverter).setData(data);
    }

    /**
     * Creates a new {@link DataGUI} with the given type and converter.
     *
     * @param <T>           the type of the data
     * @param type          the type
     * @param dataConverter the data converter
     * @return the data gui
     */
    public static <T> @NotNull DataGUI<T> newGUI(final @NotNull GUIType type,
                                                 final @NotNull Function<T, GUIContent> dataConverter) {
        return new DataGUI<>(GUI.newGUI(type), dataConverter);
    }

    /**
     * Creates a new {@link DataGUI} with the given type, converter and data.
     *
     * @param <T>           the type of the data
     * @param type          the type
     * @param dataConverter the data converter
     * @param data          the data
     * @return the data gui
     */
    @SafeVarargs
    public static <T> @NotNull DataGUI<T> newGUI(final @NotNull GUIType type,
                                                 final @NotNull Function<T, GUIContent> dataConverter,
                                                 final T @NotNull ... data) {
        return new DataGUI<>(GUI.newGUI(type), dataConverter).setData(data);
    }

    /**
     * Creates a new {@link DataGUI} with the given type, converter and data.
     *
     * @param <T>           the type of the data
     * @param type          the type
     * @param dataConverter the data converter
     * @param data          the data
     * @return the data gui
     */
    public static <T> @NotNull DataGUI<T> newGUI(final @NotNull GUIType type,
                                                 final @NotNull Function<T, GUIContent> dataConverter,
                                                 final @NotNull Collection<T> data) {
        return new DataGUI<>(GUI.newGUI(type), dataConverter).setData(data);
    }

    /**
     * Creates a new {@link DataGUI} with the given size, converter and a full size {@link #templateGUI}.
     *
     * @param <T>           the type of the data
     * @param size          the size
     * @param dataConverter the data converter
     * @return the data gui
     */
    public static <T> @NotNull DataGUI<T> newFullscreenGUI(final @Range(from = Constants.MIN_DEFAULT_SIZE, to = Constants.MAX_SIZE) int size,
                                                           final @NotNull Function<T, GUIContent> dataConverter) {
        return new DataGUI<>(GUI.newFullscreenGUI(size), dataConverter);
    }

    /**
     * Creates a new {@link DataGUI} with the given size, converter and a full size {@link #templateGUI}.
     *
     * @param <T>           the type of the data
     * @param size          the size
     * @param lowerGUISize  the size of the lower GUI
     * @param dataConverter the data converter
     * @return the data gui
     */
    public static <T> @NotNull DataGUI<T> newFullscreenGUI(final @Range(from = Constants.MIN_DEFAULT_SIZE, to = Constants.MAX_SIZE) int size,
                                                           final @Range(from = Constants.MIN_DEFAULT_SIZE, to = Constants.SECONDARY_GUI_SIZE) int lowerGUISize,
                                                           final @NotNull Function<T, GUIContent> dataConverter) {
        return new DataGUI<>(GUI.newFullscreenGUI(size, lowerGUISize), dataConverter);
    }

    /**
     * Creates a new {@link DataGUI} with the given size, converter, data and a full size {@link #templateGUI}.
     *
     * @param <T>           the type of the data
     * @param size          the size
     * @param dataConverter the data converter
     * @param data          the data
     * @return the data gui
     */
    @SafeVarargs
    public static <T> @NotNull DataGUI<T> newFullscreenGUI(final @Range(from = Constants.MIN_DEFAULT_SIZE, to = Constants.MAX_SIZE) int size,
                                                           final @NotNull Function<T, GUIContent> dataConverter,
                                                           final T @NotNull ... data) {
        return new DataGUI<>(GUI.newFullscreenGUI(size), dataConverter).setData(data);
    }

    /**
     * Creates a new {@link DataGUI} with the given size, converter, data and a full size {@link #templateGUI}.
     *
     * @param <T>           the type of the data
     * @param size          the size
     * @param lowerGUISize  the size of the lower GUI
     * @param dataConverter the data converter
     * @param data          the data
     * @return the data gui
     */
    @SafeVarargs
    public static <T> @NotNull DataGUI<T> newFullscreenGUI(final @Range(from = Constants.MIN_DEFAULT_SIZE, to = Constants.MAX_SIZE) int size,
                                                           final @Range(from = Constants.MIN_DEFAULT_SIZE, to = Constants.SECONDARY_GUI_SIZE) int lowerGUISize,
                                                           final @NotNull Function<T, GUIContent> dataConverter,
                                                           final T @NotNull ... data) {
        return new DataGUI<>(GUI.newFullscreenGUI(size, lowerGUISize), dataConverter).setData(data);
    }

    /**
     * Creates a new {@link DataGUI} with the given size, converter, data and a full size {@link #templateGUI}.
     *
     * @param <T>           the type of the data
     * @param size          the size
     * @param dataConverter the data converter
     * @param data          the data
     * @return the data gui
     */
    public static <T> @NotNull DataGUI<T> newFullscreenGUI(final @Range(from = Constants.MIN_DEFAULT_SIZE, to = Constants.MAX_SIZE) int size,
                                                           final @NotNull Function<T, GUIContent> dataConverter,
                                                           final @NotNull Collection<T> data) {
        return new DataGUI<>(GUI.newFullscreenGUI(size), dataConverter).setData(data);
    }

    /**
     * Creates a new {@link DataGUI} with the given size, converter, data and a full size {@link #templateGUI}.
     *
     * @param <T>           the type of the data
     * @param size          the size
     * @param lowerGUISize  the size of the lower GUI
     * @param dataConverter the data converter
     * @param data          the data
     * @return the data gui
     */
    public static <T> @NotNull DataGUI<T> newFullscreenGUI(final @Range(from = Constants.MIN_DEFAULT_SIZE, to = Constants.MAX_SIZE) int size,
                                                           final @Range(from = Constants.MIN_DEFAULT_SIZE, to = Constants.SECONDARY_GUI_SIZE) int lowerGUISize,
                                                           final @NotNull Function<T, GUIContent> dataConverter,
                                                           final @NotNull Collection<T> data) {
        return new DataGUI<>(GUI.newFullscreenGUI(size, lowerGUISize), dataConverter).setData(data);
    }

    /**
     * Creates a new {@link DataGUI} with the given type, converter and a full size {@link #templateGUI}.
     *
     * @param <T>           the type of the data
     * @param type          the type
     * @param dataConverter the data converter
     * @return the data gui
     */
    public static <T> @NotNull DataGUI<T> newFullscreenGUI(final @NotNull GUIType type,
                                                           final @NotNull Function<T, GUIContent> dataConverter) {
        return new DataGUI<>(GUI.newFullscreenGUI(type), dataConverter);
    }

    /**
     * Creates a new {@link DataGUI} with the given type, converter and a full size {@link #templateGUI}.
     *
     * @param <T>           the type of the data
     * @param type          the type
     * @param lowerGUISize  the size of the lower GUI
     * @param dataConverter the data converter
     * @return the data gui
     */
    public static <T> @NotNull DataGUI<T> newFullscreenGUI(final @NotNull GUIType type,
                                                           final @Range(from = Constants.MIN_DEFAULT_SIZE, to = Constants.SECONDARY_GUI_SIZE) int lowerGUISize,
                                                           final @NotNull Function<T, GUIContent> dataConverter) {
        return new DataGUI<>(GUI.newFullscreenGUI(type, lowerGUISize), dataConverter);
    }

    /**
     * Creates a new {@link DataGUI} with the given type, converter, data and a full size {@link #templateGUI}.
     *
     * @param <T>           the type of the data
     * @param type          the type
     * @param dataConverter the data converter
     * @param data          the data
     * @return the data gui
     */
    @SafeVarargs
    public static <T> @NotNull DataGUI<T> newFullscreenGUI(final @NotNull GUIType type,
                                                           final @NotNull Function<T, GUIContent> dataConverter,
                                                           final T @NotNull ... data) {
        return new DataGUI<>(GUI.newFullscreenGUI(type), dataConverter).setData(data);
    }

    /**
     * Creates a new {@link DataGUI} with the given type, converter, data and a full size {@link #templateGUI}.
     *
     * @param <T>           the type of the data
     * @param type          the type
     * @param lowerGUISize  the size of the lower GUI
     * @param dataConverter the data converter
     * @param data          the data
     * @return the data gui
     */
    @SafeVarargs
    public static <T> @NotNull DataGUI<T> newFullscreenGUI(final @NotNull GUIType type,
                                                           final @Range(from = Constants.MIN_DEFAULT_SIZE, to = Constants.SECONDARY_GUI_SIZE) int lowerGUISize,
                                                           final @NotNull Function<T, GUIContent> dataConverter,
                                                           final T @NotNull ... data) {
        return new DataGUI<>(GUI.newFullscreenGUI(type, lowerGUISize), dataConverter).setData(data);
    }

    /**
     * Creates a new {@link DataGUI} with the given type, converter, data and a full size {@link #templateGUI}.
     *
     * @param <T>           the type of the data
     * @param type          the type
     * @param dataConverter the data converter
     * @param data          the data
     * @return the data gui
     */
    public static <T> @NotNull DataGUI<T> newFullscreenGUI(final @NotNull GUIType type,
                                                           final @NotNull Function<T, GUIContent> dataConverter,
                                                           final @NotNull Collection<T> data) {
        return new DataGUI<>(GUI.newFullscreenGUI(type), dataConverter).setData(data);
    }

    /**
     * Creates a new {@link DataGUI} with the given type, converter, data and a full size {@link #templateGUI}.
     *
     * @param <T>           the type of the data
     * @param type          the type
     * @param lowerGUISize  the size of the lower GUI
     * @param dataConverter the data converter
     * @param data          the data
     * @return the data gui
     */
    public static <T> @NotNull DataGUI<T> newFullscreenGUI(final @NotNull GUIType type,
                                                           final @Range(from = Constants.MIN_DEFAULT_SIZE, to = Constants.SECONDARY_GUI_SIZE) int lowerGUISize,
                                                           final @NotNull Function<T, GUIContent> dataConverter,
                                                           final @NotNull Collection<T> data) {
        return new DataGUI<>(GUI.newFullscreenGUI(type, lowerGUISize), dataConverter).setData(data);
    }

}
