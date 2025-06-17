package it.fulminazzo.yagl.utils;

import io.netty.channel.Channel;
import lombok.Getter;
import net.minecraft.network.protocol.Packet;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
class NMSUtilsTest {
    private Player player;

    @BeforeEach
    void setUp() {
        CraftPlayer<EntityPlayer> craftPlayer = mock(CraftPlayer.class,
                withSettings().extraInterfaces(Player.class)
        );
        when(craftPlayer.getHandle()).thenReturn(new EntityPlayer(null));
        this.player = (Player) craftPlayer;
    }

    @Test
    void testSendPacket() {
        Packet packet = mock(Packet.class);

        NMSUtils.sendPacket(this.player, packet);

        EntityPlayer player = ((CraftPlayer<EntityPlayer>) this.player).getHandle();
        List<Packet> sentPackets = player.getConnection().getSentPackets();
        assertTrue(sentPackets.contains(packet),
                String.format("Sent packets (%s) should have contained packet %s", sentPackets, packet));
    }

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
    static class Connection {
        private final Channel channel;

        Connection(Channel channel) {
            this.channel = channel;
        }

    }

}