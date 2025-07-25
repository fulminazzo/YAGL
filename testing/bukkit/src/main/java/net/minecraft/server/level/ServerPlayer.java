package net.minecraft.server.level;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ServerPlayer {
    private final ServerGamePacketListenerImpl connection;
    private final AbstractContainerMenu.PlayerContainerMenu playerContainer;

    private AbstractContainerMenu openContainer;

    public ServerPlayer(Channel channel) {
        this.connection = new ServerGamePacketListenerImpl(channel);
        this.playerContainer = new AbstractContainerMenu.PlayerContainerMenu();
        this.openContainer = new AbstractContainerMenu();
    }

    @Getter
    public static class ServerGamePacketListenerImpl {
        private final Connection networkManager;
        private final List<Packet> sentPackets;

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
