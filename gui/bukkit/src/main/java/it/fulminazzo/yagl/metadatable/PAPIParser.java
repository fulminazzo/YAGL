package it.fulminazzo.yagl.metadatable;

import me.clip.placeholderapi.PlaceholderAPI;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

/**
 * A special implementation of {@link MetadatableHelper}
 * that allows to apply {@link PlaceholderAPI}
 * variables to the specified object.
 */
public final class PAPIParser extends MetadatableHelper {

    /**
     * Instantiates a new PAPIParser helper.
     */
    PAPIParser() {
        super(Collections::emptyMap);
    }

    @Override
    String apply(@NotNull String string) {
        return PlaceholderAPI.setPlaceholders(null, string);
    }

    /**
     * Parses the given object strings using PlaceholderAPI.
     *
     * @param <T>    the type of the object
     * @param object the object
     * @return the parsed object
     */
    public static <T> T parse(final @NotNull T object) {
        return new PAPIParser().apply(object);
    }

}
