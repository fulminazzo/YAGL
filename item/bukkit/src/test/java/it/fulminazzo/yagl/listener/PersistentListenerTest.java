package it.fulminazzo.yagl.listener;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import it.fulminazzo.jbukkit.BukkitUtils;
import it.fulminazzo.jbukkit.inventory.MockInventory;
import it.fulminazzo.jbukkit.inventory.MockPlayerInventory;
import it.fulminazzo.yagl.item.BukkitItem;
import it.fulminazzo.yagl.item.DeathAction;
import it.fulminazzo.yagl.item.Mobility;
import it.fulminazzo.yagl.item.PersistentItem;
import it.fulminazzo.yagl.testing.InventoryViewWrapper;
import it.fulminazzo.yagl.util.BukkitTestUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedConstruction;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("deprecation")
class PersistentListenerTest {
    private static PersistentItem maintain;
    private static PersistentItem disappear;
    private static PersistentItem none;
    private static PersistentListener listener;
    private boolean clicked;
    private static ItemStack cursor;

    @BeforeAll
    static void setAllUp() {
        BukkitUtils.setupServer();
        List<PersistentItem> list = new Refl<>(PersistentItem.class).getFieldObject("PERSISTENT_ITEMS");
        list.clear();
        maintain = PersistentItem.newItem(Material.DIAMOND_SWORD, 1).setDisplayName("Maintain").setDeathAction(DeathAction.MAINTAIN);
        disappear = PersistentItem.newItem(Material.IRON_SWORD, 1).setDisplayName("Disappear").setDeathAction(DeathAction.DISAPPEAR);
        none = PersistentItem.newItem(Material.STONE_SWORD, 1).setDisplayName("None").setDeathAction(null);
        listener = new PersistentListener();
    }

    @BeforeEach
    void setUp() {
        this.clicked = false;
        maintain.onClick((i, c, a) -> clicked = true);
        cursor = null;
    }

    @Test
    void testGetInstanceInitialized() {
        PersistentListener listener = PersistentListener.getInstance();
        assertNotNull(listener, "Expected getInstance listener to not be null");
        assertEquals(PersistentListenerTest.listener, listener, "Listeners were not equal");
    }

    @Test
    void testGetInstanceNotInitialized() {
        BukkitTestUtils.mockPlugin(p -> {
            listener.terminate();
            PersistentListener listener = PersistentListener.getInstance();
            assertNotNull(listener, "Expected getInstance listener to not be null");
            assertNotEquals(PersistentListenerTest.listener, listener, "Listeners should not be equal");
        });
    }

    private static InventoryViewWrapper setupInventoryClickEventView() {
        Inventory inventory = new MockInventory(9);
        inventory.setItem(0, maintain.create());

        Player player = getPlayer();
        player.getInventory().setItem(0, maintain.create());

        return getInventoryView(player, inventory);
    }

    private static InventoryClickEvent[] inventoryClickEvents() {
        InventoryViewWrapper view = setupInventoryClickEventView();

        return new InventoryClickEvent[]{
                new InventoryClickEvent(view.getWrapped(), InventoryType.SlotType.CONTAINER, 0, ClickType.LEFT, InventoryAction.CLONE_STACK),
                new InventoryClickEvent(view.getWrapped(), InventoryType.SlotType.CONTAINER, 0, ClickType.LEFT, InventoryAction.MOVE_TO_OTHER_INVENTORY),
                new InventoryClickEvent(view.getWrapped(), InventoryType.SlotType.CONTAINER, 2, ClickType.LEFT, InventoryAction.CLONE_STACK),
                new InventoryClickEvent(view.getWrapped(), InventoryType.SlotType.CONTAINER, 3, ClickType.NUMBER_KEY, InventoryAction.CLONE_STACK, 0),
                new InventoryClickEvent(view.getWrapped(), InventoryType.SlotType.CONTAINER, 9, ClickType.NUMBER_KEY, InventoryAction.MOVE_TO_OTHER_INVENTORY, 0),
        };
    }

