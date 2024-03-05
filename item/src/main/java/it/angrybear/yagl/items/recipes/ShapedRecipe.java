package it.angrybear.yagl.items.recipes;

import it.angrybear.yagl.items.Item;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

/**
 * An implementation of {@link Recipe} to express the shaped recipes in Minecraft.
 * An example of shaped recipe is the TNT, which requires all the items in an exact position.
 */
public class ShapedRecipe implements Recipe {
    @Getter
    private final String id;
    private final List<Item> ingredients;
    @Getter
    private final Shape shape;
    @Getter
    private Item output;

    /**
     * Instantiates a new Shaped recipe.
     *
     * @param id the id
     */
    public ShapedRecipe(final @NotNull String id) {
        this.id = id;
        this.shape = new Shape();
        this.ingredients = new LinkedList<>();
    }

    @Override
    public void setOutput(final @NotNull Item output) {
        this.output = output.copy(Item.class);
    }

    /**
     * Sets shape in the crafting table.
     * A 3x3 shape means all the crafting, a 1x1 means only the first slot.
     *
     * @param rows    the rows
     * @param columns the columns
     */
    public void setShape(final int rows, final int columns) {
        this.shape.setRows(rows);
        this.shape.setColumns(columns);
    }

    /**
     * Sets ingredient.
     *
     * @param position the position
     * @param item     the item
     */
    public void setIngredient(final int position, final Item item) {
        if (!this.shape.contains(position))
            throw new IllegalArgumentException(String.format("Shape %sx%s does not allow position %s",
                    this.shape.getRows(), this.shape.getColumns(), position));
        while (this.ingredients.size() - 1 < position) this.ingredients.add(null);
        this.ingredients.set(position, item.copy(Item.class));
    }

    @Override
    public List<Item> getIngredients() {
        return new LinkedList<>(this.ingredients);
    }

    /**
     * The type Shape.
     */
    @Getter
    public static class Shape {
        private int rows;
        private int columns;

        /**
         * Sets columns.
         *
         * @param columns the columns
         */
        public void setColumns(int columns) {
            if (columns < 1 || columns > 3)
                throw new IllegalArgumentException("Columns size should be between 1 and 3");
            this.columns = columns;
        }

        /**
         * Sets rows.
         *
         * @param rows the rows
         */
        public void setRows(int rows) {
            if (rows < 1 || rows > 3)
                throw new IllegalArgumentException("Rows size should be between 1 and 3");
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
        public boolean equals(Shape shape) {
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
