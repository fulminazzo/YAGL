package it.angrybear.yagl;

import it.angrybear.yagl.guis.GUI;
import it.angrybear.yagl.items.Item;
import it.angrybear.yagl.utils.GUIUtils;
import it.fulminazzo.jbukkit.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GUIManagerTest {

    @Nested
    class EventsTest {
        private GUIManager guiManager;
        private Player player;
        private GUI expected;

        @BeforeEach
        void setUp() {
            this.guiManager = new GUIManager();
            BukkitUtils.setupServer();

            Server server = Bukkit.getServer();
            when(server.getPluginManager()).thenReturn(mock(PluginManager.class));

            this.player = BukkitUtils.addPlayer(UUID.randomUUID(), "Alex");
            when(this.player.isOnline()).thenReturn(true);

            this.expected = GUI.newGUI(9)
                    .setTitle("Hello world")
                    .addContent(Item.newItem("stone").setDisplayName("test"));

            GUIUtils.openGUI(this.expected, GUIManager.getViewer(this.player));
        }

        @Test
        void testOpenEvent() {
            AtomicBoolean expected = new AtomicBoolean(false);
            this.expected.onOpenGUI((v, g) -> expected.set(true));

            InventoryView view = getView();

            this.guiManager.on(new InventoryOpenEvent(view));

            assertTrue(expected.get(), "OpenGUI action was not invoked");
        }

        @Test
        void testDragEvent() {
            InventoryView view = getView();
            InventoryDragEvent event = new InventoryDragEvent(view, null, new ItemStack(Material.STONE),
                    false, new HashMap<>());
            assertFalse(event.isCancelled(), "Event should not be cancelled when starting");
            this.guiManager.on(event);
            assertTrue(event.isCancelled(), "Event should be cancelled after being invoked");
        }

        @Test
        void testCloseEvent() {
            AtomicBoolean expected = new AtomicBoolean(false);
            this.expected.onCloseGUI((v, g) -> expected.set(true));

            InventoryView view = getView();

            this.guiManager.on(new InventoryCloseEvent(view));

            assertTrue(expected.get(), "CloseGUI action was not invoked");
        }

        private @NotNull InventoryView getView() {
            InventoryView view = mock(InventoryView.class);
            when(view.getPlayer()).thenReturn(this.player);
            return view;
        }

    }

}