package it.angrybear.yagl.viewers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class BukkitViewer extends Viewer {

    /**
     * Instantiates a new Viewer.
     *
     * @param uniqueId the unique id
     * @param name     the name
     */
    BukkitViewer(UUID uniqueId, String name) {
        super(uniqueId, name);
    }

    @Override
    public void executeCommand(final @NotNull String command) {
        Player player = getPlayer();
        if (player == null) throw new IllegalStateException();
        Bukkit.dispatchCommand(player, command);
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        Player player = getPlayer();
        return player != null && player.hasPermission(permission);
    }

    private Player getPlayer() {
        Player player = Bukkit.getPlayer(this.uniqueId);
        if (player == null) return null;
        else if (player.isOnline()) return player;
        else return null;
    }

    public static Viewer newViewer(final @NotNull Player player) {
        return new BukkitViewer(player.getUniqueId(), player.getName());
    }
}
