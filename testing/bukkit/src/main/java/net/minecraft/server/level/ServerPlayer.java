package net.minecraft.server.level;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ServerPlayer {
    private final @NotNull ServerGamePacketListenerImpl connection;
    private final AbstractContainerMenu.@NotNull PlayerContainerMenu playerContainer;

    private AbstractContainerMenu openContainer;

    public ServerPlayer(Channel channel) {
        this.connection = new ServerGamePacketListenerImpl(channel);
        this.playerContainer = new AbstractContainerMenu.PlayerContainerMenu();
        this.openContainer = new AbstractContainerMenu();
    }

    @Getter
    public static class ServerGamePacketListenerImpl {
        private final @NotNull Connection networkManager;
        private final @NotNull List<Packet> sentPackets;

        ServerGamePacketListenerImpl(Channel channel) {
            this.networkManager = new Connection(channel);
            this.sentPackets = new ArrayList<>();
        }

        public void sendPacket(Packet packet) {
            this.sentPackets.add(packet);
        }

    }

    @Getter
    public static class Connection {
        private final Channel channel;

        Connection(Channel channel) {
            this.channel = channel;
        }

    }

}
