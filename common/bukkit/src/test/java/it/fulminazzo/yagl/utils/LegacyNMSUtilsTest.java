package it.fulminazzo.yagl.utils;

import io.netty.channel.Channel;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import it.fulminazzo.jbukkit.inventory.MockInventory;
import it.fulminazzo.yagl.TestUtils;
import it.fulminazzo.yagl.utils.legacy.*;
import it.fulminazzo.yagl.utils.legacy.containers.*;
import net.minecraft.server.v1_14_R1.CraftServer;
import net.minecraft.server.v1_14_R1.Packet;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_14_R1.util.CraftChatMessage;
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

    @Test
    void testUpdatePlayerInternalContainersTitle() {
        BukkitTestUtils.mockNMSUtils(() -> {
            when(NMSUtils.getNMSVersion()).thenReturn("v1_14_R1");
            when(NMSUtils.getIChatBaseComponent(any())).thenCallRealMethod();

            InventoryContainer container = new InventoryContainer(
                    DefaultContainers.GENERIC_9x3,
                    null,
                    new LegacyContainer("previousTitle")
            );

            LegacyMockInventoryView inventoryView = new LegacyMockInventoryView(
                    null, this.player,
                    "previousTitle", container
            );

            when(this.player.getOpenInventory()).thenReturn(inventoryView);

            NMSUtils.updatePlayerInternalContainersTitle(this.player, "title");

            assertEquals(CraftChatMessage.fromString("title")[0], ((LegacyContainer) container
                    .getInventory())
                    .getTitle()
            );
        });
    }

    @Test
    void testObsoleteUpdatePlayerInternalContainersTitle() {
        Container container = new Container(
                DefaultContainers.GENERIC_9x3,
                new ObsoleteContainer("previousTitle")
        );

        new Refl<>(((CraftPlayer<LegacyEntityPlayer>) this.player).getHandle())
                .setFieldObject("playerContainer", new Container());

        LegacyMockInventoryView inventoryView = new LegacyMockInventoryView(
                null, this.player,
                "previousTitle", container
        );

        when(this.player.getOpenInventory()).thenReturn(inventoryView);

        NMSUtils.updatePlayerInternalContainersTitle(this.player, "title");

        assertEquals("title", ((ObsoleteContainer) container
                .getContainer())
                .getTitle()
        );
    }

    @ParameterizedTest
    @EnumSource(DefaultContainers.class)
    void testGetContainerType(DefaultContainers type) {
        Container container = new Container(type);

        Inventory inventory = new MockInventory(type.getSize());
        new Refl<>(inventory).setFieldObject("type", type.getInventoryType());
        container.setOpenInventory(inventory);

        Object actual = NMSUtils.getContainerType(container);

        assertEquals(type, actual);
    }

    @ParameterizedTest
    @EnumSource(ObfuscatedContainers.class)
    void testGetObfuscatedContainerType(ObfuscatedContainers type) {
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
        assertFalse(openContainer instanceof PlayerContainer);
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
            assertEquals("IChatBaseComponent{Hello, world}", baseComponent.toString());
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