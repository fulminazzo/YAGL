package it.angrybear.yagl.contents;

import it.angrybear.yagl.actions.GUIItemAction;
import it.angrybear.yagl.contents.requirements.RequirementChecker;
import it.angrybear.yagl.viewers.Viewer;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

/**
 * A basic implementation for {@link GUIContent}.
 */
@Getter
abstract class GUIContentImpl implements GUIContent {
    protected int priority = 0;
    protected String clickSound;
    @Getter(AccessLevel.NONE)
    protected RequirementChecker requirements;
    protected GUIItemAction clickAction;
    @Getter(AccessLevel.NONE)
    protected Map<String, String> variables;

    @Override
    public @NotNull GUIContent setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    @Override
    public @NotNull GUIContent setClickSound(String rawSound) {
        this.clickSound = rawSound;
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
        return Optional.ofNullable(this.clickAction);
    }
}
