package it.angrybear.yagl.viewers;

import it.angrybear.yagl.wrappers.Sound;
import it.fulminazzo.jbukkit.BukkitUtils;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class BukkitViewerTest {
    private Player player;
    private Viewer viewer;

    @BeforeEach
    void setUp() {
        BukkitUtils.setupServer();
        this.player = BukkitUtils.addPlayer(UUID.randomUUID(), "Alex");
        this.viewer = BukkitViewer.newViewer(this.player);
    }

    @Test
    void testNewViewer() {
        assertEquals(this.player.getUniqueId(), this.viewer.getUniqueId());
        assertEquals(this.player.getName(), this.viewer.getName());
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
    void getOfflinePlayerShouldNotBePresent() {
        when(this.player.isOnline()).thenReturn(false);
        assertFalse(((BukkitViewer) this.viewer).getPlayer().isPresent());
    }

}