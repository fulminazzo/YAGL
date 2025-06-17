package it.fulminazzo.yagl.utils;

import io.netty.channel.Channel;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import it.fulminazzo.jbukkit.inventory.MockInventory;
import it.fulminazzo.yagl.TestUtils;
import it.fulminazzo.yagl.utils.legacy.Container;
import it.fulminazzo.yagl.utils.legacy.LegacyEntityPlayer;
import net.minecraft.server.v1_14_R1.CraftServer;
import net.minecraft.server.v1_14_R1.Packet;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

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

    @ParameterizedTest
    @EnumSource(Container.DefaultContainers.class)
    void testGetContainerType(Container.DefaultContainers type) {
        Container container = new Container(type);

        Inventory inventory = new MockInventory(type.getSize());
        new Refl<>(inventory).setFieldObject("type", type.getInventoryType());
        container.setOpenInventory(inventory);

        Object actual = NMSUtils.getContainerType(container);

        assertEquals(type, actual);
    }

    @ParameterizedTest
    @EnumSource(Container.ObfuscatedContainers.class)
    void testGetObfuscatedContainerType(Container.ObfuscatedContainers type) {
        Container container = new Container(type);

        Inventory inventory = new MockInventory(type.getSize());
        new Refl<>(inventory).setFieldObject("type", type.getInventoryType());
        container.setOpenInventory(inventory);

        Object actual = NMSUtils.getContainerType(container);

        assertEquals(type, actual);
    }

    @Test
    void testGetPlayerOpenContainer() {
        @NotNull Object openContainer = NMSUtils.getPlayerOpenContainer(this.player);
        assertInstanceOf(Container.class, openContainer);
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

}