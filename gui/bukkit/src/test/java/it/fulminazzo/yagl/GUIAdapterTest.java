package it.fulminazzo.yagl;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.structures.tuples.Tuple;
import it.fulminazzo.jbukkit.BukkitUtils;
import it.fulminazzo.jbukkit.inventory.MockInventoryView;
import it.fulminazzo.jbukkit.inventory.MockPlayerInventory;
import it.fulminazzo.yagl.actions.GUIAction;
import it.fulminazzo.yagl.actions.commands.GUICommand;
import it.fulminazzo.yagl.actions.commands.GUIConsoleCommand;
import it.fulminazzo.yagl.actions.commands.GUIItemCommand;
import it.fulminazzo.yagl.actions.commands.GUIItemConsoleCommand;
import it.fulminazzo.yagl.contents.GUIContent;
import it.fulminazzo.yagl.contents.ItemGUIContent;
import it.fulminazzo.yagl.guis.*;
import it.fulminazzo.yagl.items.Item;
import it.fulminazzo.yagl.utils.BukkitTestUtils;
import it.fulminazzo.yagl.viewers.PlayerOfflineException;
import it.fulminazzo.yagl.viewers.Viewer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GUIAdapterTest {
    private Player player;
    private Inventory inventory;

    @BeforeEach
    void setUp() {
        BukkitUtils.setupServer();

        Server server = Bukkit.getServer();
        when(server.getPluginManager()).thenReturn(mock(PluginManager.class));
        when(server.isPrimaryThread()).thenReturn(true);

        this.player = BukkitUtils.addPlayer(UUID.randomUUID(), "Alex");
        when(this.player.isOnline()).thenReturn(true);
        when(this.player.openInventory(any(Inventory.class)))
                .thenAnswer(a -> {
                    this.inventory = a.getArgument(0);
                    return null;
                });
        when(this.player.getServer()).thenReturn(server);
    }

    @AfterEach
    void tearDown() {
        GUIManager.getInstance().terminate();
    }

    private static Object[] pageableFullSizeGUIParameters() {
        return Stream.concat(
                Stream.of(9, 18, 27, 36, 45, 54),
                Arrays.stream(GUIType.values())
        ).toArray(Object[]::new);
    }

    @Test
    void integrationTestSearchGUI() {
        BukkitTestUtils.mockPluginAndNMSUtils((p, c) -> {
            PlayerInventory playerInventory = new MockPlayerInventory(this.player);
            when(this.player.getInventory()).thenReturn(playerInventory);

            new MockInventoryView(
                    mock(Inventory.class),
                    this.player,
                    ""
            );

            Viewer viewer = GUIManager.getViewer(this.player);

            List<Material> materials = Arrays.asList(
                    Material.POTATO, Material.DIAMOND, Material.REDSTONE,
                    Material.STONE, Material.COBBLESTONE, Material.EMERALD,
                    Material.STICK, Material.GRASS, Material.DIRT
            );

            SearchGUI<Material> gui = SearchGUI.newGUI(18,
                            m -> ItemGUIContent.newInstance(m.name()),
                            (m, s) -> m.name().toLowerCase().contains(s.toLowerCase()),
                            materials
                    )
                    .setTitle("Update")
                    .setBottomSide(ItemGUIContent.newInstance(Material.GLASS.name()));

            gui.open(viewer);

            for (int i = 9; i < 9 + 9; i++) {
                ItemStack itemStack = playerInventory.getItem(i);
                assertNotNull(itemStack,
                        "ItemStack at player inventory slot " + i + " should be not null");
                assertEquals(materials.get(i - 9), itemStack.getType(),
                        "ItemStack at player inventory slot " + i + " does not match expected item");
            }

            for (int i = 18; i < 27; i++) {
                ItemStack itemStack = playerInventory.getItem(i);
                assertNotNull(itemStack,
                        "ItemStack at player inventory slot " + i + " should be not null");
                assertEquals(Material.GLASS, itemStack.getType(),
                        "ItemStack at player inventory slot " + i + " does not match expected item");
            }

            c.pipeline().fireChannelRead(new PacketPlayInItemName("stone"));

            List<Material> expected = Arrays.asList(
                    Material.REDSTONE,
                    Material.STONE, Material.COBBLESTONE
            );
            for (int i = 9; i < 12; i++) {
                ItemStack itemStack = playerInventory.getItem(i);
                assertNotNull(itemStack,
                        "ItemStack at player inventory slot " + i + " should be not null");
                assertEquals(expected.get(i - 9), itemStack.getType(),
                        "ItemStack at player inventory slot " + i + " does not match expected item");
            }
            for (int i = 12; i < 9 + 9; i++) {
                ItemStack itemStack = playerInventory.getItem(i);
                assertNull(itemStack,
                        "ItemStack at player inventory slot " + i + " should be null");
            }

            for (int i = 18; i < 27; i++) {
                ItemStack itemStack = playerInventory.getItem(i);
                assertNotNull(itemStack,
                        "ItemStack at player inventory slot " + i + " should be not null");
                assertEquals(Material.GLASS, itemStack.getType(),
                        "ItemStack at player inventory slot " + i + " does not match expected item");
            }
        });
    }

    @Test
    void integrationTestSearchGUIWithSameSearch() {
        BukkitTestUtils.mockPluginAndNMSUtils((p, c) -> {
            PlayerInventory playerInventory = new MockPlayerInventory(this.player);
            when(this.player.getInventory()).thenReturn(playerInventory);

            Viewer viewer = GUIManager.getViewer(this.player);

            List<Material> materials = Arrays.asList(
                    Material.POTATO, Material.DIAMOND, Material.REDSTONE,
                    Material.STONE, Material.COBBLESTONE, Material.EMERALD,
                    Material.STICK, Material.GRASS, Material.DIRT
            );

            SearchGUI<Material> gui = SearchGUI.newGUI(18,
                            m -> ItemGUIContent.newInstance(m.name()),
                            (m, s) -> m.name().toLowerCase().contains(s.toLowerCase()),
                            materials
                    )
                    .setBottomSide(ItemGUIContent.newInstance(Material.GLASS.name()));

            gui.open(viewer);

            for (int i = 9; i < 9 + 9; i++) {
                ItemStack itemStack = playerInventory.getItem(i);
                assertNotNull(itemStack,
                        "ItemStack at player inventory slot " + i + " should be not null");
                assertEquals(materials.get(i - 9), itemStack.getType(),
                        "ItemStack at player inventory slot " + i + " does not match expected item");
            }

            for (int i = 18; i < 27; i++) {
                ItemStack itemStack = playerInventory.getItem(i);
                assertNotNull(itemStack,
                        "ItemStack at player inventory slot " + i + " should be not null");
                assertEquals(Material.GLASS, itemStack.getType(),
                        "ItemStack at player inventory slot " + i + " does not match expected item");
            }

            c.pipeline().fireChannelRead(new PacketPlayInItemName(""));

            for (int i = 9; i < 9 + 9; i++) {
                ItemStack itemStack = playerInventory.getItem(i);
                assertNotNull(itemStack,
                        "ItemStack at player inventory slot " + i + " should be not null");
                assertEquals(materials.get(i - 9), itemStack.getType(),
                        "ItemStack at player inventory slot " + i + " does not match expected item");
            }

            for (int i = 18; i < 27; i++) {
                ItemStack itemStack = playerInventory.getItem(i);
                assertNotNull(itemStack,
                        "ItemStack at player inventory slot " + i + " should be not null");
                assertEquals(Material.GLASS, itemStack.getType(),
                        "ItemStack at player inventory slot " + i + " does not match expected item");
            }
        });
    }

    @Test
    void testAnvilPacketOnNonSearchGUI() {
        BukkitTestUtils.mockPluginAndNMSUtils((p, c) -> {
            PlayerInventory playerInventory = new MockPlayerInventory(this.player);
            when(this.player.getInventory()).thenReturn(playerInventory);

            Viewer viewer = GUIManager.getViewer(this.player);

            GUI gui = GUI.newGUI(9).setContents(0, ItemGUIContent.newInstance("diamond"));
            gui.open(viewer);

            ItemStack firstItemStack = this.inventory.getItem(0);
            assertNotNull(firstItemStack,
                    "ItemStack at player inventory slot 0 should be not null");
            assertEquals(Material.DIAMOND, firstItemStack.getType(),
                    "ItemStack at player inventory slot 0 does not match expected item");

            c.pipeline().fireChannelRead(new PacketPlayInItemName("stone"));

            ItemStack secondItemStack = this.inventory.getItem(0);
            assertNotNull(secondItemStack,
                    "ItemStack at player inventory slot 0 should be not null");
            assertEquals(Material.DIAMOND, secondItemStack.getType(),
                    "ItemStack at player inventory slot 0 does not match expected item");
        });
    }

    @Test
    void testUpdatePlayerGUI() {
        BukkitTestUtils.mockPlugin(p -> {
            PlayerInventory playerInventory = new MockPlayerInventory(this.player);
            when(this.player.getInventory()).thenReturn(playerInventory);

            new MockInventoryView(
                    mock(Inventory.class),
                    this.player,
                    ""
            );

            Viewer viewer = GUIManager.getViewer(this.player);

            FullSizeGUI gui = GUI.newFullSizeGUI(9);
            gui.setContents(9, ItemGUIContent.newInstance("diamond"));

            gui.open(viewer);

            ItemStack firstSlotItemStack1 = this.player.getInventory().getItem(9);
            assertNotNull(firstSlotItemStack1,
                    "ItemStack at slot 9 in player inventory should not be null before update");
            assertEquals(Material.DIAMOND, firstSlotItemStack1.getType(),
                    "ItemStack at slot 9 in player inventory should be diamond before update");

            gui.setContents(10, ItemGUIContent.newInstance("emerald"));

            gui.update(viewer);

            ItemStack firstSlotItemStack2 = this.player.getInventory().getItem(9);
            assertNotNull(firstSlotItemStack2,
                    "ItemStack at slot 9 in player inventory should not be null after update");
            assertEquals(Material.DIAMOND, firstSlotItemStack2.getType(),
                    "ItemStack at slot 9 in player inventory should be diamond after update");

            ItemStack secondSlotItemStack = this.player.getInventory().getItem(10);
            assertNotNull(secondSlotItemStack,
                    "ItemStack at slot 10 in player inventory should not be null after update");
            assertEquals(Material.EMERALD, secondSlotItemStack.getType(),
                    "ItemStack at slot 10 in player inventory should be emerald after update");
        });
    }

    @ParameterizedTest
    @MethodSource("pageableFullSizeGUIParameters")
    void testOpenPageableFullSizeGUI(Object initializer) {
        BukkitTestUtils.mockPlugin(p -> {
            PlayerInventory playerInventory = new MockPlayerInventory(this.player);
            when(this.player.getInventory()).thenReturn(playerInventory);

            final PageableGUI gui;
            if (initializer instanceof Integer)
                gui = PageableGUI.newFullSizeGUI((Integer) initializer);
            else if (initializer instanceof GUIType)
                gui = PageableGUI.newFullSizeGUI((GUIType) initializer);
            else throw new IllegalArgumentException(initializer.toString());
            gui.setPages(2);

            int previousPageSlot = gui.south() - 2;
            gui.setPreviousPage(previousPageSlot, ItemGUIContent.newInstance("book"));

            int nextPageSlot = gui.south() + 2;
            gui.setNextPage(nextPageSlot, ItemGUIContent.newInstance("book"));

            gui.getPage(0)
                    .setContents(0, ItemGUIContent.newInstance("diamond"))
                    .setContents(gui.southWest(), ItemGUIContent.newInstance("diamond"));

            gui.getPage(1)
                    .setContents(0, ItemGUIContent.newInstance("emerald"))
                    .setContents(gui.southWest(), ItemGUIContent.newInstance("emerald"));

            gui.open(GUIManager.getViewer(this.player));

            ItemStack firstStack = this.inventory.getItem(0);
            assertNotNull(firstStack, "ItemStack on first slot of inventory was supposed to be not null");
            assertEquals(Material.DIAMOND, firstStack.getType());

            ItemStack firstPlayerStack = playerInventory.getItem(0);
            assertNotNull(firstPlayerStack, "ItemStack on first slot of player inventory was supposed to be not null");
            assertEquals(Material.DIAMOND, firstPlayerStack.getType());

            @NotNull Tuple<Viewer, GUI> firstOpenGUI = GUIManager.getOpenGUIViewer(this.player);
            assertTrue(firstOpenGUI.isPresent(), "Player should have an open GUI");

            GUIContent nextPageContent = firstOpenGUI.getValue().getContent(firstOpenGUI.getKey(), nextPageSlot);
            assertNotNull(nextPageContent, "Next page item should not be null");
            nextPageContent.clickItemAction().ifPresent(a -> a.execute(firstOpenGUI.getKey(), firstOpenGUI.getValue(), nextPageContent));

            ItemStack secondStack = this.inventory.getItem(0);
            assertNotNull(secondStack, "ItemStack on first slot of inventory was supposed to be not null");
            assertEquals(Material.EMERALD, secondStack.getType());

            ItemStack secondPlayerStack = playerInventory.getItem(0);
            assertNotNull(secondPlayerStack, "ItemStack on first slot of player inventory was supposed to be not null");
            assertEquals(Material.EMERALD, secondPlayerStack.getType());

            @NotNull Tuple<Viewer, GUI> secondOpenGUI = GUIManager.getOpenGUIViewer(this.player);
            assertTrue(secondOpenGUI.isPresent(), "Player should have an open GUI");

            GUIContent previousPageContent = secondOpenGUI.getValue().getContent(secondOpenGUI.getKey(), previousPageSlot);
            assertNotNull(previousPageContent, "Previous page item should not be null");
            previousPageContent.clickItemAction().ifPresent(a -> a.execute(secondOpenGUI.getKey(), secondOpenGUI.getValue(), previousPageContent));

            ItemStack thirdStack = this.inventory.getItem(0);
            assertNotNull(thirdStack, "ItemStack on first slot of inventory was supposed to be not null");
            assertEquals(Material.DIAMOND, thirdStack.getType());

            ItemStack thirdPlayerStack = playerInventory.getItem(0);
            assertNotNull(thirdPlayerStack, "ItemStack on first slot of player inventory was supposed to be not null");
            assertEquals(Material.DIAMOND, thirdPlayerStack.getType());
        });
    }

    @ParameterizedTest
    @MethodSource("pageableFullSizeGUIParameters")
    void testOpenPageableResizedFullSizeGUI(Object initializer) {
        BukkitTestUtils.mockPlugin(p -> {
            PlayerInventory playerInventory = new MockPlayerInventory(this.player);
            playerInventory.setItem(0, new ItemStack(Material.DIAMOND_SWORD));
            playerInventory.setItem(27, new ItemStack(Material.GOLDEN_APPLE));
            when(this.player.getInventory()).thenReturn(playerInventory);

            final PageableGUI gui;
            if (initializer instanceof Integer)
                gui = PageableGUI.newFullSizeGUI((Integer) initializer, 18);
            else if (initializer instanceof GUIType)
                gui = PageableGUI.newFullSizeGUI((GUIType) initializer, 18);
            else throw new IllegalArgumentException(initializer.toString());
            gui.setPages(2);

            int previousPageSlot = gui.south() - 2;
            gui.setPreviousPage(previousPageSlot, ItemGUIContent.newInstance("book"));

            int nextPageSlot = gui.south() + 2;
            gui.setNextPage(nextPageSlot, ItemGUIContent.newInstance("book"));

            gui.getPage(0)
                    .setContents(0, ItemGUIContent.newInstance("diamond"))
                    .setContents(gui.southWest(), ItemGUIContent.newInstance("diamond"));

            gui.getPage(1)
                    .setContents(0, ItemGUIContent.newInstance("emerald"))
                    .setContents(gui.southWest(), ItemGUIContent.newInstance("emerald"));

            gui.open(GUIManager.getViewer(this.player));

            ItemStack firstStack = this.inventory.getItem(0);
            assertNotNull(firstStack, "ItemStack on first slot of inventory was supposed to be not null");
            assertEquals(Material.DIAMOND, firstStack.getType());

            ItemStack firstPlayerStack = playerInventory.getItem(18);
            assertNotNull(firstPlayerStack, "ItemStack on 18th slot of player inventory was supposed to be not null");
            assertEquals(Material.DIAMOND, firstPlayerStack.getType());

            ItemStack firstHotbarStack = playerInventory.getItem(0);
            assertNotNull(firstHotbarStack, "ItemStack on first slot of player inventory was supposed to be not null");
            assertEquals(Material.DIAMOND_SWORD, firstHotbarStack.getType());

            ItemStack firstStorageStack = playerInventory.getItem(27);
            assertNotNull(firstStorageStack, "ItemStack on 27th slot of player inventory was supposed to be not null");
            assertEquals(Material.GOLDEN_APPLE, firstStorageStack.getType());

            @NotNull Tuple<Viewer, GUI> firstOpenGUI = GUIManager.getOpenGUIViewer(this.player);
            assertTrue(firstOpenGUI.isPresent(), "Player should have an open GUI");

            GUIContent nextPageContent = firstOpenGUI.getValue().getContent(firstOpenGUI.getKey(), nextPageSlot);
            assertNotNull(nextPageContent, "Next page item should not be null");
            nextPageContent.clickItemAction().ifPresent(a -> a.execute(firstOpenGUI.getKey(), firstOpenGUI.getValue(), nextPageContent));

            ItemStack secondStack = this.inventory.getItem(0);
            assertNotNull(secondStack, "ItemStack on first slot of inventory was supposed to be not null");
            assertEquals(Material.EMERALD, secondStack.getType());

            ItemStack secondPlayerStack = playerInventory.getItem(18);
            assertNotNull(secondPlayerStack, "ItemStack on 18th slot of player inventory was supposed to be not null");
            assertEquals(Material.EMERALD, secondPlayerStack.getType());

            ItemStack secondHotbarStack = playerInventory.getItem(0);
            assertNotNull(secondHotbarStack, "ItemStack on first slot of player inventory was supposed to be not null");
            assertEquals(Material.DIAMOND_SWORD, secondHotbarStack.getType());

            ItemStack secondStorageStack = playerInventory.getItem(27);
            assertNotNull(secondStorageStack, "ItemStack on 27th slot of player inventory was supposed to be not null");
            assertEquals(Material.GOLDEN_APPLE, secondStorageStack.getType());

            @NotNull Tuple<Viewer, GUI> secondOpenGUI = GUIManager.getOpenGUIViewer(this.player);
            assertTrue(secondOpenGUI.isPresent(), "Player should have an open GUI");

            GUIContent previousPageContent = secondOpenGUI.getValue().getContent(secondOpenGUI.getKey(), previousPageSlot);
            assertNotNull(previousPageContent, "Previous page item should not be null");
            previousPageContent.clickItemAction().ifPresent(a -> a.execute(secondOpenGUI.getKey(), secondOpenGUI.getValue(), previousPageContent));

            ItemStack thirdStack = this.inventory.getItem(0);
            assertNotNull(thirdStack, "ItemStack on first slot of inventory was supposed to be not null");
            assertEquals(Material.DIAMOND, thirdStack.getType());

            ItemStack thirdPlayerStack = playerInventory.getItem(18);
            assertNotNull(thirdPlayerStack, "ItemStack on 18th slot of player inventory was supposed to be not null");
            assertEquals(Material.DIAMOND, thirdPlayerStack.getType());

            ItemStack thirdHotbarStack = playerInventory.getItem(0);
            assertNotNull(thirdHotbarStack, "ItemStack on first slot of player inventory was supposed to be not null");
            assertEquals(Material.DIAMOND_SWORD, thirdHotbarStack.getType());

            ItemStack thirdStorageStack = playerInventory.getItem(27);
            assertNotNull(thirdStorageStack, "ItemStack on 27th slot of player inventory was supposed to be not null");
            assertEquals(Material.GOLDEN_APPLE, thirdStorageStack.getType());
        });
    }

    @Test
    void testOpenFullSizeGUI() {
        BukkitTestUtils.mockPlugin(p -> {
            PlayerInventory playerInventory = new MockPlayerInventory(this.player);
            when(this.player.getInventory()).thenReturn(playerInventory);

            String[] materials = new String[]{
                    "stone", "grass_block", "dirt", "cobblestone", "oak_planks",
                    "bedrock", "sand", "gravel", "gold_ore", "iron_ore",
                    "coal_ore", "oak_log", "oak_leaves", "glass", "lapis_ore",
                    "lapis_block", "dispenser", "sandstone", "gold_block", "iron_block",
                    "bricks", "tnt", "bookshelf", "mossy_cobblestone", "obsidian",
                    "torch", "fire", "water", "lava", "diamond_ore",
                    "diamond_block", "crafting_table", "furnace", "redstone_ore", "ice",
                    "cactus", "jukebox", "netherrack", "soul_sand", "glowstone",
                    "jack_o_lantern", "stone_bricks", "melon", "nether_bricks", "end_stone"
            };

            FullSizeGUI gui = GUI.newFullSizeGUI(9);
            gui.addContent(Arrays.stream(materials)
                    .map(ItemGUIContent::newInstance)
                    .toArray(ItemGUIContent[]::new));
            gui.open(GUIManager.getViewer(this.player));

            assertNotNull(this.inventory);
            assertEquals(9, this.inventory.getSize());

            for (int i = 0; i < 9; i++) {
                ItemStack itemStack = this.inventory.getItem(i);
                assertNotNull(itemStack, "Item at slot " + i + " was null");

                Material expected = Material.valueOf(materials[i].toUpperCase());
                assertEquals(expected, itemStack.getType(), "Item at slot " + i + " did not match expected type");
            }

            for (int i = 0; i < 27; i++) {
                int slot = i + 9;
                ItemStack itemStack = playerInventory.getItem(slot);
                assertNotNull(itemStack, "Item at slot " + slot + " was null");

                Material expected = Material.valueOf(materials[slot].toUpperCase());
                assertEquals(expected, itemStack.getType(), "Item at slot " + slot + " did not match expected type");
            }

            for (int i = 0; i < 9; i++) {
                ItemStack itemStack = playerInventory.getItem(i);
                assertNotNull(itemStack, "Item at slot " + i + " was null");

                Material expected = Material.valueOf(materials[i + 36].toUpperCase());
                assertEquals(expected, itemStack.getType(), "Item at slot " + i + " did not match expected type");
            }
        });
    }

    @Test
    void testCommandActionsReplaceVariables() {
        BukkitTestUtils.mockPlugin(p -> {
            GUI gui = GUI.newGUI(9).setContents(0,
                    ItemGUIContent.newInstance("stone")
                            .onClickItem(new GUIItemCommand("say <player_name> clicked the content"))
            ).onOpenGUI(new GUICommand("say Opening GUI for <player_name>"));

            GUIAdapter.openGUI(gui, GUIManager.getViewer(this.player));
            verify(Bukkit.getServer()).dispatchCommand(this.player, "say Opening GUI for Alex");

            @NotNull Tuple<Viewer, GUI> openGUI = GUIManager.getOpenGUIViewer(this.player);
            assertTrue(openGUI.isPresent());

            GUIContent content = openGUI.getValue().getContent(openGUI.getKey(), 0);
            assertNotNull(content);

            content.clickItemAction().ifPresent(a ->
                    a.execute(openGUI.getKey(), openGUI.getValue(), content)
            );

            verify(Bukkit.getServer()).dispatchCommand(this.player, "say Alex clicked the content");
        });
    }

    @Test
    void testConsoleCommandActionsReplaceVariables() {
        BukkitTestUtils.mockPlugin(p -> {
            Server server = Bukkit.getServer();
            ConsoleCommandSender console = mock(ConsoleCommandSender.class);
            when(server.getConsoleSender()).thenReturn(console);

            GUI gui = GUI.newGUI(9).setContents(0,
                    ItemGUIContent.newInstance("stone")
                            .onClickItem(new GUIItemConsoleCommand("say <player_name> clicked the content"))
            ).onOpenGUI(new GUIConsoleCommand("say Opening GUI for <player_name>"));

            GUIAdapter.openGUI(gui, GUIManager.getViewer(this.player));
            verify(server).dispatchCommand(console, "say Opening GUI for Alex");

            @NotNull Tuple<Viewer, GUI> openGUI = GUIManager.getOpenGUIViewer(this.player);
            assertTrue(openGUI.isPresent());

            GUIContent content = openGUI.getValue().getContent(openGUI.getKey(), 0);
            assertNotNull(content);

            content.clickItemAction().ifPresent(a ->
                    a.execute(openGUI.getKey(), openGUI.getValue(), content)
            );

            verify(server).dispatchCommand(console, "say Alex clicked the content");
        });
    }

    private static GUI[] guis() {
        return new GUI[]{
                GUI.newGUI(9)
                        .setTitle("Hello world")
                        .addContent(Item.newItem("stone").setDisplayName("test")),
                GUI.newGUI(9)
                        .setTitle(null)
                        .addContent(Item.newItem("stone").setDisplayName("test")),
                GUI.newGUI(GUIType.CHEST)
                        .setTitle("Hello world")
                        .addContent(Item.newItem("stone").setDisplayName("test")),
                GUI.newGUI(GUIType.CHEST)
                        .setTitle(null)
                        .addContent(Item.newItem("stone").setDisplayName("test")),
        };
    }

    @Test
    void testOpenInAsync() {
        Server server = Bukkit.getServer();
        BukkitScheduler scheduler = mock(BukkitScheduler.class);
        when(scheduler.runTask(any(), any(Runnable.class))).then(a -> {
            ((Runnable) a.getArguments()[1]).run();
            return null;
        });
        when(server.isPrimaryThread()).thenReturn(false);
        when(server.getScheduler()).thenReturn(scheduler);

        BukkitTestUtils.mockPlugin(p -> {
            GUI gui = GUI.newGUI(GUIType.CHEST);
            GUIAdapter.openGUI(gui, GUIManager.getViewer(this.player));

            verify(server.getScheduler()).runTask(eq(p), any(Runnable.class));
        });
    }

    @ParameterizedTest
    @MethodSource("guis")
    void testOpenGUI(GUI expected) {
        openGUI(expected);

        assertNotNull(this.inventory);
        assertEquals(expected.size(), this.inventory.getSize());
        assertEquals(expected.getTitle(), new Refl<>(this.inventory).getFieldObject("title"));

        for (int i = 0; i < expected.size(); i++) {
            @NotNull List<GUIContent> contents = expected.getContents(i);
            GUIContent guiContent = contents.isEmpty() ? null : contents.get(0);
            ItemStack actual = this.inventory.getItem(i);
            if (guiContent == null) assertNull(actual);
            else {
                Item item = new Refl<>(guiContent).getFieldObject("item");
                ItemStack exp = ItemAdapter.itemToItemStack(item);
                assertEquals(exp, actual);
            }
        }
    }

    @Test
    void testOpenGUIMeta() {
        Material playerHead;
        try {
            playerHead = Material.valueOf("PLAYER_HEAD");
        } catch (IllegalArgumentException e) {
            playerHead = Material.valueOf("SKULL");
        }
        Consumer<ItemMeta> setMetaUnbreakable = m -> {
            Refl<?> meta = new Refl<>(m);
            try {
                meta.invokeMethod("setUnbreakable", true);
            } catch (IllegalArgumentException e) {
                meta.invokeMethodRefl("spigot").invokeMethod("setUnbreakable", true);
            }
        };

        GUI gui = GUI.newGUI(GUIType.CHEST)
                .setTitle(null)
                .addContent(Item.newItem(playerHead.name()).setDisplayName("<player_name>"));
        BukkitTestUtils.mockPlugin(p -> GUIAdapter.openGUI(gui, GUIManager.getViewer(this.player), setMetaUnbreakable));

        assertNotNull(this.inventory);

        ItemStack expected = new ItemStack(playerHead);
        ItemMeta meta = expected.getItemMeta();
        meta.setDisplayName(this.player.getName());
        setMetaUnbreakable.accept(meta);
        expected.setItemMeta(meta);

        assertEquals(expected, this.inventory.getItem(0));
    }

    private static Object[][] metaClasses() {
        return new Object[][]{
                new Object[]{ItemMeta.class, (Consumer<ItemMeta>) m -> {
                }},
                new Object[]{null, (Consumer<ItemMeta>) m -> {
                }},
                new Object[]{ItemMeta.class, null},
                new Object[]{null, null}
        };
    }

    @ParameterizedTest
    @MethodSource("metaClasses")
    void testOpenGUIMetaAndClass(Class<ItemMeta> itemMetaClass, Consumer<ItemMeta> metaFunction) {
        GUI expected = GUI.newGUI(9).setContents(0, ItemGUIContent.newInstance("stone"));

        BukkitTestUtils.mockPlugin(p ->
                GUIAdapter.openGUI(expected, GUIManager.getViewer(this.player), itemMetaClass, metaFunction)
        );

        assertNotNull(this.inventory);
        assertEquals(expected.size(), this.inventory.getSize());
        assertEquals(expected.getTitle(), new Refl<>(this.inventory).getFieldObject("title"));

        for (int i = 0; i < expected.size(); i++) {
            @NotNull List<GUIContent> contents = expected.getContents(i);
            GUIContent guiContent = contents.isEmpty() ? null : contents.get(0);
            ItemStack actual = this.inventory.getItem(i);
            if (guiContent == null) assertNull(actual);
            else {
                Item item = new Refl<>(guiContent).getFieldObject("item");
                ItemStack exp = ItemAdapter.itemToItemStack(item);
                assertEquals(exp, actual);
            }
        }
    }

    @Test
    void testOpenGUIRequirements() {
        when(this.player.hasPermission(anyString())).thenReturn(false);

        GUI expected = GUI.newGUI(9)
                .setTitle("Hello world")
                .addContent(ItemGUIContent.newInstance("stone").setViewRequirements(v -> false),
                        ItemGUIContent.newInstance("diamond").setViewRequirements("permission"));
        openGUI(expected);

        assertNotNull(this.inventory);
        assertEquals(expected.size(), this.inventory.getSize());
        assertEquals(expected.getTitle(), new Refl<>(this.inventory).getFieldObject("title"));

        for (int i = 0; i < expected.size(); i++)
            assertNull(this.inventory.getItem(i));
    }

    @Test
    void testOpenGUIForOfflinePlayer() {
        BukkitUtils.removePlayer(this.player);
        assertThrowsExactly(PlayerOfflineException.class, () -> openGUI(GUI.newGUI(9)));
    }

    @Test
    void testCloseGUIForOfflinePlayer() {
        BukkitUtils.removePlayer(this.player);
        assertThrowsExactly(PlayerOfflineException.class, this::closeGUI);
    }

    @Test
    void testOpenGUIAction() {
        GUI gui = GUI.newGUI(9);
        AtomicBoolean executed = new AtomicBoolean(false);
        GUIAction openAction = (g, v) -> executed.set(true);
        gui.onOpenGUI(openAction);
        openGUI(gui);
        assertTrue(executed.get(), "openAction was not executed");
    }

    @Test
    void testUpdatePlayerGUINonFullSize() {
        BukkitTestUtils.mockPlugin(p -> {
            IllegalArgumentException exception = assertThrowsExactly(IllegalArgumentException.class, () ->
                    GUIAdapter.updatePlayerGUI(GUI.newGUI(9), GUIManager.getViewer(this.player))
            );
            assertEquals("updatePlayerGUI can only be used with FullSizeGUI", exception.getMessage());
        });
    }

    private void openGUI(GUI gui) {
        BukkitTestUtils.mockPlugin(p -> gui.open(GUIManager.getViewer(this.player)));
    }

    private void closeGUI() {
        BukkitTestUtils.mockPlugin(p -> GUIAdapter.closeGUI(GUIManager.getViewer(this.player)));
    }

    @Getter
    static class PacketPlayInItemName {
        private final String itemName;

        PacketPlayInItemName(String itemName) {
            this.itemName = itemName;
        }

    }

}