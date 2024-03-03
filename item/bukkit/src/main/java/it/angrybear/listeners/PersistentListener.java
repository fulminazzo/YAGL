package it.angrybear.listeners;

import it.angrybear.items.PersistentItem;
import it.angrybear.persistent.DeathAction;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * A listener for {@link PersistentItem}s.
 */
public class PersistentListener implements Listener {
    private static boolean INITIALIZED = false;

    /**
     * Instantiates a new Persistent listener.
     */
    public PersistentListener() {
        INITIALIZED = true;
    }

    @EventHandler
    void on(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Map<Integer, PersistentItem> toRestore = new HashMap<>();
        ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            int finalI = i;
            final ItemStack item = contents[i];
            findPersistentItem(item, p -> {
                if (p.getDeathAction() == DeathAction.MAINTAIN) toRestore.put(finalI, p);
                event.getDrops().remove(item);
            });
        }
        if (toRestore.isEmpty()) return;
        new Thread(() -> {
            try {
                Thread.sleep(50);
                toRestore.forEach((i, p) -> player.getInventory().setItem(i, p.create()));
            } catch (InterruptedException ignored) {
            }
        }).start();
    }

    @EventHandler
    void on(PlayerItemConsumeEvent event) {
        findPersistentItem(event.getItem(), cancelled(event));
    }

    @EventHandler
    void on(PlayerItemDamageEvent event) {
        findPersistentItem(event.getItem(), cancelled(event));
    }

    @EventHandler
    void on(BlockPlaceEvent event) {
        PlayerInventory inventory = event.getPlayer().getInventory();
        findPersistentItem(inventory.getItem(inventory.getHeldItemSlot()), cancelled(event));
    }

    @EventHandler
    void on(PlayerDropItemEvent event) {
        findPersistentItem(event.getItemDrop().getItemStack(), cancelled(event));
    }

    @EventHandler
    void on(InventoryClickEvent event) {
        findPersistentItem(event.getCurrentItem(), cancelled(event), () ->
                findPersistentItem(event.getCursor(), cancelled(event)));
    }

    @EventHandler
    void on(InventoryDragEvent event) {
        findPersistentItem(event.getCursor(), cancelled(event), () -> findPersistentItem(event.getOldCursor(), cancelled(event), () -> {
            Collection<ItemStack> items = event.getNewItems().values();
            AtomicBoolean check = new AtomicBoolean(true);
            for (ItemStack i : items) {
                findPersistentItem(i, p -> {
                    cancelled(event).accept(p);
                    check.set(false);
                });
                if (!check.get()) break;
            }
        }));
    }

    private Consumer<PersistentItem> cancelled(Cancellable event) {
        return p -> event.setCancelled(true);
    }

    private void interactPersistentItem(final @Nullable ItemStack itemStack,
                                        final @Nullable Consumer<PersistentItem> ifPresent,
                                        final @NotNull Player player) {
        interactPersistentItem(itemStack, ifPresent, null, player);
    }

    private void interactPersistentItem(final @Nullable ItemStack itemStack,
                                        final @Nullable Consumer<PersistentItem> ifPresent,
                                        final @Nullable Runnable orElse,
                                        final @NotNull Player player) {
        findPersistentItem(itemStack, p -> {
            if (p.getInteractAction() != null) p.getInteractAction().accept(player, itemStack);
            if (ifPresent != null) ifPresent.accept(p);
        }, orElse);
    }

    private void clickPersistentItem(final @Nullable ItemStack itemStack,
                                        final @Nullable Consumer<PersistentItem> ifPresent,
                                        final @NotNull Player player) {
        clickPersistentItem(itemStack, ifPresent, null, player);
    }

    private void clickPersistentItem(final @Nullable ItemStack itemStack,
                                        final @Nullable Consumer<PersistentItem> ifPresent,
                                        final @Nullable Runnable orElse,
                                        final @NotNull Player player) {
        findPersistentItem(itemStack, p -> {
            if (p.getClickAction() != null) p.getClickAction().accept(player, itemStack);
            if (ifPresent != null) ifPresent.accept(p);
        }, orElse);
    }

    private void findPersistentItem(final @Nullable ItemStack itemStack,
                                    final @NotNull Consumer<PersistentItem> ifPresent) {
        findPersistentItem(itemStack, ifPresent, null);
    }

    private void findPersistentItem(final @Nullable ItemStack itemStack,
                                    final @NotNull Consumer<PersistentItem> ifPresent,
                                    final @Nullable Runnable orElse) {
        if (itemStack != null) {
            PersistentItem persistentItem = PersistentItem.getPersistentItem(itemStack);
            if (persistentItem != null) {
                ifPresent.accept(persistentItem);
                return;
            }
        }
        if (orElse != null) orElse.run();
    }

    /**
     * Checks if the current listener has been initialized at least once.
     *
     * @return the boolean
     */
    public static boolean isInitialized() {
        return INITIALIZED;
    }
}
