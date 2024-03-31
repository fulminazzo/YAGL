package it.angrybear.yagl.listeners;

import it.angrybear.yagl.items.PersistentItem;
import it.angrybear.yagl.persistent.DeathAction;
import it.fulminazzo.jbukkit.BukkitUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PersistentListenerTest {
    private PersistentItem maintain, disappear;
    private PersistentListener listener;

    @BeforeEach
    void setUp() {
        BukkitUtils.setupServer();
        this.maintain = new PersistentItem(Material.DIAMOND_SWORD, 1).setDisplayName("Maintain").setDeathAction(DeathAction.MAINTAIN);
        this.disappear = new PersistentItem(Material.GOLDEN_SWORD, 1).setDisplayName("Disappear").setDeathAction(DeathAction.DISAPPEAR);
        this.listener = new PersistentListener();
    }

    @Test
    void simulatePlayerDeath() {
        Player player = getPlayer();
        ItemStack[] contents = player.getInventory().getContents();
        contents[3] = this.maintain.create();
        List<ItemStack> drops = new LinkedList<>(Arrays.asList(contents));

        PlayerDeathEvent event = new PlayerDeathEvent(player, drops, 3, "Player died");
        this.listener.on(event);

        for (ItemStack i : drops) assertNull(i);

        List<ItemStack> copy = Arrays.asList(contents);
        assertTrue(copy.contains(this.maintain.create()), "The contents should contain the maintain item");
        assertFalse(copy.contains(this.disappear.create()), "The contents should not contain the disappear item");
    }

    private Player getPlayer() {
        ItemStack[] contents = new ItemStack[36];
        PlayerInventory inventory = mock(PlayerInventory.class);
        when(inventory.getContents()).thenReturn(contents);
        Player player = mock(Player.class);
        when(player.getInventory()).thenReturn(inventory);
        return player;
    }
}