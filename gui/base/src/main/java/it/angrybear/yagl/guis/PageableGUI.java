package it.angrybear.yagl.guis;

import it.angrybear.yagl.Metadatable;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

/**
 * An implementation of {@link GUI} that allows multiple GUI pages to be added.
 */
public class PageableGUI implements Iterable<GUI>, Metadatable {
    private final GUI templateGUI;
    private final List<GUI> pages = new LinkedList<>();
    private final Map<String, String> variables = new HashMap<>();

    private PageableGUI(final int size) {
        this.templateGUI = GUI.newGUI(size);
    }

    private PageableGUI(final @NotNull GUIType type) {
        this.templateGUI = GUI.newGUI(type);
    }

    /**
     * Sets pages.
     *
     * @param pages the pages
     */
    public void setPages(final int pages) {
        if (pages < 0) throw new IllegalArgumentException(String.format("Invalid pages '%s'", pages));
        int s;
        while ((s = this.pages.size()) - pages >= 0) this.pages.remove(s - 1);
        //TODO: copy method
//        while (pages - this.pages.size() > 0) this.pages.add(this.templateGUI.copy());
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

    /**
     * Creates a new {@link PageableGUI} with the given size.
     *
     * @param size the size
     * @return the pageable gui
     */
    public static PageableGUI newGUI(final int size) {
        return new PageableGUI(size);
    }

    /**
     * Creates a new {@link PageableGUI} with the given type.
     *
     * @param type the type
     * @return the pageable gui
     */
    public static PageableGUI newGUI(final @NotNull GUIType type) {
        return new PageableGUI(type);
    }

    @Override
    public @NotNull Map<String, String> variables() {
        return this.variables;
    }
}
