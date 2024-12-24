package it.fulminazzo.yagl.listeners;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ThreadUtils;
import it.fulminazzo.yagl.InstanceNotInitializedException;
import it.fulminazzo.yagl.SingleInstance;
import it.fulminazzo.yagl.items.DeathAction;
import it.fulminazzo.yagl.items.Mobility;
import it.fulminazzo.yagl.items.PersistentItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A listener for {@link PersistentItem}s.
 */
public class PersistentListener extends SingleInstance implements Listener {
    /**
     * Timeout, in milliseconds, to check before calling {@link #on(PlayerInteractEvent)}.
     * This is used to prevent double calls.
     */
    private static final long INTERACT_DELAY = 10;
    /**
     * The general sleep time used in many methods.
     */
    static final int SLEEP_TIME = 50;
    private final @NotNull Map<UUID, Long> lastUsed;

    /**
     * Instantiates a new Persistent listener.
     */
    public PersistentListener() {
        initialize();
        this.lastUsed = new HashMap<>();
    }

    @EventHandler
    protected void on(@NotNull PlayerDeathEvent event) {
        Player player = event.getEntity();
        ItemStack[] contents = player.getInventory().getContents();
        Map<Integer, PersistentItem> toRestore = parseDroppedItems(contents, event.getDrops());
        if (!toRestore.isEmpty())
            // Wait before restoring player contents.
            ThreadUtils.sleepAndThen(SLEEP_TIME, () -> toRestore.forEach((i, p) ->
                    player.getInventory().setItem(i, p.create())));
    }

    /**
     * Finds the corresponding {@link PersistentItem} from the given {@link ItemStack} array.
     * Saves the ones with {@link DeathAction#MAINTAIN} in the returning map.
     *
     * @param contents the contents
     * @param drops    the drops
     * @return the map
     */
    protected @NotNull Map<Integer, PersistentItem> parseDroppedItems(final @NotNull ItemStack[] contents,
                                                                      final @Nullable List<ItemStack> drops) {
        Map<Integer, PersistentItem> toRestore = new HashMap<>();
        for (int i = 0; i < contents.length; i++) {
            int finalI = i;
            final ItemStack item = contents[i];
            // Save every PersistentItem with the MAINTAIN action, remove if they have the DISAPPEAR one.
            findPersistentItem(p -> {
                DeathAction deathAction = p.getDeathAction();
                if (deathAction == null) return;
                if (deathAction == DeathAction.MAINTAIN) toRestore.put(finalI, p);
                if (drops != null) drops.remove(item);
            }, item);
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
        interactPersistentItem(player, event.getAction(), cancelled(event), event.getItem());
    }

    @EventHandler
    protected void on(@NotNull PlayerItemConsumeEvent event) {
        findPersistentItem(cancelled(event), event.getItem());
    }

    @EventHandler
    protected void on(@NotNull PlayerItemDamageEvent event) {
        findPersistentItem(cancelled(event), event.getItem());
    }

    @EventHandler
    protected void on(@NotNull BlockPlaceEvent event) {
        PlayerInventory inventory = event.getPlayer().getInventory();
        findPersistentItem(cancelled(event), inventory.getItem(inventory.getHeldItemSlot()));
    }

    @EventHandler
    protected void on(@NotNull PlayerDropItemEvent event) {
        findPersistentItem(cancelled(event), event.getItemDrop().getItemStack());
    }

    @EventHandler
    protected void on(@NotNull InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ClickType type = event.getClick();
        ItemStack itemStack = event.getCurrentItem();
        Consumer<PersistentItem> ifPresent = clickConsumer(event, player);

        // Check the current item and the cursor.
        if (!clickPersistentItem(player, type, ifPresent, itemStack, event.getCursor()) && type.equals(ClickType.NUMBER_KEY)) {
            // Check if a number has been used from the keyboard to move the item.
            itemStack = player.getInventory().getItem(event.getHotbarButton());
            clickPersistentItem(player, type, ifPresent, itemStack);
        }
    }

    @EventHandler
    protected void on(@NotNull InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();
        ClickType type = ClickType.LEFT;
        clickPersistentItem(player, type, cancelled(event), Stream.concat(Stream.of(
                event.getCursor(), event.getOldCursor()
        ), event.getNewItems().values().stream()).collect(Collectors.toList()));
    }

    /*
        API USAGE
     */

    /**
     * Returns a consumer that simply cancels the event.
     *
     * @param event the event
     * @return the consumer
     */
    protected @NotNull Consumer<PersistentItem> cancelled(@NotNull Cancellable event) {
        return p -> event.setCancelled(true);
    }

    /**
     * Returns the consumer used in the default {@link #on(InventoryClickEvent)}.
     * It checks if the {@link PersistentItem} is not {@link Mobility#INTERNAL}
     * or if the click is external to the player's inventory.
     * If so, it cancels the {@link InventoryClickEvent}.
     *
     * @param event  the event
     * @param player the player
     * @return the consumer
     */
    protected @NotNull Consumer<PersistentItem> clickConsumer(final @NotNull InventoryClickEvent event, final @NotNull Player player) {
        return e -> {
            // Reflections necessary for tests
            Inventory open = new Refl<>(player).invokeMethodRefl("getOpenInventory").invokeMethod("getTopInventory");
            int rawSlot = event.getRawSlot();
            if (e.getMobility() != Mobility.INTERNAL || (rawSlot < open.getSize() || event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)))
                cancelled(event).accept(e);
        };
    }

