package it.angrybear.yagl.guis;

import org.jetbrains.annotations.NotNull;

public class DataGUI<T> extends PageableGUI {

    DataGUI() {

    }

    DataGUI(int size) {
        super(size);
    }

    DataGUI(@NotNull GUIType type) {
        super(type);
    }
}
