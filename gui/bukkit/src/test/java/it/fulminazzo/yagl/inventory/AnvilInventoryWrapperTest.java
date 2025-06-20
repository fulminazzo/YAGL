package it.fulminazzo.yagl.inventory;

import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import it.fulminazzo.jbukkit.BukkitUtils;
import it.fulminazzo.jbukkit.inventory.MockInventory;
import it.fulminazzo.yagl.TestUtils;
import it.fulminazzo.yagl.testing.CraftPlayer;
import it.fulminazzo.yagl.utils.BukkitTestUtils;
import it.fulminazzo.yagl.utils.NMSUtils;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftContainer;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_14_R1.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
class AnvilInventoryWrapperTest {
    public static final String NMS_VERSION = "v1_14_R1";
    private Inventory inventory;

    private Player player;

    @BeforeEach
    void setUp() {
        BukkitUtils.setupServer();

        this.inventory = new MockInventory(3);
        this.inventory.setItem(0, new ItemStack(Material.STONE, 64));

        CraftPlayer<EntityPlayer> craftPlayer = mock(CraftPlayer.class, withSettings().extraInterfaces(Player.class));

        this.player = (Player) craftPlayer;
        when(this.player.getLocation()).thenReturn(new Location(null, 0, 0, 0));

        EntityPlayer entityPlayer = new EntityPlayer(null, this.player);
        when(craftPlayer.getHandle()).thenReturn(entityPlayer);
    }

    @Test
    void testOpen12_13() {
        preventNewerNMSClassesLoading(() -> {
            AnvilInventoryWrapper wrapper = new AnvilInventoryWrapper12_13(this.inventory);
            wrapper.open(this.player);

            EntityPlayer entityPlayer = ((CraftPlayer<EntityPlayer>) this.player).getHandle();

            Container activeContainer = entityPlayer.getActiveContainer();
            assertNotNull(activeContainer, "EntityPlayer activeContainer should not be null");
            assertInstanceOf(CraftContainer.class, activeContainer);

            List<CraftItemStack> items = ((CraftContainer) activeContainer).getDelegate().getItems();
            CraftItemStack craftItemStack = items.get(0);
            assertEquals(new CraftItemStack(Material.STONE, 64), craftItemStack);

            List<EntityPlayer> slotListeners = activeContainer.getSlotListeners();
            assertFalse(slotListeners.isEmpty(), "slotListeners should not be empty");

            EntityPlayer first = slotListeners.get(0);
            assertEquals(first, entityPlayer);

            EntityPlayer.PlayerConnection connection = entityPlayer.getPlayerConnection();

            List<Packet> packets = connection.getSentPackets();
            assertFalse(packets.isEmpty(), "packets should not be empty");

            Packet packet = packets.get(0);
            assertInstanceOf(PacketPlayOutOpenWindow.class, packet);

            PacketPlayOutOpenWindow openWindowPacket = (PacketPlayOutOpenWindow) packet;
            assertEquals(activeContainer.getWindowId(), openWindowPacket.getId(),
                    "openWindowPacket did not match container id");
            assertEquals(activeContainer.getType(), openWindowPacket.getContainerType(),
                    "openWindowPacket did not match container type");
            assertEquals(CraftChatMessage.fromString("Hello, world!")[0], openWindowPacket.getTitle(),
                    "openWindowPacket did not match expected title");
        });
    }

    @Test
    void testOpen14_16() {
        preventNewerNMSClassesLoading(() -> {
            AnvilInventoryWrapper wrapper = new AnvilInventoryWrapper14_16(this.inventory);
            wrapper.open(this.player);

            EntityPlayer entityPlayer = ((CraftPlayer<EntityPlayer>) this.player).getHandle();

            Container activeContainer = entityPlayer.getActiveContainer();
            assertNotNull(activeContainer, "EntityPlayer activeContainer should not be null");
            assertInstanceOf(CraftContainer.class, activeContainer);

            Container delegate = ((CraftContainer) activeContainer).getDelegate();
            List<CraftItemStack> items = delegate.getItems();
            CraftItemStack craftItemStack = items.get(0);
            assertEquals(new CraftItemStack(Material.STONE, 64), craftItemStack);

            ContainerAccess access = delegate.getContainerAccess();
            ContainerAccess expected = ContainerAccess.at(entityPlayer.getWorld(),
                    new BlockPosition(
                            this.player.getLocation().getBlockX(),
                            this.player.getLocation().getBlockY(),
                            this.player.getLocation().getBlockZ()
                    )
            );
            assertEquals(expected, access);

            List<EntityPlayer> slotListeners = activeContainer.getSlotListeners();
            assertFalse(slotListeners.isEmpty(), "slotListeners should not be empty");

            EntityPlayer first = slotListeners.get(0);
            assertEquals(first, entityPlayer);

            EntityPlayer.PlayerConnection connection = entityPlayer.getPlayerConnection();

            List<Packet> packets = connection.getSentPackets();
            assertFalse(packets.isEmpty(), "packets should not be empty");

            Packet packet = packets.get(0);
            assertInstanceOf(PacketPlayOutOpenWindow.class, packet);

            PacketPlayOutOpenWindow openWindowPacket = (PacketPlayOutOpenWindow) packet;
            assertEquals(activeContainer.getWindowId(), openWindowPacket.getId(),
                    "openWindowPacket did not match container id");
            assertEquals(activeContainer.getType(), openWindowPacket.getContainerType(),
                    "openWindowPacket did not match container type");
            assertEquals(CraftChatMessage.fromString("Hello, world!")[0], openWindowPacket.getTitle(),
                    "openWindowPacket did not match expected title");
        });
    }

    private void preventNewerNMSClassesLoading(Runnable runnable) {
        BukkitTestUtils.mockNMSUtils(() -> {
            when(NMSUtils.getNMSVersion()).thenReturn("v1_14_R1");
            TestUtils.mockReflectionUtils(r -> {
                r.when(() -> ReflectionUtils.getClass(any())).thenAnswer(a -> {
                    String className = a.getArgument(0);
                    if (className.contains("net.minecraft") && !className.contains(NMS_VERSION))
                        throw new IllegalArgumentException("Class " + className + " not available because of nms");
                    else return a.callRealMethod();
                });
                runnable.run();
            });
        });
    }

}