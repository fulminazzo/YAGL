package it.fulminazzo.yagl.utils;

import io.netty.channel.Channel;
import it.fulminazzo.fulmicollection.objects.Refl;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A collection of utilities to work with NMS.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NMSUtils {

    /**
     * Gets the {@link Channel} associated with the player connection.
     *
     * @param player the player
     * @return the player channel
     */
    public static @NotNull Channel getPlayerChannel(final @NotNull Player player) {
        @NotNull Refl<?> handle = getHandle(player);
        @NotNull Refl<?> connection = getPlayerConnection(handle);
        @NotNull Refl<?> networkManager = getNetworkManager(connection);
        return networkManager.getFieldObject(Channel.class);
    }

    /**
     * Gets the handle of the player.
     *
     * @param player the player
     * @return the handle
     */
    public static @NotNull Refl<?> getHandle(final @NotNull Player player) {
        return Objects.requireNonNull(new Refl<>(player).invokeMethodRefl("getHandle"), "Could not find player handle");
    }

    /**
     * Gets the player connection.
     *
     * @param handle the handle
     * @return the player connection
     */
    public static @NotNull Refl<?> getPlayerConnection(final @NotNull Refl<?> handle) {
        try {
            return handle.getFieldRefl(f -> f.getType().getSimpleName().equals("PlayerConnection"));
        } catch (IllegalArgumentException e) {
            // 1.20+
            return handle.getFieldRefl(f -> f.getType().getSimpleName().equals("ServerGamePacketListenerImpl"));
        }
    }

    /**
     * Gets the network manager.
     *
     * @param playerConnection the player connection
     * @return the network manager
     */
    public static @NotNull Refl<?> getNetworkManager(final @NotNull Refl<?> playerConnection) {
        try {
            return playerConnection.getFieldRefl(f -> f.getType().getSimpleName().equals("NetworkManager"));
        } catch (IllegalArgumentException e) {
            // 1.20+
            return playerConnection.getFieldRefl(f -> f.getType().getSimpleName().equals("Connection"));
        }
    }

}
