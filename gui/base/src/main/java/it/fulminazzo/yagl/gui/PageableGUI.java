package it.fulminazzo.yagl.gui;

import it.fulminazzo.fulmicollection.objects.FieldEquable;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.structures.tuples.Tuple;
import it.fulminazzo.yagl.metadatable.Metadatable;
import it.fulminazzo.yagl.action.BiGUIAction;
import it.fulminazzo.yagl.action.GUIAction;
import it.fulminazzo.yagl.content.GUIContent;
import it.fulminazzo.yagl.content.ItemGUIContent;
import it.fulminazzo.yagl.item.Item;
import it.fulminazzo.yagl.viewer.Viewer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * An implementation of {@link GUI} that allows multiple GUI pages to be added.
 */
public class PageableGUI extends FieldEquable implements Iterable<GUI>, Metadatable, GUI {
    /**
     * The Template gui.
     */
    protected final @NotNull GUI templateGUI;
    private final List<GUI> pages = new LinkedList<>();
    private final Map<String, String> variables = new HashMap<>();

    /**
     * The Previous page.
     */
    protected final Tuple<Integer, GUIContent> previousPage = new Tuple<>();
    /**
     * The Next page.
     */
    protected final Tuple<Integer, GUIContent> nextPage = new Tuple<>();

    /**
     * Instantiates a new Pageable gui.
     *
     * @param templateGUI the template gui
     */
    PageableGUI(final @NotNull GUI templateGUI) {
        this.templateGUI = templateGUI;
    }

    /**
     * Instantiates a new Pageable gui.
     */
    PageableGUI() {
        this(new DefaultGUI());
    }

    /**
     * Gets the number of pages
     *
     * @return the pages
     */
    public int pages() {
        return this.pages.size();
    }

    /**
     * Gets the {@link GUI} page from the given index.
     * The index starts from <b>0</b>.
     *
     * @param page the page
     * @return the corresponding {@link GUI} page
     */
    public @NotNull GUI getPage(final int page) {
        try {
            return this.pages.get(page);
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException(String.format("Could not find page '%s'", page));
        }
    }

    /**
     * Sets pages.
     *
     * @param pages the pages
     * @return this gui
     */
    public @NotNull PageableGUI setPages(final int pages) {
        if (pages < 0) throw new IllegalArgumentException(String.format("Invalid pages '%s'", pages));
        int s;
        while ((s = this.pages.size()) - pages > 0) this.pages.remove(s - 1);
        while (pages - this.pages.size() > 0) this.pages.add(copyTemplateGUI());
        return this;
    }

    /**
     * Copies the template gui.
     *
     * @return the gui
     */
    protected @NotNull GUI copyTemplateGUI() {
        final GUI newTemplateGUI;
        if (this.templateGUI instanceof FullSizeGUI) {
            FullSizeGUI fullSizeGUI = (FullSizeGUI) this.templateGUI;
            newTemplateGUI = new Refl<>(GUI.newFullSizeGUI(9))
                    .setFieldObject("upperGUI", fullSizeGUI.getUpperGUI().copy())
                    .setFieldObject("lowerGUI", fullSizeGUI.getLowerGUI().copy())
                    .getObject();
        } else {
            if (this.templateGUI instanceof TypeGUI)
                newTemplateGUI = GUI.newGUI(((TypeGUI) this.templateGUI).getInventoryType());
            else newTemplateGUI = GUI.newGUI(this.templateGUI.size());
        }
        return newTemplateGUI;
    }

    /**
     * Sets the previous page content.
     * When clicking on it, the previous page will be opened.
     *
     * @param slot         the slot
     * @param previousPage the previous page
     * @return the previous page
     */
    public @NotNull PageableGUI setPreviousPage(final int slot, final @NotNull Item previousPage) {
        return setPreviousPage(slot, ItemGUIContent.newInstance(previousPage));
    }

    /**
     * Sets the previous page content.
     * When clicking on it, the previous page will be opened.
     *
     * @param slot         the slot
     * @param previousPage the previous page
     * @return the previous page
     */
    public @NotNull PageableGUI setPreviousPage(final int slot, final @NotNull ItemGUIContent previousPage) {
        return setPreviousPage(slot, (GUIContent) previousPage);
    }

