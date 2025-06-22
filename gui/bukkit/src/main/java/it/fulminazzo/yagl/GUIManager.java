package it.fulminazzo.yagl;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.structures.tuples.Tuple;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import it.fulminazzo.yagl.contents.GUIContent;
import it.fulminazzo.yagl.exceptions.InstanceNotInitializedException;
import it.fulminazzo.yagl.guis.GUI;
import it.fulminazzo.yagl.guis.SearchGUI;
import it.fulminazzo.yagl.handlers.AnvilRenameHandler;
import it.fulminazzo.yagl.viewers.Viewer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * A general manager class that controls most {@link GUI} features.
 * It is completely independent and not required from the end user to be loaded or registered.
 */
public class GUIManager extends SingleInstance implements Listener {
    private final @NotNull List<Viewer> viewers;
    private final @NotNull List<AnvilRenameHandler> anvilRenameHandlers;
    @Getter
    private final @NotNull PlayersInventoryCache inventoryCache;

    /**
     * Instantiates a new GUI manager.
     */
    public GUIManager() {
        initialize();
        this.viewers = new ArrayList<>();
        this.anvilRenameHandlers = new ArrayList<>();
        this.inventoryCache = new PlayersInventoryCache();

        Bukkit.getOnlinePlayers().forEach(this::addNewAnvilRenameHandler);
    }

    @EventHandler
    void on(final @NotNull PlayerJoinEvent event) {
        addNewAnvilRenameHandler(event.getPlayer());
    }

    @EventHandler
    void on(final @NotNull PlayerQuitEvent event) {
        removeAnvilRenameHandler(event.getPlayer());
    }

    @EventHandler
    void on(final @NotNull InventoryClickEvent event) {
        getOpenGUIViewer(event.getWhoClicked()).ifPresent((v, g) -> {
            int slot = event.getRawSlot();
            if (slot < 0) g.clickOutsideAction().ifPresent(a -> a.execute(v, g));
            else if (slot < g.size()) {
                if (!g.isMovable(slot)) event.setCancelled(true);
                @Nullable GUIContent content = g.getContent(v, slot);
                if (content != null) content.clickItemAction().ifPresent(a -> a.execute(v, g, content));
            }
        });
    }

    @EventHandler
    void on(final @NotNull InventoryDragEvent event) {
        getOpenGUIViewer(event.getWhoClicked()).ifPresent((v, g) -> event.setCancelled(true));
    }

    @EventHandler
    void on(final @NotNull InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Viewer viewer = getViewer(player);
        GUIAdapter.closeGUI(viewer);
        restorePlayerContents(player);
    }

    @EventHandler
    void on(final @NotNull PluginDisableEvent event) {
        JavaPlugin plugin = GUIAdapter.getProvidingPlugin();
        Plugin disablingPlugin = event.getPlugin();
        if (plugin.equals(disablingPlugin)) {
            this.viewers.stream()
                    .filter(Viewer::hasOpenGUI)
                    .map(Viewer::getUniqueId)
                    .map(Bukkit::getPlayer)
                    .filter(Objects::nonNull)
                    .forEach(HumanEntity::closeInventory);
            Bukkit.getOnlinePlayers().forEach(this::removeAnvilRenameHandler);
            terminate();
        }
    }

    /**
     * Adds a new {@link AnvilRenameHandler} for the given player.
     *
     * @param player the player
     */
    void addNewAnvilRenameHandler(final @NotNull Player player) {
        AnvilRenameHandler handler = new AnvilRenameHandler(
                player.getUniqueId(),
                (p, n) -> getOpenGUIViewer(p).ifPresent((v, g) -> {
                    Class<?> clazz = g.getClass();
                    String expectedClassName = SearchGUI.class.getCanonicalName() + ".SearchFullSizeGUI";
                    Class<?> expectedClass = ReflectionUtils.getClass(expectedClassName);

                    if (expectedClass.equals(clazz)) {
                        SearchGUI<?> searchGUI = new Refl<>(g).invokeMethod("getSearchGui");
                        if (n.equals(searchGUI.getQuery())) return;
                        searchGUI.setQuery(n);
                        GUIAdapter.updatePlayerGUI(searchGUI.getFirstPage(), v);
                    }
                })
        );
        handler.inject();
        this.anvilRenameHandlers.add(handler);
    }

