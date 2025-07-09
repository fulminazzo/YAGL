package it.fulminazzo.yagl.metadatable;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;

/**
 * A special implementation of {@link MetadatableHelper}
 * that allows to apply {@link PlaceholderAPI}
 * variables to the specified object.
 */
public final class PAPIParser extends MetadatableHelper {
    private final @NotNull Player player;

    /**
     * Instantiates a new PAPIParser helper.
     *
     * @param player the player
     */
    PAPIParser(final @NotNull Player player) {
        super(Collections::emptyMap);
        this.player = player;
    }

    @Override
    @NotNull
    String apply(@NotNull String string) {
        return PlaceholderAPI.setPlaceholders(this.player, string);
    }

    /**
     * Parses the given object strings using {@link PlaceholderAPI}.
     *
     * @param <T>    the type of the object
     * @param player the target player of the placeholders
     * @param object the object
     * @return the parsed object
     */
    public static <T> @Nullable T parse(final @NotNull Player player,
                                        final @NotNull T object) {
        return new PAPIParser(player).apply(object);
    }

}
