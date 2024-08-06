package it.angrybear.yagl.guis;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link GUI} that allows a {@link GUIType}.
 */
@Getter
public class TypeGUI extends GUIImpl {
    private static final int DEFAULT_ROWS = 1;

    private static final int DROPPER_ROWS = 3;
    private static final int DROPPER_COLUMNS = 3;

    private static final int BREWING_ROWS = 2;
    private static final int BREWING_COLUMNS = 3;

    private static final int LOOM_ROWS = 2;
    private static final int LOOM_COLUMNS = 3;
    private static final int LOOM_MIDDLE_LINE = 0;
    private static final int LOOM_SOUTH = 2;

    private final GUIType inventoryType;

    /**
     * Internal constructor, used for serializing purposes.
     */
    private TypeGUI() {
        this.inventoryType = null;
    }

    /**
     * Instantiates a new Type gui.
     *
     * @param inventoryType the inventory type
     */
    TypeGUI(final @NotNull GUIType inventoryType) {
        super(inventoryType.getSize());
        this.inventoryType = inventoryType;
    }

    @Override
    public int northEast() {
        if (this.inventoryType == GUIType.BREWING) return BREWING_ROWS - 1;
        else if (this.inventoryType == GUIType.LOOM) return LOOM_ROWS - 1;
        else return super.northEast();
    }

    @Override
    public int middleLine() {
        if (this.inventoryType == GUIType.BREWING) return BREWING_ROWS;
        else if (this.inventoryType == GUIType.LOOM) return LOOM_MIDDLE_LINE;
        else return super.middleLine();
    }

    @Override
    public int middleEast() {
        if (this.inventoryType == GUIType.LOOM) return LOOM_COLUMNS;
        else return super.middleEast();
    }

    @Override
    public int southLine() {
        if (this.inventoryType == GUIType.BREWING) return BREWING_ROWS;
        else return super.southLine();
    }

    @Override
    public int southWest() {
        if (this.inventoryType == GUIType.LOOM) return LOOM_SOUTH;
        else return super.southWest();
    }

    @Override
    public int south() {
        if (this.inventoryType == GUIType.LOOM) return LOOM_SOUTH;
        else return super.south();
    }

    @Override
    public int southEast() {
        if (this.inventoryType == GUIType.LOOM) return LOOM_SOUTH;
        else return super.southEast();
    }

    @Override
    public int rows() {
        if (this.inventoryType == GUIType.WORKBENCH ||
                this.inventoryType == GUIType.DROPPER ||
                this.inventoryType == GUIType.DISPENSER) return DROPPER_ROWS;
        else if (this.inventoryType == GUIType.BREWING) return BREWING_ROWS;
        else if (this.inventoryType == GUIType.LOOM) return LOOM_ROWS;
        else if (size() > DefaultGUI.COLUMNS) return size() / DefaultGUI.COLUMNS;
        else return DEFAULT_ROWS;
    }

    @Override
    public int columns() {
        if (this.inventoryType == GUIType.WORKBENCH ||
                this.inventoryType == GUIType.DROPPER ||
                this.inventoryType == GUIType.DISPENSER) return DROPPER_COLUMNS;
        else if (this.inventoryType == GUIType.BREWING) return BREWING_COLUMNS;
        else if (this.inventoryType == GUIType.LOOM) return LOOM_COLUMNS;
        else return Math.min(size(), DefaultGUI.COLUMNS);
    }
}