    @ParameterizedTest
    @MethodSource("inventoryClickEvents")
    void simulateInventoryClick(InventoryClickEvent event) {
        if (event.getRawSlot() == 2) {
            Refl<?> view = new Refl<>(event.getView());
            cursor = maintain.setMobility(Mobility.INTERNAL).create();
            view.invokeMethod("setCursor", cursor);
        }

        assertFalse(this.clicked, "Clicked should be initialized as false");
        assertFalse(event.isCancelled(), "Event should not be cancelled");
        listener.on(event);
        assertTrue(event.isCancelled(), "Event should have been cancelled by now");
        assertTrue(this.clicked, "Clicked should have changed");
    }

    @Test
    void testMovableItem() {
        PersistentItem persistentItem = PersistentItem.newItem(Material.DIAMOND_PICKAXE).setMobility(Mobility.INTERNAL);
        InventoryViewWrapper view = setupInventoryClickEventView();
        int slot = view.getTopInventory().getSize();
        view.getBottomInventory().setItem(0, persistentItem.create());

        InventoryClickEvent event = new InventoryClickEvent(view.getWrapped(), InventoryType.SlotType.CONTAINER, slot, ClickType.LEFT, InventoryAction.CLONE_STACK);
        assertEquals(view.getBottomInventory(), event.getClickedInventory(), "Clicked inventory should be PlayerInventory");
        assertFalse(event.isCancelled(), "Event should not be cancelled");
        listener.on(event);
        assertFalse(event.isCancelled(), "Event should not be cancelled");
    }

    private static Cancellable[] cancellableEvents() {
        Player player = getPlayer();

        ItemStack itemStack = maintain.create();
        player.getInventory().setItem(0, itemStack);

        Block block = mock(Block.class);

        Item item = mock(Item.class);
        when(item.getItemStack()).thenReturn(itemStack);

        InventoryViewWrapper view = getInventoryView(player, 9);

        return new Cancellable[]{
                new PlayerItemConsumeEvent(player, itemStack),
                new PlayerItemDamageEvent(player, itemStack, 10),
                new BlockPlaceEvent(block, mock(BlockState.class), block, itemStack, player, true),
                new PlayerDropItemEvent(player, item),
                new InventoryDragEvent(view.getWrapped(), itemStack, new ItemStack(Material.AIR), false, new HashMap<>()),
                new InventoryDragEvent(view.getWrapped(), null, itemStack, false, new HashMap<>()),
                new InventoryDragEvent(view.getWrapped(), null, new ItemStack(Material.AIR), false, new HashMap<Integer, ItemStack>() {{
                    put(18, itemStack);
                }}),
        };
    }

    @ParameterizedTest
    @MethodSource("cancellableEvents")
    void simulateCancellableEvent(Cancellable event) {
        assertFalse(this.clicked, "Expected clicked to be false");
        assertFalse(event.isCancelled(), "Expected event to not be cancelled");
        new Refl<>(listener).callMethod("on", event);
        assertTrue(event.isCancelled(), "Expected event to be cancelled");
        if (event instanceof InventoryDragEvent)
            assertTrue(this.clicked, "Click action was not executed");
    }

    @Test
    void simulateDragEventWithNoItems() {
        Player player = getPlayer();

        ItemStack persistentItem = maintain.create();

        ItemStack itemStack = new ItemStack(Material.GLASS);
        assertNull(PersistentItem.getPersistentItem(itemStack));

        int size = 9;
        InventoryViewWrapper view = getInventoryView(player, size);
        for (int i = 0; i < size; i++) view.setItem(i, persistentItem);
        for (int i = 0; i < view.getBottomInventory().getSize(); i++) view.setItem(i + size, persistentItem);

        InventoryDragEvent event = new InventoryDragEvent(view.getWrapped(), null, new ItemStack(Material.AIR), false, new HashMap<Integer, ItemStack>() {{
            put(18, itemStack);
        }});
        listener.on(event);
        assertFalse(event.isCancelled(), "Expected event to not be cancelled");
        assertFalse(this.clicked, "Expected click action to not be executed");
    }