    /**
     * Sets the previous page content.
     * When clicking on it, the previous page will be opened.
     *
     * @param slot         the slot
     * @param previousPage the previous page
     * @return the previous page
     */
    public @NotNull PageableGUI setPreviousPage(final int slot, final @NotNull GUIContent previousPage) {
        this.previousPage.set(checkSlot(slot), previousPage);
        return this;
    }

    /**
     * Removes the previous page set with {@link #setPreviousPage(int, GUIContent)}.
     *
     * @return the pageable gui
     */
    public @NotNull PageableGUI unsetPreviousPage() {
        this.previousPage.set(null, null);
        return this;
    }

    /**
     * Sets the next page content.
     * When clicking on it, the next page will be opened.
     *
     * @param slot     the slot
     * @param nextPage the next page
     * @return the next page
     */
    public @NotNull PageableGUI setNextPage(final int slot, final @NotNull Item nextPage) {
        return setNextPage(slot, ItemGUIContent.newInstance(nextPage));
    }

    /**
     * Sets the next page content.
     * When clicking on it, the next page will be opened.
     *
     * @param slot     the slot
     * @param nextPage the next page
     * @return the next page
     */
    public @NotNull PageableGUI setNextPage(final int slot, final @NotNull ItemGUIContent nextPage) {
        return setNextPage(slot, (GUIContent) nextPage);
    }

    /**
     * Sets the next page content.
     * When clicking on it, the next page will be opened.
     *
     * @param slot     the slot
     * @param nextPage the next page
     * @return the next page
     */
    public @NotNull PageableGUI setNextPage(final int slot, final @NotNull GUIContent nextPage) {
        this.nextPage.set(checkSlot(slot), nextPage);
        return this;
    }

    /**
     * Removes the next page set with {@link #setNextPage(int, GUIContent)}.
     *
     * @return the pageable gui
     */
    public @NotNull PageableGUI unsetNextPage() {
        this.nextPage.set(null, null);
        return this;
    }

    private int checkSlot(final int slot) {
        if (slot < 0 || slot > size()) {
            final String message = String.format("'slot' must be between %s and %s", 0, size());
            throw new IllegalArgumentException(message);
        }
        return slot;
    }

    @NotNull
    @Override
    public Iterator<GUI> iterator() {
        return this.pages.iterator();
    }

    /**
     * Opens the current GUI for the given viewer at the first page.
     *
     * @param viewer the viewer
     */
    @Override
    public void open(final @NotNull Viewer viewer) {
        open(viewer, 0);
    }

    /**
     * Opens the current GUI for the given viewer at the specified page.
     *
     * @param viewer the viewer
     * @param page   the page
     */
    public void open(final @NotNull Viewer viewer, final int page) {
        prepareOpenGUI(getPage(page), page).open(viewer);
    }

    /**
     * Prepares the {@link GUI} at the given page for {@link #open(Viewer, int)}.
     *
     * @param gui  the gui
     * @param page the page
     * @return the gui
     */
    protected @NotNull GUI prepareOpenGUI(final @NotNull GUI gui, final int page) {
        GUI newGUI = gui.copy().copyFrom(this, false)
                .setVariable("page", String.valueOf(page + 1))
                .setVariable("previous_page", String.valueOf(page))
                .setVariable("next_page", String.valueOf(page + 2))
                .setVariable("pages", String.valueOf(pages()));
        if (page > 0) this.previousPage.ifPresent((s, p) ->
                newGUI.setContents(s, p.copy().onClickItem((v, g, i) -> open(v, page - 1))));
        if (page + 1 < pages()) this.nextPage.ifPresent((s, p) ->
                newGUI.setContents(s, p.copy().onClickItem((v, g, i) -> open(v, page + 1))));
        return newGUI;
    }

    /**
     * Counts the empty slots of the current GUI.
     * If {@link #setPreviousPage(int, GUIContent)} or {@link #setNextPage(int, GUIContent)}
     * were used, their slots are removed from the final count.
     *
     * @return the slots
     */
    @Override
    public @NotNull Set<Integer> emptySlots() {
        final Set<Integer> slots = GUI.super.emptySlots();
        this.previousPage.ifPresent((i, p) -> slots.remove(i));
        this.nextPage.ifPresent((i, p) -> slots.remove(i));
        return slots;
    }

