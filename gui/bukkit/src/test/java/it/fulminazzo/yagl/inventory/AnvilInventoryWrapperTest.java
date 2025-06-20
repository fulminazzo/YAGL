package it.fulminazzo.yagl.inventory;

import it.fulminazzo.jbukkit.BukkitUtils;
import it.fulminazzo.jbukkit.inventory.MockInventory;
import it.fulminazzo.yagl.testing.CraftPlayer;
import it.fulminazzo.yagl.utils.BukkitTestUtils;
import it.fulminazzo.yagl.utils.NMSUtils;
import net.minecraft.server.v1_14_R1.EntityPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class AnvilInventoryWrapperTest {
    private Inventory inventory;

    private Player player;

    @BeforeEach
    void setUp() {
        BukkitUtils.setupServer();

        this.inventory = new MockInventory(3);

        CraftPlayer<EntityPlayer> craftPlayer = mock(CraftPlayer.class, withSettings().extraInterfaces(Player.class));
        this.player = (Player) craftPlayer;

        EntityPlayer entityPlayer = new EntityPlayer(null, this.player);
        when(craftPlayer.getHandle()).thenReturn(entityPlayer);
    }

    @Test
    void testOpen12_13() {
        BukkitTestUtils.mockNMSUtils(() -> {
            when(NMSUtils.getNMSVersion()).thenReturn("v1_14_R1");

            AnvilInventoryWrapper wrapper = new AnvilInventoryWrapper12_13(this.inventory);
            wrapper.open(this.player);
        });
    }

}