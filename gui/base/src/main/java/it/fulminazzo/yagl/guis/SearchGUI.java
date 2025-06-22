package it.fulminazzo.yagl.guis;

import it.fulminazzo.fulmicollection.objects.IgnoreField;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yagl.actions.BiGUIAction;
import it.fulminazzo.yagl.actions.GUIAction;
import it.fulminazzo.yagl.contents.GUIContent;
import it.fulminazzo.yagl.contents.ItemGUIContent;
import it.fulminazzo.yagl.exceptions.NotImplemented;
import it.fulminazzo.yagl.items.Item;
import it.fulminazzo.yagl.metadatable.IgnoreApply;
import it.fulminazzo.yagl.metadatable.Metadatable;
import it.fulminazzo.yagl.utils.MessageUtils;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
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
    public static final String EMPTY_RENAME_TEXT =
            new Refl<>(MessageUtils.class).getFieldObject("COLOR_CHAR") + "r";

    private final @NotNull BiPredicate<T, String> searchFunction;

    @Getter
    private @NotNull String query;

    private SearchGUI() {
        super();
        this.searchFunction = (t, s) -> {
            throw new NotImplemented();
        };
        this.query = "";
    }

    private SearchGUI(@NotNull GUI templateGUI) {
        super(templateGUI);
        this.searchFunction = (t, s) -> {
            throw new NotImplemented();
        };
        this.query = "";
    }

    /**
     * Instantiates a new Search gui.
     *
     * @param templateGUI    the templateGUI
     * @param dataConverter  the data converter
     * @param searchFunction the function to use to filter the data
     */
    SearchGUI(@NotNull SearchFullSizeGUI templateGUI,
              @NotNull Function<T, GUIContent> dataConverter,
              @NotNull BiPredicate<T, String> searchFunction) {
        super(templateGUI, dataConverter);
        this.searchFunction = searchFunction;
        this.query = "";
        templateGUI.setSearchGui(this);
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

    /**
     * Occupies the slots of the {@link GUIType#ANVIL} upperGUI,
     * if not already occupied.
     *
     * @param gui  the gui
     * @param page the page
     * @return the parsed gui
     */
    @Override
    protected @NotNull GUI prepareOpenGUI(@NotNull GUI gui, int page) {
        for (int i = 0; i < GUIType.ANVIL.getSize(); i++) {
            @NotNull List<GUIContent> contents = getContents(i);
            if (contents.isEmpty())
                setContents(i, ItemGUIContent.newInstance("glass_pane").setDisplayName(" "));
            if (i == 0) {
                String query = getParsedQuery();
                getContents(i).stream()
                        .filter(c -> c instanceof ItemGUIContent)
                        .map(c -> (ItemGUIContent) c)
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
                .filter(t -> this.searchFunction.test(t, this.query))
                .collect(Collectors.toList());
    }

    @Override
    public SearchGUI<T> copy() {
        return new SearchGUI<>(
                (SearchFullSizeGUI) this.templateGUI.copy(),
                this.dataConverter,
                this.searchFunction
        );
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull SearchGUI<T> setPages(int pages) {
        throw new IllegalStateException(ERROR_MESSAGE);
    }

    @SafeVarargs
    @Override
    public final @NotNull SearchGUI<T> addData(T @NotNull ... data) {
        return (SearchGUI<T>) super.addData(data);
    }

    @Override
    public @NotNull SearchGUI<T> addData(@NotNull Collection<T> data) {
        return (SearchGUI<T>) super.addData(data);
    }

    @SafeVarargs
    @Override
    public final @NotNull SearchGUI<T> setData(T @NotNull ... data) {
        return (SearchGUI<T>) super.setData(data);
    }

    @Override
    public @NotNull SearchGUI<T> setData(@NotNull Collection<T> data) {
        return (SearchGUI<T>) super.setData(data);
    }

    @SafeVarargs
    @Override
    public final @NotNull SearchGUI<T> removeData(T @NotNull ... data) {
        return (SearchGUI<T>) super.removeData(data);
    }

    @Override
    public @NotNull SearchGUI<T> removeData(@NotNull Collection<T> data) {
        return (SearchGUI<T>) super.removeData(data);
    }

    @Override
    public @NotNull SearchGUI<T> removeData(@NotNull Predicate<T> function) {
        return (SearchGUI<T>) super.removeData(function);
    }

    @Override
    public @NotNull SearchGUI<T> clearData() {
        return (SearchGUI<T>) super.clearData();
    }

    @Override
    public @NotNull SearchGUI<T> setPreviousPage(int slot, @NotNull Item previousPage) {
        return (SearchGUI<T>) super.setPreviousPage(slot, previousPage);
    }

    @Override
    public @NotNull SearchGUI<T> setPreviousPage(int slot, @NotNull GUIContent previousPage) {
        return (SearchGUI<T>) super.setPreviousPage(slot, previousPage);
    }

    @Override
    public @NotNull SearchGUI<T> unsetPreviousPage() {
        return (SearchGUI<T>) super.unsetPreviousPage();
    }

    @Override
    public @NotNull SearchGUI<T> setNextPage(int slot, @NotNull Item nextPage) {
        return (SearchGUI<T>) super.setNextPage(slot, nextPage);
    }

    @Override
    public @NotNull SearchGUI<T> setNextPage(int slot, @NotNull GUIContent nextPage) {
        return (SearchGUI<T>) super.setNextPage(slot, nextPage);
    }

    @Override
    public @NotNull SearchGUI<T> unsetNextPage() {
        return (SearchGUI<T>) super.unsetNextPage();
    }

    @Override
    public @NotNull SearchGUI<T> setTitle(@Nullable String title) {
        return (SearchGUI<T>) super.setTitle(title);
    }

    @Override
    public @NotNull SearchGUI<T> setMovable(int slot, boolean movable) {
        return (SearchGUI<T>) super.setMovable(slot, movable);
    }

    @Override
    public @NotNull SearchGUI<T> addContent(GUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.addContent(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setContents(int slot, GUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setContents(slot, contents);
    }

    @Override
    public @NotNull SearchGUI<T> setContents(int slot, @NotNull Collection<GUIContent> contents) {
        return (SearchGUI<T>) super.setContents(slot, contents);
    }

    @Override
    public @NotNull SearchGUI<T> unsetContent(int slot) {
        return (SearchGUI<T>) super.unsetContent(slot);
    }

    @Override
    public @NotNull SearchGUI<T> onClickOutside(@NotNull GUIAction action) {
        return (SearchGUI<T>) super.onClickOutside(action);
    }

    @Override
    public @NotNull SearchGUI<T> onOpenGUI(@NotNull GUIAction action) {
        return (SearchGUI<T>) super.onOpenGUI(action);
    }

    @Override
    public @NotNull SearchGUI<T> onCloseGUI(@NotNull GUIAction action) {
        return (SearchGUI<T>) super.onCloseGUI(action);
    }

    @Override
    public @NotNull SearchGUI<T> onChangeGUI(@NotNull BiGUIAction action) {
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
    public @NotNull SearchGUI<T> addContent(Item @NotNull ... contents) {
        return (SearchGUI<T>) super.addContent(contents);
    }

    @Override
    public @NotNull SearchGUI<T> addContent(ItemGUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.addContent(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setContents(int slot, Item @NotNull ... contents) {
        return (SearchGUI<T>) super.setContents(slot, contents);
    }

    @Override
    public @NotNull SearchGUI<T> setContents(int slot, ItemGUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setContents(slot, contents);
    }

    @Override
    public @NotNull SearchGUI<T> setAllSides(Item @NotNull ... contents) {
        return (SearchGUI<T>) super.setAllSides(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setAllSides(ItemGUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setAllSides(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setAllSides(GUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setAllSides(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setAllSides(@NotNull Collection<GUIContent> contents) {
        return (SearchGUI<T>) super.setAllSides(contents);
    }

    @Override
    public @NotNull SearchGUI<T> unsetAllSides() {
        return (SearchGUI<T>) super.unsetAllSides();
    }

    @Override
    public @NotNull SearchGUI<T> setTopAndBottomSides(Item @NotNull ... contents) {
        return (SearchGUI<T>) super.setTopAndBottomSides(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setTopAndBottomSides(ItemGUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setTopAndBottomSides(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setTopAndBottomSides(GUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setTopAndBottomSides(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setTopAndBottomSides(@NotNull Collection<GUIContent> contents) {
        return (SearchGUI<T>) super.setTopAndBottomSides(contents);
    }

    @Override
    public @NotNull SearchGUI<T> unsetTopAndBottomSides() {
        return (SearchGUI<T>) super.unsetTopAndBottomSides();
    }

    @Override
    public @NotNull SearchGUI<T> setLeftAndRightSides(Item @NotNull ... contents) {
        return (SearchGUI<T>) super.setLeftAndRightSides(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setLeftAndRightSides(ItemGUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setLeftAndRightSides(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setLeftAndRightSides(GUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setLeftAndRightSides(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setLeftAndRightSides(@NotNull Collection<GUIContent> contents) {
        return (SearchGUI<T>) super.setLeftAndRightSides(contents);
    }

    @Override
    public @NotNull SearchGUI<T> unsetLeftAndRightSides() {
        return (SearchGUI<T>) super.unsetLeftAndRightSides();
    }

    @Override
    public @NotNull SearchGUI<T> setTopSide(Item @NotNull ... contents) {
        return (SearchGUI<T>) super.setTopSide(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setTopSide(ItemGUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setTopSide(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setTopSide(GUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setTopSide(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setTopSide(@NotNull Collection<GUIContent> contents) {
        return (SearchGUI<T>) super.setTopSide(contents);
    }

    @Override
    public @NotNull SearchGUI<T> unsetTopSide() {
        return (SearchGUI<T>) super.unsetTopSide();
    }

    @Override
    public @NotNull SearchGUI<T> setLeftSide(Item @NotNull ... contents) {
        return (SearchGUI<T>) super.setLeftSide(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setLeftSide(ItemGUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setLeftSide(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setLeftSide(GUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setLeftSide(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setLeftSide(@NotNull Collection<GUIContent> contents) {
        return (SearchGUI<T>) super.setLeftSide(contents);
    }

    @Override
    public @NotNull SearchGUI<T> unsetLeftSide() {
        return (SearchGUI<T>) super.unsetLeftSide();
    }

    @Override
    public @NotNull SearchGUI<T> setBottomSide(Item @NotNull ... contents) {
        return (SearchGUI<T>) super.setBottomSide(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setBottomSide(ItemGUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setBottomSide(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setBottomSide(GUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setBottomSide(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setBottomSide(@NotNull Collection<GUIContent> contents) {
        return (SearchGUI<T>) super.setBottomSide(contents);
    }

    @Override
    public @NotNull SearchGUI<T> unsetBottomSide() {
        return (SearchGUI<T>) super.unsetBottomSide();
    }

    @Override
    public @NotNull SearchGUI<T> setRightSide(Item @NotNull ... contents) {
        return (SearchGUI<T>) super.setRightSide(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setRightSide(ItemGUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setRightSide(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setRightSide(GUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setRightSide(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setRightSide(@NotNull Collection<GUIContent> contents) {
        return (SearchGUI<T>) super.setRightSide(contents);
    }

    @Override
    public @NotNull SearchGUI<T> unsetRightSide() {
        return (SearchGUI<T>) super.unsetRightSide();
    }

    @Override
    public @NotNull SearchGUI<T> setNorthWest(Item @NotNull ... contents) {
        return (SearchGUI<T>) super.setNorthWest(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setNorthWest(ItemGUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setNorthWest(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setNorthWest(GUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setNorthWest(contents);
    }

    @Override
    public @NotNull SearchGUI<T> unsetNorthWest() {
        return (SearchGUI<T>) super.unsetNorthWest();
    }

    @Override
    public @NotNull SearchGUI<T> setNorth(Item @NotNull ... contents) {
        return (SearchGUI<T>) super.setNorth(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setNorth(ItemGUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setNorth(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setNorth(GUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setNorth(contents);
    }

    @Override
    public @NotNull SearchGUI<T> unsetNorth() {
        return (SearchGUI<T>) super.unsetNorth();
    }

    @Override
    public @NotNull SearchGUI<T> setNorthEast(Item @NotNull ... contents) {
        return (SearchGUI<T>) super.setNorthEast(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setNorthEast(ItemGUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setNorthEast(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setNorthEast(GUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setNorthEast(contents);
    }

    @Override
    public @NotNull SearchGUI<T> unsetNorthEast() {
        return (SearchGUI<T>) super.unsetNorthEast();
    }

    @Override
    public @NotNull SearchGUI<T> setMiddleWest(Item @NotNull ... contents) {
        return (SearchGUI<T>) super.setMiddleWest(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setMiddleWest(ItemGUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setMiddleWest(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setMiddleWest(GUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setMiddleWest(contents);
    }

    @Override
    public @NotNull SearchGUI<T> unsetMiddleWest() {
        return (SearchGUI<T>) super.unsetMiddleWest();
    }

    @Override
    public @NotNull SearchGUI<T> setMiddle(Item @NotNull ... contents) {
        return (SearchGUI<T>) super.setMiddle(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setMiddle(ItemGUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setMiddle(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setMiddle(GUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setMiddle(contents);
    }

    @Override
    public @NotNull SearchGUI<T> unsetMiddle() {
        return (SearchGUI<T>) super.unsetMiddle();
    }

    @Override
    public @NotNull SearchGUI<T> setMiddleEast(Item @NotNull ... contents) {
        return (SearchGUI<T>) super.setMiddleEast(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setMiddleEast(ItemGUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setMiddleEast(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setMiddleEast(GUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setMiddleEast(contents);
    }

    @Override
    public @NotNull SearchGUI<T> unsetMiddleEast() {
        return (SearchGUI<T>) super.unsetMiddleEast();
    }

    @Override
    public @NotNull SearchGUI<T> setSouthWest(Item @NotNull ... contents) {
        return (SearchGUI<T>) super.setSouthWest(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setSouthWest(ItemGUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setSouthWest(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setSouthWest(GUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setSouthWest(contents);
    }

    @Override
    public @NotNull SearchGUI<T> unsetSouthWest() {
        return (SearchGUI<T>) super.unsetSouthWest();
    }

    @Override
    public @NotNull SearchGUI<T> setSouth(Item @NotNull ... contents) {
        return (SearchGUI<T>) super.setSouth(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setSouth(ItemGUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setSouth(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setSouth(GUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setSouth(contents);
    }

    @Override
    public @NotNull SearchGUI<T> unsetSouth() {
        return (SearchGUI<T>) super.unsetSouth();
    }

    @Override
    public @NotNull SearchGUI<T> setSouthEast(Item @NotNull ... contents) {
        return (SearchGUI<T>) super.setSouthEast(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setSouthEast(ItemGUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setSouthEast(contents);
    }

    @Override
    public @NotNull SearchGUI<T> setSouthEast(GUIContent @NotNull ... contents) {
        return (SearchGUI<T>) super.setSouthEast(contents);
    }

    @Override
    public @NotNull SearchGUI<T> unsetSouthEast() {
        return (SearchGUI<T>) super.unsetSouthEast();
    }

    @Override
    public @NotNull SearchGUI<T> fill(@NotNull Item content) {
        return (SearchGUI<T>) super.fill(content);
    }

    @Override
    public @NotNull SearchGUI<T> fill(@NotNull ItemGUIContent content) {
        return (SearchGUI<T>) super.fill(content);
    }

    @Override
    public @NotNull SearchGUI<T> fill(@NotNull GUIContent content) {
        return (SearchGUI<T>) super.fill(content);
    }

    @Override
    public @NotNull SearchGUI<T> clear() {
        return (SearchGUI<T>) super.clear();
    }

    @Override
    public @NotNull SearchGUI<T> onClickOutside(@NotNull String command) {
        return (SearchGUI<T>) super.onClickOutside(command);
    }

    @Override
    public @NotNull SearchGUI<T> onOpenGUI(@NotNull String command) {
        return (SearchGUI<T>) super.onOpenGUI(command);
    }

    @Override
    public @NotNull SearchGUI<T> onCloseGUI(@NotNull String command) {
        return (SearchGUI<T>) super.onCloseGUI(command);
    }

    @Override
    public @NotNull SearchGUI<T> onChangeGUI(@NotNull String command) {
        return (SearchGUI<T>) super.onChangeGUI(command);
    }

    @Override
    public @NotNull SearchGUI<T> onClickOutsideSend(@NotNull String message) {
        return (SearchGUI<T>) super.onClickOutsideSend(message);
    }

    @Override
    public @NotNull SearchGUI<T> onOpenGUISend(@NotNull String message) {
        return (SearchGUI<T>) super.onOpenGUISend(message);
    }

    @Override
    public @NotNull SearchGUI<T> onCloseGUISend(@NotNull String message) {
        return (SearchGUI<T>) super.onCloseGUISend(message);
    }

    @Override
    public @NotNull SearchGUI<T> onChangeGUISend(@NotNull String message) {
        return (SearchGUI<T>) super.onChangeGUISend(message);
    }

    @Override
    public @NotNull SearchGUI<T> setVariable(@NotNull String name, @NotNull String value) {
        return (SearchGUI<T>) super.setVariable(name, value);
    }

    @Override
    public @NotNull SearchGUI<T> unsetVariable(@NotNull String name) {
        return (SearchGUI<T>) super.unsetVariable(name);
    }

    @Override
    public @NotNull SearchGUI<T> copyAll(@NotNull GUI other, boolean replace) {
        return (SearchGUI<T>) super.copyAll(other, replace);
    }

    @Override
    public @NotNull SearchGUI<T> copyFrom(@NotNull GUI other, boolean replace) {
        return (SearchGUI<T>) super.copyFrom(other, replace);
    }

    @Override
    public @NotNull SearchGUI<T> copyAll(@NotNull Metadatable other, boolean replace) {
        return (SearchGUI<T>) super.copyAll(other, replace);
    }

    @Override
    public @NotNull SearchGUI<T> copyFrom(@NotNull Metadatable other, boolean replace) {
        return (SearchGUI<T>) super.copyFrom(other, replace);
    }

    /**
     * Creates a new {@link SearchGUI} with the given type and a converter.
     *
     * @param <T>            the type of the data
     * @param dataConverter  the data converter
     * @param searchFunction the function to use to filter the data according to the query
     * @return the search gui
     */
    public static <T> @NotNull SearchGUI<T> newGUI(
            final @NotNull Function<T, GUIContent> dataConverter,
            final @NotNull BiPredicate<T, String> searchFunction) {
        return new SearchGUI<>(new SearchFullSizeGUI(), dataConverter, searchFunction);
    }

    /**
     * Creates a new {@link SearchGUI} with the given type and a converter.
     *
     * @param <T>            the type of the data
     * @param lowerGUISize   the size of the lower GUI
     * @param dataConverter  the data converter
     * @param searchFunction the function to use to filter the data according to the query
     * @return the search gui
     */
    public static <T> @NotNull SearchGUI<T> newGUI(
            final int lowerGUISize,
            final @NotNull Function<T, GUIContent> dataConverter,
            final @NotNull BiPredicate<T, String> searchFunction) {
        SearchFullSizeGUI gui = new SearchFullSizeGUI();
        gui.getLowerGUI().resize(lowerGUISize);
        return new SearchGUI<>(gui, dataConverter, searchFunction);
    }

    /**
     * Creates a new {@link SearchGUI} with the given type, converter and data.
     *
     * @param <T>            the type of the data
     * @param dataConverter  the data converter
     * @param searchFunction the function to use to filter the data according to the query
     * @param data           the data
     * @return the search gui
     */
    @SafeVarargs
    public static <T> @NotNull SearchGUI<T> newGUI(
            final @NotNull Function<T, GUIContent> dataConverter,
            final @NotNull BiPredicate<T, String> searchFunction,
            final T @NotNull ... data) {
        return new SearchGUI<>(new SearchFullSizeGUI(), dataConverter, searchFunction).setData(data);
    }

    /**
     * Creates a new {@link SearchGUI} with the given type, converter and data.
     *
     * @param <T>            the type of the data
     * @param lowerGUISize   the size of the lower GUI
     * @param dataConverter  the data converter
     * @param searchFunction the function to use to filter the data according to the query
     * @param data           the data
     * @return the search gui
     */
    @SafeVarargs
    public static <T> @NotNull SearchGUI<T> newGUI(
            final int lowerGUISize,
            final @NotNull Function<T, GUIContent> dataConverter,
            final @NotNull BiPredicate<T, String> searchFunction,
            final T @NotNull ... data) {
        SearchFullSizeGUI gui = new SearchFullSizeGUI();
        gui.getLowerGUI().resize(lowerGUISize);
        return new SearchGUI<>(gui, dataConverter, searchFunction).setData(data);
    }

    /**
     * Creates a new {@link SearchGUI} with the given type, converter and data.
     *
     * @param <T>            the type of the data
     * @param dataConverter  the data converter
     * @param searchFunction the function to use to filter the data according to the query
     * @param data           the data
     * @return the search gui
     */
    public static <T> @NotNull SearchGUI<T> newGUI(
            final @NotNull Function<T, GUIContent> dataConverter,
            final @NotNull BiPredicate<T, String> searchFunction,
            final @NotNull Collection<T> data) {
        return new SearchGUI<>(new SearchFullSizeGUI(), dataConverter, searchFunction).setData(data);
    }

    /**
     * Creates a new {@link SearchGUI} with the given type, converter and data.
     *
     * @param <T>            the type of the data
     * @param lowerGUISize   the size of the lower GUI
     * @param dataConverter  the data converter
     * @param searchFunction the function to use to filter the data according to the query
     * @param data           the data
     * @return the search gui
     */
    public static <T> @NotNull SearchGUI<T> newGUI(
            final int lowerGUISize,
            final @NotNull Function<T, GUIContent> dataConverter,
            final @NotNull BiPredicate<T, String> searchFunction,
            final @NotNull Collection<T> data) {
        SearchFullSizeGUI gui = new SearchFullSizeGUI();
        gui.getLowerGUI().resize(lowerGUISize);
        return new SearchGUI<>(gui, dataConverter, searchFunction).setData(data);
    }

    /**
     * An implementation of {@link FullSizeGUI} that provides methods to interface with
     * the corresponding {@link SearchGUI}.
     */
    static class SearchFullSizeGUI extends FullSizeGUI {
        @Setter
        @IgnoreField
        @IgnoreApply
        private @Nullable SearchGUI<?> searchGui;

        /**
         * Instantiates a new Search full size gui.
         */
        SearchFullSizeGUI() {
            super(GUIType.ANVIL);
        }

        /**
         * Gets the internal search gui.
         * Throws {@link IllegalStateException} if not provided.
         *
         * @return the search gui
         */
        public @NotNull SearchGUI<?> getSearchGui() {
            if (this.searchGui == null) throw new IllegalStateException("SearchGUI has not been set yet.");
            return this.searchGui;
        }

        @Override
        public FullSizeGUI copy() {
            return new Refl<>(new SearchFullSizeGUI())
                    .setFieldObject("upperGUI", getUpperGUI().copy())
                    .setFieldObject("lowerGUI", getLowerGUI().copy())
                    .setFieldObject("searchGui", this.searchGui)
                    .getObject();
        }
    }

}
