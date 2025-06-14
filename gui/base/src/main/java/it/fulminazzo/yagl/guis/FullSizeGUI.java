package it.fulminazzo.yagl.guis;

import it.fulminazzo.fulmicollection.objects.FieldEquable;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link GUI} that supports a full size display.
 * When shown to the player, their inventory will be used as well to
 * show contents.
 */
@Getter
public class FullSizeGUI extends FieldEquable {
    private static final int SECOND_INVENTORY_SIZE = 45;

    private final @NotNull GUI upperGUI;
    private final @NotNull GUI lowerGUI;

    /**
     * Instantiates a new Full size gui.
     *
     * @param size the size
     */
    FullSizeGUI(final int size) {
        this.upperGUI = GUI.newGUI(size);
        this.lowerGUI = GUI.newGUI(SECOND_INVENTORY_SIZE);
    }

    /**
     * Instantiates a new Full size gui.
     *
     * @param type the type
     */
    FullSizeGUI(final @NotNull GUIType type) {
        this.upperGUI = GUI.newGUI(type);
        this.lowerGUI = GUI.newGUI(SECOND_INVENTORY_SIZE);
    }

    /**
     * Gets the relative slot from the given slot.
     *
     * @param slot the slot
     * @return the relative slot
     */
    int getCorrespondingSlot(final int slot) {
        GUIUtils.checkSlot(slot, size());
        if (slot >= upperGUI.size()) return slot - upperGUI.size();
        else return slot;
    }

    /**
     * Gets the corresponding gui from the given slot.
     *
     * @param slot the slot
     * @return the corresponding gui
     */
    @NotNull GUI getCorrespondingGUI(final int slot) {
        GUIUtils.checkSlot(slot, size());
        if (slot >= upperGUI.size()) return lowerGUI;
        else return upperGUI;
    }

}
