package it.fulminazzo.yagl.gui;


import it.fulminazzo.yagl.util.ValidationUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Represents a "chest" GUI that cannot be resized.
 */
public class DefaultGUI extends GUIImpl {

    /**
     * Instantiates a new Gui.
     *
     * @param size the size
     */
    protected DefaultGUI(final @Range(from = Constants.MIN_DEFAULT_SIZE, to = Constants.MAX_SIZE) int size) {
        super(size);
        checkValidSize(size);
    }

    /**
     * Checks if the given size is in bounds.
     *
     * @param size the size
     */
    void checkValidSize(final @Range(from = Constants.MIN_DEFAULT_SIZE, to = Constants.MAX_SIZE) int size) {
        ValidationUtils.checkRange("size", size, Constants.MIN_DEFAULT_SIZE, Constants.MAX_SIZE);
        if (size % Constants.MIN_DEFAULT_SIZE != 0)
            throw new IllegalArgumentException(String.format("%s is not a valid size. Only multiple of %s can be accepted",
                    size, Constants.MIN_DEFAULT_SIZE));
    }

    @Override
    protected @NotNull GUI initializeNewInstance() {
        return new DefaultGUI(size());
    }

    @Override
    public @Range(from = Constants.MIN_ROWS, to = Constants.MAX_ROWS) int rows() {
        return size() / Constants.MIN_DEFAULT_SIZE;
    }

    @Override
    public @Range(from = Constants.MIN_COLUMNS, to = Constants.MAX_COLUMNS) int columns() {
        return Constants.MIN_DEFAULT_SIZE;
    }

    @Override
    public String toString() {
        return super.toString().replace(getClass().getSimpleName(), GUI.class.getSimpleName());
    }

}