    @Test
    void simulateInteractEvent() {
        AtomicBoolean value = new AtomicBoolean(false);
        maintain.onInteract((i, p, a) -> value.set(true));

        Player player = getPlayer();
        PlayerInteractEvent event = new PlayerInteractEvent(player, Action.LEFT_CLICK_AIR, maintain.create(), null, BlockFace.DOWN);
        listener.on(event);
        assertTrue(value.get());

        // Simulate rapid click
        try (MockedConstruction<Date> ignored = mockConstruction(Date.class, (mock, context) ->
                when(mock.getTime()).thenAnswer(a -> {
                    Refl<PersistentListener> refl = new Refl<>(listener);
                    Map<UUID, Long> map = refl.getFieldObject("lastUsed");
                    return map.get(player.getUniqueId()) + (long) refl.getFieldObject("INTERACT_DELAY") - 1;
                }))
        ) {
            value.set(false);
            listener.on(event);
            assertFalse(value.get());
        }
    }

    @Test
    void testClickPersistentItem() {
        final int itemSize = 10;
        final List<Integer> clickedItems = new ArrayList<>();
        List<PersistentItem> items = new ArrayList<>();
        for (int i = 0; i < itemSize; i++) {
            int finalI = i;
            items.add(PersistentItem.newItem().setMaterial(Material.values()[13 + i]).onClick((p, s, c) -> clickedItems.add(finalI)));
        }
        assertTrue(listener.clickPersistentItem(mock(Player.class), ClickType.DOUBLE_CLICK, null,
                items.stream().map(BukkitItem::create).collect(Collectors.toList())), "Should have been true for found");
        assertEquals(itemSize, clickedItems.size());
    }

    @Test
    void testInteractPersistentItem() {
        final int itemSize = 10;
        final List<Integer> interactItems = new ArrayList<>();
        List<PersistentItem> items = new ArrayList<>();
        for (int i = 0; i < itemSize; i++) {
            int finalI = i;
            items.add(PersistentItem.newItem().setMaterial(Material.values()[3 + i]).onInteract((p, s, c) -> interactItems.add(finalI)));
        }
        assertTrue(listener.interactPersistentItem(mock(Player.class), Action.LEFT_CLICK_AIR, null,
                items.stream().map(BukkitItem::create).collect(Collectors.toList())), "Should have been true for found");
        assertEquals(itemSize, interactItems.size());
    }

    @Test
    void testFindPersistentItemNullStacks() {
        assertEquals(false, new Refl<>(listener).invokeMethod("findPersistentItem",
                new Class[]{Consumer.class, ItemStack[].class}, null, null));
    }

    @Test
    void testFindPersistentItemNullConsumer() {
        ItemStack[] stacks = new ItemStack[]{maintain.create()};
        assertEquals(true, new Refl<>(listener).invokeMethod("findPersistentItem",
                new Class[]{Consumer.class, ItemStack[].class}, null, stacks));
    }

    @Test
    void testFindPersistentItemNullBiConsumer() {
        ItemStack[] stacks = new ItemStack[]{maintain.create()};
        assertEquals(true, new Refl<>(listener).invokeMethod("findPersistentItem",
                new Class[]{BiConsumer.class, ItemStack[].class}, null, stacks));
    }

    private static Player getPlayer() {
        UUID uuid = UUID.randomUUID();

        Player player = mock(Player.class);
        PlayerInventory inventory = new MockPlayerInventory(player);
        when(player.getInventory()).thenReturn(inventory);
        when(player.getUniqueId()).thenReturn(uuid);
        when(player.getItemOnCursor()).thenAnswer(i -> cursor);
        return player;
    }

