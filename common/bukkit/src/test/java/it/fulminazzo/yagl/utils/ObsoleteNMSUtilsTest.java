package it.fulminazzo.yagl.utils;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import it.fulminazzo.jbukkit.inventory.MockInventory;
import it.fulminazzo.jbukkit.inventory.MockInventoryView;
import it.fulminazzo.yagl.TestUtils;
import net.minecraft.network.protocol.game.PacketPlayOutOpenWindow;
import it.fulminazzo.yagl.utils.legacy.LegacyEntityPlayer;
import it.fulminazzo.yagl.utils.legacy.LegacyMockInventoryView;
import it.fulminazzo.yagl.utils.legacy.containers.Container;
import it.fulminazzo.yagl.utils.legacy.containers.DefaultContainers;
import it.fulminazzo.yagl.utils.legacy.containers.ObsoleteContainer;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
class ObsoleteNMSUtilsTest {
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
     * 1.8-1.14
     */
    @Test
    void testObsoleteNewUpdateInventoryTitlePacket() {
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

            LegacyEntityPlayer handle = ((CraftPlayer<LegacyEntityPlayer>) this.player).getHandle();
            handle.setOpenContainer(container);

            Object actualPacket = NMSUtils.newUpdateInventoryTitlePacket(this.player, "Hello, world!");

            assertInstanceOf(net.minecraft.server.v1_8_R3.PacketPlayOutOpenWindow.class, actualPacket,
                    "Packet was supposed to be PacketPlayOutOpenWindow");

            net.minecraft.server.v1_8_R3.PacketPlayOutOpenWindow packet = (net.minecraft.server.v1_8_R3.PacketPlayOutOpenWindow) actualPacket;

            assertEquals(container.getWindowId(), packet.getId(),
                    "Packet id was supposed to be the same as container id");

            assertEquals("minecraft:chest", packet.getContainerType(),
                    "Packet type was supposed to be the same as inventory string type");

            assertEquals(CraftChatMessage.fromString("Hello, world!")[0], packet.getTitle());

            assertEquals(27, packet.getSize(),
                    "Packet size was supposed to be the same as inventory");
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

}