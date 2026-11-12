package it.fulminazzo.yagl.gui;

/**
 * Constants used across the GUI system.
 */
public final class Constants {

    /**
     * The minimum size for a standard GUI (1 row x 9 columns = 9 slots).
     */
    public static final int MIN_DEFAULT_SIZE = 9;

    /**
     * The maximum size for a standard GUI (6 rows x 9 columns = 54 slots).
     */
    public static final int MAX_SIZE = 54;

    /**
     * The maximum slot index for a GUI.
     */
    public static final int MAX_SLOT = MAX_SIZE - 1;

    /**
     * The minimum size for a {@link TypeGUI} (the smallest {@link GUIType} size is 1, e.g., BEACON, LECTERN).
     */
    public static final int MIN_TYPE_SIZE = 1;

    /**
     * The maximum size for a {@link TypeGUI} (the largest {@link GUIType} size is 36, i.e., PLAYER).
     */
    public static final int MAX_TYPE_SIZE = 36;

    /**
     * The maximum slot index for a {@link TypeGUI}.
     */
    public static final int MAX_TYPE_SLOT = MAX_TYPE_SIZE - 1;

    /**
     * The maximum size for the secondary (lower) GUI in a {@link FullscreenGUI} (player inventory = 36 slots).
     */
    public static final int SECONDARY_GUI_SIZE = 36;

    /**
     * The maximum slot index for a {@link FullscreenGUI} ({@link #MAX_SIZE} + {@link #SECONDARY_GUI_SIZE} - 1 = 89).
     */
    public static final int FULLSCREEN_MAX_SLOT = MAX_SIZE + SECONDARY_GUI_SIZE - 1;

    /**
     * The minimum number of rows for a GUI.
     */
    public static final int MIN_ROWS = 1;

    /**
     * The maximum number of rows for a standard GUI ({@link #MAX_SIZE} / {@link #MIN_DEFAULT_SIZE} = 6).
     */
    public static final int MAX_ROWS = MAX_SIZE / MIN_DEFAULT_SIZE;

    /**
     * The minimum number of columns for a GUI (1 column for typed GUIs).
     */
    public static final int MIN_COLUMNS = 1;

    /**
     * The maximum number of columns for a standard GUI (9 columns).
     */
    public static final int MAX_COLUMNS = MIN_DEFAULT_SIZE;

    /**
     * The minimum number of pages for a {@link PageableGUI}.
     */
    public static final int MIN_PAGES = 0;

}
