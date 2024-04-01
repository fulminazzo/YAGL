package it.angrybear.yagl.listeners;

import it.angrybear.yagl.items.MovablePersistentItem;
import it.angrybear.yagl.items.PersistentItem;
import it.angrybear.yagl.persistent.DeathAction;
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
import org.bukkit.inventory.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PersistentListenerTest {
    private static PersistentItem maintain, disappear;
    private static PersistentListener listener;
    private boolean clicked;
    private static ItemStack cursor;

    @BeforeAll
    static void setAllUp() {
        BukkitUtils.setupServer();
        maintain = new PersistentItem(Material.DIAMOND_SWORD, 1).setDisplayName("Maintain").setDeathAction(DeathAction.MAINTAIN);
        disappear = new PersistentItem(Material.GOLDEN_SWORD, 1).setDisplayName("Disappear").setDeathAction(DeathAction.DISAPPEAR);
        listener = new PersistentListener();
    }

    @BeforeEach
    void setUp() {
        this.clicked = false;
        maintain.onClick((i, c, a) -> clicked = true);
        cursor = null;
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
        MovablePersistentItem persistentItem = new MovablePersistentItem(Material.DIAMOND);
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

        InventoryView view = getInventoryView();

        return new Cancellable[]{
                new PlayerItemConsumeEvent(player, itemStack, EquipmentSlot.OFF_HAND),
                new PlayerItemDamageEvent(player, itemStack, 10),
                new BlockPlaceEvent(block, mock(BlockState.class), block, itemStack, player, true, EquipmentSlot.OFF_HAND),
                new PlayerDropItemEvent(player, item),
                new InventoryDragEvent(view, itemStack, new ItemStack(Material.AIR), false, new HashMap<>()),
                new InventoryDragEvent(view, null, itemStack, false, new HashMap<>()),
                new InventoryDragEvent(view, null, new ItemStack(Material.AIR), false, new HashMap<>(){{
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
        PlayerInteractEvent event = new PlayerInteractEvent(player, Action.LEFT_CLICK_AIR, maintain.create(),
                null, BlockFace.DOWN, null);
        listener.on(event);
        assertTrue(value.get());

        // Simulate rapid click
        value.set(false);
        listener.on(event);
        assertFalse(value.get());
    }

    private static Player getPlayer() {
        UUID uuid = UUID.randomUUID();

        Player player = mock(Player.class);
        PlayerInventory inventory = new MockPlayerInventory(player);
        when(player.getInventory()).thenReturn(inventory);
        when(player.getUniqueId()).thenReturn(uuid);
        return player;
    }

    private static InventoryView getInventoryView() {
        Inventory inventory = new MockInventory(9);
        InventoryView view = mock(InventoryView.class);
        when(view.getTopInventory()).thenReturn(inventory);
        return view;
    }

    private static InventoryView setupInventoryClickEventView() {
        Inventory inventory = new MockInventory(9);
        inventory.setItem(0, maintain.create());
        Player player = getPlayer();
        player.getInventory().setItem(0, maintain.create());

        InventoryView view = getInventoryView();
        when(view.getPlayer()).thenReturn(player);
        when(player.getOpenInventory()).thenReturn(view);

        when(view.getCursor()).thenAnswer(i -> cursor);

        when(view.getTopInventory()).thenReturn(inventory);
        Inventory playerInv = player.getInventory();
        when(view.getBottomInventory()).thenReturn(playerInv);

        when(view.getItem(any(int.class))).thenAnswer(i -> {
            int slot = i.getArgument(0);
            if (slot >= inventory.getSize()) return playerInv.getItem(slot);
            return inventory.getItem(slot);
        });
        return view;
    }
}