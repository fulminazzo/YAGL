package net.minecraft.server.v1_14_R1;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EntityPlayer {
    private final PlayerConnection playerConnection;
    private final Container defaultContainer;

    private Container activeContainer;

    public EntityPlayer(Channel channel) {
        this.playerConnection = new PlayerConnection(channel);
        this.defaultContainer = new Container();
        this.activeContainer = new Container();
    }

    @Getter
    public static class PlayerConnection {
        private final NetworkManager networkManager;
        private final List<Packet> sentPackets;

        public PlayerConnection(Channel channel) {
            this.networkManager = new NetworkManager(channel);
            this.sentPackets = new ArrayList<>();
        }

        public void sendPacket(Packet packet) {
            this.sentPackets.add(packet);
        }

    }

    @Getter
    public static class NetworkManager {
        private final Channel channel;

        public NetworkManager(Channel channel) {
            this.channel = channel;
        }

    }
}
