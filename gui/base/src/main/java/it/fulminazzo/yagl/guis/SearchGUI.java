package it.fulminazzo.yagl.guis;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A special implementation of {@link DataGUI} that supports dynamic
 * searching of data from a string.
 */
public final class SearchGUI {
    @Getter
    @Setter
    private @Nullable String query;

    /**
     * An implementation of {@link FullSizeGUI} that provides methods to interface with
     * the corresponding {@link SearchGUI}.
     */
    static class SearchFullSizeGUI extends FullSizeGUI {
        private final @NotNull SearchGUI gui;

        /**
         * Instantiates a new Search full size gui.
         *
         * @param gui the gui
         */
        SearchFullSizeGUI(final @NotNull SearchGUI gui) {
            super(GUIType.ANVIL);
            this.gui = gui;
        }

        /**
         * Gets the search text from the search GUI.
         *
         * @return the query
         */
        public @Nullable String getQuery() {
            return this.gui.getQuery();
        }

        /**
         * Sets the search text to the search GUI.
         *
         * @param query the query
         * @return this gui
         */
        public @NotNull SearchFullSizeGUI setQuery(final @Nullable String query) {
            this.gui.setQuery(query);
            return this;
        }

    }

}
