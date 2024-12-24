package it.fulminazzo.yagl;

import it.fulminazzo.jbukkit.BukkitUtils;
import it.fulminazzo.jbukkit.inventory.MockInventory;
import it.fulminazzo.yagl.guis.GUI;
import it.fulminazzo.yagl.wrappers.inventory.InventoryViewWrapper;
import it.fulminazzo.yagl.items.Item;
import it.fulminazzo.yagl.utils.GUITestUtils;
import it.fulminazzo.yagl.viewers.Viewer;
import it.fulminazzo.yagl.wrappers.Sound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GUIManagerTest {

    @Test
    void testGetOpenGUIViewerPlayer() {
        BukkitUtils.setupServer();
        Player player = BukkitUtils.addPlayer(UUID.randomUUID(), "Alex");
        GUITestUtils.mockPlugin(p ->
                assertFalse(GUIManager.getOpenGUIViewer(player).isPresent(), "Should not be present"));
    }

    @Test
    void testGetOpenGUIViewerUUID() {
        BukkitUtils.setupServer();
        GUITestUtils.mockPlugin(p ->
                assertFalse(GUIManager.getOpenGUIViewer(UUID.randomUUID()).isPresent(), "Should not be present"));
    }

    @Nested
    class EventsTest {
        private GUIManager guiManager;
        private Player player;
        private GUI expected;

        @BeforeEach
        void setUp() {
            BukkitUtils.setupServer();
            try {
               GUIManager.getInstance(GUIManager.class).terminate();
            } catch (InstanceNotInitializedException ignored) {}
            this.guiManager = new GUIManager();

            Server server = Bukkit.getServer();
            when(server.getPluginManager()).thenReturn(mock(PluginManager.class));
            when(server.isPrimaryThread()).thenReturn(true);

            this.player = BukkitUtils.addPlayer(UUID.randomUUID(), "Alex");
            when(this.player.isOnline()).thenReturn(true);
            when(this.player.getServer()).thenReturn(server);

            this.expected = GUI.newGUI(9)
                    .setTitle("Hello world")
                    .addContent(Item.newItem("stone").setDisplayName("test"));

            GUIAdapter.openGUI(this.expected, GUIManager.getViewer(this.player));
        }

        @AfterEach
        void tearDown() {
            this.guiManager.terminate();
        }

        @Test
        void testChangeGUI() {
            AtomicBoolean expected = new AtomicBoolean(false);
            AtomicBoolean notExpected = new AtomicBoolean(false);
            this.expected
                    .onChangeGUI((v, g1, g2) -> expected.set(true))
                    .onCloseGUI((v, g) -> notExpected.set(true));
            doAnswer(a -> {
                InventoryCloseEvent event = new InventoryCloseEvent(getView().getWrapped());
                this.guiManager.on(event);
                return null;
            }).when(this.player).closeInventory();

            Viewer viewer = GUIManager.getViewer(this.player);
            this.expected.open(viewer);

            assertTrue(expected.get(), "Change GUI was not invoked");
            assertFalse(notExpected.get(), "Close GUI should not invoked");
            assertEquals(this.expected, viewer.getPreviousGUI());
        }

        @Test
        void testClickAction() {
            AtomicBoolean expected = new AtomicBoolean(false);
            this.expected.getContents(0).forEach(e -> e.onClickItem((v, g, i) -> expected.set(true)));

            InventoryViewWrapper view = getView();
            InventoryClickEvent event = new InventoryClickEvent(view.getWrapped(), InventoryType.SlotType.CONTAINER,
                    0, ClickType.LEFT, InventoryAction.CLONE_STACK);
            assertFalse(event.isCancelled(), "Event should not be cancelled when starting");
            this.guiManager.on(event);
            assertTrue(event.isCancelled(), "Event should be cancelled after being invoked");
            assertTrue(expected.get(), "Click action was not invoked");
        }

        @Test
        void testClickActionNull() {
            AtomicBoolean expected = new AtomicBoolean(false);
            this.expected.getContents(0).forEach(e -> e.onClickItem((v, g, i) -> expected.set(true)));

            InventoryViewWrapper view = getView();
            InventoryClickEvent event = new InventoryClickEvent(view.getWrapped(), InventoryType.SlotType.CONTAINER,
                    1, ClickType.LEFT, InventoryAction.CLONE_STACK);
            assertFalse(event.isCancelled(), "Event should not be cancelled when starting");
            this.guiManager.on(event);
            assertTrue(event.isCancelled(), "Event should be cancelled after being invoked");
            assertFalse(expected.get(), "Click action should not be invoked");
        }

        @Test
        void testClickActionOutside() {
            AtomicBoolean expected = new AtomicBoolean(false);
            this.expected.getContents(0).forEach(e -> e.onClickItem((v, g, i) -> expected.set(true)));

            InventoryViewWrapper view = getView();
            InventoryClickEvent event = new InventoryClickEvent(view.getWrapped(), InventoryType.SlotType.CONTAINER,
                    this.expected.size() + 1, ClickType.LEFT, InventoryAction.CLONE_STACK);
            assertFalse(event.isCancelled(), "Event should not be cancelled when starting");
            this.guiManager.on(event);
            assertFalse(event.isCancelled(), "Event should not be cancelled after being invoked");
            assertFalse(expected.get(), "Click action should not be invoked");
        }

        @Test
        void testNullClickActionSound() {
            Sound sound = new Sound("pirate", 1f, 3f);
            Location location = new Location(null, 0, 1, 0);
            when(this.player.getLocation()).thenReturn(location);
            this.expected.getContents(0).forEach(c -> c.setClickSound(sound));

            InventoryViewWrapper view = getView();
            InventoryClickEvent event = new InventoryClickEvent(view.getWrapped(), InventoryType.SlotType.CONTAINER,
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

            InventoryViewWrapper view = getView();
            InventoryClickEvent event = new InventoryClickEvent(view.getWrapped(), InventoryType.SlotType.CONTAINER,
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

            InventoryViewWrapper view = getView();
            InventoryClickEvent event = new InventoryClickEvent(view.getWrapped(), InventoryType.SlotType.CONTAINER,
                    -1, ClickType.LEFT, InventoryAction.CLONE_STACK);
            assertFalse(event.isCancelled(), "Event should not be cancelled when starting");
            this.guiManager.on(event);
            assertFalse(event.isCancelled(), "Event should not be cancelled after being invoked");
            assertFalse(notExpected.get(), "Click action should not be invoked");
            assertTrue(expected.get(), "Outside action was not invoked");
        }

        @Test
        void testDragEvent() {
            InventoryViewWrapper view = getView();
            InventoryDragEvent event = new InventoryDragEvent(view.getWrapped(), null, new ItemStack(Material.STONE),
                    false, new HashMap<>());
            assertFalse(event.isCancelled(), "Event should not be cancelled when starting");
            this.guiManager.on(event);
            assertTrue(event.isCancelled(), "Event should be cancelled after being invoked");
        }

        @Test
        void testCloseEvent() {
            AtomicBoolean expected = new AtomicBoolean(false);
            this.expected.onCloseGUI((v, g) -> expected.set(true));

            InventoryViewWrapper view = getView();

            this.guiManager.on(new InventoryCloseEvent(view.getWrapped()));

            assertTrue(expected.get(), "CloseGUI action was not invoked");
        }

        @Test
        void testDisableThisPlugin() {
            GUITestUtils.mockPlugin(p -> {
                PluginDisableEvent event = new PluginDisableEvent(p);
                this.guiManager.on(event);
                verify(this.player).closeInventory();
            });
            this.guiManager.initialize();
        }

        @Test
        void testDisableNotThisPlugin() {
            GUITestUtils.mockPlugin(p -> {
                PluginDisableEvent event = new PluginDisableEvent(mock(JavaPlugin.class));
                this.guiManager.on(event);
                verify(this.player, never()).closeInventory();
            });
        }

        private @NotNull InventoryViewWrapper getView() {
            MockInventory inventory = new MockInventory(9);
            return new InventoryViewWrapper(inventory, this.player, "");
        }

    }

}