    @Override
    public @NotNull PageableGUI setTitle(final @Nullable String title) {
        this.templateGUI.setTitle(title);
        return this;
    }

    @Override
    public @Nullable String getTitle() {
        return this.templateGUI.getTitle();
    }

    @Override
    public int size() {
        return this.templateGUI.size();
    }

    @Override
    public boolean isMovable(int slot) {
        return this.templateGUI.isMovable(slot);
    }

    @Override
    public @NotNull PageableGUI setMovable(int slot, boolean movable) {
        this.templateGUI.setMovable(slot, movable);
        return this;
    }

    @Override
    public @NotNull List<GUIContent> getContents(int slot) {
        return this.templateGUI.getContents(slot);
    }

    @Override
    public @NotNull List<GUIContent> getContents() {
        return this.templateGUI.getContents();
    }

    @Override
    public @NotNull PageableGUI addContent(GUIContent @NotNull ... contents) {
        this.templateGUI.addContent(contents);
        return this;
    }

    @Override
    public @NotNull PageableGUI setContents(int slot, GUIContent @NotNull ... contents) {
        this.templateGUI.setContents(slot, contents);
        return this;
    }

    @Override
    public @NotNull Set<Integer> topSlots() {
        return this.templateGUI.topSlots();
    }

    @Override
    public @NotNull Set<Integer> leftSlots() {
        return this.templateGUI.leftSlots();
    }

    @Override
    public @NotNull Set<Integer> bottomSlots() {
        return this.templateGUI.bottomSlots();
    }

    @Override
    public @NotNull Set<Integer> rightSlots() {
        return this.templateGUI.rightSlots();
    }

    @Override
    public int northWest() {
        return this.templateGUI.northWest();
    }

    @Override
    public int north() {
        return this.templateGUI.north();
    }

    @Override
    public int northEast() {
        return this.templateGUI.northEast();
    }

    @Override
    public int middleLine() {
        return this.templateGUI.middleLine();
    }

    @Override
    public int middleWest() {
        return this.templateGUI.middleWest();
    }

    @Override
    public int middle() {
        return this.templateGUI.middle();
    }

    @Override
    public int middleEast() {
        return this.templateGUI.middleEast();
    }

    @Override
    public int southLine() {
        return this.templateGUI.southLine();
    }

    @Override
    public int southWest() {
        return this.templateGUI.southWest();
    }

    @Override
    public int south() {
        return this.templateGUI.south();
    }

    @Override
    public int southEast() {
        return this.templateGUI.southEast();
    }

    @Override
    public int rows() {
        return this.templateGUI.rows();
    }

    @Override
    public int columns() {
        return this.templateGUI.columns();
    }

    @Override
    public @NotNull PageableGUI clear() {
        this.templateGUI.clear();
        return this;
    }

    @Override
    public @NotNull PageableGUI setContents(int slot, @NotNull Collection<GUIContent> contents) {
        return (PageableGUI) GUI.super.setContents(slot, contents);
    }

    @Override
    public @NotNull PageableGUI unsetContent(int slot) {
        this.templateGUI.unsetContent(slot);
        return this;
    }

    @Override
    public @NotNull PageableGUI onClickOutside(final @NotNull GUIAction action) {
        this.templateGUI.onClickOutside(action);
        return this;
    }

    @Override
    public @NotNull Optional<GUIAction> clickOutsideAction() {
        return this.templateGUI.clickOutsideAction();
    }

    @Override
    public @NotNull PageableGUI onOpenGUI(final @NotNull GUIAction action) {
        this.templateGUI.onOpenGUI(action);
        return this;
    }

    @Override
    public @NotNull Optional<GUIAction> openGUIAction() {
        return this.templateGUI.openGUIAction();
    }

    @Override
    public @NotNull PageableGUI onCloseGUI(final @NotNull GUIAction action) {
        this.templateGUI.onCloseGUI(action);
        return this;
    }

    @Override
    public @NotNull Optional<GUIAction> closeGUIAction() {
        return this.templateGUI.closeGUIAction();
    }

