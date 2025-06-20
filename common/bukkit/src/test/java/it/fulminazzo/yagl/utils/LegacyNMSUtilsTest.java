package it.fulminazzo.yagl.utils;

import io.netty.channel.Channel;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import it.fulminazzo.jbukkit.inventory.MockInventory;
import it.fulminazzo.jbukkit.inventory.MockInventoryView;
import it.fulminazzo.yagl.TestUtils;
import it.fulminazzo.yagl.testing.CraftPlayer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutOpenWindow;
import net.minecraft.server.v1_14_R1.Container;
import net.minecraft.server.v1_14_R1.EntityPlayer;
import it.fulminazzo.yagl.utils.legacy.LegacyMockInventoryView;
import net.minecraft.server.v1_14_R1.containers.*;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_14_R1.CraftServer;
import org.bukkit.craftbukkit.v1_14_R1.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
class LegacyNMSUtilsTest {
    private Player player;

    @BeforeEach
    void setUp() {
        Server server = (Server) mock(CraftServer.class, withSettings().extraInterfaces(Server.class));
        new Refl<>(Bukkit.class).setFieldObject("server", server);

        CraftPlayer<EntityPlayer> craftPlayer = mock(CraftPlayer.class,
                withSettings().extraInterfaces(Player.class)
        );
        when(craftPlayer.getHandle()).thenReturn(new EntityPlayer(null));
        this.player = (Player) craftPlayer;
    }

    @Test
    void testUpdateInventoryTitle() {
        TestUtils.mockReflectionUtils(m -> {
            when(ReflectionUtils.getClass(PacketPlayOutOpenWindow.class.getCanonicalName()))
                    .thenThrow(new IllegalArgumentException("Class not found"));
            when(ReflectionUtils.getClass(Packet.class.getCanonicalName()))
                    .thenThrow(new IllegalArgumentException("Class not found"));

            AtomicBoolean firstCall = new AtomicBoolean(true);
            m.when(() -> ReflectionUtils.getMethod(any(), any(Predicate.class)))
                    .thenAnswer(a -> {
                        Class<?> clazz = a.getArgument(0);
                        if (InventoryView.class.isAssignableFrom(clazz) && firstCall.get()) {
                            firstCall.set(false);
                            throw new IllegalArgumentException("Method not found");
                        }
                        else return a.callRealMethod();
                    });

            LegacyContainer internalContainer = new LegacyContainer("Previous title");
            Container container = new Container(
                    DefaultContainers.GENERIC_9x3,
                    internalContainer
            );
            InventoryView view = new LegacyMockInventoryView(
                    new MockInventory(9),
                    this.player,
                    "Previous title",
                    container
            );
            container.setOpenInventory(view);
            ((CraftPlayer<EntityPlayer>) this.player).getHandle().setActiveContainer(container);

            NMSUtils.updateInventoryTitle(this.player, "Title");

            assertEquals(CraftChatMessage.fromString("Title")[0], internalContainer.getTitle());

            CraftPlayer<EntityPlayer> craftPlayer = (CraftPlayer<EntityPlayer>) this.player;
            List<net.minecraft.server.v1_14_R1.Packet> packets = craftPlayer.getHandle().getPlayerConnection().getSentPackets();
            assertEquals(1, packets.size(), "Expected at least one packet to be sent");
        });
    }

    /**
     * 1.14-1.17
     */
    @Test
    void testNewUpdateInventoryTitlePacket() {
        TestUtils.mockReflectionUtils(() -> {
            when(ReflectionUtils.getClass(PacketPlayOutOpenWindow.class.getCanonicalName()))
                    .thenThrow(new IllegalArgumentException("Class not found"));

            MockInventoryView inventoryView = new MockInventoryView(
                    new MockInventory(27),
                    this.player,
                    "Hello"
            );

            Container container = new Container(DefaultContainers.GENERIC_9x3);
            container.setOpenInventory(inventoryView);

            EntityPlayer handle = ((CraftPlayer<EntityPlayer>) this.player).getHandle();
            handle.setActiveContainer(container);

            Object actualPacket = NMSUtils.newUpdateInventoryTitlePacket(this.player, "Hello, world!");

            assertInstanceOf(net.minecraft.server.v1_14_R1.PacketPlayOutOpenWindow.class, actualPacket,
                    "Packet was supposed to be PacketPlayOutOpenWindow");

            net.minecraft.server.v1_14_R1.PacketPlayOutOpenWindow packet = (net.minecraft.server.v1_14_R1.PacketPlayOutOpenWindow) actualPacket;

            assertEquals(container.getWindowId(), packet.getId(),
                    "Packet id was supposed to be the same as container id");

            assertEquals(container.getType(), packet.getContainerType(),
                    "Packet type was supposed to be the same as container type");

            assertEquals(CraftChatMessage.fromString("Hello, world!")[0], packet.getTitle());
        });
    }

    @Test
    void testUpdatePlayerInternalContainersTitle() {
        InventoryContainer container = new InventoryContainer(
                DefaultContainers.GENERIC_9x3,
                null,
                new LegacyContainer("previousTitle")
        );

        DelegateContainer delegateContainer = new DelegateContainer("previousTitle");
        ((CraftPlayer<EntityPlayer>) this.player).getHandle().setActiveContainer(delegateContainer);

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
    }

    @Test
    void testUpdatePlayerInternalContainersTitleNullDelegate() {
        InventoryContainer container = new InventoryContainer(
                DefaultContainers.GENERIC_9x3,
                null,
                new LegacyContainer("previousTitle")
        );

        DelegateContainer delegateContainer = new DelegateContainer("previousTitle");
        new Refl<>(delegateContainer).setFieldObject("delegate", null);
        ((CraftPlayer<EntityPlayer>) this.player).getHandle().setActiveContainer(delegateContainer);

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
        TestUtils.mockReflectionUtils(() -> {
            when(ReflectionUtils.getClass(Packet.class.getCanonicalName()))
                    .thenThrow(new IllegalArgumentException("Class not found"));

            net.minecraft.server.v1_14_R1.Packet packet = mock(net.minecraft.server.v1_14_R1.Packet.class);

            NMSUtils.sendPacket(this.player, packet);

            EntityPlayer player = ((CraftPlayer<EntityPlayer>) this.player).getHandle();
            List<net.minecraft.server.v1_14_R1.Packet> sentPackets = player.getPlayerConnection().getSentPackets();
            assertTrue(sentPackets.contains(packet),
                    String.format("Sent packets (%s) should have contained packet %s", sentPackets, packet));
        });
    }

    @Test
    void testChatBaseComponent() {
        Object baseComponent = NMSUtils.newIChatBaseComponent("Hello, world");
        assertEquals("IChatBaseComponent{Hello, world}", baseComponent.toString());
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

}