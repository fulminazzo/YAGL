package it.fulminazzo.yagl.gui;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * An implementation of {@link GUI} that allows a {@link GUIType}.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class TypeGUI extends GUIImpl {
    private static final int DEFAULT_ROWS = Constants.MIN_ROWS;

    private static final int DROPPER_ROWS = 3;
    private static final int DROPPER_COLUMNS = 3;

    private static final int BREWING_ROWS = 2;
    private static final int BREWING_COLUMNS = 3;

    private static final int LOOM_ROWS = 2;
    private static final int LOOM_COLUMNS = 3;
    private static final int LOOM_MIDDLE_LINE = 0;
    private static final int LOOM_SOUTH = 2;

    private final @NotNull GUIType inventoryType;

    /**
     * Internal constructor, used for serializing purposes.
     */
    private TypeGUI() {
        this.inventoryType = GUIType.CHEST;
    }

    /**
     * Instantiates a new Type gui.
     *
     * @param inventoryType the inventory type
     */
    protected TypeGUI(final @NotNull GUIType inventoryType) {
        super(inventoryType.getSize());
        this.inventoryType = inventoryType;
    }

    @Override
    public @Range(from = 0, to = Constants.MAX_TYPE_SLOT) int northEast() {
        if (this.inventoryType == GUIType.BREWING) return BREWING_ROWS - 1;
        else if (this.inventoryType == GUIType.LOOM) return LOOM_ROWS - 1;
        else return super.northEast();
    }

    @Override
    public @Range(from = 0, to = Constants.MAX_TYPE_SLOT) int middleLine() {
        if (this.inventoryType == GUIType.BREWING) return BREWING_ROWS;
        else if (this.inventoryType == GUIType.LOOM) return LOOM_MIDDLE_LINE;
        else return super.middleLine();
    }

    @Override
    public @Range(from = 0, to = Constants.MAX_TYPE_SLOT) int middleEast() {
        if (this.inventoryType == GUIType.LOOM) return LOOM_COLUMNS;
        else return super.middleEast();
    }

    @Override
    public @Range(from = 0, to = Constants.MAX_TYPE_SLOT) int southLine() {
        if (this.inventoryType == GUIType.BREWING) return BREWING_ROWS;
        else return super.southLine();
    }

    @Override
    public @Range(from = 0, to = Constants.MAX_TYPE_SLOT) int southWest() {
        if (this.inventoryType == GUIType.LOOM) return LOOM_SOUTH;
        else return super.southWest();
    }

    @Override
    public @Range(from = 0, to = Constants.MAX_TYPE_SLOT) int south() {
        if (this.inventoryType == GUIType.LOOM) return LOOM_SOUTH;
        else return super.south();
    }

    @Override
    public @Range(from = 0, to = Constants.MAX_TYPE_SLOT) int southEast() {
        if (this.inventoryType == GUIType.LOOM) return LOOM_SOUTH;
        else return super.southEast();
    }

    @Override
    public @Range(from = Constants.MIN_ROWS, to = Constants.MAX_ROWS) int rows() {
        if (this.inventoryType == GUIType.WORKBENCH ||
                this.inventoryType == GUIType.DROPPER ||
                this.inventoryType == GUIType.DISPENSER) return DROPPER_ROWS;
        else if (this.inventoryType == GUIType.BREWING) return BREWING_ROWS;
        else if (this.inventoryType == GUIType.LOOM) return LOOM_ROWS;
        else if (size() > Constants.MIN_DEFAULT_SIZE) return size() / Constants.MIN_DEFAULT_SIZE;
        else return DEFAULT_ROWS;
    }

    @Override
    public @Range(from = Constants.MIN_COLUMNS, to = Constants.MAX_COLUMNS) int columns() {
        if (this.inventoryType == GUIType.WORKBENCH ||
                this.inventoryType == GUIType.DROPPER ||
                this.inventoryType == GUIType.DISPENSER) return DROPPER_COLUMNS;
        else if (this.inventoryType == GUIType.BREWING) return BREWING_COLUMNS;
        else if (this.inventoryType == GUIType.LOOM) return LOOM_COLUMNS;
        else return Math.min(size(), Constants.MIN_DEFAULT_SIZE);
    }

    @Override
    public String toString() {
        return super.toString()
                .replace(getClass().getSimpleName(), GUI.class.getSimpleName())
                .replace("size=" + size(), "type=" + inventoryType);
    }

}
