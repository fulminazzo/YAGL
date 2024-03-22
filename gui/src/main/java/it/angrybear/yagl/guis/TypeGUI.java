package it.angrybear.yagl.guis;

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
    TypeGUI() {
        super(0);
        this.inventoryType = null;
    }

    /**
     * Instantiates a new Type gui.
     *
     * @param inventoryType the inventory type
     */
    public TypeGUI(final @NotNull GUIType inventoryType) {
        super(inventoryType.getSize());
        this.inventoryType = inventoryType;
    }
}
