package it.angrybear.yagl.guis;

import it.angrybear.yagl.contents.GUIContent;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * A basic implementation of {@link GUI}.
 */
@Getter
abstract class GUIImpl implements GUI {
    protected static final int MAX_SIZE = 54;

    protected GUI previous;
    protected GUI next;
    protected GUI back;
    protected String title;
    protected List<GUIContent> contents;
    protected final Set<Integer> movableSlots;

    /**
     * Instantiates a new Gui.
     *
     * @param size the size
     */
    public GUIImpl(int size) {
        if (size < 0 || size > MAX_SIZE) throw new IllegalArgumentException("GUIs size must be bound between 0 and 54!");
        this.contents = createList(size, null);
        this.movableSlots = new HashSet<>();
    }

    @Override
    public @NotNull GUI setTitle(@Nullable String title) {
        this.title = title;
        return this;
    }

    @Override
    public @NotNull GUI setPrevious(@Nullable GUI previous) {
        this.previous = previous;
        return this;
    }

    @Override
    public @NotNull GUI setNext(@Nullable GUI next) {
        this.next = next;
        return this;
    }

    @Override
    public @NotNull GUI setBack(@Nullable GUI back) {
        this.back = back;
        return this;
    }

    @Override
    public @Nullable GUIContent getContent(int slot) {
        return this.contents.get(slot);
    }

    @Override
    public int getSize() {
        return this.contents.size();
    }

    @Override
    public @NotNull GUI setMovable(int slot, boolean movable) {
        if (movable) this.movableSlots.add(slot);
        else this.movableSlots.remove(slot);
        return this;
    }

    @Override
    public boolean isMovable(int slot) {
        return this.movableSlots.contains(slot);
    }

    @Override
    public @NotNull GUI addContent(GUIContent @NotNull ... contents) {
        int j = 0;
        main_loop:
        for (int i = 0; i < contents.length; i++) {
            GUIContent content = contents[i];
            for (; j < this.contents.size(); j++) {
                GUIContent c = this.contents.get(j);
                if (c == null) {
                    this.contents.set(j, content);
                    continue main_loop;
                }
            }
            throw new IllegalArgumentException(String.format("Could not set content at index %s because contents are already full", i));
        }
        return this;
    }

    @Override
    public @NotNull GUI setContent(int slot, @NotNull GUIContent content) {
        this.contents.set(slot, content);
        return this;
    }

    @Override
    public @NotNull GUI unsetContent(int slot) {
        this.contents.set(slot, null);
        return this;
    }

    @Override
    public @NotNull List<GUIContent> getContents() {
        return new LinkedList<>(this.contents);
    }

    @NotNull
    @Override
    public Iterator<GUIContent> iterator() {
        return this.contents.iterator();
    }

    /**
     * Create a new list of the given size.
     *
     * @param size     the size
     * @param copyFrom if not null, copy the contents of this list in the resulting one
     * @return the list
     */
    protected List<GUIContent> createList(int size, final List<GUIContent> copyFrom) {
        List<GUIContent> list = new LinkedList<>();
        for (int i = 0; i < size; i++) list.add(null);
        if (copyFrom != null) 
            for (int i = 0; i < Math.min(copyFrom.size(), list.size()); i++)
                list.set(i, copyFrom.get(i));
        return list;
    }
}
