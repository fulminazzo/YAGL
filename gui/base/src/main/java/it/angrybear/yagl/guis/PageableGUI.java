package it.angrybear.yagl.guis;

import it.angrybear.yagl.Metadatable;
import it.angrybear.yagl.actions.BiGUIAction;
import it.angrybear.yagl.actions.GUIAction;
import it.angrybear.yagl.contents.GUIContent;
import it.angrybear.yagl.contents.ItemGUIContent;
import it.angrybear.yagl.items.Item;
import it.angrybear.yagl.viewers.Viewer;
import it.fulminazzo.fulmicollection.structures.Tuple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

/**
 * An implementation of {@link GUI} that allows multiple GUI pages to be added.
 */
public class PageableGUI implements Iterable<GUI>, Metadatable, GUI {
    private final GUI templateGUI;
    private final List<GUI> pages = new LinkedList<>();
    private final Map<String, String> variables = new HashMap<>();

    private final Tuple<Integer, GUIContent> previousPage = new Tuple<>();
    private final Tuple<Integer, GUIContent> nextPage = new Tuple<>();

    private PageableGUI(final int size) {
        this.templateGUI = GUI.newGUI(size);
    }

    private PageableGUI(final @NotNull GUIType type) {
        this.templateGUI = GUI.newGUI(type);
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
     * The index starts from <b>1</b>.
     *
     * @param page the page
     * @return the corresponding {@link GUI} page
     */
    public GUI getPage(final int page) {
        try {
            return this.pages.get(page - 1);
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
    public PageableGUI setPages(final int pages) {
        if (pages < 0) throw new IllegalArgumentException(String.format("Invalid pages '%s'", pages));
        int s;
        while ((s = this.pages.size()) - pages >= 0) this.pages.remove(s - 1);
        while (pages - this.pages.size() > 0) this.pages.add(this.templateGUI.copy());
        return this;
    }

    /**
     * Sets the previous page content.
     * When clicking on it, the previous page will be opened.
     *
     * @param slot         the slot
     * @param previousPage the previous page
     * @return the previous page
     */
    public PageableGUI setPreviousPage(final int slot, final @NotNull Item previousPage) {
        return setPreviousPage(slot, (GUIContent) ItemGUIContent.newInstance(previousPage));
    }

    /**
     * Sets the previous page content.
     * When clicking on it, the previous page will be opened.
     *
     * @param slot         the slot
     * @param previousPage the previous page
     * @return the previous page
     */
    public PageableGUI setPreviousPage(final int slot, final @NotNull GUIContent previousPage) {
        this.previousPage.set(slot, previousPage);
        return this;
    }

    /**
     * Removes the previous page set with {@link #setPreviousPage(int, GUIContent)}.
     *
     * @return the pageable gui
     */
    public PageableGUI unsetPreviousPage() {
        this.previousPage.set(null, null);
        return this;
    }

    /**
     * Sets the next page content.
     * When clicking on it, the next page will be opened.
     *
     * @param slot         the slot
     * @param nextPage the next page
     * @return the next page
     */
    public PageableGUI setNextPage(final int slot, final @NotNull Item nextPage) {
        return setNextPage(slot, (GUIContent) ItemGUIContent.newInstance(nextPage));
    }

    /**
     * Sets the next page content.
     * When clicking on it, the next page will be opened.
     *
     * @param slot         the slot
     * @param nextPage the next page
     * @return the next page
     */
    public PageableGUI setNextPage(final int slot, final @NotNull GUIContent nextPage) {
        this.nextPage.set(slot, nextPage);
        return this;
    }

    /**
     * Removes the next page set with {@link #setNextPage(int, GUIContent)}.
     *
     * @return the pageable gui
     */
    public PageableGUI unsetNextPage() {
        this.nextPage.set(null, null);
        return this;
    }

    private void forEachInternal(final @NotNull Consumer<? super GUI> function) {
        function.accept(this.templateGUI);
        this.pages.forEach(function);
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
        open(viewer, 1);
    }

    /**
     * Opens the current GUI for the given viewer at the specified page.
     *
     * @param viewer the viewer
     * @param page   the page
     */
    public void open(final @NotNull Viewer viewer, final int page) {
        GUI gui = getPage(page).copy().copyFrom(this, false)
                .setVariable("page", String.valueOf(page))
                .setVariable("previous-page", String.valueOf(page - 1))
                .setVariable("next-page", String.valueOf(page + 1))
                .setVariable("pages", String.valueOf(pages()));
        if (page > 0) this.previousPage.ifPresent((s, p) ->
                gui.setContents(s, p.onClickItem((v, g, i) -> open(v, page - 1))));
        if (page < pages()) this.nextPage.ifPresent((s, p) ->
                gui.setContents(s, p.onClickItem((v, g, i) -> open(v, page + 1))));
        gui.open(viewer);
    }

    @Override
    public @NotNull PageableGUI setTitle(final @Nullable String title) {
        forEachInternal(g -> g.setTitle(title));
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
        forEachInternal(g -> g.setMovable(slot, movable));
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
        forEachInternal(g -> g.addContent(contents));
        return this;
    }

    @Override
    public @NotNull PageableGUI setContents(int slot, GUIContent @NotNull ... contents) {
        forEachInternal(g -> g.setContents(slot, contents));
        return this;
    }

    @Override
    public @NotNull PageableGUI unsetContent(int slot) {
        forEachInternal(g -> g.unsetContent(slot));
        return this;
    }

    @Override
    public @NotNull PageableGUI onClickOutside(final @NotNull GUIAction action) {
        forEachInternal(g -> g.onClickOutside(action));
        return this;
    }

    @Override
    public @NotNull Optional<GUIAction> clickOutsideAction() {
        return this.templateGUI.clickOutsideAction();
    }

    @Override
    public @NotNull PageableGUI onOpenGUI(final @NotNull GUIAction action) {
        forEachInternal(g -> g.onOpenGUI(action));
        return this;
    }

    @Override
    public @NotNull Optional<GUIAction> openGUIAction() {
        return this.templateGUI.openGUIAction();
    }

    @Override
    public @NotNull PageableGUI onCloseGUI(final @NotNull GUIAction action) {
        forEachInternal(g -> g.onCloseGUI(action));
        return this;
    }

    @Override
    public @NotNull Optional<GUIAction> closeGUIAction() {
        return this.templateGUI.closeGUIAction();
    }

    @Override
    public @NotNull PageableGUI onChangeGUI(final @NotNull BiGUIAction action) {
        forEachInternal(g -> g.onChangeGUI(action));
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
    public @NotNull PageableGUI setVariable(@NotNull String name, @NotNull String value) {
        return (PageableGUI) GUI.super.setVariable(name, value);
    }

    @Override
    public @NotNull PageableGUI unsetVariable(@NotNull String name) {
        return (PageableGUI) GUI.super.unsetVariable(name);
    }

    @Override
    public @NotNull PageableGUI copyAll(@NotNull GUI other, boolean replace) {
        return (PageableGUI) GUI.super.copyAll(other, replace);
    }

    @Override
    public @NotNull PageableGUI copyFrom(@NotNull GUI other, boolean replace) {
        return (PageableGUI) GUI.super.copyFrom(other, replace);
    }

    @Override
    public @NotNull PageableGUI copyAll(@NotNull Metadatable other, boolean replace) {
        return (PageableGUI) GUI.super.copyAll(other, replace);
    }

    @Override
    public @NotNull PageableGUI copyFrom(@NotNull Metadatable other, boolean replace) {
        return (PageableGUI) GUI.super.copyFrom(other, replace);
    }

    @Override
    public PageableGUI copy() {
        return GUI.super.copy(PageableGUI.class);
    }

    /**
     * Creates a new {@link PageableGUI} with the given size.
     *
     * @param size the size
     * @return the pageable gui
     */
    public static PageableGUI newGUI(final int size) {
        return new PageableGUI(size);
    }

    /**
     * Creates a new {@link PageableGUI} with the given type.
     *
     * @param type the type
     * @return the pageable gui
     */
    public static PageableGUI newGUI(final @NotNull GUIType type) {
        return new PageableGUI(type);
    }

    @Override
    public @NotNull Map<String, String> variables() {
        return this.variables;
    }
}
