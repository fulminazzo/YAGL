package it.fulminazzo.yagl;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.structures.tuples.Tuple;
import it.fulminazzo.jbukkit.BukkitUtils;
import it.fulminazzo.jbukkit.inventory.MockPlayerInventory;
import it.fulminazzo.yagl.actions.GUIAction;
import it.fulminazzo.yagl.actions.commands.GUICommand;
import it.fulminazzo.yagl.actions.commands.GUIConsoleCommand;
import it.fulminazzo.yagl.actions.commands.GUIItemCommand;
import it.fulminazzo.yagl.actions.commands.GUIItemConsoleCommand;
import it.fulminazzo.yagl.contents.GUIContent;
import it.fulminazzo.yagl.contents.ItemGUIContent;
import it.fulminazzo.yagl.guis.FullSizeGUI;
import it.fulminazzo.yagl.guis.GUI;
import it.fulminazzo.yagl.guis.GUIType;
import it.fulminazzo.yagl.guis.PageableGUI;
import it.fulminazzo.yagl.items.Item;
import it.fulminazzo.yagl.utils.BukkitTestUtils;
import it.fulminazzo.yagl.viewers.PlayerOfflineException;
import it.fulminazzo.yagl.viewers.Viewer;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

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

    @ParameterizedTest
    @EnumSource(GUIType.class)
    void testOpenPageableFullSizeGUIType(GUIType type) {
        BukkitTestUtils.mockPlugin(p -> {
            PlayerInventory playerInventory = new MockPlayerInventory(this.player);
            when(this.player.getInventory()).thenReturn(playerInventory);

            PageableGUI gui = PageableGUI.newFullSizeGUI(type).setPages(2);
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

    private void openGUI(GUI gui) {
        BukkitTestUtils.mockPlugin(p -> gui.open(GUIManager.getViewer(this.player)));
    }

    private void closeGUI() {
        BukkitTestUtils.mockPlugin(p -> GUIAdapter.closeGUI(GUIManager.getViewer(this.player)));
    }

}