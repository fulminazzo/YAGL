package it.fulminazzo.yagl.utils;

import io.netty.channel.Channel;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.jbukkit.inventory.MockInventory;
import it.fulminazzo.jbukkit.inventory.MockInventoryView;
import it.fulminazzo.yagl.utils.current.AbstractContainerMenu;
import it.fulminazzo.yagl.utils.current.EntityPlayer;
import it.fulminazzo.yagl.utils.current.EntityPlayerContainer;
import it.fulminazzo.yagl.utils.current.containers.Container;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.v1_14_R1.CraftServer;
import net.minecraft.server.v1_14_R1.PacketPlayOutOpenWindow;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_14_R1.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
class NMSUtilsTest {
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
    
    /**
     * 1.17-1.19
     */
    @Test
    void testConstructUpdateInventoryTitlePacket() {
        BukkitTestUtils.mockNMSUtils(() -> {
            when(NMSUtils.getIChatBaseComponent(any())).thenCallRealMethod();

            CraftPlayer<EntityPlayerContainer> player = mock(CraftPlayer.class,
                    withSettings().extraInterfaces(Player.class)
            );
            when(player.getHandle()).thenReturn(new EntityPlayerContainer());

            MockInventoryView inventoryView = new MockInventoryView(
                    new MockInventory(27),
                    (Player) player,
                    "Hello"
            );

            Container container = Container.newContainer();
            container.setOpenInventory(inventoryView);

            EntityPlayerContainer handle = player.getHandle();
            handle.setContainer(container);

            Object actualPacket = NMSUtils.constructUpdateInventoryTitlePacket((Player) player, "Hello, world!");

            assertInstanceOf(PacketPlayOutOpenWindow.class, actualPacket,
                    "Packet was supposed to be PacketPlayOutOpenWindow");

            PacketPlayOutOpenWindow packet = (PacketPlayOutOpenWindow) actualPacket;

            assertEquals(container.getId(), packet.getId(),
                    "Packet id was supposed to be the same as container id");

            assertEquals(container.getType(), packet.getContainerType(),
                    "Packet type was supposed to be the same as container type");

            assertEquals(CraftChatMessage.fromString("Hello, world!")[0], packet.getTitle());
        });
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

    private static Object[][] inventoryTypeStrings() {
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
    void testGetInventoryTypeStringFromBukkitType(InventoryType inventoryType, @Nullable String expected) {
        if (expected == null)
            assertThrowsExactly(IllegalArgumentException.class, () ->
                    NMSUtils.getInventoryTypeStringFromBukkitType(inventoryType)
            );
        else {
            expected = "minecraft:" + expected;
            assertEquals(expected, NMSUtils.getInventoryTypeStringFromBukkitType(inventoryType));
        }
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