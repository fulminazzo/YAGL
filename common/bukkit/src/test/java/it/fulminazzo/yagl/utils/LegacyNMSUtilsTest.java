package it.fulminazzo.yagl.utils;

import io.netty.channel.Channel;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import it.fulminazzo.jbukkit.inventory.MockInventory;
import it.fulminazzo.jbukkit.inventory.MockInventoryView;
import it.fulminazzo.yagl.TestUtils;
import it.fulminazzo.yagl.utils.legacy.LegacyEntityPlayer;
import it.fulminazzo.yagl.utils.legacy.LegacyMockInventoryView;
import it.fulminazzo.yagl.utils.legacy.containers.*;
import net.minecraft.server.v1_14_R1.CraftServer;
import net.minecraft.server.v1_14_R1.Packet;
import net.minecraft.server.v1_14_R1.PacketPlayOutOpenWindow;
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

    /**
     * 1.14-1.17
     */
    @Test
    void testConstructUpdateInventoryTitlePacket() {
        BukkitTestUtils.mockNMSUtils(() -> {
            when(NMSUtils.getIChatBaseComponent(any())).thenCallRealMethod();

            MockInventoryView inventoryView = new MockInventoryView(
                    new MockInventory(27),
                    this.player,
                    "Hello"
            );

            Container container = new Container(DefaultContainers.GENERIC_9x3);
            container.setOpenInventory(inventoryView);

            LegacyEntityPlayer handle = ((CraftPlayer<LegacyEntityPlayer>) this.player).getHandle();
            handle.setOpenContainer(container);

            Object actualPacket = NMSUtils.constructUpdateInventoryTitlePacket(this.player, "Hello, world!");

            assertInstanceOf(PacketPlayOutOpenWindow.class, actualPacket,
                    "Packet was supposed to be PacketPlayOutOpenWindow");

            PacketPlayOutOpenWindow packet = (PacketPlayOutOpenWindow) actualPacket;

            assertEquals(container.getWindowId(), packet.getId(),
                    "Packet id was supposed to be the same as container id");

            assertEquals(container.getType(), packet.getContainerType(),
                    "Packet type was supposed to be the same as container type");

            assertEquals(CraftChatMessage.fromString("Hello, world!")[0], packet.getTitle());
        });
    }

    @Test
    void testUpdatePlayerInternalContainersTitle() {
        BukkitTestUtils.mockNMSUtils(() -> {
            when(NMSUtils.getIChatBaseComponent(any())).thenCallRealMethod();

            InventoryContainer container = new InventoryContainer(
                    DefaultContainers.GENERIC_9x3,
                    null,
                    new LegacyContainer("previousTitle")
            );

            DelegateContainer delegateContainer = new DelegateContainer("previousTitle");
            ((CraftPlayer<LegacyEntityPlayer>) this.player).getHandle().setOpenContainer(delegateContainer);

            LegacyMockInventoryView inventoryView = new LegacyMockInventoryView(
                    null, this.player,
                    "previousTitle", container
            );

            when(this.player.getOpenInventory()).thenReturn(inventoryView);

            NMSUtils.updatePlayerInternalContainersTitle(this.player, "title");

            assertEquals(CraftChatMessage.fromString("title")[0], ((LegacyContainer) container
                            .getInventory())
                            .getTitle(),
                    "Actual container title was not changed"
            );
            assertEquals("title", delegateContainer.getCachedTitle(),
                    "Delegate container title was not changed");
            assertEquals("title", ((ObsoleteContainer) delegateContainer
                            .getDelegate()
                            .getContainer())
                            .getTitle(),
                    "Delegate container internal container title was not changed"
            );
        });
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