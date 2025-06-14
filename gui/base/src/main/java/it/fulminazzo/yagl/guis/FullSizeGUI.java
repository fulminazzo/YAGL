package it.fulminazzo.yagl.guis;

import it.fulminazzo.fulmicollection.objects.FieldEquable;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class FullSizeGUI extends FieldEquable {
    private static final int SECOND_INVENTORY_SIZE = 45;

    private final @NotNull GUI upperGUI;
    private final @NotNull GUI lowerGUI;

    FullSizeGUI(final int size) {
        this.upperGUI = GUI.newGUI(size);
        this.lowerGUI = GUI.newGUI(SECOND_INVENTORY_SIZE);
    }

    FullSizeGUI(final @NotNull GUIType type) {
        this.upperGUI = GUI.newGUI(type);
        this.lowerGUI = GUI.newGUI(SECOND_INVENTORY_SIZE);
    }

    int getCorrespondingSlot(final int slot) {
        GUIUtils.checkSlot(slot, size());
        if (slot >= upperGUI.size()) return slot - upperGUI.size();
        else return slot;
    }

    @NotNull GUI getCorrespondingGUI(final int slot) {
        GUIUtils.checkSlot(slot, size());
        if (slot >= upperGUI.size()) return lowerGUI;
        else return upperGUI;
    }

}
