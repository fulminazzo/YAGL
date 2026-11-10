package it.fulminazzo.yagl.event;

import it.fulminazzo.yagl.content.GUIContent;
import it.fulminazzo.yagl.gui.GUI;
import it.fulminazzo.yagl.viewer.Viewer;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * A collection of data used to identify the event that happens
 * when a viewer clicks on a content in an open GUI.
 */
@Getter
@RequiredArgsConstructor
@Builder
public final class ClickItemEvent {
    private final @NotNull Viewer viewer;
    private final @NotNull GUI gui;
    private final @NotNull GUIContent content;
    private final @NotNull ClickType clickType;
    private final @Nullable @Range(from = 0, to = 8) Integer hotbarKey;

}
