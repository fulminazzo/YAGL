package it.angrybear.yagl.contents;

import it.angrybear.yagl.viewers.Viewer;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

@Getter
abstract class GUIContentImpl implements GUIContent {
    protected int priority = 0;
    protected String clickSound;
    @Getter(AccessLevel.NONE)
    protected Predicate<? super Viewer> requirements;

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
    public @NotNull GUIContent setViewRequirements(@NotNull Predicate<? super Viewer> requirements) {
        this.requirements = requirements;
        return this;
    }

    @Override
    public boolean hasViewRequirements(@NotNull Viewer viewer) {
        return this.requirements == null || this.requirements.test(viewer);
    }
}
