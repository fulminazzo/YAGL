package it.fulminazzo.yagl.gui;

import it.fulminazzo.yagl.action.BiGUIAction;
import it.fulminazzo.yagl.action.GUIAction;
import it.fulminazzo.yagl.content.CustomItemGUIContent;
import it.fulminazzo.yagl.content.GUIContent;
import it.fulminazzo.yagl.content.ItemGUIContent;
import it.fulminazzo.yagl.item.Item;
import it.fulminazzo.yagl.metadatable.IgnoreApply;
import it.fulminazzo.yagl.metadatable.Metadatable;
import it.fulminazzo.yagl.reflect.Reflect;
import it.fulminazzo.yagl.util.MessageUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A special implementation of {@link SearchGUI} that supports dynamic
 * searching of data from a string.
 *
 * @param <T> the type of the data
 */
public class SearchGUI<T> extends DataGUI<T> {
    /**
     * Because of how Minecraft works,
     * it is necessary to add a default empty name
     * for the content, to avoid displaying the item
     * default Minecraft name.
     */
    public static final @NotNull String EMPTY_RENAME_TEXT = Reflect.on(MessageUtils.class).get("COLOR_CHAR").get()
            + "r";
    private static final @NotNull String SET_DATA_ERROR_MESSAGE = "Use setData with a data converter and search function";

    private @Nullable BiPredicate<T, String> searchFunction;

    @Getter
    private @NotNull String query;

    /**
     * Instantiates a new Search gui.
     *
     * @param templateGUI the templateGUI
     */
    SearchGUI(final @NotNull SearchGUI.SearchFullscreenGUI templateGUI) {
        super(templateGUI);
        this.query = "";
        templateGUI.setSearchGUI(this);
    }

    /**
     * Gets the {@link #query}.
     * If it is empty, {@link #EMPTY_RENAME_TEXT} is returned instead.
     *
     * @return the parsed query
     */
    @NotNull String getParsedQuery() {
        String query = getQuery();
        return query.isEmpty() ? EMPTY_RENAME_TEXT : query;
    }

    /**
     * Sets the {@link #query}.
     * If it starts with {@link #EMPTY_RENAME_TEXT}, the character is removed.
     *
     * @param query the query
     * @return this gui
     */
    public @NotNull SearchGUI<T> setQuery(@NotNull String query) {
        if (query.startsWith(EMPTY_RENAME_TEXT))
            query = query.substring(EMPTY_RENAME_TEXT.length());
        this.query = query;
        return this;
    }

    /**
     * Gets the first page of this GUI.
     *
     * @return the gui
     */
    public @NotNull GUI getFirstPage() {
        return prepareOpenGUI(this.templateGUI, 0);
    }

    @Override
    protected @NotNull SearchGUI<T> initializeNewInstance() {
        return new SearchGUI<>((SearchFullscreenGUI) this.templateGUI.copy());
    }

    @Override
    public @NotNull SearchGUI<T> copyTo(final @NotNull GUI other, final boolean replace) {
        super.copyTo(other, replace);
        if (other instanceof SearchGUI && replace) {
            SearchGUI<?> otherSearchGUI = (SearchGUI<?>) other;
            if (this.dataConverter != null && this.searchFunction != null)
                otherSearchGUI.setData(this.dataConverter, this.searchFunction, getData());
            otherSearchGUI.setQuery(this.query);
        }
        return this;
    }

    /**
     * Occupies the slots of the {@link GUIType#ANVIL} upperGUI,
     * if not already occupied.
     *
     * @param gui  the gui
     * @param page the page
     * @return the parsed gui
     */
    @Override
    protected @NotNull GUI prepareOpenGUI(final @NotNull GUI gui, final @Range(from = 0, to = Integer.MAX_VALUE) int page) {
        for (int i = 0; i < GUIType.ANVIL.getSize(); i++) {
            @NotNull List<GUIContent> contents = getContents(i);
            if (contents.isEmpty())
                setContents(i, ItemGUIContent.newInstance("barrier").setDisplayName(" "));
            if (i == 0) {
                String query = getParsedQuery();
                getContents(i).stream()
                        .filter(c -> c instanceof CustomItemGUIContent)
                        .map(c -> (CustomItemGUIContent<?>) c)
                        .forEach(c -> c.setDisplayName(query));
            }
        }
        return super.prepareOpenGUI(gui
                        .setVariable("query", getQuery())
                        .setVariable("search", getQuery()),
                page
        );
    }

