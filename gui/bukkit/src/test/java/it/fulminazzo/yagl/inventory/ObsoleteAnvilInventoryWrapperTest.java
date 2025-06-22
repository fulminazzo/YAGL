package it.fulminazzo.yagl.inventory;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import it.fulminazzo.jbukkit.BukkitUtils;
import it.fulminazzo.yagl.TestUtils;
import it.fulminazzo.yagl.gui.SearchGUI;
import it.fulminazzo.yagl.testing.CraftPlayer;
import it.fulminazzo.yagl.util.BukkitTestUtils;
import it.fulminazzo.yagl.util.NMSUtils;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftContainer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
class ObsoleteAnvilInventoryWrapperTest {
    public static final String NMS_VERSION = "v1_8_R3";
    private Inventory inventory;

    private Player player;

    @BeforeAll
    static void setAllUp() {
        BukkitUtils.setupServer();
    }

    @BeforeEach
    void setUp() {
        this.inventory = new ObsoleteMockInventory(3);
        ItemStack itemStack = new ItemStack(Material.STONE, 64);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(SearchGUI.EMPTY_RENAME_TEXT);
        itemStack.setItemMeta(itemMeta);
        this.inventory.setItem(0, itemStack);
        this.inventory.setItem(1, new ItemStack(Material.AIR, 64));
        new Refl<>(this.inventory).setFieldObject("type", InventoryType.ANVIL);

        CraftPlayer<EntityPlayer> craftPlayer = mock(CraftPlayer.class, withSettings().extraInterfaces(Player.class));

        this.player = (Player) craftPlayer;
        when(this.player.getLocation()).thenReturn(new Location(null, 0, 0, 0));
        when(this.player.getInventory()).thenReturn(new ObsoleteMockPlayerInventory(this.player));

        EntityPlayer entityPlayer = new EntityPlayer(this.player);
        when(craftPlayer.getHandle()).thenReturn(entityPlayer);
    }

    @Test
    void testOpen8() {
        preventNewerNMSClassesLoading(() -> {
            AnvilInventoryWrapper wrapper = new AnvilInventoryWrapper8(this.inventory);
            wrapper.open(this.player);

            EntityPlayer entityPlayer = ((CraftPlayer<EntityPlayer>) this.player).getHandle();

            Container activeContainer = entityPlayer.getActiveContainer();
            assertNotNull(activeContainer, "EntityPlayer activeContainer should not be null");
            assertInstanceOf(CraftContainer.class, activeContainer);

            List<net.minecraft.server.v1_8_R3.ItemStack> items = activeContainer.getItems();
            net.minecraft.server.v1_8_R3.ItemStack craftItemStack = items.get(0);
            assertEquals(new net.minecraft.server.v1_8_R3.ItemStack(Material.STONE, 64), craftItemStack);

            List<Slot> slots = activeContainer.getSlots();
            assertEquals(activeContainer.getSize() + this.player.getInventory().getStorageContents().length, slots.size(),
                    "Slots size should be player inventory contents size plus container size");

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
            when(NMSUtils.getNMSVersion()).thenReturn(NMS_VERSION);
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