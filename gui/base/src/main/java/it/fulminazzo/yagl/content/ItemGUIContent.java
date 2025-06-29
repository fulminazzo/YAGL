package it.fulminazzo.yagl.content;

import it.fulminazzo.yagl.action.command.GUIItemCommand;
import it.fulminazzo.yagl.content.requirement.PermissionRequirement;
import it.fulminazzo.yagl.item.Item;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link GUIContent} that contains a single {@link Item}.
 * It also extends the interface to easily modify it without accessing it directly.
 */
public class ItemGUIContent extends CustomItemGUIContent<ItemGUIContent> implements GUIContent, Item {

    private ItemGUIContent() {
        super();
    }

    private ItemGUIContent(final @NotNull String material) {
        super(material);
    }

    private ItemGUIContent(final @NotNull Item item) {
        super(item);
    }

    @Override
    public @NotNull Item internalRender() {
        return this.item.copy();
    }

    @Override
    public @NotNull ItemGUIContent internalCopy() {
        ItemGUIContent copy = ItemGUIContent.newInstance(this.item.copy()).copyFrom(this, true);
        copy.requirements = this.requirements instanceof PermissionRequirement ?
                new PermissionRequirement(this.requirements.serialize()) :
                this.requirements;
        copy.clickAction = this.clickAction instanceof GUIItemCommand ?
                new GUIItemCommand(this.clickAction.serialize()) :
                this.clickAction;
        return copy;
    }

    /**
     * Creates an instance of {@link ItemGUIContent}.
     *
     * @return the item gui content
     */
    public static ItemGUIContent newInstance() {
        return new ItemGUIContent();
    }

    /**
     * Creates an instance of {@link ItemGUIContent} with the given material.
     *
     * @param material the material
     * @return the item gui content
     */
    public static ItemGUIContent newInstance(final @NotNull String material) {
        return new ItemGUIContent(material);
    }

    /**
     * Creates an instance of {@link ItemGUIContent} with the given item.
     *
     * @param item the item
     * @return the item gui content
     */
    public static ItemGUIContent newInstance(final @NotNull Item item) {
        return new ItemGUIContent(item);
    }

}
