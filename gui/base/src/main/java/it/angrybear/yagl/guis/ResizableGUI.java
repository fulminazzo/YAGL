package it.angrybear.yagl.guis;


import it.angrybear.yagl.contents.GUIContent;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a "chest" GUI that can be resized.
 * This GUI can start with any value in bounds and multiple of 9 and can grow if necessary.
 */
public class ResizableGUI extends GUIImpl {

    private ResizableGUI() {
        this(0);
    }

    /**
     * Instantiates a new Gui.
     *
     * @param size the size
     */
    ResizableGUI(int size) {
        super(size);
        checkSize(size);
    }

    @Override
    public @NotNull GUI setContents(int slot, GUIContent @NotNull ... contents) {
        if (slot >= size() && slot < MAX_SIZE) resize((slot / 9 + 1) * 9);
        return super.setContents(slot, contents);
    }

    @Override
    public @NotNull GUI addContent(GUIContent @NotNull ... contents) {
        int j = 0;
        for (int i = 0; i < contents.length; i++) {
            GUIContent content = contents[i];
            j = addSingle(content, j);
            if (j < size()) continue;
            if (size() < MAX_SIZE) {
                resize(size() + 9);
                this.contents.set(j, new Contents(content));
            } else throw new IllegalArgumentException(String.format("Could not set content at index %s because contents are already full", i));
        }
        return this;
    }

    /**
     * Resizes the current GUI to the new size.
     * Must be a multiple of 9 not higher than {@link #MAX_SIZE}.
     *
     * @param size the size
     */
    public void resize(int size) {
        checkSize(size);
        this.contents = createContents(size, this.contents);
    }

    private void checkSize(int size) {
        if (size < 0 || size > MAX_SIZE) throw new IllegalArgumentException("GUIs size must be bound between 0 and 54!");
        if (size % 9 != 0)
            throw new IllegalArgumentException(String.format("%s is not a valid size. Only multiple of 9 can be accepted", size));
    }
}
