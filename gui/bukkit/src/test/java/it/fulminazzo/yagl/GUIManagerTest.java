package it.fulminazzo.yagl;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.jbukkit.BukkitUtils;
import it.fulminazzo.jbukkit.inventory.MockInventory;
import it.fulminazzo.jbukkit.inventory.MockPlayerInventory;
import it.fulminazzo.yagl.exception.InstanceNotInitializedException;
import it.fulminazzo.yagl.gui.GUI;
import it.fulminazzo.yagl.handler.AnvilRenameHandler;
import it.fulminazzo.yagl.item.Item;
import it.fulminazzo.yagl.testing.InventoryViewWrapper;
import it.fulminazzo.yagl.util.BukkitTestUtils;
import it.fulminazzo.yagl.viewer.Viewer;
import it.fulminazzo.yagl.wrapper.Sound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GUIManagerTest {

    @BeforeAll
    static void setAllUp() {
        BukkitUtils.setupServer();
        new ArrayList<>(Bukkit.getOnlinePlayers()).forEach(BukkitUtils::removePlayer);
    }

    @Test
    void testInitializationOfGUIManagerInjectsOnlinePlayers() {
        Player player = BukkitUtils.addPlayer(UUID.randomUUID(), "fulminazzo");
        BukkitTestUtils.mockPluginAndNMSUtils((p, c) -> {
            try {
                GUIManager.getInstance(GUIManager.class).terminate();
            } catch (InstanceNotInitializedException ignored) {
            }
            new GUIManager();

            verify(c.pipeline()).addBefore(
                    eq("packet_handler"),
                    any(),
                    any()
            );
        });
        BukkitUtils.removePlayer(player);
    }

    @Test
    void testGetOpenGUIViewerPlayer() {
        Player player = BukkitUtils.addPlayer(UUID.randomUUID(), "Alex");
        BukkitTestUtils.mockPlugin(p ->
                assertFalse(GUIManager.getOpenGUIViewer(player).isPresent(), "Should not be present"));
        BukkitUtils.removePlayer(player);
    }

    @Test
    void testGetOpenGUIViewerUUID() {
        BukkitTestUtils.mockPlugin(p ->
                assertFalse(GUIManager.getOpenGUIViewer(UUID.randomUUID()).isPresent(), "Should not be present"));
    }

    @Test
    void testExecuteUnsafeEventNormal() {
        BukkitTestUtils.mockPlugin(p -> {
            Logger logger = mock(Logger.class);
            when(p.getLogger()).thenReturn(logger);

            Player player = BukkitUtils.addPlayer(UUID.randomUUID(), "fulminazzo");

            Viewer viewer = GUIManager.getViewer(player);
            new Refl<>(viewer).setFieldObject("openGUI", GUI.newGUI(9));

            RuntimeException exception = new IllegalArgumentException("An error occurred");

            assertDoesNotThrow(() ->
                    GUIManager.executeUnsafeEvent(
                            new InventoryEvent(null),
                            player,
                            () -> {
                                throw exception;
                            }
                    )
            );

            verify(logger).log(eq(Level.WARNING), any(), eq(exception));

            BukkitUtils.removePlayer(player);
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {
            // nextGUI size:stored
            "0:false",
            "0:true",
            "9:false",
            "9:true",
    })
    void testExecuteUnsafeEventSevere(String data) {
        String[] tmp = data.split(":");
        int size = Integer.parseInt(tmp[0]);
        boolean stored = Boolean.parseBoolean(tmp[1]);

        BukkitTestUtils.mockPlugin(p -> {
            Logger logger = mock(Logger.class);
            when(p.getLogger()).thenReturn(logger);

            Player player = BukkitUtils.addPlayer(UUID.randomUUID(), "fulminazzo");
            PlayerInventory inventory = new MockPlayerInventory(player);
            when(player.getInventory()).thenReturn(inventory);
            inventory.setItem(0, new ItemStack(Material.STONE));

            if (stored) {
                PlayersInventoryCache inventoryCache = new Refl<>(GUIManager.getInstance())
                        .getFieldObject("inventoryCache");
                inventoryCache.storePlayerContents(player);
                inventory.clear();
            }

            Viewer viewer = GUIManager.getViewer(player);
            GUI nextGUI = size == 0 ? null : GUI.newGUI(size);
            new Refl<>(viewer).setFieldObject("nextGUI", nextGUI);

            RuntimeException exception = new IllegalArgumentException("An error occurred");

            assertDoesNotThrow(() ->
                    GUIManager.executeUnsafeEvent(
                            new InventoryEvent(null),
                            player,
                            () -> {
                                throw exception;
                            }
                    )
            );

            verify(logger).log(eq(Level.SEVERE), any(), eq(exception));
            verify(player).closeInventory();

            assertEquals(new ItemStack(Material.STONE), inventory.getItem(0));

            BukkitUtils.removePlayer(player);
        });
    }

    @Nested
    class EventsTest {
        private GUIManager guiManager;
        private Player player;
        private GUI expected;

        @BeforeEach
        void setUp() {
            new ArrayList<>(Bukkit.getOnlinePlayers()).forEach(BukkitUtils::removePlayer);
            try {
                GUIManager.getInstance(GUIManager.class).terminate();
            } catch (InstanceNotInitializedException ignored) {
            }
            this.guiManager = new GUIManager();

            Server server = Bukkit.getServer();
            when(server.getPluginManager()).thenReturn(mock(PluginManager.class));
            when(server.isPrimaryThread()).thenReturn(true);

            BukkitScheduler scheduler = mock(BukkitScheduler.class);
            when(scheduler.runTask(any(), any(Runnable.class))).thenAnswer(a -> {
                ((Runnable) a.getArguments()[1]).run();
                return null;
            });
            when(server.getScheduler()).thenReturn(scheduler);

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
            BukkitUtils.removePlayer(this.player);
        }

        @Test
        void testOnJoinPlayerIsInjectedAnvilRenameHandler() {
            BukkitTestUtils.mockPluginAndNMSUtils((p, c) -> {
                PlayerJoinEvent event = new PlayerJoinEvent(
                        this.player,
                        null
                );

                this.guiManager.on(event);

                verify(c.pipeline()).addBefore(
                        eq("packet_handler"),
                        any(),
                        any()
                );
            });
        }

        @Test
        void testOnQuitPlayerIsRemovedAnvilRenameHandler() {
            BukkitTestUtils.mockPluginAndNMSUtils((p, c) -> {
                AnvilRenameHandler handler = new AnvilRenameHandler(
                        this.player.getUniqueId(),
                        null
                );

                new Refl<>(this.guiManager)
                        .getFieldRefl("anvilRenameHandlers")
                        .invokeMethod("add", handler);

                PlayerQuitEvent event = new PlayerQuitEvent(
                        this.player,
                        null
                );

                this.guiManager.on(event);

                verify(c.pipeline()).remove(any(String.class));
            });
        }

        @Test
        void testOnQuitPlayerDoesNotThrowIfNoAnvilHandlerIsPresent() {
            assertDoesNotThrow(() -> {
                PlayerQuitEvent event = new PlayerQuitEvent(
                        this.player,
                        null
                );

                this.guiManager.on(event);
            });
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
        void testContentsRestoredOnCloseEvent() {
            BukkitTestUtils.mockPlugin(p -> {
                ItemStack expected = new ItemStack(Material.STONE);

                PlayerInventory inventory = new MockPlayerInventory(this.player);
                inventory.setItem(0, expected);
                when(this.player.getInventory()).thenReturn(inventory);

                PlayersInventoryCache cache = this.guiManager.getInventoryCache();
                cache.storePlayerContents(this.player);

                inventory.clear();
                ItemStack itemStack = inventory.getItem(0);
                assertNull(itemStack);

                this.guiManager.on(new InventoryCloseEvent(getView().getWrapped()));

                assertFalse(cache.areContentsStored(this.player));

                itemStack = inventory.getItem(0);
                assertNotNull(itemStack);
                assertEquals(expected, itemStack);
            });
        }

        @Test
        void testContentsNotRestoredOnCloseEventAndNextGUIPresent() {
            BukkitTestUtils.mockPlugin(p -> {
                Viewer viewer = GUIManager.getViewer(this.player);
                new Refl<>(viewer).setFieldObject("nextGUI", GUI.newGUI(9));

                ItemStack expected = new ItemStack(Material.STONE);

                PlayerInventory inventory = new MockPlayerInventory(this.player);
                inventory.setItem(0, expected);
                when(this.player.getInventory()).thenReturn(inventory);

                PlayersInventoryCache cache = this.guiManager.getInventoryCache();
                cache.storePlayerContents(this.player);

                inventory.clear();
                ItemStack itemStack = inventory.getItem(0);
                assertNull(itemStack);

                this.guiManager.on(new InventoryCloseEvent(getView().getWrapped()));

                assertTrue(cache.areContentsStored(this.player));

                itemStack = inventory.getItem(0);
                assertNull(itemStack);
            });
        }

        @Test
        void testCloseEvent() {
            BukkitTestUtils.mockPlugin(p -> {
                AtomicBoolean expected = new AtomicBoolean(false);
                this.expected.onCloseGUI((v, g) -> expected.set(true));

                InventoryViewWrapper view = getView();

                this.guiManager.on(new InventoryCloseEvent(view.getWrapped()));

                assertTrue(expected.get(), "CloseGUI action was not invoked");
            });
        }

        @Test
        void testDisableThisPlugin() {
            BukkitTestUtils.mockPluginAndNMSUtils((p, c) -> {
                AnvilRenameHandler handler = new AnvilRenameHandler(
                        this.player.getUniqueId(),
                        null
                );

                new Refl<>(this.guiManager)
                        .getFieldRefl("anvilRenameHandlers")
                        .invokeMethod("add", handler);

                PluginDisableEvent event = new PluginDisableEvent(p);
                this.guiManager.on(event);
                verify(this.player).closeInventory();

                verify(c.pipeline()).remove(any(String.class));
            });
            this.guiManager.initialize();
        }

        @Test
        void testDisableNotThisPlugin() {
            BukkitTestUtils.mockPlugin(p -> {
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
