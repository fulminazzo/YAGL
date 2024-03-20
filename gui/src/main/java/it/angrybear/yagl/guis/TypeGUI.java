package it.angrybear.yagl.guis;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link GUI} that allows a {@link GUIType}.
 */
@Getter
public class TypeGUI extends GUIImpl {
    private final GUIType type;

    /**
     * Instantiates a new Type gui.
     *
     * @param type the type
     */
    public TypeGUI(final @NotNull GUIType type) {
        super(type.getSize());
        this.type = type;
    }
}
