package it.angrybear.yagl.listeners;

import it.angrybear.yagl.items.MovablePersistentItem;
import it.angrybear.yagl.items.PersistentItem;
import it.angrybear.yagl.persistent.DeathAction;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

/**
 * A listener for {@link PersistentItem}s.
 */
public class PersistentListener implements Listener {
    private static boolean INITIALIZED = false;
    /**
     * Timeout, in milliseconds, to check before calling {@link #on(PlayerInteractEvent)}.
     * This is used to prevent double calls.
     */
    private static final long INTERACT_DELAY = 10;
    private final @NotNull Map<UUID, Long> lastUsed;

    /**
     * Instantiates a new Persistent listener.
     */
    public PersistentListener() {
        this.lastUsed = new HashMap<>();
        INITIALIZED = true;
    }

    @EventHandler
    protected void on(@NotNull PlayerDeathEvent event) {
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
    protected void on(@NotNull PlayerInteractEvent event) {
        Player player = event.getPlayer();
        long lastUsed = this.lastUsed.getOrDefault(player.getUniqueId(), 0L);
        long now = new Date().getTime();
        if (now < lastUsed + INTERACT_DELAY) return;
        this.lastUsed.put(player.getUniqueId(), now);
        interactPersistentItem(event.getItem(), player, event.getAction(), cancelled(event));
    }

    @EventHandler
    protected void on(@NotNull PlayerItemConsumeEvent event) {
        findPersistentItem(event.getItem(), cancelled(event));
    }

    @EventHandler
    protected void on(@NotNull PlayerItemDamageEvent event) {
        findPersistentItem(event.getItem(), cancelled(event));
    }

    @EventHandler
    protected void on(@NotNull BlockPlaceEvent event) {
        PlayerInventory inventory = event.getPlayer().getInventory();
        findPersistentItem(inventory.getItem(inventory.getHeldItemSlot()), cancelled(event));
    }

    @EventHandler
    protected void on(@NotNull PlayerDropItemEvent event) {
        findPersistentItem(event.getItemDrop().getItemStack(), cancelled(event));
    }

    @EventHandler
    protected void on(@NotNull InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ClickType type = event.getClick();
        ItemStack itemStack = event.getCurrentItem();
        Inventory open = player.getOpenInventory().getTopInventory();
        Inventory clicked = event.getClickedInventory();
        Inventory playerInventory = player.getInventory();

        Consumer<PersistentItem> ifPresent = e -> {
            int rawSlot = event.getRawSlot();
            if (!(e instanceof MovablePersistentItem) || !playerInventory.equals(clicked) || rawSlot < open.getSize())
                cancelled(event).accept(e);
        };

        if (clickPersistentItem(itemStack, player, type, ifPresent)) return;
        if (clickPersistentItem(event.getCursor(), player, type, ifPresent)) return;
        if (type.equals(ClickType.NUMBER_KEY)) {
            itemStack = playerInventory.getItem(event.getHotbarButton());
            clickPersistentItem(itemStack, player, type, ifPresent);
        }
    }

    @EventHandler
    protected void on(@NotNull InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();
        ClickType type = ClickType.LEFT;
        if (clickPersistentItem(event.getCursor(), player, type, cancelled(event))) return;
        if (clickPersistentItem(event.getOldCursor(), player, type, cancelled(event))) return;
        Collection<ItemStack> items = event.getNewItems().values();
        for (ItemStack i : items)
            if (clickPersistentItem(i, player, type, p -> cancelled(event).accept(p))) return;
    }

    private @NotNull Consumer<PersistentItem> cancelled(@NotNull Cancellable event) {
        return p -> event.setCancelled(true);
    }

    private boolean interactPersistentItem(final @Nullable ItemStack itemStack,
                                           final @NotNull Player player,
                                           final @NotNull Action interactAction,
                                           final @Nullable Consumer<PersistentItem> ifPresent) {
        return findPersistentItem(itemStack, p -> {
            if (itemStack != null) p.interact(player, itemStack, interactAction);
            if (ifPresent != null) ifPresent.accept(p);
        });
    }

    private boolean clickPersistentItem(final @Nullable ItemStack itemStack,
                                        final @NotNull Player player,
                                        final @NotNull ClickType clickType,
                                        final @Nullable Consumer<PersistentItem> ifPresent) {
        return findPersistentItem(itemStack, p -> {
            if (itemStack != null) p.click(player, itemStack, clickType);
            if (ifPresent != null) ifPresent.accept(p);
        });
    }

    private boolean findPersistentItem(final @Nullable ItemStack itemStack,
                                       final @NotNull Consumer<PersistentItem> ifPresent) {
        if (itemStack != null) {
            PersistentItem persistentItem = PersistentItem.getPersistentItem(itemStack);
            if (persistentItem != null) {
                ifPresent.accept(persistentItem);
                return true;
            }
        }
        return false;
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
