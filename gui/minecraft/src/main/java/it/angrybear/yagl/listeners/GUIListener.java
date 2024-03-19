package it.angrybear.yagl.listeners;

import it.angrybear.yagl.contents.GUIContent;
import it.angrybear.yagl.guis.GUI;
import it.angrybear.yagl.viewers.BukkitViewer;
import it.angrybear.yagl.viewers.Viewer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class GUIListener implements Listener {
    @Getter
    private static GUIListener instance;

    private final Map<UUID, GUI> openGUIs;

    public GUIListener() {
        instance = this;
        this.openGUIs = new LinkedHashMap<>();
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
        if (this.openGUIs.containsKey(event.getWhoClicked().getUniqueId()))
            event.setCancelled(true);
    }

    @EventHandler
    void on(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        closeGUI(player);
    }

    @EventHandler
    void on(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        closeGUI(player);
    }

    private void closeGUI(Player player) {
        getOpenGUI(player.getUniqueId()).ifPresent(gui ->
                gui.closeGUIAction().ifPresent(a -> a.execute(BukkitViewer.newViewer(player), gui)));
    }

    @EventHandler
    void on(PluginDisableEvent event) {
        JavaPlugin plugin = JavaPlugin.getProvidingPlugin(GUIListener.class);
        Plugin disablingPlugin = event.getPlugin();
        if (plugin.equals(disablingPlugin))
            this.openGUIs.keySet().stream().map(Bukkit::getPlayer).filter(Objects::nonNull).forEach(Player::closeInventory);
    }

    public static Optional<GUI> getOpenGUI(final @NotNull UUID uuid) {
        GUIListener listener = getInstance();
        if (listener == null)
            throw new IllegalStateException("GUIListener has not been initialized yet");
        return Optional.ofNullable(listener.openGUIs.get(uuid));
    }

    public static void openGUI(final @NotNull Viewer viewer, final @NotNull GUI gui) {
        GUIListener listener = getInstance();
        if (listener == null)
            throw new IllegalStateException("GUIListener has not been initialized yet");
        listener.openGUIs.put(viewer.getUniqueId(), gui);
    }
}
