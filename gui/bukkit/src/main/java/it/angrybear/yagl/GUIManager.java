package it.angrybear.yagl;

import it.angrybear.yagl.contents.GUIContent;
import it.angrybear.yagl.guis.GUI;
import it.angrybear.yagl.viewers.BukkitViewer;
import it.angrybear.yagl.viewers.Viewer;
import it.fulminazzo.fulmicollection.structures.BiOptional;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class GUIManager implements Listener {
    private static GUIManager instance;

    private final List<BukkitViewer> viewers;

    public GUIManager() {
        instance = this;
        this.viewers = new ArrayList<>();
    }

    @EventHandler
    void on(InventoryOpenEvent event) {
        getOpenGUIViewer(event.getPlayer()).ifPresent(viewer -> {
            GUI gui = viewer.getOpenGUI();
            gui.openGUIAction().ifPresent(a -> a.execute(viewer, gui));
        });
    }

    @EventHandler
    void on(InventoryClickEvent event) {
        getOpenGUIViewer(event.getWhoClicked()).ifPresent(viewer -> {
            GUI gui = viewer.getOpenGUI();
            int slot = event.getRawSlot();
            if (!gui.isMovable(slot)) event.setCancelled(true);
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
        getOpenGUIViewer(player).ifPresent(viewer -> {
            GUI gui = viewer.getOpenGUI();
            gui.closeGUIAction().ifPresent(a -> a.execute(viewer, gui));
        });
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

    public static @NotNull BiOptional<BukkitViewer, GUI> getOpenGUIViewer(final @NotNull HumanEntity player) {
        BukkitViewer viewer = getViewer(player);
        if (viewer.hasOpenGUI()) return BiOptional.of(viewer, viewer.getOpenGUI());
        else return BiOptional.empty();
    }

    public static @NotNull BiOptional<BukkitViewer, GUI> getOpenGUIViewer(final @NotNull UUID uuid) {
        BukkitViewer viewer = getViewer(uuid);
        if (viewer != null && viewer.hasOpenGUI()) return BiOptional.of(viewer, viewer.getOpenGUI());
        else return BiOptional.empty();
    }

    public static @NotNull BukkitViewer getViewer(final @NotNull HumanEntity player) {
        BukkitViewer viewer = getViewer(player.getUniqueId());
        if (viewer == null) {
            viewer = (BukkitViewer) BukkitViewer.newViewer(player);
            getInstance().viewers.add(viewer);
        }
        return viewer;
    }

    public static @Nullable BukkitViewer getViewer(final @NotNull UUID uuid) {
        return getInstance().viewers.stream()
                .filter(v -> v.getUniqueId().equals(uuid))
                .findFirst().orElse(null);
    }

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
