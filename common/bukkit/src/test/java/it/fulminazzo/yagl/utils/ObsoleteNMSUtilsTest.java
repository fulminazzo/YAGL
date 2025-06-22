package it.fulminazzo.yagl.utils;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import it.fulminazzo.jbukkit.inventory.MockInventory;
import it.fulminazzo.jbukkit.inventory.MockInventoryView;
import it.fulminazzo.yagl.TestUtils;
import it.fulminazzo.yagl.testing.CraftPlayer;
import net.minecraft.network.protocol.game.PacketPlayOutOpenWindow;
import net.minecraft.server.v1_8_R3.Container;
import net.minecraft.server.v1_8_R3.EntityPlayer;
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

        CraftPlayer<EntityPlayer> craftPlayer = mock(CraftPlayer.class,
                withSettings().extraInterfaces(Player.class)
        );
        when(craftPlayer.getHandle()).thenReturn(new EntityPlayer());
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

            Container container = new Container();
            container.setBukkitView(inventoryView);

            EntityPlayer handle = ((CraftPlayer<EntityPlayer>) this.player).getHandle();
            handle.setActiveContainer(container);

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
        Container container = new Container();
        container.setInventory(new Container().setTitle("previousTitle"));

        ObsoleteMockInventoryView inventoryView = new ObsoleteMockInventoryView(
                null, this.player,
                "previousTitle", container
        );

        when(this.player.getOpenInventory()).thenReturn(inventoryView);

        NMSUtils.updatePlayerInternalContainersTitle(this.player, "title");

        assertEquals("title", container
                .getInventory()
                .getTitle()
        );
    }

}