    @Override
    protected @NotNull List<T> getDataList() {
        return super.getDataList().stream()
                .filter(t -> this.searchFunction != null && this.searchFunction.test(t, this.query))
                .collect(Collectors.toList());
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull SearchGUI<T> setPages(final @Range(from = Constants.MIN_TYPE_SIZE, to = Integer.MAX_VALUE) int pages) {
        throw new IllegalStateException(PAGES_ERROR_MESSAGE);
    }

    /**
     * Sets the data and its converter and search function.
     *
     * @param dataConverter  the data converter
     * @param searchFunction the search function
     * @param data           the data
     * @param <D>            the type of the data
     * @return this gui with the new data type
     */
    @SafeVarargs
    public final <D> @NotNull SearchGUI<D> setData(final @NotNull Function<D, GUIContent> dataConverter,
                                                   final @NotNull BiPredicate<D, String> searchFunction,
                                                   final D @NotNull ... data) {
        return setData(dataConverter, searchFunction, Arrays.asList(data));
    }

    /**
     * Sets the data and its converter and search function.
     *
     * @param dataConverter  the data converter
     * @param searchFunction the search function
     * @param data           the data
     * @param <D>            the type of the data
     * @return this gui with the new data type
     */
    public <D> @NotNull SearchGUI<D> setData(final @NotNull Function<D, GUIContent> dataConverter,
                                             final @NotNull BiPredicate<D, String> searchFunction,
                                             final @NotNull Collection<D> data) {
        SearchGUI<D> gui = (SearchGUI<D>) super.setData(dataConverter, data);
        gui.searchFunction = searchFunction;
        return gui;
    }

    /**
     * Sets the given data in the internal data list and updates the data converter function.
     *
     * @param data the data
     * @param <D>  the type of the data
     * @return the data
     * @deprecated Use {@link #setData(Function, BiPredicate, Collection)} instead.
     */
    @Deprecated
    @Override
    public <D> @NotNull SearchGUI<D> setData(final @NotNull Function<D, GUIContent> dataConverter, final D @NotNull ... data) {
        throw new IllegalStateException(SET_DATA_ERROR_MESSAGE);
    }

    /**
     * Sets the given data in the internal data list and updates the data converter function.
     *
     * @param data the data
     * @param <D>  the type of the data
     * @return the data
     * @deprecated Use {@link #setData(Function, BiPredicate, Collection)} instead.
     */
    @Deprecated
    @Override
    public <D> @NotNull SearchGUI<D> setData(final @NotNull Function<D, GUIContent> dataConverter, final @NotNull Collection<D> data) {
        throw new IllegalStateException(SET_DATA_ERROR_MESSAGE);
    }

    /*
     * RETURN TYPE OVERRIDES.
     */

    @Override
    public @NotNull SearchGUI<?> clearData() {
        super.clearData();
        this.searchFunction = null;
        return this;
    }

    @Override
    public @NotNull SearchGUI<T> setPreviousPage(final @Range(from = 0, to = Constants.FULLSCREEN_MAX_SLOT) int slot,
                                                 final @NotNull Item previousPage) {
        return (SearchGUI<T>) super.setPreviousPage(slot, previousPage);
    }

    @Override
    public @NotNull SearchGUI<T> setPreviousPage(final @Range(from = 0, to = Constants.FULLSCREEN_MAX_SLOT) int slot,
                                                 final @NotNull GUIContent previousPage) {
        return (SearchGUI<T>) super.setPreviousPage(slot, previousPage);
    }

    @Override
    public @NotNull SearchGUI<T> unsetPreviousPage() {
        return (SearchGUI<T>) super.unsetPreviousPage();
    }

    @Override
    public @NotNull SearchGUI<T> setNextPage(final @Range(from = 0, to = Constants.FULLSCREEN_MAX_SLOT) int slot,
                                             final @NotNull Item nextPage) {
        return (SearchGUI<T>) super.setNextPage(slot, nextPage);
    }

    @Override
    public @NotNull SearchGUI<T> setNextPage(final @Range(from = 0, to = Constants.FULLSCREEN_MAX_SLOT) int slot,
                                             final @NotNull GUIContent nextPage) {
        return (SearchGUI<T>) super.setNextPage(slot, nextPage);
    }

    @Override
    public @NotNull SearchGUI<T> unsetNextPage() {
        return (SearchGUI<T>) super.unsetNextPage();
    }

    @Override
    public @NotNull SearchGUI<T> setTitle(final @Nullable String title) {
        return (SearchGUI<T>) super.setTitle(title);
    }

    @Override
    public @NotNull SearchGUI<T> setMovable(final @Range(from = 0, to = Constants.FULLSCREEN_MAX_SLOT) int slot,
                                            final boolean movable) {
        return (SearchGUI<T>) super.setMovable(slot, movable);
    }

    @Override
    public @NotNull SearchGUI<T> addContent(final GUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.addContent(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setContents(final @Range(from = 0, to = Constants.FULLSCREEN_MAX_SLOT) int slot,
                                             final GUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setContents(slot, contents);
    }

    @Override
    public @NotNull SearchGUI<T> setContents(final @Range(from = 0, to = Constants.FULLSCREEN_MAX_SLOT) int slot,
                                             final @NotNull Collection<? extends GUIContent> contents) {
        return (SearchGUI<T>) super.setContents(slot, contents);
    }

    @Override
    public @NotNull SearchGUI<T> unsetContent(final @Range(from = 0, to = Constants.FULLSCREEN_MAX_SLOT) int slot) {
        return (SearchGUI<T>) super.unsetContent(slot);
    }

    @Override
    public @NotNull SearchGUI<T> onClickOutside(final @NotNull GUIAction action) {
        return (SearchGUI<T>) super.onClickOutside(action);
    }

    @Override
    public @NotNull SearchGUI<T> onOpenGUI(final @NotNull GUIAction action) {
        return (SearchGUI<T>) super.onOpenGUI(action);
    }

    @Override
    public @NotNull SearchGUI<T> onCloseGUI(final @NotNull GUIAction action) {
        return (SearchGUI<T>) super.onCloseGUI(action);
    }

    @Override
    public @NotNull SearchGUI<T> onChangeGUI(final @NotNull BiGUIAction action) {
        return (SearchGUI<T>) super.onChangeGUI(action);
    }

    @Override
    public @NotNull SearchGUI<T> setAllMovable() {
        return (SearchGUI<T>) super.setAllMovable();
    }

    @Override
    public @NotNull SearchGUI<T> setAllUnmovable() {
        return (SearchGUI<T>) super.setAllUnmovable();
    }

    @Override
    public @NotNull SearchGUI<T> addContent(final Item @NotNull ... contents) {
        return (SearchGUI<T>) super.addContent(contents);
    }

    @Override
    public @NotNull SearchGUI<T> addContent(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (SearchGUI<T>) super.addContent(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setContents(final @Range(from = 0, to = Constants.FULLSCREEN_MAX_SLOT) int slot,
                                             final Item @NotNull ... contents) {
        return (SearchGUI<T>) super.setContents(slot, contents);
    }

    @Override
    public @NotNull SearchGUI<T> setContents(final @Range(from = 0, to = Constants.FULLSCREEN_MAX_SLOT) int slot,
                                             final CustomItemGUIContent<?> @NotNull ... contents) {
        return (SearchGUI<T>) super.setContents(slot, contents);
    }

    @Override
    public @NotNull SearchGUI<T> setAllSides(final Item @NotNull ... contents) {
        return (SearchGUI<T>) super.setAllSides(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setAllSides(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (SearchGUI<T>) super.setAllSides(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setAllSides(final GUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setAllSides(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setAllSides(final @NotNull Collection<? extends GUIContent> contents) {
        return (SearchGUI<T>) super.setAllSides(contents);
    }

    @Override
    public @NotNull SearchGUI<T> unsetAllSides() {
        return (SearchGUI<T>) super.unsetAllSides();
    }

    @Override
    public @NotNull SearchGUI<T> setTopAndBottomSides(final Item @NotNull ... contents) {
        return (SearchGUI<T>) super.setTopAndBottomSides(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setTopAndBottomSides(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (SearchGUI<T>) super.setTopAndBottomSides(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setTopAndBottomSides(final GUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setTopAndBottomSides(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setTopAndBottomSides(final @NotNull Collection<? extends GUIContent> contents) {
        return (SearchGUI<T>) super.setTopAndBottomSides(contents);
    }

    @Override
    public @NotNull SearchGUI<T> unsetTopAndBottomSides() {
        return (SearchGUI<T>) super.unsetTopAndBottomSides();
    }

    @Override
    public @NotNull SearchGUI<T> setLeftAndRightSides(final Item @NotNull ... contents) {
        return (SearchGUI<T>) super.setLeftAndRightSides(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setLeftAndRightSides(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (SearchGUI<T>) super.setLeftAndRightSides(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setLeftAndRightSides(final GUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setLeftAndRightSides(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setLeftAndRightSides(final @NotNull Collection<? extends GUIContent> contents) {
        return (SearchGUI<T>) super.setLeftAndRightSides(contents);
    }

    @Override
    public @NotNull SearchGUI<T> unsetLeftAndRightSides() {
        return (SearchGUI<T>) super.unsetLeftAndRightSides();
    }

    @Override
    public @NotNull SearchGUI<T> setTopSide(final Item @NotNull ... contents) {
        return (SearchGUI<T>) super.setTopSide(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setTopSide(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (SearchGUI<T>) super.setTopSide(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setTopSide(final GUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setTopSide(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setTopSide(final @NotNull Collection<? extends GUIContent> contents) {
        return (SearchGUI<T>) super.setTopSide(contents);
    }

    @Override
    public @NotNull SearchGUI<T> unsetTopSide() {
        return (SearchGUI<T>) super.unsetTopSide();
    }

    @Override
    public @NotNull SearchGUI<T> setLeftSide(final Item @NotNull ... contents) {
        return (SearchGUI<T>) super.setLeftSide(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setLeftSide(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (SearchGUI<T>) super.setLeftSide(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setLeftSide(final GUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setLeftSide(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setLeftSide(final @NotNull Collection<? extends GUIContent> contents) {
        return (SearchGUI<T>) super.setLeftSide(contents);
    }

    @Override
    public @NotNull SearchGUI<T> unsetLeftSide() {
        return (SearchGUI<T>) super.unsetLeftSide();
    }

    @Override
    public @NotNull SearchGUI<T> setBottomSide(final Item @NotNull ... contents) {
        return (SearchGUI<T>) super.setBottomSide(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setBottomSide(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (SearchGUI<T>) super.setBottomSide(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setBottomSide(final GUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setBottomSide(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setBottomSide(final @NotNull Collection<? extends GUIContent> contents) {
        return (SearchGUI<T>) super.setBottomSide(contents);
    }

    @Override
    public @NotNull SearchGUI<T> unsetBottomSide() {
        return (SearchGUI<T>) super.unsetBottomSide();
    }

    @Override
    public @NotNull SearchGUI<T> setRightSide(final Item @NotNull ... contents) {
        return (SearchGUI<T>) super.setRightSide(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setRightSide(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (SearchGUI<T>) super.setRightSide(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setRightSide(final GUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setRightSide(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setRightSide(final @NotNull Collection<? extends GUIContent> contents) {
        return (SearchGUI<T>) super.setRightSide(contents);
    }

    @Override
    public @NotNull SearchGUI<T> unsetRightSide() {
        return (SearchGUI<T>) super.unsetRightSide();
    }

    @Override
    public @NotNull SearchGUI<T> setNorthWest(final Item @NotNull ... contents) {
        return (SearchGUI<T>) super.setNorthWest(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setNorthWest(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (SearchGUI<T>) super.setNorthWest(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setNorthWest(final GUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setNorthWest(contents);
    }

    @Override
    public @NotNull SearchGUI<T> unsetNorthWest() {
        return (SearchGUI<T>) super.unsetNorthWest();
    }

    @Override
    public @NotNull SearchGUI<T> setNorth(final Item @NotNull ... contents) {
        return (SearchGUI<T>) super.setNorth(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setNorth(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (SearchGUI<T>) super.setNorth(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setNorth(final GUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setNorth(contents);
    }

    @Override
    public @NotNull SearchGUI<T> unsetNorth() {
        return (SearchGUI<T>) super.unsetNorth();
    }

    @Override
    public @NotNull SearchGUI<T> setNorthEast(final Item @NotNull ... contents) {
        return (SearchGUI<T>) super.setNorthEast(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setNorthEast(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (SearchGUI<T>) super.setNorthEast(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setNorthEast(final GUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setNorthEast(contents);
    }

    @Override
    public @NotNull SearchGUI<T> unsetNorthEast() {
        return (SearchGUI<T>) super.unsetNorthEast();
    }

    @Override
    public @NotNull SearchGUI<T> setMiddleWest(final Item @NotNull ... contents) {
        return (SearchGUI<T>) super.setMiddleWest(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setMiddleWest(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (SearchGUI<T>) super.setMiddleWest(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setMiddleWest(final GUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setMiddleWest(contents);
    }

    @Override
    public @NotNull SearchGUI<T> unsetMiddleWest() {
        return (SearchGUI<T>) super.unsetMiddleWest();
    }

    @Override
    public @NotNull SearchGUI<T> setMiddle(final Item @NotNull ... contents) {
        return (SearchGUI<T>) super.setMiddle(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setMiddle(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (SearchGUI<T>) super.setMiddle(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setMiddle(final GUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setMiddle(contents);
    }

    @Override
    public @NotNull SearchGUI<T> unsetMiddle() {
        return (SearchGUI<T>) super.unsetMiddle();
    }

    @Override
    public @NotNull SearchGUI<T> setMiddleEast(final Item @NotNull ... contents) {
        return (SearchGUI<T>) super.setMiddleEast(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setMiddleEast(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (SearchGUI<T>) super.setMiddleEast(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setMiddleEast(final GUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setMiddleEast(contents);
    }

    @Override
    public @NotNull SearchGUI<T> unsetMiddleEast() {
        return (SearchGUI<T>) super.unsetMiddleEast();
    }

    @Override
    public @NotNull SearchGUI<T> setSouthWest(final Item @NotNull ... contents) {
        return (SearchGUI<T>) super.setSouthWest(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setSouthWest(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (SearchGUI<T>) super.setSouthWest(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setSouthWest(final GUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setSouthWest(contents);
    }

    @Override
    public @NotNull SearchGUI<T> unsetSouthWest() {
        return (SearchGUI<T>) super.unsetSouthWest();
    }

    @Override
    public @NotNull SearchGUI<T> setSouth(final Item @NotNull ... contents) {
        return (SearchGUI<T>) super.setSouth(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setSouth(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (SearchGUI<T>) super.setSouth(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setSouth(final GUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setSouth(contents);
    }

    @Override
    public @NotNull SearchGUI<T> unsetSouth() {
        return (SearchGUI<T>) super.unsetSouth();
    }

    @Override
    public @NotNull SearchGUI<T> setSouthEast(final Item @NotNull ... contents) {
        return (SearchGUI<T>) super.setSouthEast(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setSouthEast(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (SearchGUI<T>) super.setSouthEast(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setSouthEast(final GUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setSouthEast(contents);
    }

    @Override
    public @NotNull SearchGUI<T> unsetSouthEast() {
        return (SearchGUI<T>) super.unsetSouthEast();
    }

    @Override
    public @NotNull SearchGUI<T> fill(final @NotNull Item content) {
        return (SearchGUI<T>) super.fill(content);
    }

    @Override
    public @NotNull SearchGUI<T> fill(final @NotNull CustomItemGUIContent<?> content) {
        return (SearchGUI<T>) super.fill(content);
    }

    @Override
    public @NotNull SearchGUI<T> fill(final @NotNull GUIContent content) {
        return (SearchGUI<T>) super.fill(content);
    }

    @Override
    public @NotNull SearchGUI<T> clear() {
        return (SearchGUI<T>) super.clear();
    }

    @Override
    public @NotNull SearchGUI<T> onClickOutside(final @NotNull String command) {
        return (SearchGUI<T>) super.onClickOutside(command);
    }

    @Override
    public @NotNull SearchGUI<T> onOpenGUI(final @NotNull String command) {
        return (SearchGUI<T>) super.onOpenGUI(command);
    }

    @Override
    public @NotNull SearchGUI<T> onCloseGUI(final @NotNull String command) {
        return (SearchGUI<T>) super.onCloseGUI(command);
    }

    @Override
    public @NotNull SearchGUI<T> onChangeGUI(final @NotNull String command) {
        return (SearchGUI<T>) super.onChangeGUI(command);
    }

    @Override
    public @NotNull SearchGUI<T> onClickOutsideSend(final @NotNull String message) {
        return (SearchGUI<T>) super.onClickOutsideSend(message);
    }

    @Override
    public @NotNull SearchGUI<T> onOpenGUISend(final @NotNull String message) {
        return (SearchGUI<T>) super.onOpenGUISend(message);
    }

    @Override
    public @NotNull SearchGUI<T> onCloseGUISend(final @NotNull String message) {
        return (SearchGUI<T>) super.onCloseGUISend(message);
    }

    @Override
    public @NotNull SearchGUI<T> onChangeGUISend(final @NotNull String message) {
        return (SearchGUI<T>) super.onChangeGUISend(message);
    }

    @Override
    public @NotNull SearchGUI<T> setVariable(final @NotNull String name, final @NotNull String value) {
        return (SearchGUI<T>) super.setVariable(name, value);
    }

    @Override
    public @NotNull SearchGUI<T> unsetVariable(final @NotNull String name) {
        return (SearchGUI<T>) super.unsetVariable(name);
    }

    @Override
    public @NotNull SearchGUI<T> copyFrom(final @NotNull GUI other, final boolean replace) {
        return (SearchGUI<T>) super.copyFrom(other, replace);
    }

    @Override
    public @NotNull SearchGUI<T> copyVariablesTo(final @NotNull Metadatable other, final boolean replace) {
        return (SearchGUI<T>) super.copyVariablesTo(other, replace);
    }

    @Override
    public @NotNull SearchGUI<T> copyVariablesFrom(final @NotNull Metadatable other, final boolean replace) {
        return (SearchGUI<T>) super.copyVariablesFrom(other, replace);
    }

    @Override
    public @NotNull SearchGUI<T> copy() {
        return (SearchGUI<T>) super.copy();
    }

    /*
     * INITIALIZERS
     */

    /**
     * Creates a new empty search GUI.
     *
     * @return the search gui
     */
    public static @NotNull SearchGUI<?> newSearchGUI() {
        return new SearchGUI<>(new SearchFullscreenGUI());
    }

    /**
     * Creates a new empty search GUI with the given lower GUI size.
     *
     * @param lowerGUISize the size of the lower GUI
     * @return the search gui
     */
    public static @NotNull SearchGUI<?> newSearchGUI(
            final @Range(from = Constants.MIN_DEFAULT_SIZE, to = Constants.SECONDARY_GUI_SIZE) int lowerGUISize
    ) {
        SearchFullscreenGUI gui = new SearchFullscreenGUI();
        gui.getLowerGUI().resize(lowerGUISize);
        return new SearchGUI<>(gui);
    }

    /**
     * An implementation of {@link FullscreenGUI} that provides methods to interface with
     * the corresponding {@link SearchGUI}.
     */
    @EqualsAndHashCode(callSuper = true)
    static class SearchFullscreenGUI extends FullscreenGUI {
        @Setter
        @EqualsAndHashCode.Exclude
        @IgnoreApply
        private @Nullable SearchGUI<?> searchGUI;

        /**
         * Instantiates a new Search full size gui.
         */
        SearchFullscreenGUI() {
            super(GUIType.ANVIL);
        }

        /**
         * Gets the internal search gui.
         * Throws {@link IllegalStateException} if not provided.
         *
         * @return the search gui
         */
        public @NotNull SearchGUI<?> getSearchGUI() {
            if (this.searchGUI == null) throw new IllegalStateException("SearchGUI has not been set yet.");
            return this.searchGUI;
        }

        @Override
        public @NotNull FullscreenGUI copy() {
            return Reflect.on(new SearchFullscreenGUI())
                    .set("upperGUI", getUpperGUI().copy())
                    .set("lowerGUI", getLowerGUI().copy())
                    .set("searchGUI", this.searchGUI)
                    .get();
        }

    }

}
