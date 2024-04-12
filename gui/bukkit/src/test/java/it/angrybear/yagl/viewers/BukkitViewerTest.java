package it.angrybear.yagl.viewers;

import it.fulminazzo.jbukkit.BukkitUtils;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BukkitViewerTest {

    @Test
    void testNewViewer() {
        Player player = BukkitUtils.addPlayer(UUID.randomUUID(), "Alex");
        Viewer viewer = BukkitViewer.newViewer(player);
        assertEquals(player.getUniqueId(), viewer.getUniqueId());
        assertEquals(player.getName(), viewer.getName());
    }
}