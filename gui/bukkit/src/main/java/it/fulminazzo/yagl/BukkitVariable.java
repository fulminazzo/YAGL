package it.fulminazzo.yagl;

import lombok.Getter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Represents a variable. Uses the Bukkit class {@link Player} to retrieve the correct value.
 */
final class BukkitVariable {
    public static final BukkitVariable[] DEFAULT_VARIABLES = new BukkitVariable[]{
            new BukkitVariable("player_name", HumanEntity::getName),
            new BukkitVariable("player_display_name", Player::getDisplayName),
            new BukkitVariable("player_uuid", p -> p.getUniqueId().toString()),
            new BukkitVariable("players_count", p -> String.valueOf(p.getServer().getOnlinePlayers().size())),
    };
    @Getter
    private final @NotNull String name;
    private final @NotNull Function<Player, String> value;

    /**
     * Instantiates a new Bukkit variable.
     *
     * @param name  the name
     * @param valueFunction the value function
     */
    public BukkitVariable(final @NotNull String name,
                          final @NotNull Function<Player, String> valueFunction) {
        this.name = name;
        this.value = valueFunction;
    }

    /**
     * Gets the proper value from the function
     *
     * @param player the player
     * @return the value
     */
    public @NotNull String getValue(final @NotNull Player player) {
        return this.value.apply(player);
    }

}
