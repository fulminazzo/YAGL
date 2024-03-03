package it.angrybear.listeners;

import it.angrybear.items.PersistentItem;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
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
