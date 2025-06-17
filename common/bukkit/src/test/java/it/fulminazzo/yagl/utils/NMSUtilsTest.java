package it.fulminazzo.yagl.utils;

import io.netty.channel.Channel;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.*;

class NMSUtilsTest {

    @Test
    void testGetInventoryTypeThrowsOnInvalidTypes() {
        assertThrowsExactly(IllegalArgumentException.class, () ->
                NMSUtils.getInventoryTypeStringFromBukkitType(InventoryType.PLAYER)
        );
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

    @Test
    void testGetPlayerChannel() {
        Channel expected = mock(Channel.class);
        CraftPlayer<EntityPlayer> player = mock(CraftPlayer.class,
                withSettings().extraInterfaces(Player.class)
        );
        when(player.getHandle()).thenReturn(new EntityPlayer(expected));

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

    }

    @Getter
    static class NetworkManager {
        private final Channel channel;

        NetworkManager(Channel channel) {
            this.channel = channel;
        }

    }

    @Getter
    static class EntityPlayer {
        private final ServerGamePacketListenerImpl gamePacketListener;

        EntityPlayer(Channel channel) {
            this.gamePacketListener = new ServerGamePacketListenerImpl(channel);
        }

    }

    @Getter
    static class ServerGamePacketListenerImpl {
        private final Connection connection;

        ServerGamePacketListenerImpl(Channel channel) {
            this.connection = new Connection(channel);
        }

    }

    @Getter
    static class Connection {
        private final Channel channel;

        Connection(Channel channel) {
            this.channel = channel;
        }

    }

}