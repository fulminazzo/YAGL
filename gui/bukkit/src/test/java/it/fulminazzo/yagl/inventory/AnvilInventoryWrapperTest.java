package it.fulminazzo.yagl.inventory;

import it.fulminazzo.jbukkit.BukkitUtils;
import it.fulminazzo.jbukkit.inventory.MockInventory;
import it.fulminazzo.yagl.testing.CraftPlayer;
import net.minecraft.server.v1_14_R1.EntityPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnvilInventoryWrapperTest {
    private Inventory inventory;

    private EntityPlayer entityPlayer;
    private Player player;

    @BeforeEach
    void setUp() {
        BukkitUtils.setupServer();

        this.inventory = new MockInventory(3);

        this.entityPlayer = new EntityPlayer(null);
        CraftPlayer<EntityPlayer> craftPlayer = mock(CraftPlayer.class, withSettings().extraInterfaces(Player.class));
        when(craftPlayer.getHandle()).thenReturn(this.entityPlayer);

        this.player = (Player) craftPlayer;
    }

    @Test
    void testOpen12_13() {
        AnvilInventoryWrapper wrapper = new AnvilInventoryWrapper12_13(this.inventory);
        wrapper.open(this.player);
    }

}