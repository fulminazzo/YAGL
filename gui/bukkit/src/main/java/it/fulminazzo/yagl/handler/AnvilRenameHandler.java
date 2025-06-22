package it.fulminazzo.yagl.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yagl.GUIAdapter;
import it.fulminazzo.yagl.scheduler.Scheduler;
import it.fulminazzo.yagl.scheduler.Task;
import it.fulminazzo.yagl.util.NMSUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

/**
 * A special {@link ChannelDuplexHandler} capable of intercepting and
 * reading anvil rename packets from the player.
 */
public final class AnvilRenameHandler extends ChannelDuplexHandler {
    private static final int DEBOUNCE_DELAY = 2;

    private final @NotNull Logger logger;

    private final @NotNull UUID playerId;

    private final @NotNull BiConsumer<Player, String> handler;

    private @Nullable Task handleTask;

    /**
     * Instantiates a new Anvil rename handler.
     *
     * @param playerId the player id
     * @param handler  the action to execute upon successful reading
     */
    public AnvilRenameHandler(final @NotNull UUID playerId,
                              final @NotNull BiConsumer<Player, String> handler) {
        this.logger = GUIAdapter.getProvidingPlugin().getLogger();
        this.playerId = playerId;
        this.handler = handler;
    }

    @Override
    public void channelRead(final @NotNull ChannelHandlerContext context,
                            final @NotNull Object packet) throws Exception {
        Player player = getPlayer();
        try {
            String packetName = packet.getClass().getSimpleName();
            Refl<?> packetRefl = new Refl<>(packet);
            final String name;

            switch (packetName) {
                // 1.8.8, 1.9.4, 1.10.2, 1.11.2, 1.12.2
                case "PacketPlayInCustomPayload":
                    String type = packetRefl.getFieldObject(String.class);
                    if ("MC|ItemName".equals(type)) {
                        Refl<?> refl = packetRefl.getFieldRefl("b");
                        ByteBuf buf = refl.getFieldObject(ByteBuf.class);
                        ByteBuf copy = buf.copy();

                        // First read the size of the buffer
                        int size = copy.readByte();

                        byte[] data = new byte[size];
                        copy.readBytes(data);
                        name = new String(data);
                    } else return; // Other packet
                    break;
                // 1.13.2, 1.14.4, 1.15.2, 1.16.5, 1.17.1, 1.18.2, 1.19.4
                case "PacketPlayInItemName":
                    name = packetRefl.getFieldObject(String.class);
                    break;
                // 1.20.6, 1.21.4, ...
                case "ServerboundRenameItemPacket":
                    name = packetRefl.getFieldObject(String.class);
                    break;
                // Other packet
                default:
                    return;
            }

            stopHandleTask();

            this.handleTask = Scheduler.getScheduler().runLaterAsync(
                    GUIAdapter.getProvidingPlugin(),
                    () -> this.handler.accept(player, name),
                    DEBOUNCE_DELAY
            );
        } catch (Exception e) {
            // Usually catching Exception is bad, but in this case is necessary to avoid the player getting kicked
            this.logger.severe(String.format("An error occurred while reading a packet from player '%s': %s",
                    player.getName(),
                    e.getMessage()));
            e.printStackTrace();
        } finally {
            super.channelRead(context, packet);
        }
    }

    /**
     * Stops the {@link #handleTask} if present.
     */
    void stopHandleTask() {
        if (this.handleTask != null) {
            this.handleTask.cancel();
            this.handleTask = null;
        }
    }

    /**
     * Inserts the current handler in the player's channel.
     */
    public void inject() {
        Channel channel = NMSUtils.getPlayerChannel(getPlayer());
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addBefore("packet_handler", getName(), this);
    }

    /**
     * Removes the current handler from the player's channel.
     */
    public void remove() {
        Channel channel = NMSUtils.getPlayerChannel(getPlayer());
        channel.eventLoop().submit(() -> channel.pipeline().remove(getName()));
        stopHandleTask();
    }

    /**
     * Gets name of the current handler.
     *
     * @return the name
     */
    public @NotNull String getName() {
        return String.format("%s-%s",
                getClass().getSimpleName(),
                this.playerId.toString().replace("-", "_")
        );
    }

    /**
     * Checks if the current handler belongs to the specified player.
     *
     * @param player the player
     * @return true if the {@link #playerId} matches
     */
    public boolean belongsTo(final @NotNull Player player) {
        return player.getUniqueId().equals(this.playerId);
    }

    /**
     * Gets the player.
     *
     * @return the player
     */
    @NotNull Player getPlayer() {
        Player player = Bukkit.getPlayer(this.playerId);
        if (player == null)
            throw new IllegalStateException(String.format("Player '%s' is not online", this.playerId));
        return player;
    }

}
