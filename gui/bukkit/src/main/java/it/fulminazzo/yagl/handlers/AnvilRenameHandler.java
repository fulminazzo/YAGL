package it.fulminazzo.yagl.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yagl.utils.NMSUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.Charset;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

/**
 * A special {@link ChannelDuplexHandler} capable of intercepting and
 * reading anvil rename packets from the player.
 */
public final class AnvilRenameHandler extends ChannelDuplexHandler {
    private final @NotNull Logger logger;

    private final @NotNull Player player;

    private final @NotNull BiConsumer<Player, String> handler;

    /**
     * Instantiates a new Anvil rename handler.
     *
     * @param logger  the logger
     * @param player  the player
     * @param handler the action to execute upon successful reading
     */
    public AnvilRenameHandler(final @NotNull Logger logger,
                              final @NotNull Player player,
                              final @NotNull BiConsumer<Player, String> handler) {
        this.logger = logger;
        this.player = player;
        this.handler = handler;
    }

    @Override
    public void channelRead(final @NotNull ChannelHandlerContext context,
                            final @NotNull Object packet) throws Exception {
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
                        copy.readByte();

                        name = copy.toString(Charset.defaultCharset());
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

            this.handler.accept(this.player, name);
        } catch (Exception e) {
            // Usually catching Exception is bad,
            // but in this case is necessary
            // to avoid the player getting kicked
            logger.severe(String.format("An error occurred while reading a packet from player %s: %s", this.player.getName(), e.getMessage()));
            e.printStackTrace();
        } finally {
            super.channelRead(context, packet);
        }
    }

    /**
     * Inserts the current handler in the player's channel.
     */
    public void inject() {
        Channel channel = NMSUtils.getPlayerChannel(this.player);
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addBefore("packet_handler", getName(), this);
    }

    /**
     * Removes the current handler from the player's channel.
     */
    public void remove() {
        Channel channel = NMSUtils.getPlayerChannel(this.player);
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(getName());
            return null;
        });
    }

    /**
     * Gets name of the current handler.
     *
     * @return the name
     */
    public @NotNull String getName() {
        return String.format("%s-%s",
                getClass().getSimpleName(),
                this.player.getUniqueId().toString().replace("-", "_")
        );
    }

}
