package it.fulminazzo.yagl.content;

import it.fulminazzo.yagl.action.GUIItemAction;
import it.fulminazzo.yagl.content.requirement.RequirementChecker;
import it.fulminazzo.yagl.metadatable.Metadatable;
import it.fulminazzo.yagl.wrapper.Sound;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a general {@link GUIContent} with common methods already implemented.
 * Allows creation of custom contents with ease.
 *
 * @param <C> the type of this content (for method chaining)
 */
@SuppressWarnings("unchecked")
public abstract class CustomGUIContent<C extends CustomGUIContent<C>> extends GUIContentImpl {

    @Override
    public @NotNull C setPriority(int priority) {
        return (C) super.setPriority(priority);
    }

    @Override
    public @NotNull C setClickSound(Sound sound) {
        return (C) super.setClickSound(sound);
    }

    @Override
    public @NotNull C setViewRequirements(@NotNull String permission) {
        return (C) super.setViewRequirements(permission);
    }

    @Override
    public @NotNull C setViewRequirements(@NotNull RequirementChecker requirements) {
        return (C) super.setViewRequirements(requirements);
    }

    @Override
    public @NotNull C onClickItemSend(@NotNull String message) {
        return (C) super.onClickItemSend(message);
    }

    @Override
    public @NotNull C onClickItem(@NotNull String command) {
        return (C) super.onClickItem(command);
    }

    @Override
    public @NotNull C onClickItemClose() {
        return (C) super.onClickItemClose();
    }

    @Override
    public @NotNull C onClickItem(@NotNull GUIItemAction action) {
        return (C) super.onClickItem(action);
    }

    @Override
    public @NotNull C copyAll(@NotNull Metadatable other, boolean replace) {
        return (C) super.copyAll(other, replace);
    }

    @Override
    public @NotNull C copyFrom(@NotNull Metadatable other, boolean replace) {
        return (C) super.copyFrom(other, replace);
    }

    @Override
    public @NotNull C setVariable(@NotNull String name, @NotNull String value) {
        return (C) super.setVariable(name, value);
    }

    @Override
    public @NotNull C unsetVariable(@NotNull String name) {
        return (C) super.unsetVariable(name);
    }

}
