package it.fulminazzo.yagl.utils;

import io.netty.channel.Channel;
import lombok.Getter;
import net.minecraft.server.v1_14_R1.Packet;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
class LegacyNMSUtilsTest {
    private Player player;

    @BeforeEach
    void setUp() {
        CraftPlayer<LegacyEntityPlayer> craftPlayer = mock(CraftPlayer.class,
                withSettings().extraInterfaces(Player.class)
        );
        when(craftPlayer.getHandle()).thenReturn(new LegacyEntityPlayer(null));
        this.player = (Player) craftPlayer;
    }

    @Test
    void testChatBaseComponent() {
        BukkitTestUtils.mockNMSUtils(c -> {
            when(NMSUtils.getNMSVersion()).thenReturn("v1_14_R1");
            when(NMSUtils.getIChatBaseComponent(any())).thenCallRealMethod();

            Object baseComponent = NMSUtils.getIChatBaseComponent("Hello, world");
            assertEquals("IChatBaseComponent{Hello, world}", baseComponent);
        });
    }

    @Test
    void testGetPlayerChannel() {
        Channel expected = mock(Channel.class);
        CraftPlayer<LegacyEntityPlayer> player = mock(CraftPlayer.class,
                withSettings().extraInterfaces(Player.class)
        );
        when(player.getHandle()).thenReturn(new LegacyEntityPlayer(expected));

        Channel actual = NMSUtils.getPlayerChannel((Player) player);
        assertEquals(expected, actual);
    }

    @Getter
    static class CraftPlayer<H> {
        private final H handle;

        CraftPlayer(H handle) {
            this.handle = handle;
        }

    }

    @Getter
    static class LegacyEntityPlayer {
        private final PlayerConnection playerConnection;

        LegacyEntityPlayer(Channel channel) {
            this.playerConnection = new PlayerConnection(channel);
        }

    }

    @Getter
    static class PlayerConnection {
        private final NetworkManager networkManager;
        private final List<Packet> sentPackets;

        PlayerConnection(Channel channel) {
            this.networkManager = new NetworkManager(channel);
            this.sentPackets = new ArrayList<>();
        }

        public void sendPacket(Packet packet) {
            this.sentPackets.add(packet);
        }

    }

    @Getter
    static class NetworkManager {
        private final Channel channel;

        NetworkManager(Channel channel) {
            this.channel = channel;
        }

    }

}