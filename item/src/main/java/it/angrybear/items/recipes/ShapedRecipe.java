package it.angrybear.items.recipes;

import it.angrybear.items.Item;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * An implementation of {@link Recipe} to express the shaped recipes in Minecraft.
 * An example of shaped recipe is the TNT, which requires all the items in an exact position.
 */
public class ShapedRecipe implements Recipe {
    @Getter
    private final String id;
    private final Item[] ingredients;
    @Getter
    private final Shape shape;
    @Getter
    @Setter
    private Item output;

    /**
     * Instantiates a new Shaped recipe.
     *
     * @param id the id
     */
    public ShapedRecipe(final @NotNull String id) {
        this.id = id;
        this.shape = new Shape();
        this.ingredients = new Item[0];
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
        this.ingredients[position] = item;
    }

    @Override
    public List<Item> getIngredients() {
        return Arrays.asList(this.ingredients);
    }

    /**
     * The type Shape.
     */
    @Getter
    protected static class Shape {
        private int rows;
        private int columns;

        /**
         * Sets columns.
         *
         * @param columns the columns
         */
        public void setColumns(int columns) {
            if (columns < 1 || columns > 2)
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
    }
}
