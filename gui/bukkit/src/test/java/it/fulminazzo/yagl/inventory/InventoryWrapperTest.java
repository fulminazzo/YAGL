package it.fulminazzo.yagl.inventory;

import it.fulminazzo.jbukkit.BukkitUtils;
import it.fulminazzo.yagl.utils.BukkitTestUtils;
import it.fulminazzo.yagl.utils.NMSUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.scheduler.BukkitScheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.*;

class InventoryWrapperTest {

    @BeforeEach
    void setUp() {
        BukkitUtils.setupServer();
    }

    @Test
    void testOpenSchedulesIfNotOnPrimaryThread() {
        BukkitTestUtils.mockPlugin(p -> {
            when(Bukkit.getServer().isPrimaryThread()).thenReturn(false);

            BukkitScheduler scheduler = mock(BukkitScheduler.class);
            when(scheduler.runTask(any(), any(Runnable.class))).thenAnswer(a -> {
                Runnable runnable = a.getArgument(1);
                runnable.run();
                return null;
            });
            when(Bukkit.getServer().getScheduler()).thenReturn(scheduler);

            InventoryWrapperImpl inventory = mock(InventoryWrapperImpl.class);
            doCallRealMethod().when(inventory).open(any());

            Player player = BukkitUtils.addPlayer(UUID.randomUUID(), "Fulminazzo");

            inventory.open(player);

            verify(inventory).internalOpen(player);
        });
    }

    private static Object[][] inventoryData() {
        return Arrays.stream(InventoryType.values())
                .map(t -> {
                    List<Object[]> data = new ArrayList<>();
                    for (int i = 8; i <= 21; i++) data.add(new Object[]{t, i});
                    return data;
                })
                .flatMap(Collection::stream)
                .map(o -> new Object[]{o[0], o[1], InventoryWrapperContainer.class})
                .peek(o -> {
                    int version = (int) o[1];
                    if (o[0] == InventoryType.ANVIL && (version >= 12 && version < 17 || version == 8))
                        o[2] = AnvilInventoryWrapper.class;
                })
                .toArray(Object[][]::new);
    }

    @ParameterizedTest
    @MethodSource("inventoryData")
    void testCreateInventory(InventoryType type,
                             double serverVersion,
                             Class<?> expectedType) {
        BukkitTestUtils.mockNMSUtils(() -> {
            when(NMSUtils.getNMSVersion()).thenReturn("v1_14_R1");
            when(NMSUtils.getServerVersion()).thenReturn(serverVersion);

            InventoryWrapper wrapper = InventoryWrapper.createInventory(
                    mock(Player.class),
                    type
            );

            assertInstanceOf(expectedType, wrapper);
        });
    }

    @ParameterizedTest
    @MethodSource("inventoryData")
    void testCreateInventoryTitle(InventoryType type,
                                  double serverVersion,
                                  Class<?> expectedType) {
        BukkitTestUtils.mockNMSUtils(() -> {
            when(NMSUtils.getNMSVersion()).thenReturn("v1_14_R1");
            when(NMSUtils.getServerVersion()).thenReturn(serverVersion);

            InventoryWrapper wrapper = InventoryWrapper.createInventory(
                    mock(Player.class),
                    type,
                    "Hello, world"
            );

            assertInstanceOf(expectedType, wrapper);
        });
    }

}