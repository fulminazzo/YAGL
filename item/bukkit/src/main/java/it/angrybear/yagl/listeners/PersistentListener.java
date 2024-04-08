package it.angrybear.yagl.listeners;

import it.angrybear.yagl.items.Mobility;
import it.angrybear.yagl.items.PersistentItem;
import it.angrybear.yagl.items.DeathAction;
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
    static final int SLEEP_TIME = 50;
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
        ItemStack[] contents = player.getInventory().getContents();
        Map<Integer, PersistentItem> toRestore = findPersistentItems(contents, event.getDrops());
        if (!toRestore.isEmpty()) {
            // Wait before restoring player contents.
            new Thread(() -> {
                try {
                    Thread.sleep(SLEEP_TIME);
                    toRestore.forEach((i, p) -> player.getInventory().setItem(i, p.create()));
                } catch (InterruptedException ignored) {
                }
            }).start();
        }
    }

    /**
     * Finds the corresponding {@link PersistentItem} from the given {@link ItemStack} array.
     * Saves the ones with {@link DeathAction#MAINTAIN} in the returning map.
     *
     * @param drops     the drops
     * @param contents  the contents
     * @return the map
     */
    protected @NotNull Map<Integer, PersistentItem> findPersistentItems(final @NotNull ItemStack[] contents,
                                                                        final @Nullable List<ItemStack> drops) {
        Map<Integer, PersistentItem> toRestore = new HashMap<>();
        for (int i = 0; i < contents.length; i++) {
            int finalI = i;
            final ItemStack item = contents[i];
            // Save every PersistentItem with the MAINTAIN action, remove if they have DISAPPEAR.
            findPersistentItem(item, p -> {
                DeathAction deathAction = p.getDeathAction();
                if (deathAction == null) return;
                if (deathAction == DeathAction.MAINTAIN) toRestore.put(finalI, p);
                if (drops != null) drops.remove(item);
            });
        }
        return toRestore;
    }

    @EventHandler
    protected void on(@NotNull PlayerInteractEvent event) {
        Player player = event.getPlayer();
        long lastUsed = this.lastUsed.getOrDefault(player.getUniqueId(), 0L);
        long now = new Date().getTime();
        // Check that a double click is not happening.
        if (now < lastUsed + INTERACT_DELAY) return;
        this.lastUsed.put(player.getUniqueId(), now);
        interactedWithPersistentItem(event.getItem(), player, event.getAction(), cancelled(event));
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
            if (e.getMobility() != Mobility.INTERNAL || !playerInventory.equals(clicked) ||
                    rawSlot < open.getSize() || event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY))
                cancelled(event).accept(e);
        };

        // Check current item.
        if (clickedWithPersistentItem(itemStack, player, type, ifPresent)) return;
        // Check cursor.
        if (clickedWithPersistentItem(event.getCursor(), player, type, ifPresent)) return;
        // Check if a number has been used from the keyboard to move the item.
        if (type.equals(ClickType.NUMBER_KEY)) {
            itemStack = playerInventory.getItem(event.getHotbarButton());
            clickedWithPersistentItem(itemStack, player, type, ifPresent);
        }
    }

    @EventHandler
    protected void on(@NotNull InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();
        ClickType type = ClickType.LEFT;
        if (clickedWithPersistentItem(event.getCursor(), player, type, cancelled(event))) return;
        if (clickedWithPersistentItem(event.getOldCursor(), player, type, cancelled(event))) return;
        Collection<ItemStack> items = event.getNewItems().values();
        for (ItemStack i : items)
            // Check every item from the new items, cancel on first one.
            if (clickedWithPersistentItem(i, player, type, cancelled(event))) return;
    }

    private @NotNull Consumer<PersistentItem> cancelled(@NotNull Cancellable event) {
        return p -> event.setCancelled(true);
    }

    private boolean interactedWithPersistentItem(final @Nullable ItemStack itemStack,
                                                 final @NotNull Player player,
                                                 final @NotNull Action interactAction,
                                                 final @Nullable Consumer<PersistentItem> ifPresent) {
        return findPersistentItem(itemStack, p -> {
            if (itemStack != null) p.interact(player, itemStack, interactAction);
            if (ifPresent != null) ifPresent.accept(p);
        });
    }

    private boolean clickedWithPersistentItem(final @Nullable ItemStack itemStack,
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
     * @return true if it is {@link #INITIALIZED}
     */
    public static boolean isInitialized() {
        return INITIALIZED;
    }
}
