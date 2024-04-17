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
    public int rows() {
        if (size() > DefaultGUI.COLUMNS) return size() / DefaultGUI.COLUMNS;
        switch (this.inventoryType) {
            case DROPPER:
            case DISPENSER:
                return DROPPER_ROWS;
        }
        return 1;
    }

    @Override
    public int columns() {
        switch (this.inventoryType) {
            case DISPENSER:
            case DROPPER:
                return DROPPER_COLUMNS;
        }
        return Math.min(size(), DefaultGUI.COLUMNS);
    }
}
