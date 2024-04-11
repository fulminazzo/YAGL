package it.angrybear.yagl.listeners;

import it.angrybear.yagl.contents.GUIContent;
import it.angrybear.yagl.guis.GUI;
import it.angrybear.yagl.viewers.BukkitViewer;
import it.angrybear.yagl.viewers.Viewer;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class GUIListener implements Listener {
    private static GUIListener instance;

    private final List<BukkitViewer> viewers;

    public GUIListener() {
        instance = this;
        this.viewers = new ArrayList<>();
    }

    protected @NotNull Optional<BukkitViewer> getOpenGUIViewer(final @NotNull HumanEntity player) {
        BukkitViewer viewer = getViewer(player);
        return Optional.ofNullable(viewer.hasOpenGUI() ? viewer : null);
    }

    protected @NotNull Optional<BukkitViewer> getOpenGUIViewer(final @NotNull UUID uuid) {
        BukkitViewer viewer = getViewer(uuid);
        return Optional.ofNullable(viewer == null || viewer.hasOpenGUI() ? viewer : null);
    }

    protected @NotNull BukkitViewer getViewer(final @NotNull HumanEntity player) {
        BukkitViewer viewer = getViewer(player.getUniqueId());
        if (viewer == null) {
            viewer = (BukkitViewer) BukkitViewer.newViewer(player);
            this.viewers.add(viewer);
        }
        return viewer;
    }

    protected @Nullable BukkitViewer getViewer(final @NotNull UUID uuid) {
        return this.viewers.stream()
                .filter(v -> v.getUniqueId().equals(uuid))
                .findFirst().orElse(null);
    }

    @EventHandler
    void on(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        getOpenGUI(player.getUniqueId()).ifPresent(gui ->
                gui.openGUIAction().ifPresent(a -> a.execute(BukkitViewer.newViewer(player), gui)));
    }

    @EventHandler
    void on(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        getOpenGUI(player.getUniqueId()).ifPresent(gui -> {
            int slot = event.getRawSlot();
            if (!gui.isMovable(slot)) event.setCancelled(true);
            Viewer viewer = BukkitViewer.newViewer(player);
            if (slot < 0) gui.clickOutsideAction().ifPresent(a -> a.execute(viewer, gui));
            else if (slot < gui.getSize()) {
                @Nullable GUIContent content = gui.getContent(viewer, slot);
                if (content != null) content.clickItemAction().ifPresent(a -> a.execute(viewer, gui, content));
            }
        });
    }

    @EventHandler
    void on(InventoryDragEvent event) {
        getOpenGUIViewer(event.getWhoClicked()).ifPresent(v -> event.setCancelled(true));
    }

    @EventHandler
    void on(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        onCloseGUI(player);
    }

    private void onCloseGUI(Player player) {
        getOpenGUI(player.getUniqueId()).ifPresent(gui ->
                gui.closeGUIAction().ifPresent(a -> a.execute(BukkitViewer.newViewer(player), gui)));
    }

    @EventHandler
    void on(PluginDisableEvent event) {
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

    public static Optional<GUI> getOpenGUI(final @NotNull UUID uuid) {
        GUIListener listener = getInstance();
        if (listener == null) {
            listener = new GUIListener();
            Bukkit.getPluginManager().registerEvents(listener, getProvidingPlugin());
        }
        return Optional.ofNullable(listener.openGUIs.get(uuid));
    }

    public static GUIListener getInstance() {
        GUIListener listener = instance;
        if (listener == null) {
            listener = new GUIListener();
            Bukkit.getPluginManager().registerEvents(listener, getProvidingPlugin());
        }
        return listener;
    }

    private static @NotNull JavaPlugin getProvidingPlugin() {
        return JavaPlugin.getProvidingPlugin(GUIListener.class);
    }
}
