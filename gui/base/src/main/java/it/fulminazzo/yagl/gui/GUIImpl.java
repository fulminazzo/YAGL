package it.fulminazzo.yagl.gui;

import it.fulminazzo.yagl.action.BiGUIAction;
import it.fulminazzo.yagl.action.GUIAction;
import it.fulminazzo.yagl.content.GUIContent;
import it.fulminazzo.yagl.util.ValidationUtils;
import it.fulminazzo.yagl.viewer.Viewer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.*;

/**
 * A basic implementation of {@link GUI}.
 */
@EqualsAndHashCode
abstract class GUIImpl implements GUI {
    @Getter
    protected @Nullable String title;
    protected List<Contents> contents;
    protected final @NotNull Set<Integer> movableSlots;
    protected final Map<String, String> variables = new HashMap<>();

    protected GUIAction clickOutsideAction;
    protected GUIAction openGUIAction;
    protected GUIAction closeGUIAction;
    protected BiGUIAction changeGUIAction;

    /**
     * Instantiates a new Gui.
     */
    GUIImpl() {
        this.contents = new ArrayList<>();
        this.movableSlots = new HashSet<>();
    }

    /**
     * Instantiates a new Gui.
     *
     * @param size the size
     */
    public GUIImpl(final @Range(from = Constants.MIN_TYPE_SIZE, to = Constants.MAX_SIZE) int size) {
        ValidationUtils.checkRange("size", size, Constants.MIN_TYPE_SIZE, Constants.MAX_SIZE);
        this.contents = createContents(size, null);
        this.movableSlots = new HashSet<>();
    }

    @Override
    public void open(final @NotNull Viewer viewer) {
        GUIUtils.executeGUIAdapterFunction("openGUI", this, viewer);
    }

    @Override
    public @NotNull GUI setTitle(final @Nullable String title) {
        this.title = title;
        return this;
    }

    @Override
    public int size() {
        return this.contents.size();
    }

    @Override
    public @NotNull GUI clear() {
        Collections.fill(this.contents, null);
        return this;
    }

    @Override
    public @NotNull GUI setMovable(final @Range(from = 0, to = Constants.MAX_SLOT) int slot, final boolean movable) {
        GUIUtils.checkSlot(slot, size());
        if (movable) this.movableSlots.add(slot);
        else this.movableSlots.remove(slot);
        return this;
    }

    @Override
    public boolean isMovable(final @Range(from = 0, to = Constants.MAX_SLOT) int slot) {
        GUIUtils.checkSlot(slot, size());
        return this.movableSlots.contains(slot);
    }

    @Override
    public @NotNull List<GUIContent> getContents(final @Range(from = 0, to = Constants.MAX_SLOT) int slot) {
        GUIUtils.checkSlot(slot, size());
        Contents contents = this.contents.get(slot);
        if (contents == null) return new LinkedList<>();
        return contents.getContents();
    }

    @Override
    public @NotNull GUI addContent(final GUIContent @NotNull ... contents) {
        int j = 0;
        for (int i = 0; i < contents.length; i++) {
            j = addSingle(contents[i], j);
            if (j >= size())
                throw GUIUtils.cannotAddContentAtIndexException(i);
        }
        return this;
    }

    /**
     * Tries to add the given content from the given index.
     *
     * @param content the content
     * @param index   the index
     * @return the new index if it was successful, an index higher than {@link #size()} if failed
     */
    protected int addSingle(final @NotNull GUIContent content, int index) {
        for (; index < this.contents.size(); index++) {
            Contents c = this.contents.get(index);
            if (c == null) {
                this.contents.set(index, new Contents(content));
                break;
            }
        }
        return index;
    }

    @Override
    public @NotNull GUI setContents(final @Range(from = 0, to = Constants.MAX_SLOT) int slot, final GUIContent @NotNull ... contents) {
        GUIUtils.checkSlot(slot, size());
        this.contents.set(slot, new Contents(contents));
        return this;
    }

    @Override
    public @NotNull GUI unsetContent(final @Range(from = 0, to = Constants.MAX_SLOT) int slot) {
        GUIUtils.checkSlot(slot, size());
        this.contents.set(slot, new Contents());
        return this;
    }

    @Override
    public @NotNull List<GUIContent> getContents() {
        List<GUIContent> list = new ArrayList<>();
        for (Contents content : this.contents)
            if (content != null)
                Collections.addAll(list, content.contents);
        return list;
    }

    @Override
    public @NotNull GUI onClickOutside(final @NotNull GUIAction action) {
        this.clickOutsideAction = action;
        return this;
    }

    @Override
    public @NotNull Optional<GUIAction> clickOutsideAction() {
        return Optional.ofNullable(this.clickOutsideAction);
    }

    @Override
    public @NotNull GUI onOpenGUI(final @NotNull GUIAction action) {
        this.openGUIAction = action;
        return this;
    }

    @Override
    public @NotNull Optional<GUIAction> openGUIAction() {
        return Optional.ofNullable(this.openGUIAction);
    }

    @Override
    public @NotNull GUI onCloseGUI(final @NotNull GUIAction action) {
        this.closeGUIAction = action;
        return this;
    }

    @Override
    public @NotNull Optional<GUIAction> closeGUIAction() {
        return Optional.ofNullable(this.closeGUIAction);
    }

    @Override
    public @NotNull GUI onChangeGUI(final @NotNull BiGUIAction action) {
        this.changeGUIAction = action;
        return this;
    }

    @Override
    public @NotNull Optional<BiGUIAction> changeGUIAction() {
        return Optional.ofNullable(this.changeGUIAction);
    }

    @Override
    public @NotNull Map<String, String> variables() {
        return this.variables;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder()
                .append(getClass().getSimpleName().replace("Impl", ""))
                .append("{");
        if (title != null && !title.isEmpty())
            builder.append("'").append(title).append("', ");
        return builder.append("size=").append(size()).append("}").toString();
    }

    /**
     * Create a new list of the given size.
     *
     * @param size     the size
     * @param copyFrom if not null, copy the contents of this list in the resulting one
     * @return the list
     */
    protected @NotNull List<Contents> createContents(final @Range(from = 0, to = Constants.MAX_SIZE) int size,
                                                     final @Nullable List<Contents> copyFrom) {
        List<Contents> contents = new LinkedList<>();
        for (int i = 0; i < size; i++) contents.add(null);
        if (copyFrom != null)
            for (int i = 0; i < Math.min(copyFrom.size(), contents.size()); i++)
                contents.set(i, copyFrom.get(i));
        return contents;
    }

    /**
     * A type to keep track of multiple {@link GUIContent} for one slot.
     */
    public static class Contents {
        private final GUIContent @NotNull [] contents;

        /**
         * Instantiates a new Contents.
         *
         * @param contents the contents
         */
        protected Contents(final GUIContent @NotNull ... contents) {
            this.contents = contents;
        }

        /**
         * Gets contents.
         *
         * @return the contents
         */
        public @NotNull List<GUIContent> getContents() {
            return Arrays.asList(this.contents);
        }

        /**
         * Copies the current object to a new one.
         *
         * @return the copy
         */
        public @NotNull Contents copy() {
            return new GUIImpl.Contents(Arrays.stream(this.contents)
                    .map(c -> c == null ? null : c.copy())
                    .toArray(GUIContent[]::new));
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Contents) {
                Contents c = (Contents) o;
                return Arrays.equals(this.contents, c.contents);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(this.contents);
        }

        @Override
        public @NotNull String toString() {
            return Arrays.toString(this.contents);
        }

    }

}
