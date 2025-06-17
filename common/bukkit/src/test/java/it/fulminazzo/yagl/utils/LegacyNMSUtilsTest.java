package it.fulminazzo.yagl.utils;

import io.netty.channel.Channel;
import lombok.Getter;
import net.minecraft.server.v1_14_R1.Packet;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LegacyNMSUtilsTest {

    @Test
    void testLegacyChatBaseComponent() {
        BukkitTestUtils.mockNMSUtils(c -> {
            when(NMSUtils.getNMSVersion()).thenReturn("v1_14_R1");
            when(NMSUtils.getIChatBaseComponent(any())).thenCallRealMethod();

            Object baseComponent = NMSUtils.getIChatBaseComponent("Hello, world");
            assertEquals("IChatBaseComponent{Hello, world}", baseComponent);
        });
    }

    @Test
    void testGetPlayerChannelLegacy() {
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

        PlayerConnection(Channel channel) {
            this.networkManager = new NetworkManager(channel);
        }

        public void sendPacket(Packet packet) {
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