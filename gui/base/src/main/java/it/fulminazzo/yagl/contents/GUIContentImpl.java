package it.fulminazzo.yagl.contents;

import it.fulminazzo.yagl.actions.GUIItemAction;
import it.fulminazzo.yagl.actions.GUIItemCommand;
import it.fulminazzo.yagl.contents.requirements.PermissionRequirement;
import it.fulminazzo.yagl.contents.requirements.RequirementChecker;
import it.fulminazzo.yagl.items.Item;
import it.fulminazzo.yagl.viewers.Viewer;
import it.fulminazzo.yagl.wrappers.Sound;
import it.fulminazzo.fulmicollection.objects.FieldEquable;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A basic implementation for {@link GUIContent}.
 */
abstract class GUIContentImpl extends FieldEquable implements GUIContent {
    @Getter
    protected int priority = 0;
    @Getter
    protected Sound clickSound;
    protected RequirementChecker requirements;
    protected GUIItemAction clickAction;
    protected final Map<String, String> variables = new HashMap<>();

    @Override
    public @NotNull Item render() {
        return apply(internalRender().copy());
    }

    /**
     * Executes the real {@link #render()} function.
     *
     * @return the item
     */
    protected abstract @NotNull Item internalRender();

    @Override
    public @NotNull GUIContent copy() {
        GUIContentImpl copy = internalCopy();
        copy.setPriority(this.priority);
        copy.requirements = this.requirements instanceof PermissionRequirement ?
                new PermissionRequirement(this.requirements.serialize()) :
                this.requirements;
        copy.clickAction = this.clickAction instanceof GUIItemCommand ?
                new GUIItemCommand(this.clickAction.serialize()) :
                this.clickAction;
        if (this.clickSound != null)
            copy.clickSound = new Sound(
                    this.clickSound.getName(), this.clickSound.getVolume(),
                    this.clickSound.getPitch(), this.clickSound.getCategory()
            );
        copy.variables.putAll(this.variables);
        return copy;
    }

    /**
     * Initializes a new {@link GUIContent} of the current type.
     *
     * @return the gui content
     */
    protected abstract @NotNull GUIContentImpl internalCopy();

    @Override
    public @NotNull GUIContent setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    @Override
    public @NotNull GUIContent setClickSound(Sound sound) {
        this.clickSound = sound;
        return this;
    }

    @Override
    public @NotNull GUIContent setViewRequirements(@NotNull RequirementChecker requirements) {
        this.requirements = requirements;
        return this;
    }

    @Override
    public boolean hasViewRequirements(@NotNull Viewer viewer) {
        return this.requirements == null || this.requirements.test(viewer);
    }

    @Override
    public @NotNull GUIContent onClickItem(@NotNull GUIItemAction action) {
        this.clickAction = action;
        return this;
    }

    @Override
    public @NotNull Map<String, String> variables() {
        return this.variables;
    }

    @Override
    public @NotNull Optional<GUIItemAction> clickItemAction() {
        return Optional.of((v, g, c) -> {
            if (this.clickSound != null) v.playSound(this.clickSound);
            if (this.clickAction != null) this.clickAction.execute(v, g, c);
        });
    }
}
