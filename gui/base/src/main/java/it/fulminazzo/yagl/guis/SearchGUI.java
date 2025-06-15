package it.fulminazzo.yagl.guis;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yagl.contents.GUIContent;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A special implementation of {@link SearchGUI} that supports dynamic
 * searching of data from a string.
 *
 * @param <T> the type parameter
 */
public final class SearchGUI<T> extends DataGUI<T> {
    private final @NotNull BiPredicate<T, String> searchFunction;

    @Getter
    @Setter
    private @Nullable String query;

    /**
     * Instantiates a new Search gui.
     *
     * @param templateGUI    the templateGUI
     * @param dataConverter  the data converter
     * @param searchFunction the function to use to filter the data
     */
    SearchGUI(@NotNull SearchFullSizeGUI templateGUI,
              @NotNull Function<T, GUIContent> dataConverter,
              @NotNull BiPredicate<T, String> searchFunction) {
        super(templateGUI, dataConverter);
        this.searchFunction = searchFunction;
        templateGUI.setSearchGui(this);
    }

    @Override
    protected @NotNull List<T> getDataList() {
        return super.getDataList().stream()
                .filter(t -> this.searchFunction.test(t, this.query))
                .collect(Collectors.toList());
    }

    /**
     * Creates a new {@link SearchGUI} with the given type and a converter.
     *
     * @param <T>           the type of the data
     * @param dataConverter the data converter
     * @return the search gui
     */
    public static <T> @NotNull SearchGUI<T> newGUI(
            final @NotNull Function<T, GUIContent> dataConverter,
            @NotNull BiPredicate<T, String> searchFunction) {
        return new SearchGUI<>(new SearchFullSizeGUI(), dataConverter, searchFunction);
    }

    /**
     * Creates a new {@link SearchGUI} with the given type and a converter.
     *
     * @param <T>           the type of the data
     * @param lowerGUISize  the size of the lower GUI
     * @param dataConverter the data converter
     * @return the search gui
     */
    public static <T> @NotNull SearchGUI<T> newGUI(
            final int lowerGUISize,
            final @NotNull Function<T, GUIContent> dataConverter,
            @NotNull BiPredicate<T, String> searchFunction) {
        SearchFullSizeGUI gui = new SearchFullSizeGUI();
        gui.getLowerGUI().resize(lowerGUISize);
        return new SearchGUI<>(gui, dataConverter, searchFunction);
    }

    /**
     * Creates a new {@link SearchGUI} with the given type, converter and data.
     *
     * @param <T>           the type of the data
     * @param dataConverter the data converter
     * @param data          the data
     * @return the search gui
     */
    @SafeVarargs
    public static <T> @NotNull SearchGUI<T> newGUI(
            final @NotNull Function<T, GUIContent> dataConverter,
            @NotNull BiPredicate<T, String> searchFunction,
            final T @NotNull ... data) {
        return new SearchGUI<>(new SearchFullSizeGUI(), dataConverter, searchFunction).setData(data);
    }

    /**
     * Creates a new {@link SearchGUI} with the given type, converter and data.
     *
     * @param <T>           the type of the data
     * @param lowerGUISize  the size of the lower GUI
     * @param dataConverter the data converter
     * @param data          the data
     * @return the search gui
     */
    @SafeVarargs
    public static <T> @NotNull SearchGUI<T> newGUI(
            final int lowerGUISize,
            final @NotNull Function<T, GUIContent> dataConverter,
            @NotNull BiPredicate<T, String> searchFunction,
            final T @NotNull ... data) {
        SearchFullSizeGUI gui = new SearchFullSizeGUI();
        gui.getLowerGUI().resize(lowerGUISize);
        return new SearchGUI<>(gui, dataConverter, searchFunction).setData(data);
    }

    /**
     * Creates a new {@link SearchGUI} with the given type, converter and data.
     *
     * @param <T>           the type of the data
     * @param dataConverter the data converter
     * @param data          the data
     * @return the search gui
     */
    public static <T> @NotNull SearchGUI<T> newGUI(
            final @NotNull Function<T, GUIContent> dataConverter,
            @NotNull BiPredicate<T, String> searchFunction,
            final @NotNull Collection<T> data) {
        return new SearchGUI<>(new SearchFullSizeGUI(), dataConverter, searchFunction).setData(data);
    }

    /**
     * Creates a new {@link SearchGUI} with the given type, converter and data.
     *
     * @param <T>           the type of the data
     * @param lowerGUISize  the size of the lower GUI
     * @param dataConverter the data converter
     * @param data          the data
     * @return the search gui
     */
    public static <T> @NotNull SearchGUI<T> newGUI(
            final int lowerGUISize,
            final @NotNull Function<T, GUIContent> dataConverter,
            @NotNull BiPredicate<T, String> searchFunction,
            final @NotNull Collection<T> data) {
        SearchFullSizeGUI gui = new SearchFullSizeGUI();
        gui.getLowerGUI().resize(lowerGUISize);
        return new SearchGUI<>(gui, dataConverter, searchFunction).setData(data);
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

        @Override
        public FullSizeGUI copy() {
            return new Refl<>(new SearchFullSizeGUI())
                    .setFieldObject("upperGUI", getUpperGUI().copy())
                    .setFieldObject("lowerGUI", getLowerGUI().copy())
                    .setFieldObject("searchGui", this.searchGui)
                    .getObject();
        }
    }

}
