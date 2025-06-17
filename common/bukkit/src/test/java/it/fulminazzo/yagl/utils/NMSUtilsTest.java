package it.fulminazzo.yagl.utils;

import io.netty.channel.Channel;
import it.fulminazzo.yagl.utils.current.AbstractContainerMenu;
import it.fulminazzo.yagl.utils.current.EntityPlayer;
import net.minecraft.network.protocol.Packet;
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
        CraftPlayer<EntityPlayer> craftPlayer = mock(CraftPlayer.class,
                withSettings().extraInterfaces(Player.class)
        );
        when(craftPlayer.getHandle()).thenReturn(new EntityPlayer(null));
        this.player = (Player) craftPlayer;
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