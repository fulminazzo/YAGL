package it.fulminazzo.yagl.inventory;

import it.fulminazzo.jbukkit.BukkitUtils;
import it.fulminazzo.jbukkit.inventory.MockInventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AnvilInventoryWrapperTest {
    private Inventory inventory;
    private Player player;

    @BeforeEach
    void setUp() {
        BukkitUtils.setupServer();

        this.inventory = new MockInventory(3);
        this.player = BukkitUtils.addPlayer(UUID.randomUUID(), "Fulminazzo");
    }

    @Test
    void testOpen12_13() {
        AnvilInventoryWrapper wrapper = new AnvilInventoryWrapper12_13(this.inventory);
        wrapper.open(this.player);
    }

}