    @Override
    public @NotNull PageableGUI onChangeGUI(final @NotNull BiGUIAction action) {
        this.templateGUI.onChangeGUI(action);
        return this;
    }

    @Override
    public @NotNull Optional<BiGUIAction> changeGUIAction() {
        return this.templateGUI.changeGUIAction();
    }

    @Override
    public @NotNull PageableGUI setAllMovable() {
        return (PageableGUI) GUI.super.setAllMovable();
    }

    @Override
    public @NotNull PageableGUI setAllUnmovable() {
        return (PageableGUI) GUI.super.setAllUnmovable();
    }

    @Override
    public @NotNull PageableGUI addContent(Item @NotNull ... contents) {
        return (PageableGUI) GUI.super.addContent(contents);
    }

    @Override
    public @NotNull PageableGUI addContent(ItemGUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.addContent(contents);
    }

    @Override
    public @NotNull PageableGUI setContents(int slot, Item @NotNull ... contents) {
        return (PageableGUI) GUI.super.setContents(slot, contents);
    }

    @Override
    public @NotNull PageableGUI setContents(int slot, ItemGUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setContents(slot, contents);
    }

    @Override
    public @NotNull PageableGUI setAllSides(Item @NotNull ... contents) {
        return (PageableGUI) GUI.super.setAllSides(contents);
    }

    @Override
    public @NotNull PageableGUI setAllSides(ItemGUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setAllSides(contents);
    }

    @Override
    public @NotNull PageableGUI setAllSides(GUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setAllSides(contents);
    }

    @Override
    public @NotNull PageableGUI setAllSides(@NotNull Collection<GUIContent> contents) {
        return (PageableGUI) GUI.super.setAllSides(contents);
    }

    @Override
    public @NotNull PageableGUI unsetAllSides() {
        return (PageableGUI) GUI.super.unsetAllSides();
    }

    @Override
    public @NotNull PageableGUI setTopAndBottomSides(Item @NotNull ... contents) {
        return (PageableGUI) GUI.super.setTopAndBottomSides(contents);
    }

    @Override
    public @NotNull PageableGUI setTopAndBottomSides(ItemGUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setTopAndBottomSides(contents);
    }

    @Override
    public @NotNull PageableGUI setTopAndBottomSides(GUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setTopAndBottomSides(contents);
    }

    @Override
    public @NotNull PageableGUI setTopAndBottomSides(@NotNull Collection<GUIContent> contents) {
        return (PageableGUI) GUI.super.setTopAndBottomSides(contents);
    }

    @Override
    public @NotNull PageableGUI unsetTopAndBottomSides() {
        return (PageableGUI) GUI.super.unsetTopAndBottomSides();
    }

    @Override
    public @NotNull PageableGUI setLeftAndRightSides(Item @NotNull ... contents) {
        return (PageableGUI) GUI.super.setLeftAndRightSides(contents);
    }

    @Override
    public @NotNull PageableGUI setLeftAndRightSides(ItemGUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setLeftAndRightSides(contents);
    }

    @Override
    public @NotNull PageableGUI setLeftAndRightSides(GUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setLeftAndRightSides(contents);
    }

    @Override
    public @NotNull PageableGUI setLeftAndRightSides(@NotNull Collection<GUIContent> contents) {
        return (PageableGUI) GUI.super.setLeftAndRightSides(contents);
    }

    @Override
    public @NotNull PageableGUI unsetLeftAndRightSides() {
        return (PageableGUI) GUI.super.unsetLeftAndRightSides();
    }

    @Override
    public @NotNull PageableGUI setTopSide(Item @NotNull ... contents) {
        return (PageableGUI) GUI.super.setTopSide(contents);
    }

    @Override
    public @NotNull PageableGUI setTopSide(ItemGUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setTopSide(contents);
    }

    @Override
    public @NotNull PageableGUI setTopSide(GUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setTopSide(contents);
    }

    @Override
    public @NotNull PageableGUI setTopSide(@NotNull Collection<GUIContent> contents) {
        return (PageableGUI) GUI.super.setTopSide(contents);
    }

    @Override
    public @NotNull PageableGUI unsetTopSide() {
        return (PageableGUI) GUI.super.unsetTopSide();
    }