    /**
     * Checks through every {@link ItemStack} provided for a match in {@link PersistentItem#getPersistentItem(ItemStack)}.
     * For every match, it executes an action.
     *
     * @param player         the player
     * @param interactAction the interact action
     * @param ifPresent      the consumer to run if the item is found
     * @param itemStacks     the item stacks
     * @return true if it was found
     */
    protected boolean interactPersistentItem(final @NotNull Player player,
                                             final @NotNull Action interactAction,
                                             final @Nullable Consumer<PersistentItem> ifPresent,
                                             final @NotNull Collection<ItemStack> itemStacks) {
        return interactPersistentItem(player, interactAction, ifPresent, itemStacks.toArray(new ItemStack[0]));
    }

    /**
     * Checks through every {@link ItemStack} provided for a match in {@link PersistentItem#getPersistentItem(ItemStack)}.
     * For every match, it executes an action.
     *
     * @param player         the player
     * @param interactAction the interact action
     * @param ifPresent      the consumer to run if the item is found
     * @param itemStacks     the item stacks
     * @return true if it was found
     */
    protected boolean interactPersistentItem(final @NotNull Player player,
                                             final @NotNull Action interactAction,
                                             final @Nullable Consumer<PersistentItem> ifPresent,
                                             final ItemStack @Nullable ... itemStacks) {
        return findPersistentItem((p, i) -> {
            if (ifPresent != null) ifPresent.accept(p);
            p.interact(player, i, interactAction);
        }, itemStacks);
    }

    /**
     * Checks through every {@link ItemStack} provided for a match in {@link PersistentItem#getPersistentItem(ItemStack)}.
     * For every match, it executes an action.
     *
     * @param player     the player
     * @param clickType  the click type
     * @param ifPresent  the consumer to run if the item is found
     * @param itemStacks the item stacks
     * @return true if it was found
     */
    protected boolean clickPersistentItem(final @NotNull Player player,
                                          final @NotNull ClickType clickType,
                                          final @Nullable Consumer<PersistentItem> ifPresent,
                                          final @NotNull Collection<ItemStack> itemStacks) {
        return clickPersistentItem(player, clickType, ifPresent, itemStacks.toArray(new ItemStack[0]));
    }

    /**
     * Checks through every {@link ItemStack} provided for a match in {@link PersistentItem#getPersistentItem(ItemStack)}.
     * For every match, it executes an action.
     *
     * @param player     the player
     * @param clickType  the click type
     * @param ifPresent  the consumer to run if the item is found
     * @param itemStacks the item stacks
     * @return true if it was found
     */
    protected boolean clickPersistentItem(final @NotNull Player player,
                                          final @NotNull ClickType clickType,
                                          final @Nullable Consumer<PersistentItem> ifPresent,
                                          final ItemStack @Nullable ... itemStacks) {
        return findPersistentItem((p, i) -> {
            if (ifPresent != null) ifPresent.accept(p);
            p.click(player, i, clickType);
        }, itemStacks);
    }

    /**
     * Finds {@link PersistentItem}s from the given {@link ItemStack} array.
     * For each one found, execute an action
     *
     * @param ifPresent  the action to execute
     * @param itemStacks the item stacks
     * @return true if at least one found
     */
    protected boolean findPersistentItem(final @Nullable Consumer<PersistentItem> ifPresent,
                                         final ItemStack @Nullable ... itemStacks) {
        return findPersistentItem(ifPresent == null ? null : (p, i) -> ifPresent.accept(p), itemStacks);
    }

    /**
     * Finds {@link PersistentItem}s from the given {@link ItemStack} array.
     * For each one found, execute an action
     *
     * @param ifPresent  the action to execute
     * @param itemStacks the item stacks
     * @return true if at least one found
     */
    protected boolean findPersistentItem(final @Nullable BiConsumer<PersistentItem, ItemStack> ifPresent,
                                         final ItemStack @Nullable ... itemStacks) {
        boolean found = false;
        if (itemStacks != null)
            for (final ItemStack itemStack : itemStacks) {
                PersistentItem persistentItem = PersistentItem.getPersistentItem(itemStack);
                if (persistentItem != null) {
                    if (ifPresent != null) ifPresent.accept(persistentItem, itemStack);
                    found = true;
                }
            }
        return found;
    }

    /**
     * Gets an instance of {@link PersistentListener}.
     * If none is currently loaded, it will be created.
     *
     * @return the instance
     */
    public static PersistentListener getInstance() {
        try {
            return getInstance(PersistentListener.class);
        } catch (InstanceNotInitializedException e) {
            PersistentListener listener = new PersistentListener();
            Bukkit.getPluginManager().registerEvents(listener, getProvidingPlugin());
            return listener;
        }
    }

    private static @NotNull JavaPlugin getProvidingPlugin() {
        return JavaPlugin.getProvidingPlugin(PersistentListener.class);
    }

    /**
     * Checks if the current listener has been initialized at least once.
     *
     * @return true if it is
     */
    public static boolean isInitialized() {
        try {
            getInstance(PersistentListener.class);
            return true;
        } catch (InstanceNotInitializedException e) {
            return false;
        }
    }

}
