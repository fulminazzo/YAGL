package it.fulminazzo.yagl.util;

import io.netty.channel.Channel;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A collection of utilities to work with NMS.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NMSUtils {

    /**
     * Updates the {@link Player} open inventory title.
     *
     * @param player the player
     * @param title  the new title
     */
    public static void updateInventoryTitle(final @NotNull Player player,
                                            final @NotNull String title) {
        try {
            // 1.19.4+
            new Refl<>(player.getOpenInventory()).invokeMethod("setTitle", title);
        } catch (IllegalArgumentException ex) {
            // Older versions did not have setTitle,
            // so we must send our own packet.
            Object packet = newUpdateInventoryTitlePacket(player, title);
            sendPacket(player, packet);

            // Update title on internal fields
            updatePlayerInternalContainersTitle(player, title);

            // Update inventory
            player.updateInventory();
        }
    }

    /**
     * Support function for {@link #updateInventoryTitle(Player, String)}.
     * Updates all the internal containers titles of the given {@link Player},
     * to avoid inconsistencies with Bukkit API.
     *
     * @param player the player
     * @param title  the title
     */
    static void updatePlayerInternalContainersTitle(final @NotNull Player player,
                                                    final @NotNull String title) {
        Refl<?> internalContainer = new Refl<>(player.getOpenInventory())
                // Get the first field, only one expected
                .getFieldRefl(f -> true)
                // Name depends on the Minecraft version
                .getFieldRefl(f -> f.getName().equals("container") || f.getName().equals("inventory"));

        if (internalContainer.getField("title").getType().equals(String.class))
            internalContainer.setFieldObject("title", title);
        else internalContainer.setFieldObject("title", newIChatBaseComponent(title));

        try {
            // 1.14.4, 1.15.2, 1.16.5
            Refl<?> openContainer = new Refl<>(getPlayerOpenContainer(player));
            openContainer.setFieldObject("cachedTitle", title);

            openContainer
                    .getFieldRefl(f -> f.getName().equals("delegate"))
                    .getFieldRefl(f -> f.getName().equals("container"))
                    .setFieldObject("title", title);
        } catch (IllegalArgumentException | IllegalStateException ignored) {
        }
    }

    /**
     * Gets the container type from the given container.
     * Solves a problem found in Minecraft 1.14, where getting the direct container
     * of a modified size chest inventory, will wrongfully return <code>GENERIC_9X3</code>.
     *
     * @param container the container
     * @return the container type
     * @since Minecraft 1.14
     */
    static @NotNull Object getContainerType(final @NotNull Object container) {
        Refl<?> containerRefl = new Refl<>(container);

        InventoryView view = containerRefl.getFieldObject(InventoryView.class);
        Inventory openInventory = view.getTopInventory();

        Object type = containerRefl.getFieldObject(f -> f.getType().getSimpleName().equals("Containers"));
        // If inventory is chest, manually get type from size to avoid conflicts
        if (openInventory.getType().equals(InventoryType.CHEST)) {
            Class<?> containersClass = type.getClass();
            Refl<?> containers = new Refl<>(containersClass);
            int size = openInventory.getSize() / 9;
            try {
                type = containers.getFieldObject("GENERIC_9X" + size);
            } catch (IllegalArgumentException e) {
                // Not available with deobfuscated name, since 1.17
            }
        }
        return type;
    }

    /**
     * Gets the {@link Player} open inventory in the form of an NMS container.
     *
     * @param player the player
     * @return the open container
     */
    public static @NotNull Object getPlayerOpenContainer(final @NotNull Player player) {
        Refl<?> entityPlayer = getHandle(player);
        @NotNull List<Field> containers = entityPlayer.getFields(f ->
                f.getType().getSimpleName().equals("Container") ||
                        // 1.20+
                        f.getType().getSimpleName().equals("AbstractContainerMenu")
        );
        // First container is for player inventory in older versions
        Field containerField = containers.get(Math.min(2, containers.size()) - 1);
        return entityPlayer.getFieldObject(containerField);
    }

    /*
     * INITIALIZERS
     */

    /**
     * Creates a new packet (<i>PlayOutOpenWindow</i>) to update the current
     * {@link Player#getOpenInventory()} title.
     *
     * @param player the player
     * @param title  the title
     * @return the packet
     */
    public static @NotNull Object newUpdateInventoryTitlePacket(final @NotNull Player player,
                                                                final @NotNull String title) {
        final Inventory openInventory = player.getOpenInventory().getTopInventory();

        Refl<?> container = new Refl<>(getPlayerOpenContainer(player));

        int id;
        try {
            // 1.8.8, 1.9.2, 1.10.2, 1.11.2, 1.12.2, 1.13.2, 1.14.4, 1.15.2, 1.16.5
            id = container.getFieldObject("windowId");
        } catch (IllegalArgumentException e) {
            // 1.17.1, 1.18.2
            // Second int of Container class
            @NotNull List<Field> ids = container.getFields(f ->
                    f.getType().equals(int.class) &&
                            f.getDeclaringClass().getSimpleName().equals("Container")
            );
            id = container.getFieldObject(ids.get(1));
        }

        return newOpenWindowPacket(container.getObject(), id, title, openInventory);
    }

    /**
     * Creates a new open window packet.
     *
     * @param container the container to open
     * @param id        the id to assign the container
     * @param title     the title of the inventory
     * @param inventory the corresponding {@link Bukkit} inventory. Only required in legacy versions
     * @return the object
     */
    public static @NotNull Object newOpenWindowPacket(final @NotNull Object container,
                                                      final int id,
                                                      final @NotNull String title,
                                                      final Inventory inventory) {
        final Class<?> packetPlayOutOpenWindowClass = getPacketPlayOutOpenWindowClass();

        Object chatComponentTitle = newIChatBaseComponent(title);

        Refl<?> packet;
        try {
            // 1.14.4, 1.15.2, 1.16.5, 1.17.1, 1.18.2
            Object containerType = getContainerType(container);
            packet = new Refl<>(packetPlayOutOpenWindowClass, id, containerType, chatComponentTitle);
        } catch (IllegalArgumentException e) {
            // 1.8.8, 1.9.2, 1.10.2, 1.11.2, 1.12.2, 1.13.2
            String inventoryType = inventoryTypeToNotchInventoryTypeString(inventory.getType());
            packet = new Refl<>(packetPlayOutOpenWindowClass, id, inventoryType, chatComponentTitle, inventory.getSize());
        }

        return packet.getObject();
    }

    /**
     * Gets an NMS chat component from the given string.
     *
     * @param message the message
     * @return the chat base component
     */
    public static @NotNull Object newIChatBaseComponent(final @NotNull String message) {
        Class<?> iChatBaseComponent = getCraftBukkitClass("util.CraftChatMessage");
        Object[] components = new Refl<>(iChatBaseComponent).invokeMethod("fromString", message);
        return components[0];
    }

    /*
     * CONVERTERS
     */

    /**
     * Converts the given {@link ItemStack} to its corresponding NMS type.
     *
     * @param itemStack the item stack
     * @return the NMS item stack
     */
    public static @NotNull Object itemStackToNMS(final @NotNull ItemStack itemStack) {
        Class<?> craftItemStack = getCraftBukkitClass("inventory.CraftItemStack");
        return new Refl<>(craftItemStack).invokeMethod("asNMSCopy", itemStack);
    }

    /**
     * Gets the associated Minecraft inventory type from the {@link InventoryType}.
     * Used in obsolete Minecraft versions like 1.8.
     *
     * @param inventoryType the inventory type
     * @return the minecraft inventory type
     */
    static @NotNull String inventoryTypeToNotchInventoryTypeString(final @NotNull InventoryType inventoryType) {
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

    /*
     * ENTITY PLAYER
     */

    /**
     * Sends the given packet to the specified player.
     *
     * @param player the player
     * @param packet the packet
     */
    public static void sendPacket(final @NotNull Player player,
                                  final @NotNull Object packet) {
        Refl<?> playerConnection = getPlayerConnection(getHandle(player));
        playerConnection.invokeMethod(Void.TYPE,
                new Class[]{getPacketClass()},
                packet);
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

    /*
     * UTILITIES
     */

    /**
     * Gets the corresponding NMS class from the given path.
     * <br>
     * Works only until <b>Minecraft 1.16.5</b>.
     *
     * @param path the class path (without the NMS leading path)
     * @return the class
     */
    public static @NotNull Class<?> getLegacyNMSClass(final @NotNull String path) {
        final String className = "net.minecraft.server." + getNMSVersion() + "." + path;
        return ReflectionUtils.getClass(className);
    }

    /**
     * Gets the corresponding CraftBukkit class from the given path.
     * <br>
     * The class is looked up using the following logic:
     * <ul>
     *     <li>if the server is on 1.20, the class <code>org.bukkit.craftbukkit.&lt;path&gt;</code> is searched;</li>
     *     <li>otherwise, the class <code>org.bukkit.craftbukkit.{@link #getNMSVersion()}.&lt;path&gt;</code> is used.</li>
     * </ul>
     *
     * @param path the class path (without the CraftBukkit leading path)
     * @return the class
     */
    public static @NotNull Class<?> getCraftBukkitClass(final @NotNull String path) {
        String className;
        try {
            className = "org.bukkit.craftbukkit." + getNMSVersion() + "." + path;
        } catch (IllegalStateException e) {
            // 1.20+
            className = "org.bukkit.craftbukkit." + path;
        }
        return ReflectionUtils.getClass(className);
    }

    /*
     * CLASSES
     */

    private static @NotNull Class<?> getPacketClass() {
        try {
            // 1.17.1+
            final String packetName = "net.minecraft.network.protocol.Packet";
            return ReflectionUtils.getClass(packetName);
        } catch (IllegalArgumentException e) {
            // 1.8.8, 1.9.2, 1.10.2, 1.11.2, 1.12.2, 1.13.2, 1.14.4, 1.15.2, 1.16.5
            return getLegacyNMSClass("Packet");
        }
    }

    private static @NotNull Class<?> getPacketPlayOutOpenWindowClass() {
        try {
            // 1.17.1+
            final String packetPlayOutOpenWindowName = "net.minecraft.network.protocol.game.PacketPlayOutOpenWindow";
            return ReflectionUtils.getClass(packetPlayOutOpenWindowName);
        } catch (IllegalArgumentException e) {
            // 1.8.8, 1.9.2, 1.10.2, 1.11.2, 1.12.2, 1.13.2, 1.14.4, 1.15.2, 1.16.5
            return getLegacyNMSClass("PacketPlayOutOpenWindow");
        }
    }

    /*
     * VERSIONS
     */

    /**
     * Returns the current server version in a double format.
     * If the server version is "1.X.Y", the number returned is "X.Y".
     *
     * @return the version
     */
    public static double getServerVersion() {
        Pattern pattern = Pattern.compile("[0-9]+\\.([0-9]+)(?:\\.([0-9]+))?-R[0-9]+\\.[0-9]+-SNAPSHOT");
        String version = Bukkit.getBukkitVersion();
        Matcher matcher = pattern.matcher(version);
        if (matcher.matches()) {
            String first = matcher.group(1);
            String second = matcher.group(2);
            if (second == null) second = "0";
            return Double.parseDouble(first + "." + second);
        } else throw new IllegalStateException("Could not find numeric version from server version: " + version);
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

}
