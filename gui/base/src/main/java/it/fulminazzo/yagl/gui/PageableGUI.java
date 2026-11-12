package it.fulminazzo.yagl.gui;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yagl.structure.Pair;
import it.fulminazzo.yagl.action.BiGUIAction;
import it.fulminazzo.yagl.action.GUIAction;
import it.fulminazzo.yagl.content.CustomItemGUIContent;
import it.fulminazzo.yagl.content.GUIContent;
import it.fulminazzo.yagl.content.ItemGUIContent;
import it.fulminazzo.yagl.item.Item;
import it.fulminazzo.yagl.metadatable.Metadatable;
import it.fulminazzo.yagl.util.ValidationUtils;
import it.fulminazzo.yagl.viewer.Viewer;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.*;

/**
 * An implementation of {@link GUI} that allows multiple GUI pages to be added.
 */
@EqualsAndHashCode
public class PageableGUI implements Iterable<GUI>, Metadatable, GUI {
    /**
     * The Template gui.
     */
    protected final @NotNull GUI templateGUI;
    private final List<GUI> pages = new LinkedList<>();
    private final Map<String, String> variables = new HashMap<>();

    /**
     * The Previous page.
     */
    protected final Pair<Integer, GUIContent> previousPage = new Pair<>();
    /**
     * The Next page.
     */
    protected final Pair<Integer, GUIContent> nextPage = new Pair<>();

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
    public @Range(from = Constants.MIN_PAGES, to = Integer.MAX_VALUE) int pages() {
        return this.pages.size();
    }

