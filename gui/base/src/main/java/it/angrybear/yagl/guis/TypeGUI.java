package it.angrybear.yagl.guis;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link GUI} that allows a {@link GUIType}.
 */
@Getter
public class TypeGUI extends GUIImpl {
    private static final int DROPPER_ROWS = 3;
    private static final int DROPPER_COLUMNS = 3;
    private static final int BREWING_ROWS = 2;
    private static final int BREWING_COLUMNS = 3;

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
        switch (this.inventoryType) {
            case BREWING:
                return BREWING_ROWS - 1;
            default:
                return super.northEast();
        }
    }

    @Override
    public int middleLine() {
        switch (this.inventoryType) {
            case BREWING:
                return BREWING_ROWS;
            default:
                return super.middleLine();
        }
    }

    @Override
    public int southLine() {
        switch (this.inventoryType) {
            case BREWING:
                return BREWING_ROWS;
            default:
                return super.southLine();
        }
    }

    @Override
    public int rows() {
        switch (this.inventoryType) {
            case WORKBENCH:
            case DROPPER:
            case DISPENSER:
                return DROPPER_ROWS;
            case BREWING:
                return BREWING_ROWS;
        }
        if (size() > DefaultGUI.COLUMNS) return size() / DefaultGUI.COLUMNS;
        return 1;
    }

    @Override
    public int columns() {
        switch (this.inventoryType) {
            case WORKBENCH:
            case DISPENSER:
            case DROPPER:
                return DROPPER_COLUMNS;
            case BREWING:
                return BREWING_COLUMNS;
        }
        return Math.min(size(), DefaultGUI.COLUMNS);
    }
}
