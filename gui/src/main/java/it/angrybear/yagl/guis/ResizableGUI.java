package it.angrybear.yagl.guis;


import it.angrybear.yagl.contents.GUIContent;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a "chest" GUI that can be resized.
 * This GUI can start with any value in bounds and multiple of 9 and can grow if necessary.
 */
public class ResizableGUI extends GUIImpl {

    /**
     * Instantiates a new Gui.
     *
     * @param size the size
     */
    public ResizableGUI(int size) {
        super(size);
        checkSize(size);
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
            if (getSize() < MAX_SIZE) {
                resize(getSize() + 9);
                this.contents.set(j, content);
            } else throw new IllegalArgumentException(String.format("Could not set content at index %s because contents are already full", i));
        }
        return this;
    }

    @Override
    public @NotNull GUI setContent(int slot, @NotNull GUIContent content) {
        if (slot >= getSize() && slot < MAX_SIZE) resize((slot / 9 + 1) * 9);
        return super.setContent(slot, content);
    }

    /**
     * Resizes the current GUI to the new size.
     * Must be a multiple of 9 not higher than {@link #MAX_SIZE}.
     *
     * @param size the size
     */
    public void resize(int size) {
        checkSize(size);
        this.contents = createList(size, this.contents);
    }

    private void checkSize(int size) {
        if (size < 0 || size > MAX_SIZE) throw new IllegalArgumentException("GUIs size must be bound between 0 and 54!");
        if (size % 9 != 0)
            throw new IllegalArgumentException(String.format("%s is not a valid size. Only multiple of 9 can be accepted", size));
    }
}
