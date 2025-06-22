package it.fulminazzo.yagl.util;

import io.netty.channel.Channel;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.jbukkit.BukkitUtils;
import it.fulminazzo.jbukkit.annotations.After1_;
import it.fulminazzo.jbukkit.annotations.Before1_;
import it.fulminazzo.jbukkit.inventory.MockInventory;
import it.fulminazzo.jbukkit.inventory.MockInventoryView;
import it.fulminazzo.yagl.testing.CraftPlayer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutOpenWindow;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Container;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
@Before1_(21)
class NMSUtilsTest extends BukkitUtils {
    private Player player;

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();
        Server server = (Server) mock(CraftServer.class, withSettings().extraInterfaces(Server.class));
        new Refl<>(Bukkit.class).setFieldObject("server", server);

        CraftPlayer<ServerPlayer> craftPlayer = mock(CraftPlayer.class,
                withSettings().extraInterfaces(Player.class)
        );
        when(craftPlayer.getHandle()).thenReturn(new ServerPlayer(null));
        this.player = (Player) craftPlayer;
    }

    @After1_(19)
    @Test
    void testUpdateInventoryTitle() {
        check();
        MockInventoryView view = new MockInventoryView(
                new MockInventory(9),
                this.player,
                "Previous title"
        );
        NMSUtils.updateInventoryTitle(this.player, "Title");
        assertEquals("Title", view.getTitle());
    }

    /**
     * 1.17-1.19
     */
    @Test
    void testNewUpdateInventoryTitlePacket() {
        CraftPlayer<EntityPlayer> player = mock(CraftPlayer.class,
                withSettings().extraInterfaces(Player.class)
        );
        when(player.getHandle()).thenReturn(new EntityPlayer());

        MockInventoryView inventoryView = new MockInventoryView(
                new MockInventory(27),
                (Player) player,
                "Hello"
        );

        Container container = Container.newContainer();
        new Refl<>(container).invokeMethod("setOpenInventory", inventoryView);

        EntityPlayer handle = player.getHandle();
        handle.setContainer(container);

        Object actualPacket = NMSUtils.newUpdateInventoryTitlePacket((Player) player, "Hello, world!");

        assertInstanceOf(PacketPlayOutOpenWindow.class, actualPacket,
                "Packet was supposed to be PacketPlayOutOpenWindow");

        PacketPlayOutOpenWindow packet = (PacketPlayOutOpenWindow) actualPacket;

        assertEquals(container.getId(), packet.getId(),
                "Packet id was supposed to be the same as container id");

        assertEquals(container.getType(), packet.getContainerType(),
                "Packet type was supposed to be the same as container type");

        assertEquals(CraftChatMessage.fromString("Hello, world!")[0], packet.getTitle());
    }

    @Test
    void testGetPlayerOpenContainer() {
        @NotNull Object openContainer = NMSUtils.getPlayerOpenContainer(this.player);
        assertInstanceOf(AbstractContainerMenu.class, openContainer);
        assertFalse(openContainer instanceof AbstractContainerMenu.PlayerContainerMenu);
    }

    @Test
    void testSendPacket() {
        Packet packet = mock(Packet.class);

        NMSUtils.sendPacket(this.player, packet);

        ServerPlayer player = ((CraftPlayer<ServerPlayer>) this.player).getHandle();
        List<Packet> sentPackets = player.getConnection().getSentPackets();
        assertTrue(sentPackets.contains(packet),
                String.format("Sent packets (%s) should have contained packet %s", sentPackets, packet));
    }

    @Test
    void testChatBaseComponent() {
        Object baseComponent = NMSUtils.newIChatBaseComponent("Hello, world");
        assertEquals("IChatBaseComponent{Hello, world}", baseComponent.toString());
    }

    @Test
    void testGetNMSVersionOfNewerServer() {
        Server server = (Server) mock(CraftServer.class, withSettings().extraInterfaces(Server.class));
        new Refl<>(Bukkit.class).setFieldObject("server", server);

        assertThrowsExactly(IllegalStateException.class, NMSUtils::getNMSVersion);
    }

    private static Object[][] inventoryTypeStrings() {
        check();
        return Arrays.stream(InventoryType.values())
                .map(t -> new Object[]{t, null})
                .peek(t -> {
                    InventoryType inventoryType = (InventoryType) t[0];
                    switch (inventoryType) {
                        case CHEST:
                        case ENDER_CHEST:
                            t[1] = "chest";
                            break;
                        case FURNACE:
                            t[1] = "furnace";
                            break;
                        case WORKBENCH:
                            t[1] = "crafting_table";
                            break;
                        case ANVIL:
                            t[1] = "anvil";
                            break;
                        case BREWING:
                            t[1] = "brewing_stand";
                            break;
                        case DISPENSER:
                        case DROPPER:
                            t[1] = "dropper";
                            break;
                        case HOPPER:
                            t[1] = "hopper";
                            break;
                        case BEACON:
                            t[1] = "beacon";
                            break;
                        case ENCHANTING:
                            t[1] = "enchanting_table";
                            break;
                        case MERCHANT:
                            t[1] = "villager";
                            break;
                        case SHULKER_BOX:
                            t[1] = "shulker_box";
                            break;
                    }
                })
                .toArray(Object[][]::new);
    }

    @ParameterizedTest
    @MethodSource("inventoryTypeStrings")
    void testInventoryTypeToNotchInventoryTypeString(InventoryType inventoryType, @Nullable String expected) {
        if (expected == null)
            assertThrowsExactly(IllegalArgumentException.class, () ->
                    NMSUtils.inventoryTypeToNotchInventoryTypeString(inventoryType)
            );
        else {
            expected = "minecraft:" + expected;
            assertEquals(expected, NMSUtils.inventoryTypeToNotchInventoryTypeString(inventoryType));
        }
    }

    @Test
    void testGetPlayerChannel() {
        Channel expected = mock(Channel.class);
        CraftPlayer<ServerPlayer> player = mock(CraftPlayer.class,
                withSettings().extraInterfaces(Player.class)
        );
        when(player.getHandle()).thenReturn(new ServerPlayer(expected));

        Channel actual = NMSUtils.getPlayerChannel((Player) player);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "1.20.4-R0.1-SNAPSHOT:20.4",
            "1.20-R0.1-SNAPSHOT:20.0",
            "1.8.9-R0.1-SNAPSHOT:8.9",
            "1.8-R0.1-SNAPSHOT:8.0",
    })
    void testGetServerVersion(String data) {
        String[] tmp = data.split(":");
        String version = tmp[0];
        double expected = Double.parseDouble(tmp[1]);

        when(Bukkit.getServer().getBukkitVersion()).thenReturn(version);

        double actual = NMSUtils.getServerVersion();

        assertEquals(expected, actual);
    }

    @Test
    void testGetServerVersionOfInvalidVersion() {
        when(Bukkit.getServer().getBukkitVersion()).thenReturn("INVALID_VERSION");
        assertThrowsExactly(IllegalStateException.class, NMSUtils::getServerVersion);
    }

}