    @Override
    public @NotNull PageableGUI setLeftSide(Item @NotNull ... contents) {
        return (PageableGUI) GUI.super.setLeftSide(contents);
    }

    @Override
    public @NotNull PageableGUI setLeftSide(ItemGUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setLeftSide(contents);
    }

    @Override
    public @NotNull PageableGUI setLeftSide(GUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setLeftSide(contents);
    }

    @Override
    public @NotNull PageableGUI setLeftSide(@NotNull Collection<GUIContent> contents) {
        return (PageableGUI) GUI.super.setLeftSide(contents);
    }

    @Override
    public @NotNull PageableGUI unsetLeftSide() {
        return (PageableGUI) GUI.super.unsetLeftSide();
    }

    @Override
    public @NotNull PageableGUI setBottomSide(Item @NotNull ... contents) {
        return (PageableGUI) GUI.super.setBottomSide(contents);
    }

    @Override
    public @NotNull PageableGUI setBottomSide(ItemGUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setBottomSide(contents);
    }

    @Override
    public @NotNull PageableGUI setBottomSide(GUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setBottomSide(contents);
    }

    @Override
    public @NotNull PageableGUI setBottomSide(@NotNull Collection<GUIContent> contents) {
        return (PageableGUI) GUI.super.setBottomSide(contents);
    }

    @Override
    public @NotNull PageableGUI unsetBottomSide() {
        return (PageableGUI) GUI.super.unsetBottomSide();
    }

    @Override
    public @NotNull PageableGUI setRightSide(Item @NotNull ... contents) {
        return (PageableGUI) GUI.super.setRightSide(contents);
    }

    @Override
    public @NotNull PageableGUI setRightSide(ItemGUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setRightSide(contents);
    }

    @Override
    public @NotNull PageableGUI setRightSide(GUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setRightSide(contents);
    }

    @Override
    public @NotNull PageableGUI setRightSide(@NotNull Collection<GUIContent> contents) {
        return (PageableGUI) GUI.super.setRightSide(contents);
    }

    @Override
    public @NotNull PageableGUI unsetRightSide() {
        return (PageableGUI) GUI.super.unsetRightSide();
    }

    @Override
    public @NotNull PageableGUI setNorthWest(Item @NotNull ... contents) {
        return (PageableGUI) GUI.super.setNorthWest(contents);
    }

    @Override
    public @NotNull PageableGUI setNorthWest(ItemGUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setNorthWest(contents);
    }

    @Override
    public @NotNull PageableGUI setNorthWest(GUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setNorthWest(contents);
    }

    @Override
    public @NotNull PageableGUI unsetNorthWest() {
        return (PageableGUI) GUI.super.unsetNorthWest();
    }

    @Override
    public @NotNull PageableGUI setNorth(Item @NotNull ... contents) {
        return (PageableGUI) GUI.super.setNorth(contents);
    }

    @Override
    public @NotNull PageableGUI setNorth(ItemGUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setNorth(contents);
    }

    @Override
    public @NotNull PageableGUI setNorth(GUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setNorth(contents);
    }

    @Override
    public @NotNull PageableGUI unsetNorth() {
        return (PageableGUI) GUI.super.unsetNorth();
    }

    @Override
    public @NotNull PageableGUI setNorthEast(Item @NotNull ... contents) {
        return (PageableGUI) GUI.super.setNorthEast(contents);
    }

    @Override
    public @NotNull PageableGUI setNorthEast(ItemGUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setNorthEast(contents);
    }

    @Override
    public @NotNull PageableGUI setNorthEast(GUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setNorthEast(contents);
    }

    @Override
    public @NotNull PageableGUI unsetNorthEast() {
        return (PageableGUI) GUI.super.unsetNorthEast();
    }

    @Override
    public @NotNull PageableGUI setMiddleWest(Item @NotNull ... contents) {
        return (PageableGUI) GUI.super.setMiddleWest(contents);
    }

    @Override
    public @NotNull PageableGUI setMiddleWest(ItemGUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setMiddleWest(contents);
    }

    @Override
    public @NotNull PageableGUI setMiddleWest(GUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setMiddleWest(contents);
    }

    @Override
    public @NotNull PageableGUI unsetMiddleWest() {
        return (PageableGUI) GUI.super.unsetMiddleWest();
    }

