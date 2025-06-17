package it.fulminazzo.yagl.utils;

import io.netty.channel.Channel;
import it.fulminazzo.fulmicollection.objects.Refl;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A collection of utilities to work with NMS.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NMSUtils {

    /**
     * Gets the associated Minecraft inventory type from the {@link InventoryType}.
     * Used in obsolete Minecraft versions like 1.8.
     *
     * @param inventoryType the inventory type
     * @return the minecraft inventory type
     */
    static @NotNull String getInventoryTypeStringFromBukkitType(final @NotNull InventoryType inventoryType) {
        switch (inventoryType) {
            case CHEST:
            case ENDER_CHEST:
                return "minecraft:chest";
            case FURNACE:
                return "minecraft:furnace";
            case WORKBENCH:
                return "minecraft:crafting_table";
            case ANVIL:
                return "minecraft:anvil";
            case BREWING:
                return "minecraft:brewing_stand";
            case DISPENSER:
            case DROPPER:
                return "minecraft:dropper";
            case HOPPER:
                return "minecraft:hopper";
            case BEACON:
                return "minecraft:beacon";
            case ENCHANTING:
                return "minecraft:enchanting_table";
            case MERCHANT:
                return "minecraft:villager";
            case SHULKER_BOX:
                return "minecraft:shulker_box";
        }
        throw new IllegalArgumentException("Could not find associated legacy inventory type from Bukkit type: " + inventoryType);
    }

    /**
     * Gets the NMS version of the current version.
     * <br>
     * <b>WARNING</b>: not supported in versions higher than 1.20.
     *
     * @return the version
     */
    public static @NotNull String getNMSVersion() {
        Class<? extends Server> serverClass = Bukkit.getServer().getClass();
        String version = serverClass.getPackage().getName();
        version = version.substring(version.lastIndexOf('.') + 1);
        if (version.equals("craftbukkit"))
            throw new IllegalStateException("Could not find the NMS version from the current server class: " + serverClass.getSimpleName() + ". " +
                    "Are you on a version higher than 1.20?");
        return version;
    }

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
