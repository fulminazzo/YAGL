package it.angrybear.yagl.items.recipes;

import it.angrybear.yagl.items.Item;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

/**
 * An implementation of {@link Recipe} to express the shaped recipes in Minecraft.
 * An example of shaped recipe is the TNT, which requires all the items in an exact position.
 */
public class ShapedRecipe extends RecipeImpl {
    private final @NotNull List<Item> ingredients;
    @Getter
    private final @NotNull Shape shape;

    private ShapedRecipe() {
        this("pending");
    }

    /**
     * Instantiates a new Shaped recipe.
     *
     * @param id the id
     */
    public ShapedRecipe(final @NotNull String id) {
        super(id);
        this.shape = new Shape();
        this.ingredients = new LinkedList<>();
    }

    @Override
    public @NotNull ShapedRecipe setOutput(final @NotNull Item output) {
        this.output = output.copy(Item.class);
        return this;
    }

    /**
     * Sets shape in the crafting table.
     * A 3x3 shape means all the crafting, a 1x1 means only the first slot.
     *
     * @param rows    the rows
     * @param columns the columns
     * @return this recipe
     */
    public @NotNull ShapedRecipe setShape(final int rows, final int columns) {
        this.shape.setRows(rows);
        this.shape.setColumns(columns);
        return this;
    }

    /**
     * Sets all the given ingredients using {@link #setIngredient(int, Item)},
     * where the first number is the index of the item.
     * If the array is too big, an {@link IllegalArgumentException} will be thrown
     * (but the ingredients will still be put in place).
     *
     * @param items the items
     * @return this recipe
     */
    public @NotNull ShapedRecipe setIngredients(final Item @NotNull ... items) {
        for (int i = 0; i < items.length; i++) setIngredient(i, items[i]);
        return this;
    }

    /**
     * Sets ingredient.
     *
     * @param position the position
     * @param item     the item
     * @return this recipe
     */
    public @NotNull ShapedRecipe setIngredient(final int position, final @NotNull Item item) {
        if (!this.shape.contains(position))
            throw new IllegalArgumentException(String.format("Shape %sx%s does not allow position %s",
                    this.shape.getRows(), this.shape.getColumns(), position));
        while (this.ingredients.size() - 1 < position) this.ingredients.add(null);
        this.ingredients.set(position, item.copy(Item.class));
        return this;
    }

    @Override
    public @NotNull List<Item> getIngredients() {
        return new LinkedList<>(this.ingredients);
    }

    /**
     * The type Shape.
     */
    @Getter
    public static class Shape {
        static final int MIN_COLUMNS = 1, MAX_COLUMNS = 3;
        static final int MIN_ROWS = 1, MAX_ROWS = 3;
        private int rows;
        private int columns;

        /**
         * Sets columns.
         * Should be between {@link #MIN_COLUMNS} and {@link #MAX_COLUMNS}.
         *
         * @param columns the columns
         */
        public void setColumns(final int columns) {
            if (columns < MIN_COLUMNS || columns > MAX_COLUMNS)
                throw new IllegalArgumentException(String.format("Columns size should be between %s and %s", MIN_COLUMNS, MAX_COLUMNS));
            this.columns = columns;
        }

        /**
         * Sets rows.
         * Should be between {@link #MIN_ROWS} and {@link #MAX_ROWS}.
         *
         * @param rows the rows
         */
        public void setRows(final int rows) {
            if (rows < MIN_ROWS || rows > MAX_ROWS)
                throw new IllegalArgumentException(String.format("Rows size should be between %s and %s", MIN_ROWS, MAX_ROWS));
            this.rows = rows;
        }

        /**
         * Checks if the given number is in a valid range between 0 and {@link #rows} * {@link #columns}.
         *
         * @param num the num
         * @return true if contains
         */
        public boolean contains(int num) {
            return num >= 0 && num < this.rows * this.columns;
        }

        /**
         * Compares the given shape with the current one.
         *
         * @param shape the shape
         * @return true if they have the same rows and columns
         */
        public boolean equals(@Nullable Shape shape) {
            if (shape == null) return false;
            return this.columns == shape.columns && this.rows == shape.rows;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Shape) return equals((Shape) o);
            return super.equals(o);
        }

        @Override
        public String toString() {
            return String.format("Shape {rows: %s, columns: %s}", this.rows, this.columns);
        }
    }
}
