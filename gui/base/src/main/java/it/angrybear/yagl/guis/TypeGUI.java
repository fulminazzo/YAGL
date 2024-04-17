package it.angrybear.yagl.guis;

import it.angrybear.yagl.exceptions.NotImplemented;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link GUI} that allows a {@link GUIType}.
 */
@Getter
public class TypeGUI extends GUIImpl {
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
        throw new NotImplemented();
    }

    @Override
    public int columns() {
        throw new NotImplemented();
    }
}
