package it.fulminazzo.yagl.handlers;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import it.fulminazzo.yagl.utils.NMSUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

/**
 * A special {@link ChannelDuplexHandler} capable of intercepting and
 * reading anvil rename packets from the player.
 */
public final class AnvilRenameHandler extends ChannelDuplexHandler {
    private final @NotNull Logger logger;

    private final @NotNull Player player;

    /**
     * Instantiates a new Anvil rename handler.
     *
     * @param logger the logger
     * @param player the player
     */
    public AnvilRenameHandler(final @NotNull Logger logger, @NotNull Player player) {
        this.logger = logger;
        this.player = player;
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object packet) throws Exception {
        try {
            // Reading logic
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
