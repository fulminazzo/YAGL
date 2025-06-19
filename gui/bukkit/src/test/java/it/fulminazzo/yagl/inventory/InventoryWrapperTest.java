package it.fulminazzo.yagl.inventory;

import it.fulminazzo.jbukkit.BukkitUtils;
import it.fulminazzo.yagl.utils.BukkitTestUtils;
import it.fulminazzo.yagl.utils.NMSUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InventoryWrapperTest {

    @BeforeEach
    void setUp() {
        BukkitUtils.setupServer();
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
                    if (o[0] == InventoryType.ANVIL && (int) o[1] < 17)
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