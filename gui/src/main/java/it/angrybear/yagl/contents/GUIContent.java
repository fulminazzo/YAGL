package it.angrybear.yagl.contents;

import it.angrybear.yagl.items.Item;
import it.angrybear.yagl.viewers.Viewer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

/**
 * Represents the generic content of a GUI.
 */
public interface GUIContent {

    /**
     * Renders the current content in an Item.
     * This will NOT check for {@link #hasViewRequirements(Viewer)} or {@link #getPriority()}.
     *
     * @return the item
     */
    @NotNull Item render();

    /**
     * Sets priority.
     * If two {@link GUIContent} share the same slot,
     * only the one with higher priority will be shown.
     *
     * @param priority the priority
     * @return this content
     */
    @NotNull GUIContent setPriority(final int priority);

    /**
     * Gets priority.
     *
     * @return the priority
     */
    int getPriority();

    /**
     * Sets the sound played upon clicking on the content in the GUI.
     * This is NOT checked until the sound is actually played.
     *
     * @param rawSound the raw sound
     * @return this content
     */
    @NotNull GUIContent setClickSound(final String rawSound);

    /**
     * Gets the sound played upon clicking on this content in the GUI.
     *
     * @return the sound, if set
     */
    @Nullable String getClickSound();

    /**
     * Sets the given predicate as a checker for {@link #hasViewRequirements(Viewer)}.
     *
     * @param requirements the requirements
     * @return this content
     */
    @NotNull GUIContent setViewRequirements(final @NotNull Predicate<? super Viewer> requirements);

    /**
     * Check if the given {@link Viewer} has enough requirements to view this content.
     *
     * @param viewer the viewer
     * @return the boolean
     */
    boolean hasViewRequirements(final @NotNull Viewer viewer);

    /**
     * Copy this content to another one.
     *
     * @return the copy
     */
    GUIContent copy();
}
