package it.angrybear.yagl;

import it.angrybear.yagl.guis.GUI;
import it.angrybear.yagl.items.Item;
import it.angrybear.yagl.utils.GUIUtils;
import it.angrybear.yagl.wrappers.Sound;
import it.fulminazzo.jbukkit.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

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
        void testChangeGUI() {
            AtomicBoolean expected = new AtomicBoolean(false);
            AtomicBoolean notExpected = new AtomicBoolean(false);
            this.expected
                    .onChangeGUI((v, g1, g2) -> expected.set(true))
                    .onCloseGUI((v, g) -> notExpected.set(true));
            doAnswer(a -> {
                InventoryCloseEvent event = new InventoryCloseEvent(getView());
                this.guiManager.on(event);
                return null;
            }).when(this.player).closeInventory();

            this.expected.open(GUIManager.getViewer(this.player));

            assertTrue(expected.get(), "Change GUI was not invoked");
            assertFalse(notExpected.get(), "Close GUI should not invoked");
        }

        @Test
        void testClickAction() {
            AtomicBoolean expected = new AtomicBoolean(false);
            this.expected.getContents(0).forEach(e -> e.onClickItem((v, g, i) -> expected.set(true)));

            InventoryView view = getView();
            InventoryClickEvent event = new InventoryClickEvent(view, InventoryType.SlotType.CONTAINER,
                    0, ClickType.LEFT, InventoryAction.CLONE_STACK);
            assertFalse(event.isCancelled(), "Event should not be cancelled when starting");
            this.guiManager.on(event);
            assertTrue(event.isCancelled(), "Event should be cancelled after being invoked");
            assertTrue(expected.get(), "Click action was not invoked");
        }

        @Test
        void testNullClickActionSound() {
            Sound sound = new Sound("pirate", 1f, 3f);
            Location location = new Location(null, 0, 1, 0);
            when(this.player.getLocation()).thenReturn(location);
            this.expected.getContents(0).forEach(c -> c.setClickSound(sound));

            InventoryView view = getView();
            InventoryClickEvent event = new InventoryClickEvent(view, InventoryType.SlotType.CONTAINER,
                    0, ClickType.LEFT, InventoryAction.CLONE_STACK);
            assertFalse(event.isCancelled(), "Event should not be cancelled when starting");
            this.guiManager.on(event);
            assertTrue(event.isCancelled(), "Event should be cancelled after being invoked");

            verify(this.player).playSound(location, sound.getName(), sound.getVolume(), sound.getPitch());
        }

        @Test
        void testClickActionMovable() {
            AtomicBoolean expected = new AtomicBoolean(false);
            this.expected.getContents(0).forEach(e -> e.onClickItem((v, g, i) -> expected.set(true)));
            this.expected.setAllMovable();

            InventoryView view = getView();
            InventoryClickEvent event = new InventoryClickEvent(view, InventoryType.SlotType.CONTAINER,
                    0, ClickType.LEFT, InventoryAction.CLONE_STACK);
            assertFalse(event.isCancelled(), "Event should not be cancelled when starting");
            this.guiManager.on(event);
            assertFalse(event.isCancelled(), "Event should not be cancelled with movable item");
            assertTrue(expected.get(), "Click action was not invoked");
        }

        @Test
        void testClickOutside() {
            AtomicBoolean expected = new AtomicBoolean(false);
            AtomicBoolean notExpected = new AtomicBoolean(false);
            this.expected.getContents(0).forEach(e -> e.onClickItem((v, g, i) -> notExpected.set(true)));
            this.expected.onClickOutside((v, g) -> expected.set(true));
            this.expected.setAllMovable();

            InventoryView view = getView();
            InventoryClickEvent event = new InventoryClickEvent(view, InventoryType.SlotType.CONTAINER,
                    -1, ClickType.LEFT, InventoryAction.CLONE_STACK);
            assertFalse(event.isCancelled(), "Event should not be cancelled when starting");
            this.guiManager.on(event);
            assertTrue(event.isCancelled(), "Event should be cancelled after being invoked");
            assertFalse(notExpected.get(), "Click action should not be invoked");
            assertTrue(expected.get(), "Outside action was not invoked");
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

        @Test
        void testDisableThisPlugin() {
            try (MockedStatic<JavaPlugin> ignored = mockStatic(JavaPlugin.class)) {
                JavaPlugin plugin = mock(JavaPlugin.class);
                when(JavaPlugin.getProvidingPlugin(any())).thenAnswer(a -> plugin);

                PluginDisableEvent event = new PluginDisableEvent(plugin);
                this.guiManager.on(event);
                verify(this.player).closeInventory();
            }
        }

        @Test
        void testDisableNotThisPlugin() {
            try (MockedStatic<JavaPlugin> ignored = mockStatic(JavaPlugin.class)) {
                JavaPlugin plugin = mock(JavaPlugin.class);
                when(JavaPlugin.getProvidingPlugin(any())).thenAnswer(a -> plugin);

                PluginDisableEvent event = new PluginDisableEvent(mock(JavaPlugin.class));
                this.guiManager.on(event);
                verify(this.player, never()).closeInventory();
            }
        }

        private @NotNull InventoryView getView() {
            InventoryView view = mock(InventoryView.class);
            when(view.getPlayer()).thenReturn(this.player);
            return view;
        }

    }

}