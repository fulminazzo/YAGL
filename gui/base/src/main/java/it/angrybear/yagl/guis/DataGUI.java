package it.angrybear.yagl.guis;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/**
 * An implementation of {@link PageableGUI} that allows data to be automatically displayed in a multi-paged format.
 *
 * @param <T> the type of the data
 */
public class DataGUI<T> extends PageableGUI {
    private final List<T> data;

    /**
     * Instantiates a new Data gui.
     */
    DataGUI() {
        this.data = new ArrayList<>();
    }

    /**
     * Instantiates a new Data gui.
     *
     * @param size the size
     * @param data the data
     */
    DataGUI(int size, List<T> data) {
        super(size);
        this.data = data;
    }

    /**
     * Instantiates a new Data gui.
     *
     * @param type the type
     * @param data the data
     */
    DataGUI(@NotNull GUIType type, List<T> data) {
        super(type);
        this.data = data;
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
    
}
