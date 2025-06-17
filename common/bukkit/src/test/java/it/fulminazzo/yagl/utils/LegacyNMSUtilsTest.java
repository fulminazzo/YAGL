package it.fulminazzo.yagl.utils;

import io.netty.channel.Channel;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import it.fulminazzo.yagl.TestUtils;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_14_R1.CraftServer;
import net.minecraft.server.v1_14_R1.Packet;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
class LegacyNMSUtilsTest {
    private Player player;

    @BeforeEach
    void setUp() {
        Server server = (Server) mock(CraftServer.class, withSettings().extraInterfaces(Server.class));
        new Refl<>(Bukkit.class).setFieldObject("server", server);

        CraftPlayer<LegacyEntityPlayer> craftPlayer = mock(CraftPlayer.class,
                withSettings().extraInterfaces(Player.class)
        );
        when(craftPlayer.getHandle()).thenReturn(new LegacyEntityPlayer(null));
        this.player = (Player) craftPlayer;
    }

    @Test
    void testGetPlayerOpenContainer() {
        Refl<?> openContainer = NMSUtils.getPlayerOpenContainer(this.player);
        assertInstanceOf(Container.class, openContainer.getObject());
    }

    @Test
    void testSendPacket() {
        BukkitTestUtils.mockNMSUtils(() ->
                TestUtils.mockReflectionUtils(() -> {
                    when(ReflectionUtils.getClass(net.minecraft.network.protocol.Packet.class.getCanonicalName()))
                            .thenAnswer(a -> {
                                throw new IllegalArgumentException("Class not found");
                            });

                    Packet packet = mock(Packet.class);

                    NMSUtils.sendPacket(this.player, packet);

                    LegacyEntityPlayer player = ((CraftPlayer<LegacyEntityPlayer>) this.player).getHandle();
                    List<Packet> sentPackets = player.getPlayerConnection().getSentPackets();
                    assertTrue(sentPackets.contains(packet),
                            String.format("Sent packets (%s) should have contained packet %s", sentPackets, packet));
                })
        );
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
    @Setter
    static class CraftPlayer<H> {
        private final H handle;

        private Container openContainer;
        private final Container playerContainer;

        CraftPlayer(H handle) {
            this.handle = handle;
            this.openContainer = new Container();
            this.playerContainer = new Container();
        }

    }

    static class Container {

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