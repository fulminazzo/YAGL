package it.angrybear.yagl.guis;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * An implementation of {@link GUI} that allows multiple GUI pages to be added.
 */
public class PageableGUI implements Iterable<GUI> {
    private final GUI templateGUI;
    private final List<GUI> pages = new LinkedList<>();

    private PageableGUI(final int size) {
        this.templateGUI = GUI.newGUI(size);
    }

    private PageableGUI(final @NotNull GUIType type) {
        this.templateGUI = GUI.newGUI(type);
    }

    private void forEachInternal(final @NotNull Consumer<? super GUI> function) {
        function.accept(this.templateGUI);
        this.pages.forEach(function);
    }

    @NotNull
    @Override
    public Iterator<GUI> iterator() {
        return this.pages.iterator();
    }
}
