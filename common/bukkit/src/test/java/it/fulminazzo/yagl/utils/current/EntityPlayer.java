package it.fulminazzo.yagl.utils.current;

import io.netty.channel.Channel;
import it.fulminazzo.yagl.utils.current.containers.Container;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.protocol.Packet;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EntityPlayer {
    private final ServerGamePacketListenerImpl connection;
    private final AbstractContainerMenu.PlayerContainerMenu playerContainer;

    private AbstractContainerMenu openContainer;

    private Container container;

    public EntityPlayer(Channel channel) {
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
