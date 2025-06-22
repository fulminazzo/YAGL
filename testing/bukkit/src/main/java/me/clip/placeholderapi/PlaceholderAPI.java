package me.clip.placeholderapi;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Mock representation of the respective PlaceholderAPI class.
 */
public final class PlaceholderAPI {

    public static @NotNull String setPlaceholders(final Player player, @NotNull String text) {
        return text
                .replace("%player_name%", player.getName());
    }

}