    private static InventoryViewWrapper getInventoryView(Player player, int size) {
        return getInventoryView(player, new MockInventory(size));
    }

    private static InventoryViewWrapper getInventoryView(Player player, Inventory inventory) {
        return new InventoryViewWrapper(inventory, player, "");
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class InventoryClick {

        private InventoryViewWrapper setupInventoryClickEventView() {
            Inventory inventory = new MockInventory(9);
            inventory.setItem(0, maintain.create());

            Player player = getPlayer();
            player.getInventory().setItem(0, maintain.create());

            return getInventoryView(player, inventory);
        }

        private InventoryClickEvent[] inventoryClickEvents() {
            InventoryViewWrapper view = setupInventoryClickEventView();

            return new InventoryClickEvent[]{
                    new InventoryClickEvent(view.getWrapped(), InventoryType.SlotType.CONTAINER, 0, ClickType.LEFT, InventoryAction.CLONE_STACK),
                    new InventoryClickEvent(view.getWrapped(), InventoryType.SlotType.CONTAINER, 2, ClickType.LEFT, InventoryAction.CLONE_STACK),
                    new InventoryClickEvent(view.getWrapped(), InventoryType.SlotType.CONTAINER, 3, ClickType.NUMBER_KEY, InventoryAction.CLONE_STACK, 0),
                    new InventoryClickEvent(view.getWrapped(), InventoryType.SlotType.CONTAINER, 0, ClickType.SHIFT_LEFT, InventoryAction.MOVE_TO_OTHER_INVENTORY),
            };
        }

        @ParameterizedTest
        @MethodSource("inventoryClickEvents")
        void simulateInventoryClick(InventoryClickEvent event) {
            if (event.getRawSlot() == 2) {
                Refl<?> view = new Refl<>(event.getView());
                cursor = maintain.setMobility(Mobility.INTERNAL).create();
                view.invokeMethod("setCursor", cursor);
            }

            assertFalse(clicked, "Clicked should be initialized as false");
            assertFalse(event.isCancelled(), "Event should not be cancelled");
            listener.on(event);
            assertTrue(event.isCancelled(), "Event should have been cancelled by now");
            assertTrue(clicked, "Clicked should have changed");
        }

        private Object[] notMovableItems() {
            return new Object[]{
                    PersistentItem.newItem(Material.IRON_HOE).setMobility(Mobility.INTERNAL),
                    PersistentItem.newItem(Material.DIAMOND_AXE).setMobility(Mobility.STATIC)
            };
        }

        @ParameterizedTest
        @MethodSource("notMovableItems")
        void testItemOutsideCouldNotBeMoved(PersistentItem persistentItem) {
            InventoryViewWrapper view = setupInventoryClickEventView();
            int slot = view.getTopInventory().getSize();
            view.getBottomInventory().setItem(0, persistentItem.create());

            InventoryClickEvent event = new InventoryClickEvent(view.getWrapped(), InventoryType.SlotType.CONTAINER, slot, ClickType.NUMBER_KEY, InventoryAction.CLONE_STACK, 0);
            assertEquals(view.getBottomInventory(), event.getClickedInventory(), "Clicked inventory should be PlayerInventory");
            assertFalse(event.isCancelled(), "Event should not be cancelled");
            listener.on(event);
            assertEquals(persistentItem.getMobility() == Mobility.STATIC, event.isCancelled(), "Event cancel state was not as expected");
        }

        @ParameterizedTest
        @MethodSource("notMovableItems")
        void testItemInsideShouldNotBeMoved(PersistentItem persistentItem) {
            InventoryViewWrapper view = setupInventoryClickEventView();
            int slot = 0;
            view.getTopInventory().setItem(slot, persistentItem.create());

            InventoryClickEvent event = new InventoryClickEvent(view.getWrapped(), InventoryType.SlotType.CONTAINER, slot, ClickType.LEFT, InventoryAction.CLONE_STACK);
            assertEquals(view.getTopInventory(), event.getClickedInventory(), "Clicked inventory should be top inventory");
            assertFalse(event.isCancelled(), "Event should not be cancelled");
            listener.on(event);
            assertTrue(event.isCancelled(), "Event should be cancelled");
        }

        @Test
        void testClickInBottomInventoryWithNoKeyPress() {
            PersistentItem persistentItem = PersistentItem.newItem(Material.DIAMOND_HELMET);
            InventoryViewWrapper view = setupInventoryClickEventView();
            int slot = view.getTopInventory().getSize();
            view.getBottomInventory().setItem(0, persistentItem.create());

            InventoryClickEvent event = new InventoryClickEvent(view.getWrapped(), InventoryType.SlotType.CONTAINER, slot + 1, ClickType.LEFT, InventoryAction.CLONE_STACK);
            assertEquals(view.getBottomInventory(), event.getClickedInventory(), "Clicked inventory should be PlayerInventory");
            assertFalse(event.isCancelled(), "Event should not be cancelled");
            listener.on(event);
            assertFalse(event.isCancelled(), "Event should not be cancelled");
        }

    }

    @Nested
    class PlayerDeath {

        @Test
        void simulatePlayerDeath() throws InterruptedException {
            Player player = getPlayer();
            ItemStack[] contents = player.getInventory().getContents();
            contents[3] = maintain.create();
            contents[4] = disappear.create();
            contents[5] = none.create();
            List<ItemStack> drops = new LinkedList<>(Arrays.asList(contents));

            PlayerDeathEvent event = generatePlayerDeathEvent(player, drops);
            listener.on(event);
            // Simulate removal of contents
            Arrays.fill(contents, null);

            Thread.sleep(PersistentListener.SLEEP_TIME * 2);

            for (ItemStack i : drops) {
                // Drop item in case of null
                if (i != null) assertEquals(none.create(), i);
            }

            List<ItemStack> copy = Arrays.asList(contents);
            assertTrue(copy.contains(maintain.create()), "The contents should contain the maintain item");
            assertFalse(copy.contains(disappear.create()), "The contents should not contain the disappear item");
            assertFalse(copy.contains(none.create()), "The contents should not contain the none item");
        }

        @Test
        void simulateNullDrops() throws InterruptedException {
            Player player = getPlayer();
            ItemStack[] contents = player.getInventory().getContents();
            contents[3] = maintain.create();
            PlayerDeathEvent event = generatePlayerDeathEvent(player, null);
            listener.on(event);
            // Simulate removal of contents
            Arrays.fill(contents, null);

            Thread.sleep(PersistentListener.SLEEP_TIME * 2);

            List<ItemStack> copy = Arrays.asList(contents);
            assertTrue(copy.contains(maintain.create()), "The contents should contain the maintain item");
        }

        @Test
        void simulateNothingToRestore() throws InterruptedException {
            Player player = getPlayer();
            ItemStack[] contents = player.getInventory().getContents();
            PlayerDeathEvent event = generatePlayerDeathEvent(player, Arrays.asList(contents));
            listener.on(event);
            // Simulate removal of contents
            Arrays.fill(contents, null);

            Thread.sleep(PersistentListener.SLEEP_TIME * 2);

            for (ItemStack i : contents)
                assertNull(i, "Nothing should be restored");
        }

        private PlayerDeathEvent generatePlayerDeathEvent(Player player, List<ItemStack> drops) {
            try {
                return new Refl<>(PlayerDeathEvent.class, player,
                        drops, 3, "Player died").getObject();
            } catch (IllegalArgumentException e) {
                // Versions higher than 1.20.6
                return new Refl<>(PlayerDeathEvent.class, player,
                        mock(ReflectionUtils.getClass("org.bukkit.damage.DamageSource")),
                        drops, 3, "Player died").getObject();
            }
        }

    }

}
