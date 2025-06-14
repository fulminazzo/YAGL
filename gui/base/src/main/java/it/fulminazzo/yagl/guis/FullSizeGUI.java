package it.fulminazzo.yagl.guis;

import it.fulminazzo.fulmicollection.objects.FieldEquable;
import org.jetbrains.annotations.NotNull;

public class FullSizeGUI extends FieldEquable implements GUI {
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

}