    /**
     * Removes the {@link AnvilRenameHandler} of the given player.
     *
     * @param player the player
     */
    void removeAnvilRenameHandler(final @NotNull Player player) {
        AnvilRenameHandler handler = this.anvilRenameHandlers.stream()
                .filter(h -> h.belongsTo(player))
                .findFirst().orElse(null);
        if (handler != null) {
            handler.remove();
            this.anvilRenameHandlers.remove(handler);
        }
    }

    /**
     * Restores the specified player contents if present.
     *
     * @param player the player
     */
    static void restorePlayerContents(final @NotNull Player player) {
        GUIManager guiManager = getInstance();
        @NotNull PlayersInventoryCache inventoryCache = guiManager.inventoryCache;
        Viewer viewer = getViewer(player);
        if (inventoryCache.areContentsStored(player) && viewer.getNextGUI() == null) {
            inventoryCache.restorePlayerContents(player);
            inventoryCache.clearPlayerContents(player);
        }
    }

    /**
     * Gets a {@link Tuple} with the corresponding {@link Viewer} and open {@link GUI} if present.
     *
     * @param player the player
     * @return the Tuple
     */
    public static @NotNull Tuple<Viewer, GUI> getOpenGUIViewer(final @NotNull HumanEntity player) {
        Viewer viewer = getViewer(player);
        if (viewer.hasOpenGUI()) return new Tuple<>(viewer, viewer.getOpenGUI());
        else return new Tuple<>();
    }

    /**
     * Gets a {@link Tuple} with the corresponding {@link Viewer} and open {@link GUI} if present.
     *
     * @param uuid the uuid
     * @return the Tuple
     */
    public static @NotNull Tuple<Viewer, GUI> getOpenGUIViewer(final @NotNull UUID uuid) {
        Viewer viewer = getViewer(uuid);
        if (viewer != null && viewer.hasOpenGUI()) return new Tuple<>(viewer, viewer.getOpenGUI());
        else return new Tuple<>();
    }

    /**
     * Gets the corresponding {@link Viewer} to the provided player.
     * If it is not present, it will be created.
     *
     * @param player the player
     * @return the viewer
     */
    public static @NotNull Viewer getViewer(final @NotNull HumanEntity player) {
        Viewer viewer = getViewer(player.getUniqueId());
        if (viewer == null) {
            viewer = newViewer(player);
            getInstance().viewers.add(viewer);
        }
        return viewer;
    }

    private static @NotNull Viewer newViewer(final @NotNull HumanEntity player) {
        final String packageName = Viewer.class.getPackage().getName();
        final Class<?> bukkitViewer = ReflectionUtils.getClass(packageName + ".BukkitViewer");
        Viewer viewer = new Refl<>(bukkitViewer).invokeMethod("newViewer", player);
        return Objects.requireNonNull(viewer);
    }

    /**
     * Gets the corresponding {@link Viewer} to the provided player.
     * If it is not present, it will be returned null.
     *
     * @param uuid the uuid
     * @return the viewer
     */
    public static @Nullable Viewer getViewer(final @NotNull UUID uuid) {
        return getInstance().viewers.stream()
                .filter(v -> v.getUniqueId().equals(uuid))
                .findFirst().orElse(null);
    }

    /**
     * Gets an instance of {@link GUIManager}.
     * If none is currently loaded, it will be created.
     *
     * @return the instance
     */
    public static GUIManager getInstance() {
        try {
            return getInstance(GUIManager.class);
        } catch (InstanceNotInitializedException e) {
            GUIManager manager = new GUIManager();
            Bukkit.getPluginManager().registerEvents(manager, GUIAdapter.getProvidingPlugin());
            return manager;
        }
    }

}
