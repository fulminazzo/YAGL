package it.fulminazzo.yagl.guis;

import it.fulminazzo.yagl.contents.GUIContent;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * A special implementation of {@link DataGUI} that supports dynamic
 * searching of data from a string.
 */
public final class SearchGUI<T> extends DataGUI<T> {
    @Getter
    @Setter
    private @Nullable String query;

    /**
     * Instantiates a new Search gui.
     *
     * @param templateGUI   the templateGUI
     * @param dataConverter the data converter
     */
    SearchGUI(@NotNull SearchFullSizeGUI templateGUI, @NotNull Function<T, GUIContent> dataConverter) {
        super(templateGUI, dataConverter);
        templateGUI.setSearchGui(this);
    }

    /**
     * An implementation of {@link FullSizeGUI} that provides methods to interface with
     * the corresponding {@link SearchGUI}.
     */
    static class SearchFullSizeGUI extends FullSizeGUI {
        @Setter
        private @Nullable SearchGUI<?> searchGui;

        /**
         * Instantiates a new Search full size gui.
         */
        SearchFullSizeGUI() {
            super(GUIType.ANVIL);
        }

        /**
         * Gets the search text from the search GUI.
         *
         * @return the query
         */
        public @Nullable String getQuery() {
            return getSearchGui().getQuery();
        }

        /**
         * Sets the search text to the search GUI.
         *
         * @param query the query
         * @return this gui
         */
        public @NotNull SearchFullSizeGUI setQuery(final @Nullable String query) {
            getSearchGui().setQuery(query);
            return this;
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

    }

}
