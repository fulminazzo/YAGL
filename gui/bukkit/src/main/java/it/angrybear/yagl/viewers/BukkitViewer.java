package it.angrybear.yagl.viewers;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * An implementation of {@link Viewer} for the Bukkit platform.
 */
public class BukkitViewer extends Viewer {

    /**
     * Instantiates a new Viewer.
     *
     * @param uniqueId the unique id
     * @param name     the name
     */
    BukkitViewer(final @NotNull UUID uniqueId, final @NotNull String name) {
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

    /**
     * New viewer viewer.
     *
     * @param player the player
     * @return the viewer
     */
    public static Viewer newViewer(final @NotNull HumanEntity player) {
        return new BukkitViewer(player.getUniqueId(), player.getName());
    }
}
