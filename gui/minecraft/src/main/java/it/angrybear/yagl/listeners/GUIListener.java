package it.angrybear.yagl.listeners;

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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

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
        GUI gui = this.openGUIs.get(player.getUniqueId());
        if (gui != null) gui.openGUIAction().ifPresent(a -> a.execute(BukkitViewer.newViewer(player), gui));
    }

    @EventHandler
    void on(InventoryClickEvent event) {
        GUI gui = this.openGUIs.get(event.getWhoClicked().getUniqueId());
        if (gui != null && !gui.isMovable(event.getRawSlot()))
            event.setCancelled(true);
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
        GUI gui = this.openGUIs.remove(player.getUniqueId());
        if (gui != null) gui.closeGUIAction().ifPresent(a -> a.execute(BukkitViewer.newViewer(player), gui));
    }

    @EventHandler
    void on(PluginDisableEvent event) {
        JavaPlugin plugin = JavaPlugin.getProvidingPlugin(GUIListener.class);
        Plugin disablingPlugin = event.getPlugin();
        if (plugin.equals(disablingPlugin))
            this.openGUIs.keySet().stream().map(Bukkit::getPlayer).filter(Objects::nonNull).forEach(Player::closeInventory);
    }

    public static void openGUI(final @NotNull Viewer viewer, final @NotNull GUI gui) {
        GUIListener listener = getInstance();
        if (listener == null)
            throw new IllegalStateException("GUIListener has not been initialized yet");
        listener.openGUIs.put(viewer.getUniqueId(), gui);
    }
}
