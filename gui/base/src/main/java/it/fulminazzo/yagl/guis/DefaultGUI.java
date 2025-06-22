package it.fulminazzo.yagl.guis;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Represents a "chest" GUI that cannot be resized.
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
class DefaultGUI extends GUIImpl {
    static final int COLUMNS = 9;

    /**
     * Instantiates a new Gui.
     *
     * @param size the size
     */
    DefaultGUI(int size) {
        super(size);
        checkSize(size);
    }

    /**
     * Checks if the given size is in bounds.
     *
     * @param size  the size
     */
    void checkSize(int size) {
        if (size < COLUMNS || size > MAX_SIZE)
            throw new IllegalArgumentException(String.format("GUIs size must be bound between %s and %s!", COLUMNS, MAX_SIZE));
        if (size % COLUMNS != 0)
            throw new IllegalArgumentException(String.format("%s is not a valid size. Only multiple of %s can be accepted",
                    size, COLUMNS));
    }

    @Override
    public int rows() {
        return size() / COLUMNS;
    }

    @Override
    public int columns() {
        return COLUMNS;
    }
}
