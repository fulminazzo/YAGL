package it.angrybear.yagl.listeners;

import it.angrybear.yagl.items.BukkitItem;
import it.angrybear.yagl.items.DeathAction;
import it.angrybear.yagl.items.Mobility;
import it.angrybear.yagl.items.PersistentItem;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.jbukkit.BukkitUtils;
import it.fulminazzo.jbukkit.inventory.MockInventory;
import it.fulminazzo.jbukkit.inventory.MockPlayerInventory;
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
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PersistentListenerTest {
    private static PersistentItem maintain;
    private static PersistentItem disappear;
    private static PersistentListener listener;
    private boolean clicked;
    private static ItemStack cursor;

    @BeforeAll
    static void setAllUp() {
        BukkitUtils.setupServer();
        maintain = new PersistentItem(Material.DIAMOND_SWORD, 1).setDisplayName("Maintain").setDeathAction(DeathAction.MAINTAIN);
        disappear = new PersistentItem(Material.IRON_SWORD, 1).setDisplayName("Disappear").setDeathAction(DeathAction.DISAPPEAR);
        listener = new PersistentListener();
    }

    @BeforeEach
    void setUp() {
        this.clicked = false;
        maintain.onClick((i, c, a) -> clicked = true);
        cursor = null;
    }

    private static InventoryView setupInventoryClickEventView() {
        Inventory inventory = new MockInventory(9);
        inventory.setItem(0, maintain.create());

        Player player = getPlayer();
        player.getInventory().setItem(0, maintain.create());

        return getInventoryView(player, inventory);
    }

    private static InventoryClickEvent[] inventoryClickEvents() {
        InventoryView view = setupInventoryClickEventView();

        return new InventoryClickEvent[]{
                new InventoryClickEvent(view, InventoryType.SlotType.CONTAINER, 0, ClickType.LEFT, InventoryAction.CLONE_STACK),
                new InventoryClickEvent(view, InventoryType.SlotType.CONTAINER, 2, ClickType.LEFT, InventoryAction.CLONE_STACK),
                new InventoryClickEvent(view, InventoryType.SlotType.CONTAINER, 3, ClickType.NUMBER_KEY, InventoryAction.CLONE_STACK, 0),
        };
    }

    @ParameterizedTest
    @MethodSource("inventoryClickEvents")
    void simulateInventoryClick(InventoryClickEvent event) {
        if (event.getRawSlot() == 2) cursor = maintain.create();

        assertFalse(this.clicked, "Clicked should be initialized as false");
        assertFalse(event.isCancelled(), "Event should not be cancelled");
        listener.on(event);
        assertTrue(event.isCancelled(), "Event should have been cancelled by now");
        assertTrue(this.clicked, "Clicked should have changed");
    }

    @Test
    void testMovableItem() {
        PersistentItem persistentItem = new PersistentItem(Material.DIAMOND).setMobility(Mobility.INTERNAL);
        InventoryView view = setupInventoryClickEventView();
        int slot = view.getTopInventory().getSize();
        view.getBottomInventory().setItem(0, persistentItem.create());

        InventoryClickEvent event = new InventoryClickEvent(view, InventoryType.SlotType.CONTAINER, slot, ClickType.LEFT, InventoryAction.CLONE_STACK);
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

        InventoryView view = getInventoryView(player, 9);

        return new Cancellable[]{
                new PlayerItemConsumeEvent(player, itemStack),
                new PlayerItemDamageEvent(player, itemStack, 10),
                new BlockPlaceEvent(block, mock(BlockState.class), block, itemStack, player, true),
                new PlayerDropItemEvent(player, item),
                new InventoryDragEvent(view, itemStack, new ItemStack(Material.AIR), false, new HashMap<>()),
                new InventoryDragEvent(view, null, itemStack, false, new HashMap<>()),
                new InventoryDragEvent(view, null, new ItemStack(Material.AIR), false, new HashMap<Integer, ItemStack>(){{
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
        InventoryView view = getInventoryView(player, size);
        for (int i = 0; i < size; i++) view.setItem(i, persistentItem);
        for (int i = 0; i < view.getBottomInventory().getSize(); i++) view.setItem(i + size, persistentItem);

        InventoryDragEvent event = new InventoryDragEvent(view, null, new ItemStack(Material.AIR), false, new HashMap<Integer, ItemStack>(){{
            put(18, itemStack);
        }});
        listener.on(event);
        assertFalse(event.isCancelled(), "Expected event to not be cancelled");
        assertFalse(this.clicked, "Expected click action to not be executed");
    }

    @Test
    void simulatePlayerDeath() throws InterruptedException {
        Player player = getPlayer();
        ItemStack[] contents = player.getInventory().getContents();
        contents[3] = maintain.create();
        contents[4] = disappear.create();
        List<ItemStack> drops = new LinkedList<>(Arrays.asList(contents));

        PlayerDeathEvent event = new PlayerDeathEvent(player, drops, 3, "Player died");
        listener.on(event);
        // Simulate removal of contents
        Arrays.fill(contents, null);

        Thread.sleep(PersistentListener.SLEEP_TIME * 2);

        for (ItemStack i : drops) assertNull(i);

        List<ItemStack> copy = Arrays.asList(contents);
        assertTrue(copy.contains(maintain.create()), "The contents should contain the maintain item");
        assertFalse(copy.contains(disappear.create()), "The contents should not contain the disappear item");
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
        value.set(false);
        listener.on(event);
        assertFalse(value.get());
    }

    @Test
    void testInteractPersistentItem() {
        final int itemSize = 10;
        final List<Integer> clickedItems = new ArrayList<>();
        List<PersistentItem> items = new ArrayList<>();
        for (int i = 0; i < itemSize; i++) {
            int finalI = i;
            items.add(new PersistentItem().setMaterial(Material.values()[3 + i]).onInteract((p, s, c) -> clickedItems.add(finalI)));
        }
        assertTrue(listener.interactPersistentItem(mock(Player.class), Action.LEFT_CLICK_AIR, null,
                items.stream().map(BukkitItem::create).collect(Collectors.toList())), "Should have been true for found");
        assertEquals(itemSize, clickedItems.size());
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

    private static InventoryView getInventoryView(Player player, int size) {
        return getInventoryView(player, new MockInventory(size));
    }

    private static InventoryView getInventoryView(Player player, Inventory inventory) {
        InventoryView view = mock(InventoryView.class);
        PlayerInventory playerInventory = player.getInventory();
        when(view.getBottomInventory()).thenAnswer(a -> playerInventory);
        when(view.getTopInventory()).thenReturn(inventory);
        when(player.getOpenInventory()).thenReturn(view);
        when(view.getPlayer()).thenReturn(player);
        when(view.getItem(any(int.class))).thenCallRealMethod();
        when(view.getInventory(any(int.class))).thenAnswer(a -> {
            int slot = a.getArgument(0);
            if (slot > inventory.getSize()) return inventory;
            return playerInventory;
        });
        return view;
    }
}