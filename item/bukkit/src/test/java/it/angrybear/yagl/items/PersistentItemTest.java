package it.angrybear.yagl.items;

import it.fulminazzo.jbukkit.BukkitUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class PersistentItemTest {

    @BeforeEach
    void setUp() {
        BukkitUtils.setupServer();
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