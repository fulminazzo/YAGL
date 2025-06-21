package net.minecraft.server.v1_8_R3;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EntityPlayer {
    private final Player player;
    private final Container defaultContainer;

    private final PlayerConnection playerConnection;

    private Container activeContainer;

    private int nextContainerCounter;

    public EntityPlayer() {
        this(null);
    }

    public EntityPlayer(Player player) {
        this.player = player;

        this.playerConnection = new PlayerConnection();

        this.defaultContainer = new Container();
        this.activeContainer = new Container();
    }

    public int nextContainerCounter() {
        return nextContainerCounter++;
    }
    
    @Getter
    public static class PlayerConnection {
        private final List<Packet> sentPackets;

        public PlayerConnection() {
            this.sentPackets = new ArrayList<>();
        }

        public void sendPacket(Packet packet) {
            this.sentPackets.add(packet);
        }

    }

}