    /**
     * Gets the {@link GUI} page from the given index.
     * The index starts from <b>0</b>.
     *
     * @param page the page
     * @return the corresponding {@link GUI} page
     */
    public @NotNull GUI getPage(final @Range(from = 0, to = Integer.MAX_VALUE) int page) {
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
    public @NotNull PageableGUI setPages(final @Range(from = Constants.MIN_PAGES, to = Integer.MAX_VALUE) int pages) {
        ValidationUtils.checkMin("pages", pages, Constants.MIN_PAGES);
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
        if (this.templateGUI instanceof FullscreenGUI) {
            FullscreenGUI fullscreenGUI = (FullscreenGUI) this.templateGUI;
            newTemplateGUI = new Refl<>(GUI.newFullscreenGUI(9))
                    .setFieldObject("upperGUI", fullscreenGUI.getUpperGUI().copy())
                    .setFieldObject("lowerGUI", fullscreenGUI.getLowerGUI().copy())
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
    public @NotNull PageableGUI setPreviousPage(final @Range(from = 0, to = Constants.MAX_SLOT) int slot, final @NotNull Item previousPage) {
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
    public @NotNull PageableGUI setPreviousPage(final @Range(from = 0, to = Constants.MAX_SLOT) int slot, final @NotNull ItemGUIContent previousPage) {
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
    public @NotNull PageableGUI setPreviousPage(final @Range(from = 0, to = Constants.MAX_SLOT) int slot, final @NotNull GUIContent previousPage) {
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
    public @NotNull PageableGUI setNextPage(final @Range(from = 0, to = Constants.MAX_SLOT) int slot, final @NotNull Item nextPage) {
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
    public @NotNull PageableGUI setNextPage(final @Range(from = 0, to = Constants.MAX_SLOT) int slot, final @NotNull ItemGUIContent nextPage) {
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
    public @NotNull PageableGUI setNextPage(final @Range(from = 0, to = Constants.MAX_SLOT) int slot, final @NotNull GUIContent nextPage) {
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
        ValidationUtils.checkRange("slot", slot, 0, size());
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
    public void open(final @NotNull Viewer viewer, final @Range(from = 0, to = Integer.MAX_VALUE) int page) {
        prepareOpenGUI(getPage(page), page).open(viewer);
    }

    /**
     * Prepares the {@link GUI} at the given page for {@link #open(Viewer, int)}.
     *
     * @param gui  the gui
     * @param page the page
     * @return the gui
     */
    protected @NotNull GUI prepareOpenGUI(final @NotNull GUI gui, final @Range(from = 0, to = Integer.MAX_VALUE) int page) {
        GUI newGUI = gui.copy().copyFrom(this, false)
                .setVariable("page", String.valueOf(page + 1))
                .setVariable("previous_page", String.valueOf(page))
                .setVariable("next_page", String.valueOf(page + 2))
                .setVariable("pages", String.valueOf(pages()));
        if (page > 0) this.previousPage.ifPresent((s, p) ->
                newGUI.setContents(s, p.copy().onClickItem(e -> open(e.getViewer(), page - 1))));
        if (page + 1 < pages()) this.nextPage.ifPresent((s, p) ->
                newGUI.setContents(s, p.copy().onClickItem(e -> open(e.getViewer(), page + 1))));
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
    public boolean isMovable(final @Range(from = 0, to = Constants.MAX_SLOT) int slot) {
        return this.templateGUI.isMovable(slot);
    }

    @Override
    public @NotNull PageableGUI setMovable(final @Range(from = 0, to = Constants.MAX_SLOT) int slot, final boolean movable) {
        this.templateGUI.setMovable(slot, movable);
        return this;
    }

    @Override
    public @NotNull List<GUIContent> getContents(final @Range(from = 0, to = Constants.MAX_SLOT) int slot) {
        return this.templateGUI.getContents(slot);
    }

    @Override
    public @NotNull List<GUIContent> getContents() {
        return this.templateGUI.getContents();
    }

    @Override
    public @NotNull PageableGUI addContent(final GUIContent @NotNull ... contents) {
        this.templateGUI.addContent(contents);
        return this;
    }

    @Override
    public @NotNull PageableGUI setContents(final @Range(from = 0, to = Constants.MAX_SLOT) int slot, final GUIContent @NotNull ... contents) {
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
    public @Range(from = 0, to = Constants.MAX_SLOT) int northWest() {
        return this.templateGUI.northWest();
    }

    @Override
    public @Range(from = 0, to = Constants.MAX_SLOT) int north() {
        return this.templateGUI.north();
    }

    @Override
    public @Range(from = 0, to = Constants.MAX_SLOT) int northEast() {
        return this.templateGUI.northEast();
    }

    @Override
    public @Range(from = 0, to = Constants.MAX_SLOT) int middleLine() {
        return this.templateGUI.middleLine();
    }

    @Override
    public @Range(from = 0, to = Constants.MAX_SLOT) int middleWest() {
        return this.templateGUI.middleWest();
    }

    @Override
    public @Range(from = 0, to = Constants.MAX_SLOT) int middle() {
        return this.templateGUI.middle();
    }

    @Override
    public @Range(from = 0, to = Constants.MAX_SLOT) int middleEast() {
        return this.templateGUI.middleEast();
    }

    @Override
    public @Range(from = 0, to = Constants.MAX_SLOT) int southLine() {
        return this.templateGUI.southLine();
    }

    @Override
    public @Range(from = 0, to = Constants.MAX_SLOT) int southWest() {
        return this.templateGUI.southWest();
    }

    @Override
    public @Range(from = 0, to = Constants.MAX_SLOT) int south() {
        return this.templateGUI.south();
    }

    @Override
    public @Range(from = 0, to = Constants.MAX_SLOT) int southEast() {
        return this.templateGUI.southEast();
    }

    @Override
    public @Range(from = Constants.MIN_ROWS, to = Constants.MAX_ROWS) int rows() {
        return this.templateGUI.rows();
    }

    @Override
    public @Range(from = Constants.MIN_COLUMNS, to = Constants.MAX_COLUMNS) int columns() {
        return this.templateGUI.columns();
    }

    @Override
    public @NotNull PageableGUI clear() {
        this.templateGUI.clear();
        return this;
    }

    @Override
    public @NotNull PageableGUI setContents(final @Range(from = 0, to = Constants.MAX_SLOT) int slot, final @NotNull Collection<? extends GUIContent> contents) {
        return (PageableGUI) GUI.super.setContents(slot, contents);
    }

    @Override
    public @NotNull PageableGUI unsetContent(final @Range(from = 0, to = Constants.MAX_SLOT) int slot) {
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
    public String toString() {
        String base = templateGUI.toString();
        base = base.substring(base.indexOf("{"), base.length() - 1);
        return getClass().getSimpleName() + base + String.format(", pages=%s}", pages.size());
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
    public @NotNull PageableGUI addContent(final Item @NotNull ... contents) {
        return (PageableGUI) GUI.super.addContent(contents);
    }

    @Override
    public @NotNull PageableGUI addContent(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (PageableGUI) GUI.super.addContent(contents);
    }

    @Override
    public @NotNull PageableGUI setContents(final @Range(from = 0, to = Constants.MAX_SLOT) int slot, final Item @NotNull ... contents) {
        return (PageableGUI) GUI.super.setContents(slot, contents);
    }

    @Override
    public @NotNull PageableGUI setContents(final @Range(from = 0, to = Constants.MAX_SLOT) int slot, final CustomItemGUIContent<?> @NotNull ... contents) {
        return (PageableGUI) GUI.super.setContents(slot, contents);
    }

    @Override
    public @NotNull PageableGUI setAllSides(final Item @NotNull ... contents) {
        return (PageableGUI) GUI.super.setAllSides(contents);
    }

    @Override
    public @NotNull PageableGUI setAllSides(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (PageableGUI) GUI.super.setAllSides(contents);
    }

    @Override
    public @NotNull PageableGUI setAllSides(final GUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setAllSides(contents);
    }

    @Override
    public @NotNull PageableGUI setAllSides(final @NotNull Collection<? extends GUIContent> contents) {
        return (PageableGUI) GUI.super.setAllSides(contents);
    }

    @Override
    public @NotNull PageableGUI unsetAllSides() {
        return (PageableGUI) GUI.super.unsetAllSides();
    }

    @Override
    public @NotNull PageableGUI setTopAndBottomSides(final Item @NotNull ... contents) {
        return (PageableGUI) GUI.super.setTopAndBottomSides(contents);
    }

    @Override
    public @NotNull PageableGUI setTopAndBottomSides(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (PageableGUI) GUI.super.setTopAndBottomSides(contents);
    }

    @Override
    public @NotNull PageableGUI setTopAndBottomSides(final GUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setTopAndBottomSides(contents);
    }

    @Override
    public @NotNull PageableGUI setTopAndBottomSides(final @NotNull Collection<? extends GUIContent> contents) {
        return (PageableGUI) GUI.super.setTopAndBottomSides(contents);
    }

    @Override
    public @NotNull PageableGUI unsetTopAndBottomSides() {
        return (PageableGUI) GUI.super.unsetTopAndBottomSides();
    }

    @Override
    public @NotNull PageableGUI setLeftAndRightSides(final Item @NotNull ... contents) {
        return (PageableGUI) GUI.super.setLeftAndRightSides(contents);
    }

    @Override
    public @NotNull PageableGUI setLeftAndRightSides(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (PageableGUI) GUI.super.setLeftAndRightSides(contents);
    }

    @Override
    public @NotNull PageableGUI setLeftAndRightSides(final GUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setLeftAndRightSides(contents);
    }

    @Override
    public @NotNull PageableGUI setLeftAndRightSides(final @NotNull Collection<? extends GUIContent> contents) {
        return (PageableGUI) GUI.super.setLeftAndRightSides(contents);
    }

    @Override
    public @NotNull PageableGUI unsetLeftAndRightSides() {
        return (PageableGUI) GUI.super.unsetLeftAndRightSides();
    }

    @Override
    public @NotNull PageableGUI setTopSide(final Item @NotNull ... contents) {
        return (PageableGUI) GUI.super.setTopSide(contents);
    }

    @Override
    public @NotNull PageableGUI setTopSide(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (PageableGUI) GUI.super.setTopSide(contents);
    }

    @Override
    public @NotNull PageableGUI setTopSide(final GUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setTopSide(contents);
    }

    @Override
    public @NotNull PageableGUI setTopSide(final @NotNull Collection<? extends GUIContent> contents) {
        return (PageableGUI) GUI.super.setTopSide(contents);
    }

    @Override
    public @NotNull PageableGUI unsetTopSide() {
        return (PageableGUI) GUI.super.unsetTopSide();
    }

    @Override
    public @NotNull PageableGUI setLeftSide(final Item @NotNull ... contents) {
        return (PageableGUI) GUI.super.setLeftSide(contents);
    }

    @Override
    public @NotNull PageableGUI setLeftSide(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (PageableGUI) GUI.super.setLeftSide(contents);
    }

    @Override
    public @NotNull PageableGUI setLeftSide(final GUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setLeftSide(contents);
    }

    @Override
    public @NotNull PageableGUI setLeftSide(final @NotNull Collection<? extends GUIContent> contents) {
        return (PageableGUI) GUI.super.setLeftSide(contents);
    }

    @Override
    public @NotNull PageableGUI unsetLeftSide() {
        return (PageableGUI) GUI.super.unsetLeftSide();
    }

    @Override
    public @NotNull PageableGUI setBottomSide(final Item @NotNull ... contents) {
        return (PageableGUI) GUI.super.setBottomSide(contents);
    }

    @Override
    public @NotNull PageableGUI setBottomSide(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (PageableGUI) GUI.super.setBottomSide(contents);
    }

    @Override
    public @NotNull PageableGUI setBottomSide(final GUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setBottomSide(contents);
    }

    @Override
    public @NotNull PageableGUI setBottomSide(final @NotNull Collection<? extends GUIContent> contents) {
        return (PageableGUI) GUI.super.setBottomSide(contents);
    }

    @Override
    public @NotNull PageableGUI unsetBottomSide() {
        return (PageableGUI) GUI.super.unsetBottomSide();
    }

    @Override
    public @NotNull PageableGUI setRightSide(final Item @NotNull ... contents) {
        return (PageableGUI) GUI.super.setRightSide(contents);
    }

    @Override
    public @NotNull PageableGUI setRightSide(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (PageableGUI) GUI.super.setRightSide(contents);
    }

    @Override
    public @NotNull PageableGUI setRightSide(final GUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setRightSide(contents);
    }

    @Override
    public @NotNull PageableGUI setRightSide(final @NotNull Collection<? extends GUIContent> contents) {
        return (PageableGUI) GUI.super.setRightSide(contents);
    }

    @Override
    public @NotNull PageableGUI unsetRightSide() {
        return (PageableGUI) GUI.super.unsetRightSide();
    }

    @Override
    public @NotNull PageableGUI setNorthWest(final Item @NotNull ... contents) {
        return (PageableGUI) GUI.super.setNorthWest(contents);
    }

    @Override
    public @NotNull PageableGUI setNorthWest(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (PageableGUI) GUI.super.setNorthWest(contents);
    }

    @Override
    public @NotNull PageableGUI setNorthWest(final GUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setNorthWest(contents);
    }

    @Override
    public @NotNull PageableGUI unsetNorthWest() {
        return (PageableGUI) GUI.super.unsetNorthWest();
    }

    @Override
    public @NotNull PageableGUI setNorth(final Item @NotNull ... contents) {
        return (PageableGUI) GUI.super.setNorth(contents);
    }

    @Override
    public @NotNull PageableGUI setNorth(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (PageableGUI) GUI.super.setNorth(contents);
    }

    @Override
    public @NotNull PageableGUI setNorth(final GUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setNorth(contents);
    }

    @Override
    public @NotNull PageableGUI unsetNorth() {
        return (PageableGUI) GUI.super.unsetNorth();
    }

    @Override
    public @NotNull PageableGUI setNorthEast(final Item @NotNull ... contents) {
        return (PageableGUI) GUI.super.setNorthEast(contents);
    }

    @Override
    public @NotNull PageableGUI setNorthEast(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (PageableGUI) GUI.super.setNorthEast(contents);
    }

    @Override
    public @NotNull PageableGUI setNorthEast(final GUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setNorthEast(contents);
    }

    @Override
    public @NotNull PageableGUI unsetNorthEast() {
        return (PageableGUI) GUI.super.unsetNorthEast();
    }

    @Override
    public @NotNull PageableGUI setMiddleWest(final Item @NotNull ... contents) {
        return (PageableGUI) GUI.super.setMiddleWest(contents);
    }

    @Override
    public @NotNull PageableGUI setMiddleWest(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (PageableGUI) GUI.super.setMiddleWest(contents);
    }

    @Override
    public @NotNull PageableGUI setMiddleWest(final GUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setMiddleWest(contents);
    }

    @Override
    public @NotNull PageableGUI unsetMiddleWest() {
        return (PageableGUI) GUI.super.unsetMiddleWest();
    }

    @Override
    public @NotNull PageableGUI setMiddle(final Item @NotNull ... contents) {
        return (PageableGUI) GUI.super.setMiddle(contents);
    }

    @Override
    public @NotNull PageableGUI setMiddle(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (PageableGUI) GUI.super.setMiddle(contents);
    }

    @Override
    public @NotNull PageableGUI setMiddle(final GUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setMiddle(contents);
    }

    @Override
    public @NotNull PageableGUI unsetMiddle() {
        return (PageableGUI) GUI.super.unsetMiddle();
    }

    @Override
    public @NotNull PageableGUI setMiddleEast(final Item @NotNull ... contents) {
        return (PageableGUI) GUI.super.setMiddleEast(contents);
    }

    @Override
    public @NotNull PageableGUI setMiddleEast(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (PageableGUI) GUI.super.setMiddleEast(contents);
    }

    @Override
    public @NotNull PageableGUI setMiddleEast(final GUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setMiddleEast(contents);
    }

    @Override
    public @NotNull PageableGUI unsetMiddleEast() {
        return (PageableGUI) GUI.super.unsetMiddleEast();
    }

    @Override
    public @NotNull PageableGUI setSouthWest(final Item @NotNull ... contents) {
        return (PageableGUI) GUI.super.setSouthWest(contents);
    }

    @Override
    public @NotNull PageableGUI setSouthWest(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (PageableGUI) GUI.super.setSouthWest(contents);
    }

    @Override
    public @NotNull PageableGUI setSouthWest(final GUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setSouthWest(contents);
    }

    @Override
    public @NotNull PageableGUI unsetSouthWest() {
        return (PageableGUI) GUI.super.unsetSouthWest();
    }

    @Override
    public @NotNull PageableGUI setSouth(final Item @NotNull ... contents) {
        return (PageableGUI) GUI.super.setSouth(contents);
    }

    @Override
    public @NotNull PageableGUI setSouth(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (PageableGUI) GUI.super.setSouth(contents);
    }

    @Override
    public @NotNull PageableGUI setSouth(final GUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setSouth(contents);
    }

    @Override
    public @NotNull PageableGUI unsetSouth() {
        return (PageableGUI) GUI.super.unsetSouth();
    }

    @Override
    public @NotNull PageableGUI setSouthEast(final Item @NotNull ... contents) {
        return (PageableGUI) GUI.super.setSouthEast(contents);
    }

    @Override
    public @NotNull PageableGUI setSouthEast(final CustomItemGUIContent<?> @NotNull ... contents) {
        return (PageableGUI) GUI.super.setSouthEast(contents);
    }

    @Override
    public @NotNull PageableGUI setSouthEast(final GUIContent @NotNull ... contents) {
        return (PageableGUI) GUI.super.setSouthEast(contents);
    }

    @Override
    public @NotNull PageableGUI unsetSouthEast() {
        return (PageableGUI) GUI.super.unsetSouthEast();
    }

    @Override
    public @NotNull PageableGUI fill(final @NotNull Item content) {
        return (PageableGUI) GUI.super.fill(content);
    }

    @Override
    public @NotNull PageableGUI fill(final @NotNull CustomItemGUIContent<?> content) {
        return (PageableGUI) GUI.super.fill(content);
    }

    @Override
    public @NotNull PageableGUI fill(final @NotNull GUIContent content) {
        return (PageableGUI) GUI.super.fill(content);
    }

    @Override
    public @NotNull PageableGUI onClickOutside(final @NotNull String command) {
        return (PageableGUI) GUI.super.onClickOutside(command);
    }

    @Override
    public @NotNull PageableGUI onOpenGUI(final @NotNull String command) {
        return (PageableGUI) GUI.super.onOpenGUI(command);
    }

    @Override
    public @NotNull PageableGUI onCloseGUI(final @NotNull String command) {
        return (PageableGUI) GUI.super.onCloseGUI(command);
    }

    @Override
    public @NotNull PageableGUI onChangeGUI(final @NotNull String command) {
        return (PageableGUI) GUI.super.onChangeGUI(command);
    }

    @Override
    public @NotNull PageableGUI onClickOutsideSend(final @NotNull String message) {
        return (PageableGUI) GUI.super.onClickOutsideSend(message);
    }

    @Override
    public @NotNull PageableGUI onOpenGUISend(final @NotNull String message) {
        return (PageableGUI) GUI.super.onOpenGUISend(message);
    }

    @Override
    public @NotNull PageableGUI onCloseGUISend(final @NotNull String message) {
        return (PageableGUI) GUI.super.onCloseGUISend(message);
    }

    @Override
    public @NotNull PageableGUI onChangeGUISend(final @NotNull String message) {
        return (PageableGUI) GUI.super.onChangeGUISend(message);
    }

    @Override
    public @NotNull PageableGUI setVariable(final @NotNull String name, final @NotNull String value) {
        return (PageableGUI) GUI.super.setVariable(name, value);
    }

    @Override
    public @NotNull PageableGUI unsetVariable(final @NotNull String name) {
        return (PageableGUI) GUI.super.unsetVariable(name);
    }

    @Override
    public @NotNull PageableGUI copyAll(final @NotNull GUI other, final boolean replace) {
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
                previousPage.map((i, c) -> new Pair<>(i, c.copy())).ifPresent(otherGUI::setPreviousPage);
            if (!otherGUI.nextPage.isPresent() || replace)
                nextPage.map((i, c) -> new Pair<>(i, c.copy())).ifPresent(otherGUI::setNextPage);
        }
        return this;
    }

    @Override
    public @NotNull PageableGUI copyFrom(final @NotNull GUI other, final boolean replace) {
        return (PageableGUI) GUI.super.copyFrom(other, replace);
    }

    @Override
    public PageableGUI copy() {
        return (PageableGUI) GUI.super.copy();
    }

    @Override
    public @NotNull PageableGUI copyAll(final @NotNull Metadatable other, final boolean replace) {
        return (PageableGUI) GUI.super.copyAll(other, replace);
    }

    @Override
    public @NotNull PageableGUI copyFrom(final @NotNull Metadatable other, final boolean replace) {
        return (PageableGUI) GUI.super.copyFrom(other, replace);
    }

    /**
     * Creates a new {@link PageableGUI} with the given size.
     *
     * @param size the size
     * @return the pageable gui
     */
    public static @NotNull PageableGUI newGUI(final @Range(from = Constants.MIN_DEFAULT_SIZE, to = Constants.MAX_SIZE) int size) {
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
    public static @NotNull PageableGUI newFullScreenGUI(final @Range(from = Constants.MIN_DEFAULT_SIZE, to = Constants.MAX_SIZE) int size) {
        return new PageableGUI(GUI.newFullscreenGUI(size));
    }

    /**
     * Creates a new {@link PageableGUI} with the given size and a full size {@link #templateGUI}.
     *
     * @param size         the size
     * @param lowerGUISize the size of the lower GUI
     * @return the pageable gui
     */
    public static @NotNull PageableGUI newFullScreenGUI(final @Range(from = Constants.MIN_DEFAULT_SIZE, to = Constants.MAX_SIZE) int size,
                                                        final @Range(from = Constants.MIN_DEFAULT_SIZE, to = Constants.SECONDARY_GUI_SIZE) int lowerGUISize) {
        return new PageableGUI(GUI.newFullscreenGUI(size, lowerGUISize));
    }

    /**
     * Creates a new {@link PageableGUI} with the given type and a full size {@link #templateGUI}.
     *
     * @param type the type
     * @return the pageable gui
     */
    public static @NotNull PageableGUI newFullScreenGUI(final @NotNull GUIType type) {
        return new PageableGUI(GUI.newFullscreenGUI(type));
    }

    /**
     * Creates a new {@link PageableGUI} with the given type and a full size {@link #templateGUI}.
     *
     * @param type         the type
     * @param lowerGUISize the size of the lower GUI
     * @return the pageable gui
     */
    public static @NotNull PageableGUI newFullScreenGUI(final @NotNull GUIType type,
                                                        final @Range(from = Constants.MIN_DEFAULT_SIZE, to = Constants.SECONDARY_GUI_SIZE) int lowerGUISize) {
        return new PageableGUI(GUI.newFullscreenGUI(type, lowerGUISize));
    }

    @Override
    public @NotNull Map<String, String> variables() {
        return this.variables;
    }
}
