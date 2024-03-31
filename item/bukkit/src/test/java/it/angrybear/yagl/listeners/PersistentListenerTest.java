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
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PersistentListenerTest {
    private static PersistentItem maintain, disappear;
    private static PersistentListener listener;

    @BeforeAll
    static void setAllUp() {
        BukkitUtils.setupServer();
        maintain = new PersistentItem(Material.DIAMOND_SWORD, 1).setDisplayName("Maintain").setDeathAction(DeathAction.MAINTAIN);
        disappear = new PersistentItem(Material.GOLDEN_SWORD, 1).setDisplayName("Disappear").setDeathAction(DeathAction.DISAPPEAR);
        listener = new PersistentListener();
    }

    private static Cancellable[] cancellableEvents() {
        Player player = getPlayer();
        ItemStack itemStack = maintain.create();
        player.getInventory().setItem(0, itemStack);
        Block block = mock(Block.class);
        Item item = mock(Item.class);
        when(item.getItemStack()).thenReturn(itemStack);
        return new Cancellable[]{
                new PlayerItemConsumeEvent(player, itemStack, EquipmentSlot.OFF_HAND),
                new PlayerItemDamageEvent(player, itemStack, 10),
                new BlockPlaceEvent(block, mock(BlockState.class), block, itemStack, player, true, EquipmentSlot.OFF_HAND),
                new PlayerDropItemEvent(player, item)
        };
    }

    @ParameterizedTest
    @MethodSource("cancellableEvents")
    void simulateCancellableEvent(Cancellable event) {
        assertFalse(event.isCancelled());
        new Refl<>(listener).callMethod("on", event);
        assertTrue(event.isCancelled());
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
        ItemStack[] contents = new ItemStack[36];
        PlayerInventory inventory = mock(PlayerInventory.class);
        when(inventory.getContents()).thenReturn(contents);
        doAnswer(i -> contents[(int) i.getArgument(0)] = i.getArgument(1)).when(inventory).setItem(any(int.class), any(ItemStack.class));
        when(inventory.getItem(any(int.class))).thenAnswer(i -> contents[(int) i.getArgument(0)]);
        when(inventory.getHeldItemSlot()).thenReturn(0);
        Player player = mock(Player.class);
        when(player.getInventory()).thenReturn(inventory);
        when(player.getUniqueId()).thenReturn(uuid);
        return player;
    }
}