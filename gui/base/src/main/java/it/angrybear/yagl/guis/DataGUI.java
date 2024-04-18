package it.angrybear.yagl.guis;

import it.angrybear.yagl.contents.GUIContent;
import it.angrybear.yagl.viewers.Viewer;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An implementation of {@link PageableGUI} that allows data to be automatically displayed in a multi-paged format.
 *
 * @param <T> the type of the data
 */
public class DataGUI<T> extends PageableGUI {
    private static final String ERROR_MESSAGE = "Pages are dynamically calculated when opening this GUI. They cannot be singly edited";

    private final List<T> data;
    private final Function<T, GUIContent> dataConverter;

    /**
     * Instantiates a new Data gui.
     *
     * @param size          the size
     * @param dataConverter the data converter
     */
    DataGUI(final int size, final @NotNull Function<T, GUIContent> dataConverter) {
        super(size);
        this.data = new LinkedList<>();
        this.dataConverter = dataConverter;
    }

    /**
     * Instantiates a new Data gui.
     *
     * @param type          the type
     * @param dataConverter the data converter
     */
    DataGUI(final @NotNull GUIType type, final @NotNull Function<T, GUIContent> dataConverter) {
        super(type);
        this.data = new LinkedList<>();
        this.dataConverter = dataConverter;
    }

    /**
     * Adds the given data.
     *
     * @param data the data
     * @return this gui
     */
    public DataGUI<T> addData(final T @NotNull ... data) {
        return addData(Arrays.asList(data));
    }

    /**
     * Adds the given data.
     *
     * @param data the data
     * @return this gui
     */
    public DataGUI<T> addData(final @NotNull Collection<T> data) {
        this.data.addAll(data);
        return this;
    }

    /**
     * Clears the current data and sets the given data.
     *
     * @param data the data
     * @return the data
     */
    public DataGUI<T> setData(final T @NotNull ... data) {
        return setData(Arrays.asList(data));
    }

    /**
     * Clears the current data and sets the given data.
     *
     * @param data the data
     * @return the data
     */
    public DataGUI<T> setData(final @NotNull Collection<T> data) {
        return clearData().addData(data);
    }

    /**
     * Removes the data equal to any of the given data.
     *
     * @param data the data
     * @return this gui
     */
    public DataGUI<T> removeData(final T @NotNull ... data) {
        return removeData(Arrays.asList(data));
    }

    /**
     * Removes the data equal to any of the given data.
     *
     * @param data the data
     * @return this gui
     */
    public DataGUI<T> removeData(final @NotNull Collection<T> data) {
        return removeData(t -> t.equals(data));
    }

    /**
     * Removes the data that match the given {@link Predicate} function.
     *
     * @param function the function
     * @return this gui
     */
    public DataGUI<T> removeData(final @NotNull Predicate<T> function) {
        this.data.removeIf(function);
        return this;
    }

    /**
     * Removes all the data.
     *
     * @return this gui
     */
    public DataGUI<T> clearData() {
        this.data.clear();
        return this;
    }

    @Override
    public void open(@NotNull Viewer viewer, int page) {
        GUI templateGUI = this.templateGUI;
        if (templateGUI == null) throw new IllegalStateException("templateGUI did not load correctly");
        fillContents(prepareOpenGUI(templateGUI, page), page).open(viewer);
    }

    private @NotNull GUI fillContents(final @NotNull GUI gui, final int page) {
        int emptySlots = gui.emptySlots().size();
        int min = emptySlots * page;
        int max = emptySlots * (page + 1);
        int size = this.data.size();
        for (int i = Math.min(min, size); i < Math.min(max, size); i++)
            gui.addContent(this.dataConverter.apply(this.data.get(i)));
        return gui;
    }

    /**
     * Gets the {@link GUI} page from the given index.
     * The index starts from <b>0</b>.
     *
     * @param page the page
     * @deprecated In {@link DataGUI}s pages are not pre-defined, but rather calculated upon opening.
     * @return the corresponding {@link GUI} page
     */
    @Override
    @Deprecated
    public GUI getPage(int page) {
        throw new IllegalStateException(ERROR_MESSAGE);
    }

    /**
     * Sets pages.
     *
     * @param pages the pages
     * @deprecated In {@link DataGUI}s pages are not pre-defined, but rather calculated upon opening.
     * @return this gui
     */
    @Override
    public PageableGUI setPages(int pages) {
        throw new IllegalStateException(ERROR_MESSAGE);
    }

    /**
     * Gets the number of pages based on the amount of data provided.
     *
     * @return the pages
     */
    @Override
    public int pages() {
        double pages = (double) this.data.size() / emptySlots().size();
        return (int) Math.ceil(pages);
    }

    /**
     * Creates a new {@link DataGUI} with the given size and converter.
     *
     * @param <T>           the type of the data
     * @param size          the size
     * @param dataConverter the data converter
     * @return the data gui
     */
    public static <T> DataGUI<T> newGUI(final int size, final @NotNull Function<T, GUIContent> dataConverter) {
        return new DataGUI<>(size, dataConverter);
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
    public static <T> DataGUI<T> newGUI(final int size, final @NotNull Function<T, GUIContent> dataConverter,
                                        final T @NotNull ... data) {
        return new DataGUI<>(size, dataConverter).setData(data);
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
    public static <T> DataGUI<T> newGUI(final int size, final @NotNull Function<T, GUIContent> dataConverter,
                                        final @NotNull Collection<T> data) {
        return new DataGUI<>(size, dataConverter).setData(data);
    }

    /**
     * Creates a new {@link DataGUI} with the given type and converter.
     *
     * @param <T>           the type of the data
     * @param type          the type
     * @param dataConverter the data converter
     * @return the data gui
     */
    public static <T> DataGUI<T> newGUI(final @NotNull GUIType type, final @NotNull Function<T, GUIContent> dataConverter) {
        return new DataGUI<>(type, dataConverter);
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
    public static <T> DataGUI<T> newGUI(final @NotNull GUIType type, final @NotNull Function<T, GUIContent> dataConverter,
                                        final T @NotNull ... data) {
        return new DataGUI<>(type, dataConverter).setData(data);
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
    public static <T> DataGUI<T> newGUI(final @NotNull GUIType type, final @NotNull Function<T, GUIContent> dataConverter,
                                        final @NotNull Collection<T> data) {
        return new DataGUI<>(type, dataConverter).setData(data);
    }

}