    @Override
    public @NotNull PageableGUI setMiddle(Item @NotNull ... contents) {
        return (PageableGUI) GUI.super.setMiddle(contents);
    }

    @Override
    public @NotNull PageableGUI setMiddle(ItemGUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setMiddle(contents);
    }

    @Override
    public @NotNull PageableGUI setMiddle(GUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setMiddle(contents);
    }

    @Override
    public @NotNull PageableGUI unsetMiddle() {
        return (PageableGUI) GUI.super.unsetMiddle();
    }

    @Override
    public @NotNull PageableGUI setMiddleEast(Item @NotNull ... contents) {
        return (PageableGUI) GUI.super.setMiddleEast(contents);
    }

    @Override
    public @NotNull PageableGUI setMiddleEast(ItemGUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setMiddleEast(contents);
    }

    @Override
    public @NotNull PageableGUI setMiddleEast(GUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setMiddleEast(contents);
    }

    @Override
    public @NotNull PageableGUI unsetMiddleEast() {
        return (PageableGUI) GUI.super.unsetMiddleEast();
    }

    @Override
    public @NotNull PageableGUI setSouthWest(Item @NotNull ... contents) {
        return (PageableGUI) GUI.super.setSouthWest(contents);
    }

    @Override
    public @NotNull PageableGUI setSouthWest(ItemGUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setSouthWest(contents);
    }

    @Override
    public @NotNull PageableGUI setSouthWest(GUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setSouthWest(contents);
    }

    @Override
    public @NotNull PageableGUI unsetSouthWest() {
        return (PageableGUI) GUI.super.unsetSouthWest();
    }

    @Override
    public @NotNull PageableGUI setSouth(Item @NotNull ... contents) {
        return (PageableGUI) GUI.super.setSouth(contents);
    }

    @Override
    public @NotNull PageableGUI setSouth(ItemGUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setSouth(contents);
    }

    @Override
    public @NotNull PageableGUI setSouth(GUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setSouth(contents);
    }

    @Override
    public @NotNull PageableGUI unsetSouth() {
        return (PageableGUI) GUI.super.unsetSouth();
    }

    @Override
    public @NotNull PageableGUI setSouthEast(Item @NotNull ... contents) {
        return (PageableGUI) GUI.super.setSouthEast(contents);
    }

    @Override
    public @NotNull PageableGUI setSouthEast(ItemGUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setSouthEast(contents);
    }

    @Override
    public @NotNull PageableGUI setSouthEast(GUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setSouthEast(contents);
    }

    @Override
    public @NotNull PageableGUI unsetSouthEast() {
        return (PageableGUI) GUI.super.unsetSouthEast();
    }

    @Override
    public @NotNull PageableGUI fill(@NotNull Item content) {
        return (PageableGUI) GUI.super.fill(content);
    }

    @Override
    public @NotNull PageableGUI fill(@NotNull ItemGUIContent content) {
        return (PageableGUI) GUI.super.fill(content);
    }

    @Override
    public @NotNull PageableGUI fill(@NotNull GUIContent content) {
        return (PageableGUI) GUI.super.fill(content);
    }

    @Override
    public @NotNull PageableGUI onClickOutside(@NotNull String command) {
        return (PageableGUI) GUI.super.onClickOutside(command);
    }

    @Override
    public @NotNull PageableGUI onOpenGUI(@NotNull String command) {
        return (PageableGUI) GUI.super.onOpenGUI(command);
    }

    @Override
    public @NotNull PageableGUI onCloseGUI(@NotNull String command) {
        return (PageableGUI) GUI.super.onCloseGUI(command);
    }

    @Override
    public @NotNull PageableGUI onChangeGUI(@NotNull String command) {
        return (PageableGUI) GUI.super.onChangeGUI(command);
    }

    @Override
    public @NotNull PageableGUI onClickOutsideSend(@NotNull String message) {
        return (PageableGUI) GUI.super.onClickOutsideSend(message);
    }

    @Override
    public @NotNull PageableGUI onOpenGUISend(@NotNull String message) {
        return (PageableGUI) GUI.super.onOpenGUISend(message);
    }

