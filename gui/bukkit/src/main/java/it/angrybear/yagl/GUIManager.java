package it.angrybear.yagl;

import it.angrybear.yagl.contents.GUIContent;
import it.angrybear.yagl.guis.GUI;
import it.angrybear.yagl.viewers.Viewer;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.structures.tuples.Tuple;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
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
public class GUIManager implements Listener {
    private static GUIManager instance;

    private final List<Viewer> viewers;

    /**
     * Instantiates a new GUI manager.
     */
    public GUIManager() {
        instance = this;
        this.viewers = new ArrayList<>();
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
        GUIAdapter.closeGUI(getViewer(event.getPlayer()));
    }

    @EventHandler
    void on(final @NotNull PluginDisableEvent event) {
        JavaPlugin plugin = getProvidingPlugin();
        Plugin disablingPlugin = event.getPlugin();
        if (plugin.equals(disablingPlugin)) {
            this.viewers.stream()
                    .filter(Viewer::hasOpenGUI)
                    .map(Viewer::getUniqueId)
                    .map(Bukkit::getPlayer)
                    .filter(Objects::nonNull)
                    .forEach(HumanEntity::closeInventory);
            instance = null;
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
        GUIManager manager = instance;
        if (manager == null) {
            manager = new GUIManager();
            Bukkit.getPluginManager().registerEvents(manager, getProvidingPlugin());
        }
        return manager;
    }

    private static @NotNull JavaPlugin getProvidingPlugin() {
        return JavaPlugin.getProvidingPlugin(GUIManager.class);
    }
}
