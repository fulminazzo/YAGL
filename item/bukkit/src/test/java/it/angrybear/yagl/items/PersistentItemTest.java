package it.angrybear.yagl.items;

import it.angrybear.yagl.items.fields.ItemFlag;
import it.fulminazzo.jbukkit.BukkitUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class PersistentItemTest {

    @BeforeEach
    void setUp() {
        BukkitUtils.setupServer();
    }

    @Test
    void testNullInteractAction() {
        PersistentItem persistentItem = new PersistentItem("stone");
        Player player = mock(Player.class);
        ItemStack itemStack = mock(ItemStack.class);
        assertDoesNotThrow(() -> persistentItem.interact(player, itemStack, Action.LEFT_CLICK_AIR));
    }

    @Test
    void testNullClickAction() {
        PersistentItem persistentItem = new PersistentItem("stone");
        Player player = mock(Player.class);
        ItemStack itemStack = mock(ItemStack.class);
        assertDoesNotThrow(() -> persistentItem.click(player, itemStack, ClickType.DOUBLE_CLICK));
    }

    @Test
    void testClearPersistentItem() {
        // Clear previous
        PersistentItem.clearPersistentItems();

        PersistentItem persistentItem = new PersistentItem(Material.STONE);
        ItemStack itemStack = persistentItem.create();
        PersistentItem.clearPersistentItems();

        assertNull(PersistentItem.getPersistentItem(itemStack));
    }

    @Test
    void testGetPersistentItem() {
        BukkitUtils.setupEnchantments();
        PersistentItem expected = new PersistentItem(Material.DIAMOND_SWORD, 1)
                .addLore("Extremely deep lore").setDisplayName("Cool sword")
                .addEnchantment("flame", 3).addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                .setCustomModelData(3);
        ItemStack itemStack = expected.create();

        assertEquals(expected, PersistentItem.getPersistentItem(itemStack));
    }

    @Test
    void testInteract() {
        AtomicBoolean value = new AtomicBoolean(false);
        PersistentItem persistentItem = new PersistentItem().setMaterial("STONE")
                .onInteract((i, p, a) -> value.set(true))
                .onClick((i, p, a) -> value.set(false));
        persistentItem.click(mock(Player.class), persistentItem.create(), ClickType.DOUBLE_CLICK);
        assertFalse(value.get());
        persistentItem.interact(mock(Player.class), persistentItem.create(), Action.LEFT_CLICK_AIR);
        assertTrue(value.get());
    }

    @Test
    void testClick() {
        AtomicBoolean value = new AtomicBoolean(false);
        PersistentItem persistentItem = new PersistentItem().setMaterial("STONE")
                .onClick((i, p, a) -> value.set(true))
                .onInteract((i, p, a) -> value.set(false));
        persistentItem.interact(mock(Player.class), persistentItem.create(), Action.LEFT_CLICK_AIR);
        assertFalse(value.get());
        persistentItem.click(mock(Player.class), persistentItem.create(), ClickType.DOUBLE_CLICK);
        assertTrue(value.get());
    }
}