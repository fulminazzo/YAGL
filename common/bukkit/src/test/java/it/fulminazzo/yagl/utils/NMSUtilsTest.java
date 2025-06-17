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
    void testChatBaseComponent() {
        BukkitTestUtils.mockNMSUtils(c -> {
            when(NMSUtils.getNMSVersion()).thenAnswer(a -> {
                throw new IllegalStateException("NMS Version Mismatch");
            });
            when(NMSUtils.getIChatBaseComponent(any())).thenCallRealMethod();

            Object baseComponent = NMSUtils.getIChatBaseComponent("Hello, world");
            assertEquals("IChatBaseComponent{Hello, world}", baseComponent);
        });
    }

    @Test
    void testGetInventoryTypeThrowsOnInvalidTypes() {
        assertThrowsExactly(IllegalArgumentException.class, () ->
                NMSUtils.getInventoryTypeStringFromBukkitType(InventoryType.PLAYER)
        );
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
    static class EntityPlayer {
        private final ServerGamePacketListenerImpl connection;

        EntityPlayer(Channel channel) {
            this.connection = new ServerGamePacketListenerImpl(channel);
        }

    }

    @Getter
    static class ServerGamePacketListenerImpl {
        private final Connection networkManager;

        ServerGamePacketListenerImpl(Channel channel) {
            this.networkManager = new Connection(channel);
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