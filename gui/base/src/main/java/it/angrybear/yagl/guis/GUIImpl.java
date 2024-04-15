package it.angrybear.yagl.guis;

import it.angrybear.yagl.actions.BiGUIAction;
import it.angrybear.yagl.actions.GUIAction;
import it.angrybear.yagl.contents.GUIContent;
import it.angrybear.yagl.viewers.Viewer;
import it.fulminazzo.fulmicollection.objects.FieldEquable;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * A basic implementation of {@link GUI}.
 */
class GUIImpl extends FieldEquable implements GUI {
    protected static final int MAX_SIZE = 54;

    @Getter
    protected String title;
    protected List<Contents> contents;
    protected final Set<Integer> movableSlots;
    @Getter(AccessLevel.NONE)
    protected final Map<String, String> variables = new HashMap<>();

    protected GUIAction clickOutsideAction;
    protected GUIAction openGUIAction;
    protected GUIAction closeGUIAction;
    protected BiGUIAction changeGUIAction;

    private GUIImpl() {
        this.contents = new LinkedList<>();
        this.movableSlots = new HashSet<>();
    }

    /**
     * Instantiates a new Gui.
     *
     * @param size the size
     */
    public GUIImpl(int size) {
        if (size < 0 || size > MAX_SIZE) throw new IllegalArgumentException("GUIs size must be bound between 0 and 54");
        this.contents = createContents(size, null);
        this.movableSlots = new HashSet<>();
    }

    @Override
    public void open(@NotNull Viewer viewer) {
        final Class<?> guiUtils;
        try {
            guiUtils = ReflectionUtils.getClass("it.angrybear.yagl.GUIAdapter");
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Could not find GUIAdapter class. This function requires the 'gui:bukkit' module to be added");
        }
        new Refl<>(guiUtils).invokeMethod("openGUI", this, viewer);
    }

    @Override
    public @NotNull GUI setTitle(@Nullable String title) {
        this.title = title;
        return this;
    }

    @Override
    public int size() {
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
    public @NotNull List<GUIContent> getContents(int slot) {
        Contents contents = this.contents.get(slot);
        if (contents == null) return new LinkedList<>();
        return contents.getContents();
    }

    @Override
    public @NotNull GUI addContent(GUIContent @NotNull ... contents) {
        int j = 0;
        for (int i = 0; i < contents.length; i++) {
            j = addSingle(contents[i], j);
            if (j >= size())
                throw new IllegalArgumentException(String.format("Could not set content at index %s because contents are already full", i));
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
    public @NotNull GUI setContents(int slot, GUIContent @NotNull ... contents) {
        this.contents.set(slot, new Contents(contents));
        return this;
    }

    @Override
    public @NotNull GUI unsetContent(int slot) {
        this.contents.set(slot, null);
        return this;
    }

    @Override
    public @NotNull List<GUIContent> getContents() {
        List<GUIContent> list = new LinkedList<>();
        for (Contents content : this.contents)
            if (content != null)
                list.addAll(Arrays.asList(content.contents));
        return list;
    }

    @Override
    public @NotNull GUI onClickOutside(@NotNull GUIAction action) {
        this.clickOutsideAction = action;
        return this;
    }

    @Override
    public @NotNull Optional<GUIAction> clickOutsideAction() {
        return Optional.ofNullable(this.clickOutsideAction);
    }

    @Override
    public @NotNull GUI onOpenGUI(@NotNull GUIAction action) {
        this.openGUIAction = action;
        return this;
    }

    @Override
    public @NotNull Optional<GUIAction> openGUIAction() {
        return Optional.ofNullable(this.openGUIAction);
    }

    @Override
    public @NotNull GUI onCloseGUI(@NotNull GUIAction action) {
        this.closeGUIAction = action;
        return this;
    }

    @Override
    public @NotNull Optional<GUIAction> closeGUIAction() {
        return Optional.ofNullable(this.closeGUIAction);
    }

    @Override
    public @NotNull GUI onChangeGUI(@NotNull BiGUIAction action) {
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

    /**
     * Create a new list of the given size.
     *
     * @param size     the size
     * @param copyFrom if not null, copy the contents of this list in the resulting one
     * @return the list
     */
    protected List<Contents> createContents(int size, final List<Contents> copyFrom) {
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
        private final GUIContent[] contents;

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
        public List<GUIContent> getContents() {
            return Arrays.asList(this.contents);
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Contents) {
                Contents c = (Contents) o;
                if (this.contents.length != c.contents.length) return false;
                for (int i = 0; i < this.contents.length; i++) {
                    GUIContent c1 = this.contents[i];
                    GUIContent c2 = c.contents[i];
                    if (!Objects.equals(c1, c2)) return false;
                }
                return true;
            }
            return false;
        }

        @Override
        public int hashCode() {
            int hash = Contents.class.hashCode();

            for (GUIContent content : this.contents) {
                hash *= 31;
                hash += content == null ? 191 : content.hashCode();
            }

            return hash;
        }
    }
}
