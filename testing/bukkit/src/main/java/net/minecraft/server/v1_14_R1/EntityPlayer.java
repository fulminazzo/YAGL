package net.minecraft.server.v1_14_R1;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EntityPlayer {
    private final Player player;
    private final World world;

    private final PlayerConnection playerConnection;
    private final Container defaultContainer;

    private Container activeContainer;
    private int nextContainerCounter;

    public EntityPlayer(Channel channel) {
        this(channel, null);
    }

    public EntityPlayer(Channel channel, Player player) {
        this.playerConnection = new PlayerConnection(channel);
        this.player = player;
        this.world = new World("default");
        this.defaultContainer = new Container();
        this.activeContainer = new Container();
    }

    public int nextContainerCounter() {
        return nextContainerCounter++;
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
