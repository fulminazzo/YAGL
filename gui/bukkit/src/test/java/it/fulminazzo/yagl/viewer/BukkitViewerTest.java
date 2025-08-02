package it.fulminazzo.yagl.viewer;

import it.fulminazzo.jbukkit.BukkitUtils;
import it.fulminazzo.yagl.item.Item;
import it.fulminazzo.yagl.wrapper.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BukkitViewerTest {
    private Player player;
    private Viewer viewer;
    private ItemStack cursor;

    @BeforeAll
    static void setAllUp() {
        BukkitUtils.setupServer();
    }

    @BeforeEach
    void setUp() {
        this.player = BukkitUtils.addPlayer(UUID.randomUUID(), "Alex");
        when(this.player.getItemOnCursor()).thenAnswer(a -> cursor);
        doAnswer(a -> {
            cursor = a.getArgument(0);
            return null;
        }).when(this.player).setItemOnCursor(any());
        when(this.player.isOnline()).thenReturn(true);
        this.viewer = BukkitViewer.newViewer(this.player);
    }

    @Test
    void testSetAndGetCursor() {
        @Nullable Item cursor = this.viewer.getCursor();
        assertNull(cursor);

        Item newCursor = Item.newItem("STONE").setAmount(3);
        this.viewer.setCursor(newCursor);
        assertEquals(newCursor, this.viewer.getCursor());

        this.viewer.setCursor(null);
        assertNull(this.viewer.getCursor());

        this.viewer.setCursor(Item.newItem("air"));
        assertNull(this.viewer.getCursor());
    }

    @Test
    void testNewViewer() {
        assertEquals(this.player.getUniqueId(), this.viewer.getUniqueId());
        assertEquals(this.player.getName(), this.viewer.getName());
    }

    @Test
    void testSendMessage() {
        String expected = "Hello world";
        this.viewer.sendMessage(expected);
        verify(this.player, atLeastOnce()).sendMessage(expected);
    }

    @Test
    void testSendMessageOffline() {
        BukkitUtils.removePlayer(this.player);
        assertThrowsExactly(PlayerOfflineException.class, () -> this.viewer.sendMessage("Message"));
    }

    @Test
    void testPlaySoundOffline() {
        BukkitUtils.removePlayer(this.player);
        assertThrowsExactly(PlayerOfflineException.class, () -> this.viewer.playSound(new Sound("tmp")));
    }

    @Test
    void testExecuteCommandOffline() {
        BukkitUtils.removePlayer(this.player);
        assertThrowsExactly(PlayerOfflineException.class, () -> this.viewer.executeCommand("tmp"));
    }

    @Test
    void testHasPermissionOffline() {
        BukkitUtils.removePlayer(this.player);
        assertFalse(this.viewer.hasPermission("permission"));
    }

    @Test
    void testCloseGUI() {
        this.viewer.closeGUI();
        verify(this.player).closeInventory();
    }

    @Test
    void getOfflinePlayerShouldNotBePresent() {
        when(this.player.isOnline()).thenReturn(false);
        assertFalse(((BukkitViewer) this.viewer).getPlayer().isPresent());
    }

}
