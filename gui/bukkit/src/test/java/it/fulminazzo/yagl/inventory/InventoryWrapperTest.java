package it.fulminazzo.yagl.inventory;

import it.fulminazzo.jbukkit.BukkitUtils;
import it.fulminazzo.yagl.utils.BukkitTestUtils;
import it.fulminazzo.yagl.utils.NMSUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InventoryWrapperTest {

    @BeforeEach
    void setUp() {
        BukkitUtils.setupServer();
    }

    private static Object[][] inventoryData() {
        return new Object[][]{
                // TODO: populate with proper data
                new Object[]{InventoryType.ANVIL, 16, AnvilInventoryWrapper.class}
        };
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

}