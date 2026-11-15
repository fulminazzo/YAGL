package it.fulminazzo.yagl.item;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

/**
 * A simple implementation of {@link AbstractItem} used for testing purposes.
 */
@SuppressWarnings("deprecation")
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public final class Item implements AbstractItem {
    private final @NotNull String name;

}
