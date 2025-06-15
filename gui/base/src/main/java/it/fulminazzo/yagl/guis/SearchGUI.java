package it.fulminazzo.yagl.guis;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

/**
 * A special implementation of {@link DataGUI} that supports dynamic
 * searching of data from a string.
 */
public final class SearchGUI {
    @Getter
    @Setter
    private @Nullable String query;

}
