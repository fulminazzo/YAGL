package it.angrybear.yagl.listeners;

import it.angrybear.yagl.items.PersistentItem;
import it.angrybear.yagl.persistent.DeathAction;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.jbukkit.BukkitUtils;
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
import static org.mockito.Mockito.*;

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
    }

    private static InventoryClickEvent[] inventoryClickEvents() {
        Inventory inventory = mockInventory(9);
        inventory.setItem(0, maintain.create());
        Player player = getPlayer();

        InventoryView view = getInventoryView();
        when(view.getPlayer()).thenReturn(player);
        when(view.getCursor()).thenAnswer(i -> cursor);
        when(player.getOpenInventory()).thenReturn(view);
        Inventory playerInv = player.getInventory();
        when(view.getTopInventory()).thenReturn(inventory);
        when(view.getBottomInventory()).thenReturn(playerInv);
        when(view.getItem(any(int.class))).thenAnswer(i -> inventory.getItem(i.getArgument(0)));

        return new InventoryClickEvent[]{
                new InventoryClickEvent(view, InventoryType.SlotType.CONTAINER, 0, ClickType.LEFT, InventoryAction.CLONE_STACK),
                new InventoryClickEvent(view, InventoryType.SlotType.CONTAINER, 2, ClickType.LEFT, InventoryAction.CLONE_STACK),
                new InventoryClickEvent(view, InventoryType.SlotType.CONTAINER, 0, ClickType.NUMBER_KEY, InventoryAction.CLONE_STACK, 0),
        };
    }

    @ParameterizedTest
    @MethodSource("inventoryClickEvents")
    void simulateInventoryClick(InventoryClickEvent event) {
        if (event.getRawSlot() == 2) cursor = maintain.create();
        else cursor = null;

        assertFalse(this.clicked);
        assertFalse(event.isCancelled());
        listener.on(event);
        assertTrue(event.isCancelled());
        assertTrue(this.clicked);
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
                new InventoryDragEvent(view, itemStack, new ItemStack(Material.STONE), false, new HashMap<>()),
                new InventoryDragEvent(view, null, itemStack, false, new HashMap<>()),
                new InventoryDragEvent(view, null, new ItemStack(Material.STONE), false, new HashMap<>(){{
                    put(18, itemStack);
                }}),
        };
    }

    @ParameterizedTest
    @MethodSource("cancellableEvents")
    void simulateCancellableEvent(Cancellable event) {
        assertFalse(event.isCancelled());
        new Refl<>(listener).callMethod("on", event);
        assertTrue(event.isCancelled());
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
        PlayerInventory inventory = mockInventory(36);
        when(player.getInventory()).thenReturn(inventory);
        when(player.getUniqueId()).thenReturn(uuid);
        return player;
    }

    private static PlayerInventory mockInventory(int size) {
        ItemStack[] contents = new ItemStack[size];
        PlayerInventory inventory = mock(PlayerInventory.class);
        when(inventory.getSize()).thenReturn(contents.length);
        when(inventory.getContents()).thenReturn(contents);
        doAnswer(i -> contents[(int) i.getArgument(0)] = i.getArgument(1)).when(inventory).setItem(any(int.class), any(ItemStack.class));
        when(inventory.getItem(any(int.class))).thenAnswer(i -> contents[(int) i.getArgument(0)]);
        when(inventory.getHeldItemSlot()).thenReturn(0);
        return inventory;
    }

    private static InventoryView getInventoryView() {
        Inventory inventory = mock(Inventory.class);
        InventoryView view = mock(InventoryView.class);
        when(view.getTopInventory()).thenReturn(inventory);
        return view;
    }
}