    @Override
    public @NotNull PageableGUI onCloseGUISend(@NotNull String message) {
        return (PageableGUI) GUI.super.onCloseGUISend(message);
    }

    @Override
    public @NotNull PageableGUI onChangeGUISend(@NotNull String message) {
        return (PageableGUI) GUI.super.onChangeGUISend(message);
    }

    @Override
    public @NotNull PageableGUI setVariable(@NotNull String name, @NotNull String value) {
        return (PageableGUI) GUI.super.setVariable(name, value);
    }

    @Override
    public @NotNull PageableGUI unsetVariable(@NotNull String name) {
        return (PageableGUI) GUI.super.unsetVariable(name);
    }

    @Override
    public @NotNull PageableGUI copyAll(@NotNull GUI other, boolean replace) {
        GUI.super.copyAll(other, replace);
        if (other instanceof PageableGUI) {
            PageableGUI otherGUI = (PageableGUI) other;
            if (replace)
                try {
                    otherGUI.setPages(pages());
                } catch (IllegalStateException ignored) {
                    // DataGUI does not support pages
                }
            if (!otherGUI.previousPage.isPresent() || replace)
                previousPage.map((i, c) -> new Tuple<>(i, c.copy())).ifPresent(otherGUI::setPreviousPage);
            if (!otherGUI.nextPage.isPresent() || replace)
                nextPage.map((i, c) -> new Tuple<>(i, c.copy())).ifPresent(otherGUI::setNextPage);
        }
        return this;
    }

    @Override
    public @NotNull PageableGUI copyFrom(@NotNull GUI other, boolean replace) {
        return (PageableGUI) GUI.super.copyFrom(other, replace);
    }

    @Override
    public PageableGUI copy() {
        return (PageableGUI) GUI.super.copy();
    }

    @Override
    public @NotNull PageableGUI copyAll(@NotNull Metadatable other, boolean replace) {
        return (PageableGUI) GUI.super.copyAll(other, replace);
    }

    @Override
    public @NotNull PageableGUI copyFrom(@NotNull Metadatable other, boolean replace) {
        return (PageableGUI) GUI.super.copyFrom(other, replace);
    }

    /**
     * Creates a new {@link PageableGUI} with the given size.
     *
     * @param size the size
     * @return the pageable gui
     */
    public static @NotNull PageableGUI newGUI(final int size) {
        return new PageableGUI(GUI.newGUI(size));
    }

    /**
     * Creates a new {@link PageableGUI} with the given type.
     *
     * @param type the type
     * @return the pageable gui
     */
    public static @NotNull PageableGUI newGUI(final @NotNull GUIType type) {
        return new PageableGUI(GUI.newGUI(type));
    }

    /**
     * Creates a new {@link PageableGUI} with the given size and a full size {@link #templateGUI}.
     *
     * @param size the size
     * @return the pageable gui
     */
    public static @NotNull PageableGUI newFullSizeGUI(final int size) {
        return new PageableGUI(GUI.newFullSizeGUI(size));
    }

    /**
     * Creates a new {@link PageableGUI} with the given size and a full size {@link #templateGUI}.
     *
     * @param size         the size
     * @param lowerGUISize the size of the lower GUI
     * @return the pageable gui
     */
    public static @NotNull PageableGUI newFullSizeGUI(final int size, final int lowerGUISize) {
        return new PageableGUI(GUI.newFullSizeGUI(size, lowerGUISize));
    }

    /**
     * Creates a new {@link PageableGUI} with the given type and a full size {@link #templateGUI}.
     *
     * @param type the type
     * @return the pageable gui
     */
    public static @NotNull PageableGUI newFullSizeGUI(final @NotNull GUIType type) {
        return new PageableGUI(GUI.newFullSizeGUI(type));
    }

    /**
     * Creates a new {@link PageableGUI} with the given type and a full size {@link #templateGUI}.
     *
     * @param type         the type
     * @param lowerGUISize the size of the lower GUI
     * @return the pageable gui
     */
    public static @NotNull PageableGUI newFullSizeGUI(final @NotNull GUIType type, final int lowerGUISize) {
        return new PageableGUI(GUI.newFullSizeGUI(type, lowerGUISize));
    }

    @Override
    public @NotNull Map<String, String> variables() {
        return this.variables;
    }
}
